//package airline.system.resource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Queue;
import java.io.*;

public class ServerProcessor implements ConstantSet
{
	public ServerProcessor()
	{

		if (initDatabaseConnection())
		{
			write.println("BackGround database operator connection made!");
		}
	}

	public boolean initDatabaseConnection()
	{

		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("LINKING ERROR");
			System.out.println("error");
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

			if (conn != null)
			{
				stmt = conn.createStatement();
				return true;
			}
		}
		catch (SQLException e)
		{
			System.out.println("error");
			return false;
		}
		return false;
	}

	boolean sqlProcessor(Command command, Result result)
	{

		String tempSql = command.getArgv()[0];

		String select = "^\\s*" + "select.++" + "\\s*$";

		if (tempSql.matches(select))
		{
			try
			{
				rs = stmt.executeQuery(tempSql);
				while (rs.next())
				{
					result.resultString = new String("no select could be executed");
					return true;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				stmt.executeUpdate(tempSql);
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				result.resultString = new String("Database sql execution error");
				return false;
			}
		}

		return true;

	}

	boolean bulletinProcessor(Command command, Result result)
	{

		sql = "select * from bulletin";
		String tempBulletin = new String();
		String tempAnswer = new String();
		tempBulletin = new String("bulletin details:\n");
		try
		{
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				bulletin.setBulletinAnswerNumber(rs.getInt("answerNumber"));
				bulletin.setBulletinAnswer(rs.getString("answer"));
				bulletin.setBulletinAnswerFrom(currentUser.getUserAccountName());
				bulletin.setBulletinAnswerTo(rs.getString("answerTo"));
				bulletin.setBulletinQuestion(rs.getString("question"));

				tempBulletin += bulletin.toString() + "\n";
			}
		}
		catch (SQLException e)
		{
			result.resultString = new String("DATABASE QUERY ERROR");
			System.out.println("error");
			return false;
		}

		write.println(tempBulletin);
		write.println("Please input the number of question you want to answer, 0 to exit");
		int tempBulletinNumber = 0;
		try
		{
			String numberString = new String(read.readLine());
			try
			{
				tempBulletinNumber = Integer.parseInt(numberString);
			}
			catch (NumberFormatException ee)
			{
				write.println("Not a valid input");
				return false;
			}

			if (tempBulletinNumber == 0)
			{
				write.println("exit");
				return false;
			}
			else
			{
				write.println("please input the answer to the question:");
				tempAnswer = new String(read.readLine());

				if (tempAnswer.isEmpty())
				{
					write.println("empty string");
					return false;
				}

				sql = "update bulletin set answer = '" + tempAnswer + "', answerFrom = '" + currentUser.getUserAccountName() + "' where answerNumber = "
						+ tempBulletinNumber;

				write.println(sql);
				stmt.executeUpdate(sql);
				return true;
			}
		}
		catch (Exception e)
		{
			System.out.println("error");
			return false;
		}
	}

	boolean viewPendingRequestProcessor(Command command, Result result)
	{

		Queue<String> requestsInBuf = AirlineSystemServer.getMainDatabaseSqlRequests();
		
		System.out.println("Pending requests in the city buffer");
		for (String x : requestsInBuf)
		{
			System.out.println(x);
		}
		
		return true;
	}

	boolean deletePendingRequestProcessor(Command command, Result result)
	{

		write.println("please input the sql number you want to delete:");
		try
		{
			int tempSqlNumber = Integer.parseInt(read.readLine());
			if (tempSqlNumber == 0)
			{
				write.println("no such sql");
				return false;
			}

			sql = "delete from pendingSql where sqlNumber = " + tempSqlNumber;
			stmt.executeUpdate(sql);
		}
		catch (Exception e)
		{
			System.out.println("error");
			write.println("error");
			return false;
		}
		return true;
	}

	boolean companyRequestProcessor(Command command, Result result)
	{

		String tempUsername = command.getArgv()[0];
		String tempPassword = command.getArgv()[1];

		int bookedTickets = 0;
		int canceledTickets = 0;
		int pendingTickets = 0;
		int ticketsExpired = 0;
		float seatsOccupancy = 0;
		String rushFlight = "";
		String mostFrequentClient = "";

		try
		{
			sql = "select * from user where accountName = '" + tempUsername + "' and accountPassword = '" + tempPassword + "'";
			rs = stmt.executeQuery(sql);

			while (rs.next())
			{
				tempUsername = new String(rs.getString("accountName"));
				if (tempUsername.equals("JAL") || tempUsername.equals("ETIHAD") || tempUsername.equals("AA"))
				{
					result.resultString = new String("Ticket information of your company as follows:\n\n");

					sql = "select count(*) from ticket where bookConfirmation = 'Yes' and flightAirline = '"+tempUsername+"'";
					rs = stmt.executeQuery(sql);
					if (rs.next())
					{
						bookedTickets = rs.getInt("count(*)");
					}

					sql = "select count(*) from ticket where bookConfirmation = 'Pending' and flightAirline = '"+tempUsername+"'";
					rs = stmt.executeQuery(sql);
					if (rs.next())
					{
						pendingTickets = rs.getInt("count(*)");
					}

					sql = "select count(*) from ticket where bookConfirmation = 'canceled' and flightAirline = '"+tempUsername+"'";
					rs = stmt.executeQuery(sql);
					if (rs.next())
					{
						canceledTickets = rs.getInt("count(*)");
					}

					sql = "select count(*) from ticket where bookConfirmation = 'Yes' and (flightDepartureDate < curDate() or (flightDepartureTime = curdate() and flightDepartureTime < curtime()) and flightAirline = '"+tempUsername+"')";
					rs = stmt.executeQuery(sql);
					if (rs.next())
					{
						ticketsExpired = rs.getInt("count(*)");
					}

					sql = "select flightID from ticket where flightAirline = '"+tempUsername+"' group by flightID order by count(flightID) DESC";
					rs = stmt.executeQuery(sql);
					if (rs.next())
					{
						rushFlight = new String(rs.getString("flightID"));
					}

					result.resultString += new String("bookedTickets: " + bookedTickets + "\n");
					result.resultString += new String("pendingTickets: " + pendingTickets + "\n");
					result.resultString += new String("canceledTickets: " + canceledTickets + "\n");
					result.resultString += new String("ticketsExpired: " + ticketsExpired + "\n");
					result.resultString += new String("rushFlight: " + rushFlight + "\n\n\n");

					sql = "select * from ticket where flightAirline = '" + tempUsername + "'";
					rs = stmt.executeQuery(sql);

					while (rs.next())
					{
						ticket.setTicketBookConfirmation(rs.getString("bookConfirmation"));
						ticket.setTicketBookTime(rs.getTimestamp("bookTime"));
						ticket.setTicketUserAccountName(rs.getString("accountName"));
						ticket.setTicketFlightAirline(rs.getString("flightAirline"));
						ticket.setTicketFlightArrival(rs.getString("flightArrival"));
						ticket.setTicketFlightDeparture(rs.getString("flightDeparture"));
						ticket.setTicketFlightDepartureDate(rs.getDate("flightDepartureDate"));
						ticket.setTicketFlightDepatureTime(rs.getTime("flightDepartureTime"));
						ticket.setTicketFlightID(rs.getString("flightID"));
						ticket.setTicketSequenceNumber(rs.getInt("sequenceNumber"));
						ticket.setTicketUserName(rs.getString("name"));
						ticket.setTicketUserPassportID(rs.getString("passportID"));

						result.resultString += ticket.toString() + "\n";
						ticket = new Ticket();

					}
					return true;
				}
				else
				{
					result.resultString = new String("Not a valid airline name");
					return false;
				}
			}

		}
		catch (Exception e)
		{
			System.out.println("error");
		}

		return false;
	}

	public boolean commandProcessor(Command command, Result result)
	{

		Command tempCommand = new Command(command);
		boolean flag = false;
		// System.out.println(tempCommand.toString());
		switch (tempCommand.getCommand())
		{
			case SQL:
				flag = sqlProcessor(tempCommand, result);
				break;
			case VIEW_PENDING_REQUEST:
				flag = viewPendingRequestProcessor(tempCommand, result);
				break;
			case VIEW_BULLETIN:
				flag = bulletinProcessor(tempCommand, result);
				break;
			case DELETE_PENDING_REQUEST:
				flag = deletePendingRequestProcessor(tempCommand, result);
				break;
			case AIRLINE_REQUEST:
				flag = companyRequestProcessor(tempCommand, result);
				break;
			default:
				flag = false;
				break;
		}
		return flag;
	}

	// Variables for database operation
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	String url = null;
	String user = null;
	String password = null;
	String sql = null;

	private BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
	private PrintWriter write = new PrintWriter(new OutputStreamWriter(System.out), true);

	private User currentUser = new User(1, "root", "root", "Juntao Gu", "guju@tcd.ie", "G36688286");
	private Ticket ticket = new Ticket();
	private Bulletin bulletin = new Bulletin();
	private Flight flight = new Flight();
}
