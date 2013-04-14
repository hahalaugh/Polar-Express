package com.middleware;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.common.types.*;
import com.geolocation.GeoLocator;
import com.sun.jersey.api.client.*;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlElement;

public class MiddlewareImpl implements Middleware {

	private final String databasePath = "middleware.sqlite";
	private final double COORDINATES_THRESH = 0.01;
	
	// storage of thread handlers
	Map<Integer, CallbackProcessor> processors = Collections.synchronizedMap(new HashMap<Integer, CallbackProcessor>());

	private Connection connection = null;

	public MiddlewareImpl(){
		try{
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
			createTables();
			System.out.println("Middleware Implementation created.");		
		}
		catch (ClassNotFoundException e){
			System.err.println("SQLite jar and classes not found");		
		} catch (SQLException e) {
			System.err.println("SQL caused exception");
		}
	}

	private void createTables()
	{
		String createDataStreams = "CREATE TABLE if not exists DataStreams (" +
				"DataStreamID INTEGER NOT NULL, " +
				"DataProducer varchar(45) NOT NULL, " +
				"DataType varchar(20) NOT NULL, " +
				"Longitude double(10) NOT NULL, " +
				"Latitude double(10) NOT NULL, " +
				"Altitude double(10) NOT NULL, " +
				"DataModel varchar(255) NOT NULL, " +
				"SamplingRate integer(10) NOT NULL, " +
				"PRIMARY KEY (DataStreamID));";
		executeQuery(createDataStreams);

		String createSubscriptionDataStreams = "CREATE TABLE if not exists SubscriptionDataStreams (" +
				"SubscriptionID integer(10) NOT NULL, " +
				"DataStreamID integer(10) NOT NULL);";
		executeQuery(createSubscriptionDataStreams);

		String createSubscriptions = "CREATE TABLE if not exists Subscriptions (" +
				"SubscriptionID integer NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE DEFAULT 0," +
				"DataConsumer varchar(45) NOT NULL," +
				"DataType varchar(20) NOT NULL," +
				"SubscriptionType varchar(10) NOT NULL," +
				"SubscriptionValue varchar(45)," +
				"LocationID integer(10) NOT NULL);";
		executeQuery(createSubscriptions);

		String createLocations = "CREATE TABLE if not exists Locations (" +
				"LocationID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT DEFAULT 0," +
				"Longitude double(10) NOT NULL," +
				"Latitude double(10) NOT NULL," +
				"Altitude double(10) NOT NULL)";
		executeQuery(createLocations);
	}

	private void executeQuery(String query)
	{
		try {
			Statement statement = connection.createStatement();
			statement.execute(query);
			statement.close();
		} catch (SQLException e) {
			System.err.println("Could not execute query: " + query);
			e.printStackTrace();
		}
	}

	/* 1. Return the list of available data types
	 */
	public GetDataTypesReply GetDataTypes() {
		ArrayList<String> types = new ArrayList<String>();	

		Statement statement = null;
		ResultSet table = null;
		String query = "SELECT DISTINCT DataType FROM DataStreams;";
		try {
			statement = connection.createStatement();
			table = statement.executeQuery(query);
			while (table.next()) {	
				if (table != null){
					String type = table.getString("DataType");
					types.add(type);		
				}
			}
			statement.close();				
			table.close();
		} catch (SQLException e) {
			System.out.println("Problem with SQL");			
		}

		GetDataTypesReply reply = new GetDataTypesReply();
		reply.types = types;
		return reply;
	}

