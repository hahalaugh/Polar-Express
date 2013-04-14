/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package airline.system.resource;

/**
 * 
 * @author Juntao Gu <juntao.gu@gmail.com>
 */
public interface ConstantSet
{
	static public final int SERVER_CONNECTION = 0;
	static public final int TERMINAL = 1;
	static public final int DATA_INPUT = 2;
	static public final int BROWSING = 3;
	static public final int BROWSINGEND = 4;
	static public final int LOGIN = 0;
	static public final int LOGOFF = 1;
	static public final int VIEW_LOG = 2;
	static public final int MY_ACCOUNT = 3;
	static public final int FAQ = 4;
	static public final int CONTACT_SERVER = 5;
	static public final int SEARCH = 6;
	static public final int BOOK = 7;
	static public final int MODIFY = 8;
	static public final int CANCEL = 9;
	static public final int FLIGHT_TRACE = 10;
	static public final int CONNECT_SERVER = 11;
	static public final int USER_REGISTER = 12;
	static public final int AIRLINE_REQUEST = 13;
	static public final String[] COMMAND = {
		"login",
		"logoff",
		"view log",
		"my account",
		"?",
		"contact server",
		"search",
		"book",
		"modify",
		"cancel",
		"flight trace",
		"connect server",
		"register",
		"airline request"
		};

	static public final String[] COMMAND_TIPS = {
		"to login the system with your username and password",
		"to logout the system",
		"to view the error log(to be implemented)",
		"to display details include your account detail, ticket status and bulletin communication system",
		"to display the help documentation",
		"to contact the system administrator with bulletin system for help(like to amend the user information)",
		"to search the ticket",
		"to book the ticket",
		"to modify the ticket(to be implemented)",
		"to cancel the ticket",
		"to trace the current status of flight",
		"to connect to server, please do not invoke it manually",
		"to register as the system user. You have to be the system user before you want to execute most operations.",
		"to query the ticket records and statistical information of tickets selling."
	};
	static public final int NO_ERROR = 0;
	static public final int ERROR_SERVER_CONNECTION = 1;
	static public final int ERROR_DATABASE_OPERATION = 2;
	static public final int ERROR_UNKNOWN_COMMAND = 3;
	static public final String[] PROCESSOR_ERROR = { "No Error", "Server Connection Failed", "Database Operation Failed", "Unknown Command" };
	static public final int TICKET_CONFIRMATION_YES = 0;
	static public final int TICKET_CONFIRMATION_DENIED = 1;
	static public final int TICKET_CONFIRMATION_PENDING = 2;
	static public final int TICKET_CONFIRMATION_CANCELED = 3;
	static public final String[] TICKET_BOOKING_CONFIRMATION_STATUES = { "Yes", "Denied", "Pending", "Canceled" };

	static public final int PORT_NUM = 9527;
}
