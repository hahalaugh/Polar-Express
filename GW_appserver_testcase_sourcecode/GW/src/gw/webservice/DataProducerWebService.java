package gw.webservice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import gw.resource.GV;
import gw.resource.dataproducer.DataToBeSent;
import gw.resource.dataproducer.ID;
import gw.resource.dataproducer.Location;
import gw.resource.dataproducer.PushSensorDataRequest;
import gw.resource.dataproducer.PushSensorDataResponse;
import gw.resource.dataproducer.RegisterDSResponse;
import gw.resource.dataproducer.SensorData;
import gw.resource.dataproducer.SubscriptionLocalStorage;

import gw.resource.dataproducer.RegisterDS;
import gw.resource.dataproducer.DataType;
import gw.resource.dataproducer.SetSubscriptionParam;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Path("/DP")
public class DataProducerWebService
{
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	/**
	 * @param request
	 * @consumes JSON
	 * @return processPushSensorDataRequest return a PushSensorDataResponse
	 *         typed value which indicates the data pushing operation is
	 *         successful or not.
	 * 
	 *         This function work as web service interface which listen to the
	 *         push request and generate a data producer instance in the local
	 *         memory as data cache.
	 */
	@Path("/push")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public PushSensorDataResponse processPushSensorDataRequest(PushSensorDataRequest request)
	{
		System.out.println("data push request received.");
		// if sensorId is empty, this sensor couldnt be added into the datalist
		// since sensorId is a essential field in the system.
		if (request.getSensorId().isEmpty())
		{
			// sensor id is essential when create data producer instance.
			System.out.println("sensor id is empty");
			return new PushSensorDataResponse(false);
		}
		else
		{
			System.out.println("data push request valid!");
			Integer tempds = 0;
			// check if this data stream with same id(the geological
			// information, a combination of longitude and latitude)
			Integer datastreamId = existedDataStream(request);

			if (datastreamId == -1)
			{
				// if this is a new data stream which position has not been
				// registered in the data pool before,
				// register this data stream to middle ware.

				// get datatype list which is essential when registering data
				// stream.
				ArrayList<DataType> list = getDataTypeList(request);

				// generate a new data stream registration request with
				// geological information and datatype list.
				RegisterDS registerDS = new RegisterDS(new Location(request.getLatitude(), request.getLongitude()),
						list);
				System.out.println("data model is " + registerDS.dataType.get(0).dataModel.dataModel);

				// register the data stream and get the data stream id from
				// middleware
				tempds = registerDataStream(registerDS);

				System.out.println("get datastream id 	" + tempds);

				// add this new data stream to data pool after registration.
				GV.sensorDataMap.put(tempds, new SensorData(request));
				System.out.println("data stream is " + GV.sensorDataMap.get(tempds).toString());
			}
			else
			{
				// exsited data. so dont register and update the local data pool
				// directly.
				GV.sensorDataMap.put(datastreamId, new SensorData(request));
			}

			return new PushSensorDataResponse(true);
		}
	}

	/**
	 * @param param
	 * @param dataStreamId
	 * @param inRequest
	 * @return return an ID to middle ware as subscription id in XML.
	 * 
	 *         This function listen to the set subscription request from
	 *         middleware.
	 * 
	 * @Consumes XML
	 * 
	 */
	@Path("/setSubscription/{dataStreamId}")
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public ID processSetSubscriptionRequest(JAXBElement<SetSubscriptionParam> param,
			@PathParam("dataStreamId") Integer dataStreamId, @Context HttpServletRequest inRequest)
	{
		System.out.println("set subscription from mw for data stream id = " + dataStreamId);
		if (isValidDataStream(dataStreamId))
		{
			// if this is a valid data stream.

			// generate a local subscription storage format and put into
			// subscription pool.
			SubscriptionLocalStorage sls = new SubscriptionLocalStorage(dataStreamId, param.getValue().name);
			GV.subscriptionMap.put(GV.currentSubscriptionId, sls);

			// create a new Id.
			ID result = new ID(GV.currentSubscriptionId);
			GV.currentSubscriptionId++;

			String requiredParam = param.getValue().name;
			Double value = getValue(dataStreamId, requiredParam);
			// send data to mw immediately after subscription is set.
			final DataToBeSent data = new DataToBeSent(result.id, dataStreamId, value);

			System.out.println("DP will send for subscription = " + result.id.toString());
			System.out.println("DP will send for data stream = " + dataStreamId);
			System.out.println("DP will send value = " + value);
			// sendToMW(data);

			// set a timer and send the data after 500ms.
			Timer timer = new Timer(true);
			timer.schedule(new TimerTask()
			{
				@Override
				public void run()
				{
					// send data to middleware
					sendToMW(data);
				}
			}, 500);

			// return the subscription number to middleware
			return result;
		}
		else
		{
			System.out.println("Invalid data stream!");
			if (GV.sensorDataMap.containsKey(dataStreamId))
			{
				// remove the data stream from data stream pool.
				System.out.println("Remove the data stream");
				GV.sensorDataMap.remove(dataStreamId);
			}

			// remove data stream = dataStreamId.
			removeDataStreamId(dataStreamId);
			System.out.println("Remove datastream finished");
			return new ID(-1);
		}
	}

	/**
	 * @param subscriptionId
	 * @return
	 * 
	 *         return ID if remove subscription id successfully return -1 if no
	 *         such subscription existed.
	 */
	@Path("/removeSubscription/{subscriptionId}")
	@DELETE
	@Consumes(MediaType.APPLICATION_XML)
	public ID processRemoveSubscriptionRequest(@PathParam("subscriptionId") Integer subscriptionId)
	{
		if (GV.subscriptionMap.containsKey(subscriptionId))
		{
			// if subscription id existed in the subscription pool.
			GV.subscriptionMap.remove(subscriptionId);
			return new ID(subscriptionId);
		}

		return new ID(-1);
	}

