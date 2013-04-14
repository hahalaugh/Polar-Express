/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package airline.system.resource;

import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author Juntao Gu <juntao.gu@gmail.com>
 */
public class ClientProcessor implements ConstantSet
{
	public ClientProcessor()
	{

		tempResult = new Result();
		currentUser = new User();
		ticket = new Ticket();
		bulletin = new Bulletin();
		flight = new Flight();

		sender = new Sender();
	}

	public boolean timeToSendRequests()
	{

		java.sql.Time executeTime = new java.sql.Time(System.currentTimeMillis());
		java.sql.Time currentTime = new java.sql.Time(System.currentTimeMillis());
		try
		{
			sql = "select departureTime from flight where departureTime >= CURRENT_TIME() and departure = 'Camp' ";

			rs = stmt.executeQuery(sql);

			if (rs.next())
			{
				executeTime = rs.getTime("departureTime");

				if (executeTime.toString().equals(currentTime.toString()))
				//if(currentTime.toString().substring(6, 8).equals("30"))
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

	private class Sender extends Thread
	{
		public Sender()
		{

			start();
		}

		public void run()
		{

			while (true)
			{
				if (rs != null)
				{
					if (timeToSendRequests())
					{
						while (ClientProcessor.requestsToBeSent.peek() != null)
						{
							try
							{
								toServer.println(ClientProcessor.requestsToBeSent.poll());
								socket.setSoTimeout(50000);
								if (!fromServer.readLine().equals("Roger"))
								{
									continue;
								}
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
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
							e.printStackTrace();
						}
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
						e.printStackTrace();
					}
				}
			}
		}

	}

	boolean loginProcessor(Command command, Result result)
	{

		String tempUserName = command.getArgv()[0];
		String tempPassword = command.getArgv()[1];

		if (isLogin)
		{
			if (tempUserName.equals(currentUser.getUserAccountName()))
			{
				result.resultString = new String("You already login");
			}
			else
			{
				result.resultString = new String("user " + currentUser.getUserName() + " is login.");
			}
			return false;
		}
		else
		{
			try
			{
				sql = "select * from User where accountName = '" + tempUserName + "' and accountPassword = '" + tempPassword + "'";
				rs = stmt.executeQuery(sql);
				if (rs.next())
				{
					currentUser.setUserID(rs.getInt("ID"));
					currentUser.setUserAccountName(rs.getString("accountName"));
					currentUser.setUserPassword(rs.getString("accountPassword"));
					currentUser.setUserName(rs.getString("name"));
					currentUser.setUserPassportID(rs.getString("passportID"));
					currentUser.setUserEmailAddress(rs.getString("emailAddress"));

					isLogin = true;

					result.resultString = new String("Welcome back " + currentUser.getUserName() + "!");
					return true;
				}
				else
				{
					result.resultString = new String("Incorrect username or password! Login failed");
					return false;
				}
			}
			catch (SQLException e)
			{
				// e.printStackTrace();
				System.out.println("error");
			}
			return false;
		}
	}

	boolean logoffProcessor(Command command, Result result)
	{

		if (!isLogin)
		{
			result.resultString = new String("No one is login.");
			return false;
		}
		else
		{
			result.resultString = new String("See you " + currentUser.getUserName());
			currentUser = new User();
			isLogin = false;

			return true;
		}
	}

	boolean airlineRequestProcessor(Command command, Result result)
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

					sql = "select count(*) from ticket where bookConfirmation = 'Yes' and flightAirline = '" + tempUsername + "'";
					rs = stmt.executeQuery(sql);
					if (rs.next())
					{
						bookedTickets = rs.getInt("count(*)");
					}

					sql = "select count(*) from ticket where bookConfirmation = 'Pending' and flightAirline = '" + tempUsername + "'";
					rs = stmt.executeQuery(sql);
					if (rs.next())
					{
						pendingTickets = rs.getInt("count(*)");
					}

					sql = "select count(*) from ticket where bookConfirmation = 'canceled' and flightAirline = '" + tempUsername + "'";
					rs = stmt.executeQuery(sql);
					if (rs.next())
					{
						canceledTickets = rs.getInt("count(*)");
					}

					sql = "select count(*) from ticket where bookConfirmation = 'Yes' and (flightDepartureDate < curDate() or (flightDepartureTime = curdate() and flightDepartureTime < curtime()) and flightAirline = '"
							+ tempUsername + "')";
					rs = stmt.executeQuery(sql);
					if (rs.next())
					{
						ticketsExpired = rs.getInt("count(*)");
					}

					sql = "select flightID from ticket where flightAirline = '" + tempUsername + "' group by flightID order by count(flightID) DESC";
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
						ticket.setTicketBookDateTime(rs.getTimestamp("bookTime"));
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

	boolean userRegisterProcessor(Command command, Result result)
	{

		String[] tempArgv = command.getArgv();

		String userName = tempArgv[0];
		String userPassword = tempArgv[1];
		String name = tempArgv[2];
		String emailAddress = tempArgv[3];
		String passportNumber = tempArgv[4];

		String tempUserName = new String();

		String tempSql = new String();

		try
		{
			sql = "select accountName from user";
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				tempUserName = rs.getString("accountName");

				if (tempUserName.equals(userName))
				{
					result.resultString = new String("username" + userName + "has been registered");
					return false;
				}
			}
		}
		catch (SQLException e)
		{
			// e.printStackTrace();
			System.out.println("error");
		}

		tempSql = "'" + userName + "','" + userPassword + "','" + name + "','" + emailAddress + "','" + passportNumber + "'";
		sql = "insert into User(accountName,accountPassword,name,emailAddress,passportID) values(" + tempSql + ")";

		requestsToBeSent.offer(sql);
		result.resultString = new String("To see the response please use myaccount command.Your request will be processed soon");
		/*
		 * try { toServer.println(sql); socket.setSoTimeout(50000); if
		 * (fromServer.readLine().equals("Roger")) { result.resultString = new
		 * String(
		 * "To see the response please use myaccount command.Your request will be processed soon"
		 * ); return true; } } catch (SocketException e) { result.resultString =
		 * new String("Connection Error"); return false; // e.printStackTrace();
		 * } catch (IOException e) { // e.printStackTrace();
		 * System.out.println("error"); }
		 */

		return true;
	}

	boolean viewLogProcessor(Command command, Result result)
	{

		result.resultString = new String("Not implemented.");
		return false;
	}

	boolean myAccountProcessor(Command command, Result result)
	{

		if (isLogin)
		{
			if (command.getArgv()[0].equals(currentUser.getUserPassword()))
			{
				try
				{
					sql = "select * from User where accountName = '" + currentUser.getUserAccountName() + "'";
					rs = stmt.executeQuery(sql);
					if (rs.next())
					{
						currentUser.setUserID(rs.getInt("ID"));
						currentUser.setUserAccountName(rs.getString("accountName"));
						currentUser.setUserPassword(rs.getString("accountPassword"));
						currentUser.setUserName(rs.getString("name"));
						currentUser.setUserPassportID(rs.getString("passportID"));
						currentUser.setUserEmailAddress(rs.getString("emailAddress"));

						result.resultString = new String("Your account details are as follows:\n");
						result.resultString += currentUser.toString();
					}
					else
					{
						result.resultString = new String("Incorrect username or password! Login failed");
						return false;
					}

					sql = "select * from ticket where accountName = '" + currentUser.getUserAccountName() + "'";
					rs = stmt.executeQuery(sql);

					result.resultString += new String("\nYour tickets details are as follows\n");
					while (rs.next())
					{
						ticket.setTicketBookConfirmation(rs.getString("bookConfirmation"));
						ticket.setTicketBookDateTime(rs.getTimestamp("bookTime"));
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

					sql = "select * from bulletin where answerTo = '" + currentUser.getUserAccountName() + "'";
					rs = stmt.executeQuery(sql);

					result.resultString += new String("\nYour bulletin details are as follows\n");
					while (rs.next())
					{
						bulletin.setBulletinAnswer(rs.getString("answer"));
						bulletin.setBulletinAnswerFrom(rs.getString("answerFrom"));
						bulletin.setBulletinAnswerNumber(rs.getInt("answerNumber"));
						bulletin.setBulletinAnswerTo(rs.getString("answerTo"));
						bulletin.setBulletinQuestion(rs.getString("question"));

						result.resultString += new String(bulletin.toString() + "\n");
					}
					return true;
				}
				catch (SQLException e)
				{
					// e.printStackTrace();
					System.out.println("error");
				}
				return false;
			}
			else
			{
				result.resultString = new String("Incorrect password.");
				return false;
			}
		}
		result.resultString = new String("No user login.Please login first.");
		return false;
	}

	boolean faqProcessor(Command command, Result result)
	{

		int i = 0;
		result.resultString += new String("Input these commands:\n");
		for (i = 0; i < COMMAND.length; i++)
		{
			result.resultString += COMMAND[i] + ":	" + COMMAND_TIPS[i] + "\n";
		}
		return true;
	}

	boolean contactServerProcessor(Command command, Result result)
	{

		// int tempArgc = command.getArgc();
		String tempArgv = new String(command.getArgv()[0]);

		if (isLogin)
		{
			sql = "Insert into bulletin(answerTo,question) values('" + currentUser.getUserAccountName() + "','" + tempArgv + "')";
			requestsToBeSent.offer(sql);
			result.resultString = new String("To see the answer please use myaccount command.Your question will be answered in 24H.");
			return true;
			/*
			 * toServer.println(sql); try { socket.setSoTimeout(50000); if
			 * (fromServer.readLine().equals("Roger")) { result.resultString =
			 * new String(
			 * "To see the answer please use myaccount command.Your question will be answered in 24H."
			 * ); return true; } } catch (SocketException e) {
			 * result.resultString = new String("Connection Error"); return
			 * false; } catch (IOException e) { result.resultString = new
			 * String("IO Error"); return false; } result.resultString = new
			 * String("Unknown Error"); return false;
			 */
		}
		else
		{
			result.resultString = new String("Please login first.");
			return false;
		}

	}

	boolean searchProcessor(Command command, Result result)
	{

		String[] tempArgv = command.getArgv();

		String tempDeparture = new String(tempArgv[0]);
		String tempArrival = new String(tempArgv[1]);
		// String tempSingleOrRound = new String(tempArgv[2]);
		String tempDepartureDate = new String(tempArgv[3]);
		// String tempReturnDepartureDate = new String(tempArgv[4]);

		try
		{
			java.util.Date currentDate = new java.util.Date(System.currentTimeMillis());
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String dateStr = formatter.format(currentDate);

			if (tempDepartureDate.equals(dateStr))
			{
				sql = "select * from flight where (" + "departure = " + "'" + tempDeparture + "' and " + "arrival = " + "'" + tempArrival
						+ "' and departureTime > curtime())";
			}
			else
			{
				sql = "select * from flight where (" + "departure = " + "'" + tempDeparture + "' and " + "arrival = " + "'" + tempArrival + "')";
			}
			rs = stmt.executeQuery(sql);

			result.resultString = new String("The available flight details are as follows:\n");
			while (rs.next())
			{
				flight.setFlightID(rs.getString("ID"));
				flight.setFlightAirline(rs.getString("airline"));
				flight.setFlightArrival(rs.getString("arrival"));
				flight.setFlightArrivalTime(rs.getTime("arrivalTime"));
				flight.setFlightDeparture(rs.getString("departure"));
				flight.setFlightDepartureTime(rs.getTime("departureTime"));
                                

				result.resultString += new String(flight.toString() + "\n");
			}
			return true;
		}
		catch (SQLException e)
		{
			// result.resultString = new String(" sql unknown error");
			e.printStackTrace();
			return false;
		}
	}

	boolean bookProcessor(Command command, Result result)
	{

		String[] tempArgv = command.getArgv();

		// java.sql.Timestamp currentTime = new
		// java.sql.Timestamp(System.currentTimeMillis());

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// �����ʽ
		java.sql.Timestamp now = new java.sql.Timestamp(System.currentTimeMillis());// ��ȡϵͳ��ǰʱ��
		String time = df.format(now);
		java.sql.Timestamp currentTime = java.sql.Timestamp.valueOf(time);

		System.out.println(currentTime.toString());

		String tempFlightID = tempArgv[0];
		tempFlightID = tempFlightID.toUpperCase();
		String tempFlightDate = tempArgv[1];

		java.util.Date date = new java.util.Date();
		java.sql.Date flightDate = new java.sql.Date(date.getTime());

		try
		{
			java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat("yyyy-MM-dd");
			java.util.Date xdate = formater.parse(tempFlightDate);
			flightDate = new java.sql.Date(xdate.getTime());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		if (isLogin)
		{
			try
			{
				sql = "select * from flight where ID = " + "'" + tempFlightID + "'";
				rs = stmt.executeQuery(sql);

				ticket = new Ticket();
				ticket.setTicketAccountName("null");
				while (rs.next())
				{
					ticket.setTicketUserAccountName(currentUser.getUserAccountName());
					ticket.setTicketBookDateTime(currentTime);
					ticket.setTicketFlightAirline(rs.getString("airline"));
					ticket.setTicketFlightArrival(rs.getString("arrival"));
					ticket.setTicketFlightDeparture(rs.getString("departure"));
					ticket.setTicketFlightDepartureDate(flightDate);
					ticket.setTicketFlightDepatureTime(rs.getTime("departureTime"));
					ticket.setTicketFlightID(rs.getString("ID"));
					ticket.setTicketUserName(currentUser.getUserName());
					ticket.setTicketUserPassportID(currentUser.getUserPassportID());
				}

				if (ticket.getTicketFlightID().equals("null"))
				{
					result.resultString = new String("cannot find flight:" + tempFlightID);
					return false;
				}
			}
			catch (SQLException e)
			{
				// e.printStackTrace();
				System.out.println("error");
			}

			try
			{
				String fmt = "yyyyMMdd";
				SimpleDateFormat sdf = new SimpleDateFormat(fmt);
				String dateStr = sdf.format(ticket.getTicketFlightDepartureDate());
				String flightSequenceNumber = dateStr + ticket.getTicketFlightID();

				sql = "Insert into ticket (accountName,flightSequenceNumber,flightAirline,flightArrival,flightDeparture,flightDepartureDate,"
						+ "flightDepartureTime,flightID,name,passportID,bookConfirmation,bookTime)" + " values('"
						+ ticket.getTicketAccountName()
						+ "','"
						+ flightSequenceNumber
						+ "','"
						+ ticket.getTicketFlightAirline()
						+ "','"
						+ ticket.getTicketFlightArrival()
						+ "','"
						+ ticket.getTicketFlightDeparture()
						+ "','"
						+ ticket.getTicketFlightDepartureDate()
						+ "','"
						+ ticket.getTicketFlightDepatureTime()
						+ "','"
						+ ticket.getTicketFlightID()
						+ "','"
						+ ticket.getTicketUserName()
						+ "','"
						+ ticket.getTicketUserPassportID() + "','Pending','" + ticket.getTicketBookDateTime() + "')";

				requestsToBeSent.offer(sql);
				result.resultString = new String("To see the booking result please use 'my account' command.Your request will be processed soon.");
				return true;
				/*
				 * toServer.println(sql);
				 * 
				 * socket.setSoTimeout(50000); if
				 * (fromServer.readLine().equals("Roger")) { result.resultString
				 * = new String(
				 * "To see the booking result please use 'my account' command.Your request will be processed soon."
				 * ); return true; } } catch (SocketException e) {
				 * result.resultString = new String("Connection Error"); return
				 * false; // e.printStackTrace(); } catch (IOException e) { //
				 * e.printStackTrace(); result.resultString = new
				 * String("IO Error"); return false; } result.resultString = new
				 * String("Unknown Error"); return false;
				 */
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return false;
			}
		}
		else
		{
			result.resultString = new String("Please login first.");
			return false;
		}

	}

	boolean modifyProcessor(Command command, Result result)
	{

		result.resultString = new String("Not implemented");
		return false;
	}

	boolean cancelProcessor(Command command, Result result)
	{

		String[] tempArgv = command.getArgv();

		// java.sql.Time currentTime = new
		// java.sql.Time(System.currentTimeMillis());
		String tempTicketSequenceNumber = tempArgv[0];

		String tempFlightID = new String();
		java.sql.Date tempFlightDate = new java.sql.Date(1);

		if (isLogin)
		{
			try
			{
				sql = "select * from ticket where (sequenceNumber = " + tempTicketSequenceNumber + " and (bookConfirmation = 'Yes')) "
						+ " and ( TO_DAYS(flightDepartureDate) - TO_DAYS(CURDATE())) >= 1 and accountName = '" + currentUser.getUserAccountName() + "'";
				System.out.println(sql);
				rs = stmt.executeQuery(sql);

				while (rs.next())
				{
					tempFlightDate = rs.getDate("flightDepartureDate");
					tempFlightID = rs.getString("flightID");
				}

				if (tempFlightID.isEmpty() || tempFlightDate.toString().isEmpty())
				{
					result.resultString = new String("No such ticket found or the cancel deadline expires\n");
					result.resultString += "Only the ticket with a Yes flag could be canceled and please check my account for details";
					return false;
				}

				sql = "update ticket set bookConfirmation = 'Canceled' where sequenceNumber = " + tempTicketSequenceNumber;
				requestsToBeSent.offer(sql);
				result.resultString = new String("To see the booking result please use 'my account' command.Your request will be processed soon.");
				return true;
				/*
				 * toServer.println(sql);
				 * 
				 * if (fromServer.readLine().equals("Roger")) {
				 * result.resultString = new String(
				 * "To see the booking result please use 'my account' command.Your request will be processed soon."
				 * ); return true; }
				 * 
				 * } catch (SQLException e) { // e.printStackTrace();
				 * result.resultString = " sql Unknown Error"; return false; }
				 * catch (IOException e) { result.resultString =
				 * " io Unknown Error"; // e.printStackTrace(); }
				 */}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			result.resultString = new String("Please Login First");
			return false;
		}
		return false;
	}

	boolean flightTraceProcessor(Command command, Result result)
	{

		String flightID = command.getArgv()[0];
		String tempDepartureTime = "";
		String tempArrivalTime = "";
		result.resultString = new String("Flight " + flightID);
		sql = "select * from flight where ID = '" + flightID + "'";

		try
		{
			rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				tempDepartureTime = rs.getTime("departureTime").toString();
				tempArrivalTime = rs.getTime("arrivalTime").toString();
			}
			else
			{
				result.resultString += new String(" does not exist.");
				return false;
			}

			java.util.Calendar t = java.util.Calendar.getInstance();
			java.util.Date time = t.getTime();

			String tempTime = time.toString();

			if (tempTime.compareTo(tempDepartureTime) < 0)
			{
				result.resultString += new String(" Not departured");
				return true;
			}
			else
			{
				if (tempTime.compareTo(tempDepartureTime) > 0 && tempTime.compareTo(tempArrivalTime) < 0)
				{
					result.resultString += new String(" flying!");
					return true;
				}
				else
				{
					result.resultString += new String(" landed");
					return true;
				}
			}

		}
		catch (Exception e)
		{
			// e.printStackTrace();
		}

		result.resultString = new String("ERROR");
		return false;
	}

	boolean connectToServer(Command command, Result result)
	{

		// int argc = command.getArgc();
		String[] s = command.getArgv();

		String ipAddr = s[0];

		try
		{
			socket = new Socket(ipAddr, portNumber);
			fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			toServer = new PrintWriter(socket.getOutputStream(), true);

			if (!fromServer.readLine().isEmpty())
			{
				if (initDatabaseConnection(socket.getInetAddress().toString()))
				{
					System.out.println("database connection done!");
				}

				result.resultString = new String("No Error");
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (ConnectException e)
		{
			// e.printStackTrace();
			System.out.println("Connection error");
			return false;
		}
		catch (UnknownHostException e)
		{
			// e.printStackTrace();
			System.out.println("IPerror");
			return false;
		}
		catch (IOException e)
		{
			// e.printStackTrace();
			System.out.println("IOerror");
			return false;
		}
	}

	public boolean initDatabaseConnection(String IPAddress)
	{

		System.out.println(IPAddress);
		String tempIPAddress = new String(IPAddress.substring(1, IPAddress.length()));
		System.out.println(tempIPAddress);
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("LINKING ERROR");
			// e.printStackTrace();
			return false;
		}
		catch (Exception e)
		{
			// e.printStackTrace();
			System.out.println("error");
		}

		try
		{
			//url = "jdbc:mysql://" + tempIPAddress + ":3306/airline_system_sub_database";
                        url = "jdbc:mysql://localhost:3306/airline_system_sub_database";
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
			// e.printStackTrace();
			System.out.println("error");
			return false;
		}
		return false;
	}

	boolean checkNextSendTime(Date nextSendTime)
	{

		return true;
	}

	public boolean commandProcessor(Command command, Result result)
	{

		Command tempCommand = new Command(command);
		boolean flag = false;
		// System.out.println(tempCommand.toString());
		switch (tempCommand.getCommand())
		{
			case LOGIN:
				flag = loginProcessor(tempCommand, result);
				break;
			case LOGOFF:
				flag = logoffProcessor(tempCommand, result);
				break;
			case VIEW_LOG:
				flag = viewLogProcessor(tempCommand, result);
				break;
			case MY_ACCOUNT:
				flag = myAccountProcessor(tempCommand, result);
				break;
			case FAQ:
				flag = faqProcessor(tempCommand, result);
				break;
			case CONTACT_SERVER:
				flag = contactServerProcessor(tempCommand, result);
				break;
			case SEARCH:
				flag = searchProcessor(tempCommand, result);
				break;
			case BOOK:
				flag = bookProcessor(tempCommand, result);
				break;
			case MODIFY:
				flag = modifyProcessor(tempCommand, result);
				break;
			case CANCEL:
				flag = cancelProcessor(tempCommand, result);
				break;
			case FLIGHT_TRACE:
				flag = flightTraceProcessor(tempCommand, result);
				break;
			case CONNECT_SERVER:
				flag = connectToServer(tempCommand, result);
				break;
			case USER_REGISTER:
				flag = userRegisterProcessor(tempCommand, result);
				break;
			case AIRLINE_REQUEST:
				flag = airlineRequestProcessor(tempCommand, result);
				break;
			default:
				flag = false;
				break;
		}
		return flag;
	}

	static public BufferedReader getFromServer()
	{

		return fromServer;
	}

	static public PrintWriter getToServer()
	{

		return toServer;
	}

	// Variables for database operation
	static Connection conn = null;
	Statement stmt = null;
	ResultSet rs = null;
	String url = null;
	String user = null;
	String password = null;
	String sql = null;
	// Variables for Socket Communication
	private int portNumber = PORT_NUM;
	private Socket socket;
	// Variables for IO operation
	static private BufferedReader fromServer = null;
	static private PrintWriter toServer = null;
	Result tempResult;
	private boolean isLogin = false;
	private Ticket ticket;
	User currentUser;
	Bulletin bulletin;
	Flight flight;
	Sender sender;
	static public Queue<String> requestsToBeSent = new LinkedList<String>();
}
