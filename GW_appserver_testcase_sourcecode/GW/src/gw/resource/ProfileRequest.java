package gw.resource;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProfileRequest
{
	public ProfileRequest()
	{
		super();
	}

	public ProfileRequest(String username)
	{
		super();
		this.username = username;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	private String username;
}
