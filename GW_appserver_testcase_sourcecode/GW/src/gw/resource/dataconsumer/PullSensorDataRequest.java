package gw.resource.dataconsumer;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PullSensorDataRequest
{
	public PullSensorDataRequest()
	{
		super();
	}

	public PullSensorDataRequest(String location, String valueName)
	{
		super();
		this.location = location;
		this.valueName = valueName;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public String getValueName()
	{
		return valueName;
	}

	public void setValueName(String valueName)
	{
		this.valueName = valueName;
	}

	private String location;
	private String valueName;
}
