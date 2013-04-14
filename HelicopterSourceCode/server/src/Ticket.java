/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package airline.system.resource;

import java.sql.*;

/**
 *
 * @author Juntao Gu <juntao.gu@gmail.com>
 */
public class Ticket
{
    public Ticket()
    {
        
    }
    
    public Ticket(long ticketSequenceNumber, String ticketUserName, String ticketUserPassportID, String ticketFlightDeparture, String ticketFlightArrival, Timestamp ticketBookTime, String ticketBookConfirmation, String ticketFlightID, String ticketFlightAirline, Date ticketFlightDepartureDate, Time ticketFlightDepatureTime, String ticketAccountName)
    {
        this.ticketSequenceNumber = ticketSequenceNumber;
        this.ticketUserName = ticketUserName;
        this.ticketUserPassportID = ticketUserPassportID;
        this.ticketFlightDeparture = ticketFlightDeparture;
        this.ticketFlightArrival = ticketFlightArrival;
        this.ticketBookTime = ticketBookTime;
        this.ticketBookConfirmation = ticketBookConfirmation;
        this.ticketFlightID = ticketFlightID;
        this.ticketFlightAirline = ticketFlightAirline;
        this.ticketFlightDepartureDate = ticketFlightDepartureDate;
        this.ticketFlightDepatureTime = ticketFlightDepatureTime;
        this.ticketAccountName = ticketAccountName;
    }

    public long getTicketSequenceNumber()
    {
        return ticketSequenceNumber;
    }

    public String getTicketUserName()
    {
        return ticketUserName;
    }

    public String getTicketUserPassportID()
    {
        return ticketUserPassportID;
    }

    public String getTicketFlightDeparture()
    {
        return ticketFlightDeparture;
    }

    public String getTicketFlightArrival()
    {
        return ticketFlightArrival;
    }

    public Timestamp getTicketBookTime()
    {
        return ticketBookTime;
    }

    public String getTicketBookConfirmation()
    {
        return ticketBookConfirmation;
    }

    public String getTicketFlightID()
    {
        return ticketFlightID;
    }

    public String getTicketFlightAirline()
    {
        return ticketFlightAirline;
    }

    public Date getTicketFlightDepartureDate()
    {
        return ticketFlightDepartureDate;
    }

    public Time getTicketFlightDepatureTime()
    {
        return ticketFlightDepatureTime;
    }

    public String getTicketAccountName()
    {
        return ticketAccountName;
    }

    public void setTicketSequenceNumber(long ticketSequenceNumber)
    {
        this.ticketSequenceNumber = ticketSequenceNumber;
    }

    public void setTicketUserName(String ticketUserName)
    {
        this.ticketUserName = ticketUserName;
    }

    public void setTicketUserPassportID(String ticketUserPassportID)
    {
        this.ticketUserPassportID = ticketUserPassportID;
    }

    public void setTicketFlightDeparture(String ticketFlightDeparture)
    {
        this.ticketFlightDeparture = ticketFlightDeparture;
    }

    public void setTicketFlightArrival(String ticketFlightArrival)
    {
        this.ticketFlightArrival = ticketFlightArrival;
    }

    public void setTicketBookTime(Timestamp ticketBookTime)
    {
        this.ticketBookTime = ticketBookTime;
    }

    public void setTicketBookConfirmation(String ticketBookConfirmation)
    {
        this.ticketBookConfirmation = ticketBookConfirmation;
    }

    public void setTicketFlightID(String ticketFlightID)
    {
        this.ticketFlightID = ticketFlightID;
    }

    public void setTicketFlightAirline(String ticketFlightAirline)
    {
        this.ticketFlightAirline = ticketFlightAirline;
    }

    public void setTicketFlightDepartureDate(Date ticketFlightDepartureDate)
    {
        this.ticketFlightDepartureDate = ticketFlightDepartureDate;
    }

    public void setTicketFlightDepatureTime(Time ticketFlightDepatureTime)
    {
        this.ticketFlightDepatureTime = ticketFlightDepatureTime;
    }

    public void setTicketUserAccountName(String ticketAccountName)
    {
        this.ticketAccountName = ticketAccountName;
    }

    @Override
    public String toString()
    {
        return "ticketSequenceNumber:" + ticketSequenceNumber + "\n"
                + "ticketUserName:" + ticketUserName + "\n"
                + "ticketUserPassportID: " + ticketUserPassportID + "\n"
                + "ticketFlightDeparture: " + ticketFlightDeparture + "\n"
                + "ticketFlightArrival: " + ticketFlightArrival + "\n"
                + "ticketBookTime: " + ticketBookTime + "\n"
                + "ticketBookConfirmation: " + ticketBookConfirmation + "\n"
                + "ticketFlightID: " + ticketFlightID + "\n"
                + "ticketFlightAirline: " + ticketFlightAirline + "\n"
                + "ticketFlightDepartureDate: " + ticketFlightDepartureDate + "\n"
                + "ticketFlightDepatureTime: " + ticketFlightDepatureTime + "\n"
                + "ticketAccountName: " + ticketAccountName + "\n";
    }
    
    private long ticketSequenceNumber;
    private String ticketUserName;
    private String ticketUserPassportID;
    private String ticketFlightDeparture;
    private String ticketFlightArrival;
    private Timestamp ticketBookTime;
    private String ticketBookConfirmation;
    private String ticketFlightID;
    private String ticketFlightAirline;
    private Date ticketFlightDepartureDate;
    private Time ticketFlightDepatureTime;
    private String ticketAccountName;  
}
