package gw.resource;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SubmitLandRequest
{
	public SubmitLandRequest()
	{
		super();
	}

	public SubmitLandRequest(String username, String location)
	{
		super();
		this.username = username;
		this.location = location;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	private String username;
	private String location;
}
