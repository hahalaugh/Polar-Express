/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package airline.system.resource;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;


/**
 *
 * @author Juntao Gu <juntao.gu@gmail.com>
 */
public class ClientUI extends Thread implements ConstantSet
{
    public ClientUI()
    {
        processor = new ClientProcessor();
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
        String caution = new String("'" + r + "'" + " is not recognized as a valid command.Input ? for usage");
        return caution;
    }

    public boolean UILoginProcessor(Command command)
    {
        String tempUsername = null;
        String tempPassword = null;
        String[] tempArgv = new String[2];
        try
        {
            toUser.print("Please input your username:");
            toUser.flush();
            tempUsername = new String(fromUser.readLine());
            toUser.print("Please input your password:");
            toUser.flush();
            tempPassword = new String(fromUser.readLine());
        }
        catch (IOException e)
        {
            //e.printStackTrace();
        	System.out.println("error");
        }

        tempArgv[0] = new String(tempUsername);
        tempArgv[1] = new String(tempPassword);

        command.setArgc(2);
        command.setCommand(LOGIN);
        command.setArgv(tempArgv);

        if (!command.getArgv()[0].isEmpty())
        {
            return true;
        }

        return false;
    }

    public boolean UILogoffProcessor(Command command)
    {
        String tempAnswer = new String();
        toUser.print("Are you sure to logoff? Y/N");
        toUser.flush();

        try
        {
            tempAnswer = fromUser.readLine();
        }
        catch (IOException e)
        {
            System.out.println("error");
        }
        if (tempAnswer.equals("Y") || tempAnswer.equals("y"))
        {
            command.setArgc(0);
            command.setCommand(LOGOFF);

            return true;
        }
        else
        {
            if (tempAnswer.equals("N") || tempAnswer.equals("n"))
            {
                toUser.println("Logoff canceled");
                return false;
            }
            else
            {
                toUser.println(tempAnswer + "is not a valid option");
                return false;
            }
        }
    }

    public boolean UIUserRegister(Command command)
    {
        String tempUsername = null;
        String tempPassword = null;
        String tempName = null;
        String tempEmailAddress = null;
        String tempPassportNumber = null;
        String[] tempArgv = new String[5];

        try
        {
            toUser.print("Please input your username:");
            toUser.flush();
            tempUsername = new String(fromUser.readLine());
            
            toUser.print("Please input your password:");
            toUser.flush();
            tempPassword = new String(fromUser.readLine());
            
            toUser.print("Please input your name:");
            toUser.flush();
            tempName = new String(fromUser.readLine());
            
            toUser.print("Please input your email address:");
            toUser.flush();
            tempEmailAddress = new String(fromUser.readLine());
            
            toUser.print("Please input your passport number:");
            toUser.flush();
            tempPassportNumber = new String(fromUser.readLine());
            
        }
        catch (IOException e)
        {
            System.out.println("error");
        }

        tempArgv[0] = new String(tempUsername);
        tempArgv[1] = new String(tempPassword);
        tempArgv[2] = new String(tempName);
        tempArgv[3] = new String(tempEmailAddress);
        tempArgv[4] = new String(tempPassportNumber);
        
        command.setArgc(5);
        command.setCommand(USER_REGISTER);
        command.setArgv(tempArgv);

        if (!command.getArgv()[0].isEmpty())
        {
            return true;
        }

        return false;
    }

    public boolean UIAirlineRequest(Command command)
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
    public boolean UIViewLogProcessor(Command command)
    {
        command.setArgc(0);
        command.setCommand(VIEW_LOG);
        return true;
    }

    public boolean UIMyAccountProcessor(Command command)
    {
        String tempPassword = null;
        String[] tempArgv = new String[1];

        try
        {
            toUser.print("Please input the password of the current login account:");
            toUser.flush();
            tempPassword = new String(fromUser.readLine());
        }
        catch (IOException e)
        {
            System.out.println("error");
        }
        
        tempArgv[0] = tempPassword;
        
        command.setArgc(1);
        command.setCommand(MY_ACCOUNT);
        command.setArgv(tempArgv);
        
        if (!command.getArgv()[0].isEmpty())
        {
            return true;
        }
        return false;
    }

