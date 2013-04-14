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

	
	public Flight(String flightID, String flightAirline, String flightDeparture, Time flightDepartureTime, String flightArrival, Time flightArrivalTime)
	{

		super();
		this.flightID = flightID;
		this.flightAirline = flightAirline;
		this.flightDeparture = flightDeparture;
		this.flightDepartureTime = flightDepartureTime;
		this.flightArrival = flightArrival;
		this.flightArrivalTime = flightArrivalTime;
	}


	public String getFlightID()
	{
	
		return flightID;
	}


	public void setFlightID(String flightID)
	{
	
		this.flightID = flightID;
	}


	public String getFlightAirline()
	{
	
		return flightAirline;
	}


	public void setFlightAirline(String flightAirline)
	{
	
		this.flightAirline = flightAirline;
	}


	public String getFlightDeparture()
	{
	
		return flightDeparture;
	}


	public void setFlightDeparture(String flightDeparture)
	{
	
		this.flightDeparture = flightDeparture;
	}


	public Time getFlightDepartureTime()
	{
	
		return flightDepartureTime;
	}


	public void setFlightDepartureTime(Time flightDepartureTime)
	{
	
		this.flightDepartureTime = flightDepartureTime;
	}


	public String getFlightArrival()
	{
	
		return flightArrival;
	}


	public void setFlightArrival(String flightArrival)
	{
	
		this.flightArrival = flightArrival;
	}


	public Time getFlightArrivalTime()
	{
	
		return flightArrivalTime;
	}


	public void setFlightArrivalTime(Time flightArrivalTime)
	{
	
		this.flightArrivalTime = flightArrivalTime;
	}


	@Override
	public String toString()
	{

		return "flightID: " + flightID + "\n" + "flightAirline: " + flightAirline + "\n" + "flightDeparture: " + flightDeparture + "\n" + "flightDepartureTime: "
				+ flightDepartureTime.toString() + "\n" + "flightArrival: " + flightArrival + "\n" + "flightArrivalTime: " + flightArrivalTime.toString()
				+ "\n";
	}

	private String flightID;
	private String flightAirline;
	private String flightDeparture;
	private Time flightDepartureTime;
	private String flightArrival;
	private Time flightArrivalTime;

}
