/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package airline.system.server;

import java.text.SimpleDateFormat;
import java.util.*;
import java.net.*;
import java.io.*;
//import airline.system.resource.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

/**
 * 
 * @author Juntao Gu <juntao.gu@gmail.com>
 */
public class AirlineSystemServer extends ServerSocket
{
	private static final int PORT_NUM = 9527;

	public AirlineSystemServer() throws IOException
	{

		super(PORT_NUM);
		System.out.println("Standard edition server!");
		sqlProcessing sqlProcessor = new sqlProcessing();
		serverUI = new ServerUI();
		serverUI.start();
		System.out.println(this.getInetAddress().getLocalHost().getHostAddress());
		try
		{
			while (true)
			{
				Socket socket = this.accept();
				new NewServerThread(socket);
			}
		}
		catch (IOException e)
		{
			// e.printStackTrace();
			System.out.println("error");
		}
		finally
		{
			close();
		}
	}

	private class sqlProcessing extends Thread
	{
		public sqlProcessing()
		{

			initDBConnection();
			start();
		}

		public void run()
		{

			while (true)
			{
				if (timeToProcessSqlRequest())
				{
					if (processSqlRequest())
					{
						System.out.println("requests processed");
					}
					else
					{
						System.out.println("sql requests process failed");
					}
				}
				else if (timeToSynchronizeSubDatabas())
				{
					if (synchronizeSubDatabase())
					{
						System.out.println("Database synchronized");
					}
					else
					{
						System.out.println("Database synchronization failed");
					}
				}
				else
				{
					try
					{
						Thread.sleep(1000 * 1);
					}
					catch (Exception e)
					{
						// e.printStackTrace();
						System.out.println("");
					}

				}

			}
		}

		public boolean initDBConnection()
		{

			try
			{
				Class.forName("com.mysql.jdbc.Driver").newInstance(); // 加载mysq驱动
			}
			catch (ClassNotFoundException e)
			{
				System.out.println("DriverError");
				// e.printStackTrace();
				return false;
			}
			catch (Exception e)
			{
				System.out.println("error");
			}

			try
			{
				url = "jdbc:mysql://localhost:3306/airline_system_main_database";
				user = "root";
				password = "123456";

				conn = DriverManager.getConnection(url, user, password);
				stmt = conn.createStatement();
				
				subUrl = "jdbc:mysql://localhost:3306/airline_system_sub_database";
				subUser = "root";
				subPassword = "123456";

				subConn = DriverManager.getConnection(subUrl, subUser, subPassword);
				subStmt = subConn.createStatement();

			}
			catch (SQLException e)
			{
                                e.printStackTrace();
				System.out.println("error");
				return false;
			}
                        System.out.println("processing made");
			return false;
		}

