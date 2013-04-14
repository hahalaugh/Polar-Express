package gw.resource.dataproducer;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Location implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5929218534659612165L;

	public Location()
	{

	}

	public Location(Location l)
	{
		this.longitude = l.longitude;
		this.latitude = l.latitude;
		this.altitude = l.altitude;
	}

	public Location(Double la, Double lo)
	{
		// TODO Auto-generated constructor stub
		latitude = la;
		longitude = lo;
		altitude = (Double) 0.0;
	}

	public Double longitude;
	public Double latitude;
	public Double altitude;
}
