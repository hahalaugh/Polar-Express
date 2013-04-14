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
public class Flight
{

    public Flight()
    {
        
    }
    
    public Flight(String flightSequenceNumber, String flightID, String flightAirline, Date flightDate, String flightDeparture, Time flightDepartureTime, String flightArrival, Time flightArrivalTime, int flightSeatsNumber, int flightBookedSeatsNumber)
    {
        this.flightSequenceNumber = flightSequenceNumber;
        this.flightID = flightID;
        this.flightAirline = flightAirline;
        this.flightDate = flightDate;
        this.flightDeparture = flightDeparture;
        this.flightDepartureTime = flightDepartureTime;
        this.flightArrival = flightArrival;
        this.flightArrivalTime = flightArrivalTime;
        this.flightSeatsNumber = flightSeatsNumber;
        this.flightBookedSeatsNumber = flightBookedSeatsNumber;
    }

    public String getFlightSequenceNumber()
    {
        return flightSequenceNumber;
    }

    public String getFlightID()
    {
        return flightID;
    }

    public String getFlightAirline()
    {
        return flightAirline;
    }

    public Date getFlightDate()
    {
        return flightDate;
    }

    public String getFlightDeparture()
    {
        return flightDeparture;
    }

    public Time getFlightDepartureTime()
    {
        return flightDepartureTime;
    }

    public String getFlightArrival()
    {
        return flightArrival;
    }

    public Time getFlightArrivalTime()
    {
        return flightArrivalTime;
    }

    public int getFlightSeatsNumber()
    {
        return flightSeatsNumber;
    }

    public int getFlightBookedSeatsNumber()
    {
        return flightBookedSeatsNumber;
    }

    public void setFlightSequenceNumber(String flightSequenceNumber)
    {
        this.flightSequenceNumber = flightSequenceNumber;
    }

    public void setFlightID(String flightID)
    {
        this.flightID = flightID;
    }

    public void setFlightAirline(String flightAirline)
    {
        this.flightAirline = flightAirline;
    }

    public void setFlightDate(Date flightDate)
    {
        this.flightDate = flightDate;
    }

    public void setFlightDeparture(String flightDeparture)
    {
        this.flightDeparture = flightDeparture;
    }

    public void setFlightDepartureTime(Time flightDepartureTime)
    {
        this.flightDepartureTime = flightDepartureTime;
    }

    public void setFlightArrival(String flightArrival)
    {
        this.flightArrival = flightArrival;
    }

    public void setFlightArrivalTime(Time flightArrivalTime)
    {
        this.flightArrivalTime = flightArrivalTime;
    }

    public void setFlightSeatsNumber(int flightSeatsNumber)
    {
        this.flightSeatsNumber = flightSeatsNumber;
    }

    public void setFlightBookedSeatsNumber(int flightBookedSeatsNumber)
    {
        this.flightBookedSeatsNumber = flightBookedSeatsNumber;
    }

    @Override
    public String toString()
    {
        return "flightSequenceNumber: " + flightSequenceNumber+ "\n"
                + "flightAirline: " + flightAirline + "\n"
                + "flightDate: " + flightDate.toString() + "\n"
                + "flightDeparture: " + flightDeparture + "\n"
                + "flightDepartureTime: " + flightDepartureTime.toString() + "\n"
                + "flightArrival: " + flightArrival + "\n"
                + "flightArrivalTime: " + flightArrivalTime.toString() +"\n";
    }
    
    private String flightSequenceNumber;
    private String flightID;
    private String flightAirline;
    private Date flightDate;
    private String flightDeparture;
    private Time flightDepartureTime;
    private String flightArrival;
    private Time flightArrivalTime;
    private int flightSeatsNumber;
    private int flightBookedSeatsNumber;

}