	/* 1. Find data type in the list of available data types
	 * 2. Return data model for this data type
	 * NOTE: data types with the same name must have same data model  
	 */
	public DescribeDataTypeReply DescribeDataType(String dataType) {
		DescribeDataTypeReply reply = null;
		String model = null;
		Statement statement = null;
		ResultSet table = null;
		String query = "SELECT DataModel FROM DataStreams WHERE DataType = \"" + dataType +"\";";
		System.out.println("Describe: " + query);
		try {
			statement = connection.createStatement();
			table = statement.executeQuery(query);
			while (table.next()) {	
				if (table != null){
					model = table.getString("DataModel");
					reply = new DescribeDataTypeReply();
					reply.name = dataType;
					reply.model = model;		
					break;
				}
			}
			statement.close();				
			table.close();
		} catch (SQLException e) {
			System.out.println("Problem with SQL");			
		}

		return reply;
	}

	/*
	 * 1. Check if subscription with such parameters already exists
	 * 2. If there are data sources for this type of data - create thread for processing and 
	 * 	  create subscription. One subsription from consumer - one thread for it. 
	 */
	public int SetSubscription(SetSubscriptionRequest request) {
		// check if same subscription already exists
		int subscriptionID = getSubscriptionID(request);
		if (subscriptionID != -1) {
			// no need to create new, return existing id
			return subscriptionID;
		} else {
			// check if there any data producer available
			List<DataProducerInfo> producers = getDataSources(request.dataType, request.location);
			if (producers.size() != 0) {
				// create new subscription entry in database for request
				subscriptionID = newSubscription(request);
				// create thread which will connect to data producers and send new data to consumers
				CallbackProcessor proc = new CallbackProcessor(subscriptionID, request, this);
				
				// save thread handle into map to provide ability to stop it
				processors.put(subscriptionID, proc);
				
				// start thread
				proc.start();
				
				return subscriptionID;				
			} else {
				System.out.println("SetSubscription(): no available data producers.");
				return -1;
			}
		}
	}

	/*
	 * 1. Check if there subscription with such id
	 * 2. Stop processing thread of subscription
	 * 3. Remove records from database (Locations and Subscriptions)
	 */
	public int RemoveSubscription(int subscriptionID) {
		if (subscriptionExists(subscriptionID)) {
			// stor executing thread
			CallbackProcessor proc = processors.get(subscriptionID);
			if (proc != null) {
				proc.terminate();
				processors.remove(subscriptionID);
			}
			
			// remove database records
			removeLocationForSubscription(subscriptionID);
			String query = "DELETE FROM Subscriptions WHERE SubscriptionID = " + subscriptionID + ";";
			executeQuery(query);
		}
		
		return 0;
	}
		
	/*
	 * 1. Save information into data base. That's all.
	 * Assumptions: dataTypes list always contains one data type. 
	 */
	public int RegisterDataStream(RegisterDSRequest request, String dataProducerURI) {		
		int dataStreamID = -1;
		String query = "INSERT INTO DataStreams (DataProducer, DataType, Latitude, " +
				"Longitude, Altitude, DataModel, SamplingRate) VALUES (" +
				"\"" + dataProducerURI + "\"," +
				"\"" + request.dataTypes.get(0).name + "\"," +
				request.location.latitude + "," +
				request.location.longitude + "," +
				request.location.altitude + "," +
				"\"" + request.dataTypes.get(0).metaData.dataModel +  "\"," +
				"\"" + request.dataTypes.get(0).metaData.samplingRate + ")";
		System.out.println(query);
		executeQuery(query);

		// as far as DataStreamID is auto increment			
		// get new data stream's id
		Statement statement = null;
		ResultSet table = null;
		String idQuery = "SELECT max(DataStreamID) FROM DataStreams";
		try {
			statement = connection.createStatement();
			table = statement.executeQuery(idQuery);
			while (table.next()) {	
				if (table != null){
					dataStreamID = table.getInt("max(DataStreamID)");					
					break;
				}
			}
			statement.close();				
			table.close();
		} catch (SQLException e) {
			System.out.println("Problem with SQL. get new location id failed.");			
		}
		
		return dataStreamID;
	}