	/**
	 * @param data
	 *            send subscribed data to middle ware which include
	 *            1.subscription id 2. data stream id 3. data value
	 */
	public void sendToMW(DataToBeSent data)
	{
		System.out.println("DP Send data to mw:");
		System.out.println("datastream id = " + data.dataStreamID);
		System.out.println("data subscription id = " + data.subscriptionID);
		System.out.println("data value = " + data.value);

		WebResource resource = Client.create().resource(GV.MWUrl + GV.MWUrlNewData);
		// send http put request to MWUrlNewData interface of middleware
		resource.type(MediaType.APPLICATION_XML).put(ClientResponse.class, data);
	}

	/**
	 * @param dataStreamId
	 * @param requiredParam
	 * @return get value from data pool where data stream id = dataStreamId and
	 *         value name = requiredParam
	 */
	public Double getValue(Integer dataStreamId, String requiredParam)
	{
		int flag = -1;
		Double value = 0.0;

		SensorData sd = GV.sensorDataMap.get(dataStreamId);

		System.out.println("temperature is = " + sd.getTemperature());
		System.out.println("Humidity is = " + sd.getHumidity());
		System.out.println("no2 is = " + sd.getNo2());
		for (int i = 0; i < GV.parameterType.length; i++)
		{
			// get the corresponding name
			if (requiredParam.equals(GV.parameterType[i]))
			{
				flag = i;
				break;
			}
		}

		System.out.println("flag number is " + flag);
		// switch the value name and get value data.
		switch (flag)
		{
		case 0:
			value = sd.getTemperature();
			break;
		case 1:
			value = sd.getHumidity();
			break;
		case 2:
			value = sd.getNo();
			break;
		case 3:
			value = sd.getNo2();
			break;
		case 4:
			value = sd.getCo();
			break;
		case 5:
			value = sd.getSo2();
			break;
		default:
			break;
		}
		return value;
	}

	/**
	 * @param request
	 * @return generate and retun a list of data type of certain sensor to
	 *         describe the sensor ability.
	 */
	public ArrayList<DataType> getDataTypeList(PushSensorDataRequest request)
	{
		Double samplingRate = 1.0;
		ArrayList<DataType> list = new ArrayList<DataType>();

		list.add(new DataType("temperature", samplingRate));
		list.add(new DataType("humidity", samplingRate));
		list.add(new DataType("no", samplingRate));
		list.add(new DataType("no2", samplingRate));
		list.add(new DataType("co", samplingRate));
		list.add(new DataType("so2", samplingRate));

		return list;
	}

	/**
	 * @param rds
	 * @return an Integer value indicates the data stream from middle ware. -1
	 *         if this data stream existed in the middleware.
	 */
	public Integer registerDataStream(RegisterDS rds)
	{
		System.out.println("registering datastream!");
		WebResource resource = Client.create().resource(GV.MWUrl + GV.MWUrlRegisterDataStream);
		System.out.println(resource.getURI());

		// put the request to PushSensorDataResponse interface of middle ware
		ClientResponse response = resource.type(MediaType.APPLICATION_XML).put(ClientResponse.class, rds);

		// get data stream id. if ID = -1 then this data stream has been
		// registered in the middleware
		ID rsp = (ID) response.getEntity(ID.class);

		return rsp.id;
	}

	/**
	 * @param dataStreamId
	 * 
	 *            call the remove data stream interface of middle ware to remove
	 *            the registered data stream.
	 */
	public void removeDataStreamId(Integer dataStreamId)
	{
		WebResource resource = Client.create().resource(GV.MWUrl + GV.MWUrlRemoveDataStream + dataStreamId.toString());
		// set http delete request to middleware MWUrlRemoveDataStream interface
		// to delete data stream id = dataStreamId
		resource.type(MediaType.APPLICATION_XML).delete(ClientResponse.class, null);
	}

	/**
	 * @param dataStreamId
	 * @return return data stream id if this data stream id still existed in the
	 *         data stream list return -1 if this data stream id is invalid or
	 *         certain data stream has expired
	 */
	public Boolean isValidDataStream(Integer dataStreamId)
	{
		if (GV.sensorDataMap.containsKey(dataStreamId))
		{
			// if data stream list include this data stream id as key.
			System.out.println("data stream id " + dataStreamId + " contained in the local list");

			// get sensor data according to data stream id
			SensorData data = GV.sensorDataMap.get(dataStreamId);

			// get data stream last updated timestamp.
			String lastUpdateTime = data.getDatetime();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String date = sdf.format(new java.util.Date());
			System.out.println("last update time = " + lastUpdateTime);
			System.out.println("current time = " + date);

			// if this data stream is updated in the same day, it's valid,
			// return true
			// else return false
			return date.equals(lastUpdateTime.substring(0, 10));
		}
		else
		{
			return false;
		}
	}

	/**
	 * @param request
	 * @return the datastream id as an integer. If there is no similar data
	 *         stream in the local data pool, return -1
	 */
	public Integer existedDataStream(PushSensorDataRequest request)
	{
		Iterator<Integer> it = GV.sensorDataMap.keySet().iterator();
		while (it.hasNext())
		{
			Integer flag = it.next();
			SensorData sd = GV.sensorDataMap.get(flag);
			if (sd.getLatitude().equals(request.getLatitude()) && sd.getLongitude().equals(request.getLongitude()))
			{
				// if same datastream has been registerd in the local memory
				return flag;
			}
		}
		return -1;

	}

}
