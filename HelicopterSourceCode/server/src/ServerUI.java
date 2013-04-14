
//package airline.system.resource;

import java.io.*;

public class ServerUI extends Thread implements ConstantSet
{

	public ServerUI()
	{

		processor = new ServerProcessor();
		command = new Command();

		fromUser = new BufferedReader(new InputStreamReader(System.in));
		toUser = new PrintWriter(new OutputStreamWriter(System.out), true);

		tempResult = new Result();
	}

	public boolean syntaxProcess(String r)
	{

		return commandFilter(r);
	}

	public String incorrectCommandCaution(String r)
	{

		String caution = new String("'" + r + "'" + " is not recognized as a valid command");
		return caution;
	}

	public boolean commandFilter(String r)
	{

		for (int i = 0; i < COMMAND.length; i++)
		{
			if (r.equalsIgnoreCase(COMMAND[i]))
			{
				if(i == VIEW_BULLETIN || i == VIEW_PENDING_REQUEST || i == AIRLINE_REQUEST)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		return false;
	}

	public boolean UIServerSQLProcessor(Command command)
	{

		String tempInquiry = new String();
		String[] tempArgv = new String[1];
		toUser.print("Please input the SQL you want to execute:");
		toUser.flush();
		try
		{
			tempInquiry = fromUser.readLine();
		}
		catch (IOException e)
		{
			System.out.println("error");
		}

		tempArgv[0] = tempInquiry;
		if (!tempInquiry.isEmpty())
		{
			command.setArgc(1);
			command.setCommand(SQL);
			command.setArgv(tempArgv);
			return true;
		}
		else
		{
			toUser.println("Empty string");
			return false;
		}
	}

	public boolean UIServerViewBulletin(Command command)
	{

		String tempInquiry = new String();
		String[] tempArgv = new String[1];

		tempArgv[0] = tempInquiry;

		command.setArgc(1);
		command.setCommand(VIEW_BULLETIN);
		command.setArgv(tempArgv);
		return true;
	}

	public boolean UIServerViewPendingRequest(Command command)
	{

		String tempInquiry = new String();
		String[] tempArgv = new String[1];

		tempArgv[0] = tempInquiry;

		command.setArgc(1);
		command.setCommand(VIEW_PENDING_REQUEST);
		command.setArgv(tempArgv);
		return true;
	}
	
	public boolean UIServerDeletePendingRequest(Command command)
	{

		String tempInquiry = new String();
		String[] tempArgv = new String[1];

		tempArgv[0] = tempInquiry;

		command.setArgc(1);
		command.setCommand(DELETE_PENDING_REQUEST);
		command.setArgv(tempArgv);
		return true;
	}
	
	public boolean UICompanyRequest(Command command)
	{
		String tempInquiry = new String();
		String[] tempArgv = new String[2];
		toUser.print("Please input the Your Company's name:");
		toUser.flush();
		try
		{
			tempInquiry = fromUser.readLine();
		}
		catch (IOException e)
		{
			System.out.println("error");
		}
		tempArgv[0] = tempInquiry;
		
		toUser.print("Please input the Your Company's password:");
		toUser.flush();
		try
		{
			tempInquiry = fromUser.readLine();
		}
		catch (IOException e)
		{
			System.out.println("error");
		}
		tempArgv[1] = tempInquiry;
		
		
		if (!tempInquiry.isEmpty())
		{
			command.setArgc(2);
			command.setCommand(AIRLINE_REQUEST);
			command.setArgv(tempArgv);
			return true;
		}
		else
		{
			toUser.println("Empty string");
			return false;
		}
	}

	public boolean commandPreProcess(String source, Command command)
	{

		boolean flag = false;

		for (int i = 0; i < COMMAND.length; i++)
		{
			if (source.equalsIgnoreCase(COMMAND[i]))
			{
				currOperation = i;
				break;
			}
		}

		System.out.println("operation   " + currOperation);
		switch (currOperation)
		{
			case SQL:
				flag = UIServerSQLProcessor(command);
				break;
			case VIEW_BULLETIN:
				flag = UIServerViewBulletin(command);
				break;
			case VIEW_PENDING_REQUEST:
				flag = UIServerViewPendingRequest(command);
				break;
			case DELETE_PENDING_REQUEST:
				flag = UIServerDeletePendingRequest(command);
				break;
			case AIRLINE_REQUEST:
				flag = UICompanyRequest(command);
				break;		
			default:
				flag = false;
				break;
		}
		return flag;
		//return false;
	}

	public void run()
	{

		while (true)
		{
			try
			{
				toUser.print("command:");
				toUser.flush();
				stringFromUser = fromUser.readLine();

				if (stringFromUser.length() == 0)
				{
					continue;
				}

				if (syntaxProcess(stringFromUser))
				{
					if (commandPreProcess(stringFromUser, command))
					{
						tempResult = new Result();
						if (processor.commandProcessor(command, tempResult))
						{
							toUser.println(tempResult.resultString);
						}
						else
						{
							toUser.println(tempResult.resultString);
							continue;
						}
					}
					else
					{
						toUser.println("Command Pre-Process Error.");
						continue;
					}
				}
				else
				{
					toUser.println(incorrectCommandCaution(stringFromUser));
					continue;
				}

			}
			catch (Exception e)
			{
				System.out.println("error");
				toUser.println("Exception Occurs");
			}
		}
	}
	
	private Command command;
	private ServerProcessor processor;
	private BufferedReader fromUser;
	private PrintWriter toUser;
	private Result tempResult;

	int currOperation;
	private String stringFromUser = new String();
	private String stringToUser = new String();

}
