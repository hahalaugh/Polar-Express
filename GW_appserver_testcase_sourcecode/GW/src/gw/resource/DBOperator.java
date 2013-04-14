package gw.resource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class DBOperator
{
	public DBOperator()
	{
		initDBConnection();
	}

	public boolean initDBConnection()
	{
		try
		{
			// Initialise the jdbc driver
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("DriverError");
			e.printStackTrace();
			return false;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		try
		{
			url = "jdbc:mysql://localhost:3306/greenwatch";
			user = "root";
			password = "root";

			// connect to db driver
			conn = DriverManager.getConnection(url, user, password);
			stmt = conn.createStatement();
			extraStmt = conn.createStatement();
			if (conn != null)
			{
				System.out.println("Connect to Main Database Successfully!");
				return true;
			}

		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * @param request
	 * @return a Boolean value indicates if this is a valid login request. a
	 *         valid login request should include both non-empty username and
	 *         password and also it should be pre registered in the database.
	 */
	public static Boolean isValidLoginRequest(LoginRequest request)
	{
		if (request.getPassword().isEmpty() || request.getUsername().isEmpty())
		{
			return false;
		}
		else if (!isExistedUser(request.getUsername()))
		{
			// if not a existed user in the database
			return false;
		}
		else
		{
			return true;
		}

	}

	/**
	 * @param request
	 * @return a Boolean value indicates if this is a valid register request A
	 *         valid register request should include all non-empty fields in the
	 *         request body.
	 * 
	 */
	public static Boolean isValidRegisterRequest(RegisterRequest request)
	{
		if (request.getUsername().isEmpty() || request.getPassword().isEmpty() || request.getName().isEmpty()
				|| request.getEmail().isEmpty())
		{
			return false;
		}
		else if (DBOperator.isExistedUser(request.getUsername()))
		{
			// if the register request has same username with records in the
			// database.
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * @param username
	 * @param password
	 * @return a Boolean value indicates if this is a request from valid user.
	 */
	public static Boolean isValidUser(String username, String password)
	{
		LoginRequest request = new LoginRequest(username, password);
		if (!isValidLoginRequest(request))
		{
			// return false if not a valid login request.
			return false;
		}
		else
		{
			// Fetch corresponding user information from database and match.
			String sql = "select username, password from member";
			try
			{
				rs = stmt.executeQuery(sql);
				while (rs.next())
				{
					if (username.equals(rs.getString("username")) && password.equals(rs.getString("password")))
					{
						// if a user with same pairs of username and password
						// found, valid user request.
						return true;
					}
				}
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			return false;
		}
	}

	/**
	 * @param username
	 * @return a Boolean value indicates if user with certain username has been
	 *         registered in the database
	 */
	public static Boolean isExistedUser(String username)
	{
		String sql = "select username from member";
		try
		{
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				if (username.equals(rs.getString("username")))
				{
					// if this username existed in the database
					return true;
				}
			}
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * @param request
	 * @return a Boolean value indicates if register successfully
	 */
	public static Boolean register(RegisterRequest request)
	{
		if (!isValidRegisterRequest(request))
		{
			// if not a valid register return false
			return false;
		}
		else
		{
			// Insert new member informations
			String sql = "insert member (username, password, name, email, capacity) values('" + request.getUsername()
					+ "','" + request.getPassword() + "','" + request.getName() + "','" + request.getEmail() + "','"
					+ request.getAbility() + "')";

			try
			{
				stmt.execute(sql);
				return true;
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
	}

	/**
	 * @param taskname
	 * @return a boolean value indicates if the task is existed in the database
	 */
	public static Boolean isExistedTask(String taskname)
	{
		String sql = "select name from task where name = '" + taskname + "'";
		try
		{
			rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				return true;
			}

		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * @param communityName
	 * @return a boolean value indicates if this community exsited
	 */
	public static Boolean isExistedCommunity(String communityName)
	{
		String sql = "select name from community";
		ArrayList<String> list = new ArrayList<String>();
		try
		{
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				list.add(rs.getString("name"));
			}
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return list.contains(communityName);
	}

	/**
	 * @param request
	 * @return a boolean value indicates the community generated successfully or
	 *         not.
	 */
	public static Boolean createCommunity(CreateCommunityRequest request)
	{
		if (request.getCommunityName().isEmpty() || request.getLocation().isEmpty() || request.getUsername().isEmpty())
		{
			// if request not valid
			System.out.println("something is empty");
			System.out.println("community name = " + request.getCommunityName());
			System.out.println("username = " + request.getUsername());
			System.out.println("location = " + request.getLocation());
			return false;
		}
		else if (isExistedCommunity(request.getCommunityName()))
		{
			// if community name existed in the db
			System.out.println("Community Existed");
			return false;
		}
		else
		{
			// create the community
			String sql = "insert community (name, location, founder) values ('" + request.getCommunityName() + "','"
					+ request.getLocation() + "','" + request.getUsername() + "')";

			try
			{
				stmt.execute(sql);
				System.out.println("Insert community finished");
				return true;
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				System.out.println("Insert community error");
				e.printStackTrace();
				return false;
			}
		}
	}

	/**
	 * @param request
	 * @return
	 * a boolean value indicates creation of a new task is successful or not
	 */
	public static Boolean createTask(CreateTaskRequest request)
	{
		System.out.println("new task creation request received for" + request.getTaskName());
		if (isExistedTask(request.getTaskName()))
		{
			//if task name existed then return false
			return false;
		}
		else
		{
			//insert the task
			String sql = "insert task (name, description) values ('" + request.getTaskName() + "','"
					+ request.getTaskDescription() + "')";
			try
			{
				stmt.execute(sql);
				return true;
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
	}

	/**
	 * @param location
	 * @return
	 * a boolean value indicates if this land name existed in the db.
	 */
	public static Boolean isExistedLand(String location)
	{
		String sql = "select location from land where location = '" + location + "'";
		try
		{
			rs = stmt.executeQuery(sql);
			//return if the query has result.
			return rs.next();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
	}

	/**
	 * @param request
	 * @return
	 * a boolean value indicates if this land submission request is valid
	 */
	public static Boolean isValidSubmitLandRequest(SubmitLandRequest request)
	{
		if (request.getUsername().isEmpty() || request.getLocation().isEmpty())
		{
			//if there is empty field
			return false;
		}
		else if (isExistedLand(request.getLocation()))
		{
			//if the same name existed 
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * @param request
	 * @return
	 * a SubmitLandResponse type value(a boolean essentially) indicates if the inserting land operation 
	 * is successful. 
	 */
	public static SubmitLandResponse submitLand(SubmitLandRequest request)
	{
		if (!isValidSubmitLandRequest(request))
		{
			//if not a valid submit land request
			return new SubmitLandResponse(false);
		}
		else
		{
			//insert the land info
			String sql = "insert land (location, uploader) values ('" + request.getLocation() + "','"
					+ request.getUsername() + "')";

			try
			{
				stmt.execute(sql);
				return new SubmitLandResponse(true);
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new SubmitLandResponse(false);
			}
		}
	}

	/**
	 * @param username
	 * @return a ProfileResponse value include username, password, emailaddr and
	 *         ability information
	 */
	public static ProfileResponse getUserProfile(String username)
	{
		if (!isExistedUser(username))
		{
			// return nothing but a null response.
			return new ProfileResponse();
		}
		else
		{
			//valid user, fetch user information from db.
			String sql = "select username, password, email, capacity from member where username = '" + username + "'";

			try
			{
				rs = stmt.executeQuery(sql);
				if (rs.next())
				{
					String password = rs.getString("password");
					String email = rs.getString("email");
					String capacity = rs.getString("capacity");

					// return the initialized response
					return new ProfileResponse(username, password, email, capacity);
				}
				else
				{
					return new ProfileResponse();
				}
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new ProfileResponse();
			}
		}

	}

	/**
	 * @return
	 * a CommunityInfoResponse type return value include all the community information from database
	 */
	public static CommunityInfoResponse getCommunityInfo()
	{
		String sql = "select distinct location from community";
		String result = new String();
		ArrayList<String> commList = new ArrayList<String>();
		try
		{
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				//add result to an arraylist. Later it will be processed as string splited by ","
				commList.add(rs.getString("location"));
			}
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//add , as indicator to split the string.
		Iterator<String> it = commList.iterator();
		while (it.hasNext())
		{
			result += it.next();
			if (it.hasNext())
			{
				result += ",";
			}
		}

		return new CommunityInfoResponse(result);
	}

	/**
	 * @return
	 * a value with type of TaskResponse include all the task information from database
	 */
	public static TaskResponse getTaskInfo()
	{
		String sql = "select * from task";
		ArrayList<String> nameList = new ArrayList<String>();
		ArrayList<String> descriptionList = new ArrayList<String>();
		try
		{
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				nameList.add(rs.getString("name"));
				descriptionList.add(rs.getString("description"));
			}
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String task = new String();
		String description = new String();

		// add , as indicator to split the string task name
		Iterator<String> it = nameList.iterator();
		while (it.hasNext())
		{
			task += it.next();
			if (it.hasNext())
			{
				task += ",";
			}
		}

		//add , as indicator to split the string task description
		it = descriptionList.iterator();
		while (it.hasNext())
		{
			description += it.next();
			if (it.hasNext())
			{
				description += ",";
			}
		}
		System.out.println("name list is :" + nameList.toString());
		System.out.println("description list is :" + descriptionList.toString());

		return new TaskResponse(task, description);
	}

	public static String parseStringArrayToString(String[] target)
	{
		String result = new String();
		for (int i = 0; i < target.length; i++)
		{
			result += target[i];
			if (i < target.length - 1)
			{
				result += ",";
			}
		}
		return result;
	}

	public static String[] parseStringToStringArray(String target)
	{
		return target.split(",");
	}

	/**
	 * @return
	 * ViewSubmittedLandResponse type value include all the land information from database
	 */
	public static ViewSubmittedLandResponse getLandInfo()
	{
		String sql = "select * from land";
		ArrayList<String> locationList = new ArrayList<String>();
		ArrayList<String> uploaderList = new ArrayList<String>();

		String location = new String();
		String uploader = new String();
		try
		{
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				locationList.add(rs.getString("location"));
				uploaderList.add(rs.getString("uploader"));
			}

			//add , as indicator to split the string location
			Iterator<String> it = locationList.iterator();
			while (it.hasNext())
			{
				location += it.next();
				if (it.hasNext())
				{
					location += ",";
				}
			}
			
			//add , as indicator to split the string uploader
			it = uploaderList.iterator();
			while (it.hasNext())
			{
				uploader += it.next();
				if (it.hasNext())
				{
					uploader += ",";
				}
			}

			System.out.println("location information is transfered: " + location);
			return new ViewSubmittedLandResponse(location, uploader);
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ViewSubmittedLandResponse(null, null);
		}
	}

	public static Connection conn = null;
	public static Statement stmt = null;
	public static ResultSet rs = null;

	public static Statement extraStmt = null;
	public static ResultSet extraRs = null;

	public static DBOperator dbOperator = new DBOperator();
	private String url = null;
	private String user = null;
	private String password = null;

}