    public boolean UIFaqProcessor(Command command)
    {
        command.setArgc(0);
        command.setCommand(FAQ);
        return true;
    }

    public boolean UIContactServerProcessor(Command command)
    {
        String tempInquiry = new String();
        String[] tempArgv = new String[1];
        toUser.print("Please input your question or request:");
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
            command.setCommand(CONTACT_SERVER);
            command.setArgv(tempArgv);
            return true;
        }
        else
        {
            toUser.println("Empty string");
            return false;
        }
    }

    public boolean UISearchProcessor(Command command)
    {

        String tempInquiry = new String();
        String[] tempArgv = new String[12];
        toUser.println("Please follow the instruction to search the ticket.");
        toUser.println("Input * for any condition");

        try
        {
            while (true)
            {
                toUser.print("Departure:");
                toUser.flush();
                tempInquiry = "";
                tempInquiry = fromUser.readLine();
                if (!tempInquiry.isEmpty())
                {
                    tempArgv[0] = new String(tempInquiry);
                    break;
                }
            }

            while (true)
            {
                toUser.print("Arrival:");
                toUser.flush();
                tempInquiry = "";
                tempInquiry = fromUser.readLine();
                if (!tempInquiry.isEmpty())
                {
                    tempArgv[1] = new String(tempInquiry);
                    break;
                }
            }
            
            while (true)
            {
                toUser.print("Departure on(YYYY-MM-DD):");
                toUser.flush();
                tempInquiry = fromUser.readLine();
                if (dateFilter(tempInquiry))
                {
                    tempArgv[3] = new String(tempInquiry);
                    break;
                }
                toUser.println("Date format error or Date error.Please input the correct date.");
            }
        }
        catch (IOException e)
        {
            System.out.println("error");
        }

        command.setArgc(5);
        command.setCommand(SEARCH);
        command.setArgv(tempArgv);

        if (!tempArgv[0].isEmpty())
        {
            return true;
        }
        return false;
    }

    public boolean UIBookProcessor(Command command)
    {
    	// p1 FLIGHT ID
    	// p2 FLIGHT DATE
        String tempInquiry = new String();
        String[] tempArgv = new String[2];
        try
        {
            toUser.print("Please input the FlightID number you want to book:");
            toUser.flush();
            tempInquiry = fromUser.readLine();
            tempArgv[0] = tempInquiry;
            while(true)
            {
            	toUser.print("Please input the date of flight you want to book:");
            	toUser.flush();
            	tempInquiry = fromUser.readLine();
            	if(!dateFilter(tempInquiry))
            	{
            		toUser.println("Not a valid date,please enter again");
            	}
            	else
            	{
            		tempArgv[1] = tempInquiry;
            		break;
            	}
            }
        }
        catch (IOException e)
        {
            System.out.println("error");
        }

        
        if (!tempInquiry.isEmpty())
        {
            command.setArgc(2);
            command.setCommand(BOOK);
            command.setArgv(tempArgv);
            return true;
        }
        else
        {
            toUser.println("Empty string");
            return false;
        }
    }

    public boolean UIModifyProcessor(Command command)
    {
        String tempInquiry = new String();
        String[] tempArgv = new String[1];
        toUser.print("Please input the flight ticket's sequence number you want to modify:");
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
            command.setCommand(MODIFY);
            command.setArgv(tempArgv);
            return true;
        }
        else
        {
            toUser.println("Empty string");
            return false;
        }
    }

    public boolean UICancelProcessor(Command command)
    {
        String tempInquiry = new String();
        String[] tempArgv = new String[1];
        toUser.print("Please input the flight ticket's sequence number you want to cancle:");
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
            command.setCommand(CANCEL);
            command.setArgv(tempArgv);
            return true;
        }
        else
        {
            toUser.println("Empty string");
            return false;
        }
    }

    public boolean UIFlightTraceProcessor(Command command)
    {
        String tempInquiry = new String();
        String[] tempArgv = new String[1];
        toUser.print("Please input the flight you want to trace:");
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
            command.setCommand(FLIGHT_TRACE);
            command.setArgv(tempArgv);
            return true;
        }
        else
        {
            toUser.println("Empty string");
            return false;
        }
    }

    public boolean UIConnectToServer(String s)
    {
        String[] tempArgv = new String[16];

        tempArgv[0] = s;
        int tempArgc = 1;


        toUser.println("Connecting...");
        command = new Command(CONNECT_SERVER, tempArgv, tempArgc);

        if (processor.commandProcessor(command, tempResult))
        {
            toUser.println("Reach Server " + s + " Successfully!");
            return true;
        }
        return false;
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

        //System.out.println("operation   " + currOperation);
        switch (currOperation)
        {
            case LOGIN:
                flag = UILoginProcessor(command);
                break;
            case LOGOFF:
                flag = UILogoffProcessor(command);
                break;
            case VIEW_LOG:
                flag = UIViewLogProcessor(command);
                break;
            case MY_ACCOUNT:
                flag = UIMyAccountProcessor(command);
                break;
            case FAQ:
                flag = UIFaqProcessor(command);
                break;
            case CONTACT_SERVER:
                flag = UIContactServerProcessor(command);
                break;
            case SEARCH:
                flag = UISearchProcessor(command);
                break;
            case BOOK:
                flag = UIBookProcessor(command);
                break;
            case MODIFY:
                flag = UIModifyProcessor(command);
                break;
            case CANCEL:
                flag = UICancelProcessor(command);
                break;
            case FLIGHT_TRACE:
                flag = UIFlightTraceProcessor(command);
                break;
            case USER_REGISTER:
                flag = UIUserRegister(command);
                break;
            case AIRLINE_REQUEST:
            	flag = UIAirlineRequest(command);
            	break;
            case CONNECT_SERVER:
                flag = UIConnectToServer(source);
                break;
            default:
                flag = false;
                break;
        }
        return flag;
    }

    public boolean isRoundTripDateValid(String dateDeparture, String dateReturn)
    {
        int yearDeparture = Integer.parseInt(dateDeparture.substring(0, 4));
        int yearReturn = Integer.parseInt(dateReturn.substring(0, 4));
        int monthDeparture = Integer.parseInt(dateDeparture.substring(5, 7));
        int monthReturn = Integer.parseInt(dateReturn.substring(5, 7));
        int dayDeparture = Integer.parseInt(dateDeparture.substring(8, 10));
        int dayReturn = Integer.parseInt(dateReturn.substring(8, 10));

        if (dateDeparture.equals("*") || dateReturn.equals("*"))
        {
            return true;
        }
        if ((yearDeparture >= yearReturn) && (monthDeparture <= monthReturn) && (dayDeparture <= dayReturn))
        {
            return true;
        }
        return false;
    }

    public boolean ipAddressFilter(String r)
    {
        String regex = "^\\s*" + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)" + "\\s*$";
        if (r.matches(regex))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean dateFilter(String r)
    {
        String regex = "^\\s*" + "(201[2-9]|20[2-9][0-9]|2[1-9][0-9][0-9])-(0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-9]|3[0-1])" + "\\s*$";
        if (r.matches(regex) || r.equals("*"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean commandFilter(String r)
    {
        for (int i = 0; i < COMMAND.length; i++)
        {
            if (r.equalsIgnoreCase(COMMAND[i]))
            {
                return true;
            }
        }
        return false;
    }

    public ClientProcessor getProcessor()
	{
	
		return processor;
	}

	public BufferedReader getFromUser()
    {
        return fromUser;
    }

    public PrintWriter getToUser()
    {
        return toUser;
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
                //System.out.println("error");
                toUser.println("Exception Occurs");
            }
        }
    }
    private ClientProcessor processor;
    private int currOperation;
    //private int errorTypeFromProcessor;
    private Command command;
    private BufferedReader fromUser;
    private PrintWriter toUser;
    private String stringFromUser = new String();
    private String stringToUser = new String();
    private String stringFromProcessor = new String();
    private String stringToProcessor = new String();
    private Result tempResult;
}