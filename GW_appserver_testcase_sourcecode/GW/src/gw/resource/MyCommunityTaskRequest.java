package gw.resource;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MyCommunityTaskRequest
{
	public MyCommunityTaskRequest()
	{
		super();
	}

	public MyCommunityTaskRequest(String username)
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
