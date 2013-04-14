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
    static public final int SQL = 13;
    static public final int VIEW_BULLETIN = 14;
    static public final int VIEW_PENDING_REQUEST = 15;
    static public final int DELETE_PENDING_REQUEST = 16;
    static public final int AIRLINE_REQUEST= 17;
    
    static public final String[] COMMAND =
    {
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
        "sqlupdate",
        "view bulletin",
        "view pending request",
        "delete pending request",
        "airline request"
    };
    static public final int NO_ERROR = 0;
    static public final int ERROR_SERVER_CONNECTION = 1;
    static public final int ERROR_DATABASE_OPERATION = 2;
    static public final int ERROR_UNKNOWN_COMMAND = 3;
    static public final String[] PROCESSOR_ERROR =
    {
        "No Error",
        "Server Connection Failed",
        "Database Operation Failed",
        "Unknown Command"
    };
    static public final int TICKET_CONFIRMATION_YES = 0;
    static public final int TICKET_CONFIRMATION_NO = 1;
    static public final int TICKET_CONFIRMATION_PENDING = 2;
    static public final int TICKET_CONFIRMATION_CANCELED = 3;
    static public final String[] TICKET_BOOKING_CONFIRMATION_STATUES = 
    {
        "Yes",
        "Deny",
        "Pending" ,
        "Canceled"
    };
    
    static public final int PORT_NUM = 9527;
}
