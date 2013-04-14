package gw.resource;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CreateCommunityRequest
{
	public CreateCommunityRequest(String communityName, String location, String username)
	{
		super();
		this.communityName = communityName;
		this.location = location;
		this.username = username;
	}

	public CreateCommunityRequest()
	{
		super();
	}

	public String getCommunityName()
	{
		return communityName;
	}

	public void setCommunityName(String communityName)
	{
		this.communityName = communityName;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
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

	private String communityName;
	private String location;
	private String username;
}
