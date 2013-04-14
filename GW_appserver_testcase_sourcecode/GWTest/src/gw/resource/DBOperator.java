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

	public static Boolean isValidLoginRequest(LoginRequest request)
	{
		if (request.getPassword().isEmpty() || request.getUsername().isEmpty())
		{
			return false;
		}
		else if (!isExistedUser(request.getUsername()))
		{
			return false;
		}
		else
		{
			return true;
		}

	}

	public static Boolean isValidRegisterRequest(RegisterRequest request)
	{
		if (request.getUsername().isEmpty() || request.getPassword().isEmpty()
				|| request.getName().isEmpty() || request.getEmail().isEmpty())
		{
			return false;
		}
		else if (DBOperator.isExistedUser(request.getUsername()))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public static Boolean isValidUser(String username, String password)
	{
		LoginRequest request = new LoginRequest(username, password);
		if (!isValidLoginRequest(request))
		{
			return false;
		}
		else
		{
			String sql = "select username, password from members";
			try
			{
				rs = stmt.executeQuery(sql);
				while (rs.next())
				{
					if (username.equals(rs.getString("username"))
							&& password.equals(rs.getString("password")))
					{
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

	public static Boolean isExistedUser(String username)
	{
		String sql = "select username from members";
		try
		{
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				if (username.equals(rs.getString("username")))
				{
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

	public static Boolean register(RegisterRequest request)
	{
		if (!isValidRegisterRequest(request))
		{
			return false;
		}
		else
		{
			String ability = parseStringArrayToString(request.getAbility());

			String sql = "insert members (username, password, name, email, ability) values('"
					+ request.getUsername() + "','" + request.getPassword() + "','"
					+ request.getName() + "','" + request.getEmail() + "','" + ability + "')";

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

	public static Boolean createCommunity(CreateCommunityRequest request)
	{
		if (request.getCommunityName().isEmpty() || request.getLocation().isEmpty()
				|| request.getUsername().isEmpty())
		{
			return false;
		}
		else if (isExistedCommunity(request.getCommunityName()))
		{
			return false;
		}
		else
		{
			String sql = "insert community (name, location, founder) values ('"
					+ request.getCommunityName() + "','" + request.getLocation() + "','"
					+ request.getUsername() + "')";

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

	public static Boolean createTask(CreateTaskRequest request)
	{
		if (isExistedTask(request.getTaskName()))
		{
			return false;
		}
		else
		{
			String sql = "insert task (name, description) values ('" + request.getTaskName()
					+ "','" + request.getTaskDescription() + "')";
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

	public static Boolean isExistedLand(String location)
	{
		String sql = "select location from land where location = '" + location + "'";
		try
		{
			rs = stmt.executeQuery(sql);
			return rs.next();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
	}

	public static Boolean isValidSubmitLandRequest(SubmitLandRequest request)
	{
		if (request.getUsername().isEmpty() || request.getLocation().isEmpty())
		{
			return false;
		}
		else if (isExistedLand(request.getLocation()))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public static SubmitLandResponse submitLand(SubmitLandRequest request)
	{
		if (!isValidSubmitLandRequest(request))
		{
			return new SubmitLandResponse(false);
		}
		else
		{
			String sql = "insert land (location, uploader) values ('" + request.getLocation()
					+ "','" + request.getUsername() + "')";

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

	public static ProfileResponse getUserProfile(String username)
	{
		if (!isExistedUser(username))
		{
			return new ProfileResponse();
		}
		else
		{
			String sql = "select username, password, email, ability from members where request = '"
					+ username + "'";

			try
			{
				rs = stmt.executeQuery(sql);
				if (rs.next())
				{
					String password = rs.getString("password");
					String email = rs.getString("email");
					String[] ability = parseStringToStringArray(rs.getString("ability"));
					return new ProfileResponse(username, password, email, ability);
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
		String[] task = new String[nameList.size()];
		String[] description = new String[descriptionList.size()];

		for (int i = 0; i < nameList.size(); i++)
		{
			task[i] = nameList.get(i);
			description[i] = descriptionList.get(i);
		}

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
				result += "|";
			}
		}
		return result;
	}

	public static String[] parseStringToStringArray(String target)
	{
		return target.split("|");
	}

	public static ViewSubmittedLandResponse getLandInfo()
	{
		String sql = "select * from land";
		ArrayList<String> locationList = new ArrayList<String>();
		ArrayList<String> founderList = new ArrayList<String>();

		String[] location = new String[locationList.size()];
		String[] founder = new String[locationList.size()];
		try
		{
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				locationList.add(rs.getString("location"));
				founderList.add(rs.getString("founder"));
			}

			for (int i = 0; i < locationList.size(); i++)
			{
				location[i] = locationList.get(i);
				founder[i] = founderList.get(i);
			}

			return new ViewSubmittedLandResponse(location, founder);
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

	private String url = null;
	private String user = null;
	private String password = null;

}