		public boolean isTimeToBeProcessed(String sql)
		{
			
			Date currentDate = new Date(System.currentTimeMillis());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        java.sql.Time currentTime = new java.sql.Time(System.currentTimeMillis()); 
			if (sql.contains("Pending"))
			{
				String bookingDateTime = new String(sql.substring(sql.length() - 23, sql.length() - 4));
				//System.out.println(bookingDateTime);
				try
				{
					Date dateTime = sdf.parse(bookingDateTime);
					long secondDifference = currentDate.getTime() - dateTime.getTime();
					long minuteDifference = secondDifference / (60 * 1000);

					//if (minuteDifference >= 10)
                                        if (minuteDifference >= 2)
					{
						return true;
					}
					else
					{
						return false;
					}
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
				return false;
			}
			else
			{
				return true;
			}
		}

		public boolean timeToSynchronizeSubDatabas()
		{

			java.sql.Time executeTime = new java.sql.Time(System.currentTimeMillis());
			java.sql.Time currentTime = new java.sql.Time(System.currentTimeMillis());
			try
			{
				sql = "select arrivalTime from flight where departureTime >= CURRENT_TIME() and departure = 'City' ";

				rs = stmt.executeQuery(sql);

				if (rs.next())
				{
					executeTime = rs.getTime("arrivalTime");

					//if(executeTime.toString().equals(currentTime.toString()))
					if(currentTime.toString().substring(6, 8).equals("30"))
					{
						return true;
					}
				}
			}
			catch (SQLException e)
			{

				System.out.println("error");
			}
			return false;
		}

		public boolean timeToProcessSqlRequest()
		{

			java.sql.Time executeTime = new java.sql.Time(System.currentTimeMillis());
			java.sql.Time currentTime = new java.sql.Time(System.currentTimeMillis());
			try
			{
				sql = "select departureTime from flight where departureTime >= CURRENT_TIME() and departure = 'City' ";

				rs = stmt.executeQuery(sql);

				if (rs.next())
				{
					executeTime = rs.getTime("departureTime");

					//if (executeTime.toString().equals(currentTime.toString()))
					if(currentTime.toString().substring(6, 8).equals("00"))
					{
						return true;
					}
				}
			}
			catch (SQLException e)
			{

				System.out.println("error");
			}
			return false;
		}

		public boolean synchronizeSubDatabase()
		{
			try
			{
				while ((sql = subCancelingRequests.poll()) != null)
				{
					subStmt.executeUpdate(sql);
					System.out.println(sql + " processed sub!");
				}
	
				while ((sql = subSystemRequests.poll()) != null)
				{
					subStmt.executeUpdate(sql);
					System.out.println(sql + " processed sub!");
				}
	
				while ((sql = subBookingRequests.poll()) != null)
				{
					subStmt.executeUpdate(sql);
					System.out.println(sql + " processed sub!");
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			try
			{
				sql = "select distinct flightSequenceNumber from ticket";
				subRs = subStmt.executeQuery(sql);
				String tempFlightSequenceNumber = new String();
				while (subRs.next())
				{
					tempFlightSequenceNumber = new String(subRs.getString("flightSequenceNumber"));
					sql = "update ticket set bookConfirmation = 'Yes' where ticket.flightSequenceNumber = '" + tempFlightSequenceNumber + "' and "
							+ "(bookConfirmation = 'Pending' or bookConfirmation = 'Yes') order by ticket.bookTime limit 4";

					subTicketUpdateSqlRequest.offer(sql);
				}

			}
			catch (Exception e)
			{
				//System.out.println("4");
				e.printStackTrace();
				return false;
			}

			try
			{
				subConn.setAutoCommit(false);
				while ((sql = subTicketUpdateSqlRequest.poll()) != null)
				{
					subStmt.executeUpdate(sql);
				}
				subConn.commit();
				subConn.setAutoCommit(true);

			}
			catch (Exception e)
			{
				System.out.println("error");
				try
				{
					subConn.rollback();
					System.out.println("5");
					return false;
				}
				catch (SQLException sqle)
				{
					System.out.println("error");
				}
			}
			
			try
			{
				Thread.sleep(1000 * 1);
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				System.out.println("");
			}
			
			return true;
		}

		public boolean processSqlRequest()
		{

			try
			{
				conn.setAutoCommit(false);
				while ((sql = mainDatabaseSqlRequests.poll()) != null)
				{
					if (isTimeToBeProcessed(sql))
					{
						if (sql.contains("Pending"))
						{
							bookingRequests.offer(sql);
							subBookingRequests.offer(sql);
						}
						else if (sql.contains("Canceled"))
						{
							cancelingRequests.offer(sql);
							subCancelingRequests.offer(sql);
						}
						else
						{
							systemRequests.offer(sql);
							subSystemRequests.offer(sql);
						}
					}
					else
					{
						notNowRequests.offer(sql);
					}
				}

				while ((sql = cancelingRequests.poll()) != null)
				{
					stmt.executeUpdate(sql);
					System.out.println(sql + " processed!");
				}

				while ((sql = systemRequests.poll()) != null)
				{
					stmt.executeUpdate(sql);
					System.out.println(sql + " processed!");
				}

				while ((sql = bookingRequests.poll()) != null)
				{
					stmt.executeUpdate(sql);
					System.out.println(sql + " processed!");
				}

				while ((sql = notNowRequests.poll()) != null)
				{
					mainDatabaseSqlRequests.offer(sql);
					System.out.println(sql + " back to buffer!");
				}

				conn.commit();
				conn.setAutoCommit(true);
			}
			catch (Exception e)
			{
				System.out.println("error");
                                e.printStackTrace();
				try
				{
					System.out.println("1");
					conn.rollback();
					return false;
				}
				catch (SQLException sqle)
				{
					System.out.println("error");
					return false;
				}
			}

			// refresh the ticket records
			try
			{
				sql = "select distinct flightSequenceNumber from ticket";
				rs = stmt.executeQuery(sql);
				String tempFlightSequenceNumber = new String();
				while (rs.next())
				{
					tempFlightSequenceNumber = new String(rs.getString("flightSequenceNumber"));
					sql = "update ticket set bookConfirmation = 'Yes' where ticket.flightSequenceNumber = '" + tempFlightSequenceNumber + "' and "
							+ "(bookConfirmation = 'Pending' or bookConfirmation = 'Yes') order by ticket.bookTime limit 4";

					ticketUpdateSqlRequest.offer(sql);
				}
                                System.out.println("refreshed");
			}
			catch (Exception e)
			{
                                e.printStackTrace();
				System.out.println("4");
				return false;
			}

			try
			{
				conn.setAutoCommit(false);
				while ((sql = ticketUpdateSqlRequest.poll()) != null)
				{
					stmt.executeUpdate(sql);
				}
				conn.commit();
				conn.setAutoCommit(true);

			}
			catch (Exception e)
			{
				System.out.println("error");
				try
				{
					conn.rollback();
					System.out.println("5");
					return false;
				}
				catch (SQLException sqle)
				{
					System.out.println("error");
				}
			}
			
			try
			{
				Thread.sleep(1000 * 1);
			}
			catch (Exception e)
			{
				// e.printStackTrace();
				System.out.println("");
			}
			
			return true;

		}

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String url = null;
		String user = null;
		String password = null;
		String sql = null;
		
		Connection subConn = null;
		Statement subStmt = null;
		ResultSet subRs = null;
		String subUrl = null;
		String subUser = null;
		String subPassword = null;
		String subSql = null;
	}

	
	static public Queue<String> getMainDatabaseSqlRequests()
	{
	
		return mainDatabaseSqlRequests;
	}

	static public Queue<String> getNotNowRequests()
	{
	
		return notNowRequests;
	}

	private class NewServerThread extends Thread
	{
		public NewServerThread(Socket s) throws IOException
		{

			client = s;
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);

			out.println("welcome!");
			start();
		}

		public void run()
		{

			while (true)
			{
				try
				{

					receivedString = new String(in.readLine());
					out.println("Roger");
					mainDatabaseSqlRequests.offer(receivedString);
					System.out.println(receivedString + " received.To be processed");
				}
				catch (Exception e)
				{
					try
					{
						System.out.println("client " + client.getInetAddress().toString() + " disconnected");
						in.close();
						out.close();
						client.close();
						return;
					}
					catch (IOException ee)
					{
						System.out.println("IOerror");
					}
				}
			}
		}

		private Socket client;
		private BufferedReader in;
		private PrintWriter out;
		String receivedString = new String();
		// String receivedString_2 = new String();
	}

	
	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args)
	{

		// TODO code application logic here
		try
		{
			new AirlineSystemServer();
		}
		catch (IOException e)
		{
			// e.printStackTrace();
		}
	}

	private ServerSocket serverSocket;
	private ServerUI serverUI;

	static private Queue<String> mainDatabaseSqlRequests = new LinkedList<String>();

	private Queue<String> cancelingRequests = new LinkedList<String>();
	private Queue<String> bookingRequests = new LinkedList<String>();
	private Queue<String> systemRequests = new LinkedList<String>();

	static private Queue<String> notNowRequests = new LinkedList<String>();
	private Queue<String> ticketUpdateSqlRequest = new LinkedList<String>();
	
	private Queue<String> subCancelingRequests = new LinkedList<String>();
	private Queue<String> subBookingRequests = new LinkedList<String>();
	private Queue<String> subSystemRequests = new LinkedList<String>();
	private Queue<String> subTicketUpdateSqlRequest = new LinkedList<String>();
	

}