	public int RemoveDataStream(int dataStreamID) {
		// remove records from data streams and SubscriptionDataStreams
		String query = "DELETE FROM DataStreams WHERE DataStreamID = " + dataStreamID + ";";
		executeQuery(query);		
		query = "DELETE FROM SubscriptionDataStreams WHERE DataStreamID = " + dataStreamID + ";";
		executeQuery(query);	
		
		return 0;
	}

	public int OnCallback(DataToBeSent data) {
		// data to be sent contains data stream id and subscription id (from DP)
		// send onCallback signal to all existing threads, they will decide what to do next
		Iterator it = processors.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        CallbackProcessor proc = (CallbackProcessor) pairs.getValue();
	        proc.onCallback(data);
	    }
		return 0;
	}

	// ---------------------------- AUXILLARY FUNCTIONALITY --------------------------------------

	public Location getLocationByID(int id){

		String query = "SELECT * FROM Locations WHERE LocationID = \'" + id + "\';";

		Statement statement = null;
		ResultSet table = null;
		try {
			statement = connection.createStatement();
			table = statement.executeQuery(query);
			while (table.next()) {
				if (table != null){		
					Location loc = new Location();
					loc.longitude = Double.valueOf(table.getString("Longitude"));
					loc.latitude = Double.valueOf(table.getString("Latitude"));
					loc.altitude = Double.valueOf(table.getString("Altitude"));
					return loc;
				}
			}
			statement.close();				
			table.close();
		} catch (SQLException e) {
			System.out.println("Problem with SQL. getLocationByID()");			
		}	
		return null;
	}

	// add database records
	// 1. add location entry
	// 2. add subscription entry
	public int newSubscription(SetSubscriptionRequest request) {		

		int subscriptionID = -1;
		int newLocationID = insertNewLocation(request.location);
		if (newLocationID != -1) {

			// insert data into db
			String query = "INSERT INTO Subscriptions (" +
					"DataConsumer, DataType, LocationID, SubscriptionType, SubscriptionValue) VALUES (" +
					"\"" + request.callbackURI + "\"" +  "," +
					"\"" + request.dataType + "\"" + "," +
					newLocationID + "," +
					"\"" + request.subscription.getSubscriptionType() + "\""; 

			if (request.subscription.getSubscriptionTypeValue() == null) {
				query += ",null)";
			} else {
				query += ",\"" + request.subscription.getSubscriptionTypeValue() + "\"" + ")";
			}
			System.out.println(query);
			executeQuery(query);

			// get the id for new subscription considering auto increment behavior
			Statement statement = null;
			ResultSet table = null;
			String idQuery = "SELECT max(SubscriptionID) FROM Subscriptions";
			try {
				statement = connection.createStatement();
				table = statement.executeQuery(idQuery);
				while (table.next()) {	
					if (table != null){
						String id = table.getString("max(SubscriptionID)");
						subscriptionID = Integer.valueOf(id);
						break;
					}
				}
				statement.close();				
				table.close();				
			} catch (SQLException e) {
				System.out.println("Problem with SQL. createNewSubscription() get id failed.");
				e.printStackTrace();
			}		
		} else {
			System.out.println("Failed to add new subscription. Location insert failed.");
		}
		return subscriptionID;
	}

	public List<DataProducerInfo> getDataSourcesForViewPort(String dataType, List<Location> viewPort) {
		if (viewPort != null && viewPort.size() == 2) {
			Location sw = viewPort.get(0);
			Location ne = viewPort.get(1);

			String latitudeStr  = "Latitude >= " + sw.latitude + " and Latitude <= " + ne.latitude;
			String longitudeStr = "Longitude >= " + sw.longitude + " and Longitude <= " + ne.longitude;
			String dataTypeStr  = "DataType = \"" + dataType + "\"";

			String query = "SELECT * FROM DataStreams WHERE " + latitudeStr + " and " + longitudeStr + " and " + dataTypeStr;
			System.out.println(query);

			List<DataProducerInfo> dataSources = new ArrayList<DataProducerInfo>();					
			Statement statement = null;
			ResultSet table = null;					
			try {
				statement = connection.createStatement();
				table = statement.executeQuery(query);
				while (table.next()) {
					if (table != null){
						DataProducerInfo info = new DataProducerInfo();
						info.dataStreamID 	 = table.getInt("DataStreamID");
						info.dataProducerURI = table.getString("DataProducer");
						dataSources.add(info);								
					}
				}
				statement.close();				
				table.close();
			} catch (SQLException e) {
				System.out.println("Problem with SQL. getDataSourcesForviewPort() failed.");
				return null;
			}					
			return dataSources;
		} else {
			System.out.println("2 locations for viewport expected.");
		}
		return new ArrayList<DataProducerInfo>();
	}
	
	public List<DataProducerInfo> getDataSourcesForPlace(String dataType, String placeName) {
		List<Location> viewPort = GeoLocator.getViewPortOf(placeName);		
		return getDataSourcesForViewPort(dataType, viewPort);
	}

	public List<DataProducerInfo> getDataSourcesForCoordinates(String dataType, String coordinates) {

		String[] tmp = coordinates.split(";");
		
		if (tmp.length == 3) {
			
			double latitude = Double.valueOf(tmp[0]);
			double longitude = Double.valueOf(tmp[1]);
			double altitude = Double.valueOf(tmp[2]);
			
			Location sw = new Location();
			sw.latitude = latitude - COORDINATES_THRESH;
			sw.longitude = longitude - COORDINATES_THRESH;
			sw.altitude = altitude;
			
			Location ne = new Location();
			ne.latitude = latitude + COORDINATES_THRESH;
			ne.longitude = longitude + COORDINATES_THRESH;
			ne.altitude = altitude;
			
			List<Location> viewPort = new ArrayList<Location>();
			viewPort.add(sw);
			viewPort.add(ne);
			
			return getDataSourcesForViewPort(dataType, viewPort);			
			
		} else {
			System.out.println("Wrong coordinates format. 3 values expected.");
			return new ArrayList<DataProducerInfo>();
		}
	}
	
	// returns the id and URI of data sources which gives data in specified location
	// used mostly in callback processor
	public List<DataProducerInfo> getDataSources(String dataType, LocationNamed location) {		
		
		// check if such data type presents in the db
		if (DescribeDataType(dataType) != null) {
			if (location.type.equals("placename")) {
				return getDataSourcesForPlace(dataType, location.value);
			} else if (location.type.equals("coordinates")) {
				return getDataSourcesForCoordinates(dataType, location.value);
			} else {
				System.out.println("getDataSources(): incorrect type of location");
			}
		} else {
			System.out.println("getDataSources(): no data producer for this data type");
		}
		return new ArrayList<DataProducerInfo>();		
	}

	// create new location record in the database and return its id
	public int insertNewLocation(LocationNamed location) {
		System.out.println("insert new location");
		Location loc = null;
		if (location.type.equals("place")) {
			loc = GeoLocator.getCenterOf(location.value);
		} else {
			String[] tmp = location.value.split(";");
			loc = new Location();
			loc.latitude = Double.valueOf(tmp[0]);
			loc.longitude = Double.valueOf(tmp[1]);
			loc.altitude = Double.valueOf(tmp[2]);
		}

		int locID = -1;
		if (loc != null) {
			String query = "INSERT INTO Locations (Latitude, Longitude, Altitude) VALUES (" +
					loc.latitude + ","+ loc.longitude + ","+ loc.altitude + ")";

			// add new location
			executeQuery(query);

			// as far as LocationID is auto increment			
			// get new location's id
			Statement statement = null;
			ResultSet table = null;
			String idQuery = "SELECT max(LocationID) FROM Locations";
			try {
				statement = connection.createStatement();
				table = statement.executeQuery(idQuery);
				while (table.next()) {	
					if (table != null){
						String id = table.getString("max(LocationID)");
						locID = Integer.valueOf(id);
						break;
					}
				}
				statement.close();				
				table.close();
			} catch (SQLException e) {
				System.out.println("Problem with SQL. get new location id failed.");			
			}						

			return locID;
		} else {
			return -1;			
		}		
	}

	// check if such subscription already exists
	public int getSubscriptionID(SetSubscriptionRequest request) {

		String query = "SELECT * FROM Subscriptions WHERE " +
				"DataConsumer = \"" + request.callbackURI + "\" and " +
				"DataType = \"" + request.dataType + "\" and " +
				"SubscriptionType = \"" + request.subscription.getSubscriptionType() + "\"";
		if (!request.subscription.getSubscriptionTypeValue().equals("")) {
			query += " and SubscriptionValue = \"" + request.subscription.getSubscriptionTypeValue() + "\";";
		}

		Statement statement = null;
		ResultSet table = null;
		try {
			statement = connection.createStatement();
			table = statement.executeQuery(query);
			while (table.next()) {
				if (table != null){
					String id = table.getString("SubscriptionID");
					Location loc = getLocationByID(table.getInt("LocationID"));

					if (loc == null) {
						// no such location
						return -1;
					}					

					// we have location, now we need to compare location in request and location
					// which corresponds to current table row. return id if they are the same
					boolean corresponds = false;
					if (request.location.type == "place") {
						List<Location> viewPort = GeoLocator.getViewPortOf(request.location.value);
						corresponds = GeoLocator.inViewPort(loc, viewPort);

					} else {
						// location is defined by coordinates
						String coords = request.location.value;				
						String[] tmp = coords.split(";");
						Location location = new Location();
						location.latitude = Double.valueOf(tmp[0]);
						location.longitude = Double.valueOf(tmp[1]);
						location.altitude = Double.valueOf(tmp[2]);

						// compare
						if (Math.abs(loc.latitude - location.latitude) <= COORDINATES_THRESH &&
								Math.abs(loc.longitude - location.longitude) <= COORDINATES_THRESH) {
							corresponds = true;
						}
					}

					if (corresponds) {					
						return Integer.valueOf(id);
					}
				}
			}
			statement.close();
			table.close();
		} catch (SQLException e) {
			System.out.println("Problem with SQL. getSubscriptionID()");
			e.printStackTrace();
		}		

		// in case when subscription not exists
		return -1;
	}

	// get subscription object from database
	public SetSubscriptionRequest getSubscriptionByID(int id) {

		String query = "SELECT * FROM Subscriptions WHERE SubscriptionID = " + id + ";";
		System.out.println(query);
		Statement statement = null;
		ResultSet table = null;
		try {
			statement = connection.createStatement();
			table = statement.executeQuery(query);
			while (table.next()) {
				if (table != null){
					SetSubscriptionRequest req = new SetSubscriptionRequest();

					req.callbackURI = table.getString("DataConsumer");
					req.dataType = table.getString("DataType");

					req.location.type = "coordinates";
					Location loc = getLocationByID(table.getInt("LocationID"));

					if (loc == null) {
						// there is no such location, it means subscription record is wrong
						return null;
					}

					req.location.value = String.valueOf(loc.latitude) + ";" + String.valueOf(loc.longitude) + ";" + String.valueOf(loc.altitude);
					Subscription sub = new Subscription();
					sub.setSubscriptionType(table.getString("SubscriptionType"));
					if (table.getString("SubscriptionValue") != null) {
						sub.setSubscriptionTypeValue(table.getString("SubscriptionValue"));						
					} else {
						sub.setSubscriptionTypeValue("");
					}					
					req.subscription = sub;
					return req;
				}
			}
			statement.close();
			table.close();
		} catch (SQLException e) {
			System.out.println("Problem with SQL. getSubscriptionByID()");
			e.printStackTrace();
		}		

		return null;
	}

	// tested. not needed
	public List<DataProducerInfo> getDPForSubscription(int subscriptionID) {
		ArrayList<DataProducerInfo> producers = new ArrayList<DataProducerInfo>();
		String query = "SELECT DataStreamID, DataProducer FROM (" +
				"SELECT * FROM SubscriptionDataStreams INNER JOIN DataStreams " +
				"ON SubscriptionDataStreams.DataStreamID = DataStreams.DataStreamID) " +
				"WHERE SubscriptionID = " + subscriptionID + ";";		
		Statement statement = null;
		ResultSet table = null;
		try {
			statement = connection.createStatement();
			table = statement.executeQuery(query);
			while (table.next()) {
				if (table != null){
					DataProducerInfo info = new DataProducerInfo();
					info.dataStreamID = table.getInt("DataStreamID");
					info.dataProducerURI = table.getString("DataProducer");
					producers.add(info);
				}
			}
			statement.close();
			table.close();
		} catch (SQLException e) {
			System.out.println("Problem with SQL. getDPForSubscription()");
			e.printStackTrace();
		}		
		return producers;
	}

	
	// tested
	public synchronized void updateSubscriptionDataStreams(int subscriptionID, int dataStreamID) {
		String query = "INSERT INTO SubscriptionDataStreams(DataStreamID, SubscriptionID) " +
				"VALUES ("+ dataStreamID + "," + subscriptionID + ");";
		executeQuery(query);
	}

	// tested
	public synchronized void removeSubscriptionDataStreams(int subscriptionID, int dataStreamID) {
		String query = "DELETE FROM SubscriptionDataStreams WHERE " +
				"DataStreamID = " + dataStreamID + " and SubscriptionID = " + subscriptionID + ";";
		executeQuery(query);
	}

	// tested
	public boolean subscriptionExists(int id) {
		boolean exists = false;
		String query = "SELECT count(*) FROM Subscriptions WHERE SubscriptionID = " + id + ";";
		Statement statement = null;
		ResultSet table = null;
		try {
			statement = connection.createStatement();
			table = statement.executeQuery(query);
			while (table.next()) {
				if (table != null){
					String count = table.getString("count(*)");
					if (count.equals("1")) {
						exists = true;
						break;
					} 
				}
			}
			statement.close();
			table.close();
		} catch (SQLException e) {
			System.out.println("Problem with SQL. subscriptionExists()");
			e.printStackTrace();
		}
		return exists;
	}

	// tested
	public void removeLocationForSubscription(int subscriptionID) {
		String query = "select LocationID from Subscriptions where SubscriptionID = " + subscriptionID + ";";
		Statement statement = null;
		ResultSet table = null;		
		try {
			statement = connection.createStatement();
			table = statement.executeQuery(query);
			while (table.next()) {	
				if (table != null){
					int locationID = table.getInt("LocationID");
					String q = "delete from Locations where LocationID = " + locationID + ";";
					executeQuery(q);
					break;
				}
			}
			statement.close();				
			table.close();
		} catch (SQLException e) {
			System.out.println("Problem with SQL. removeLocationForSubscription() failed.");			
		}		
	}
	
	// tested

	public boolean pairExists(int dataStreamID, int producerSubscriptionID) {
		boolean exists = false;
		String query = "SELECT count(*) FROM SubscriptionDataStreams " +
				"WHERE SubscriptionID = " + producerSubscriptionID + " and DataStreamID = " + dataStreamID + ";";
		Statement statement = null;
		ResultSet table = null;
		try {
			statement = connection.createStatement();
			table = statement.executeQuery(query);
			while (table.next()) {
				if (table != null){
					String count = table.getString("count(*)");
					if (count.equals("1")) {
						exists = true;
						break;
					} 
				}
			}
			statement.close();
			table.close();
		} catch (SQLException e) {
			System.out.println("Problem with SQL. subscriptionExists()");
			e.printStackTrace();
		}
		return exists;		
	}
}
