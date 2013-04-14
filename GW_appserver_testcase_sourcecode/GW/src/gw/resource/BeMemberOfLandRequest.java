package gw.resource;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BeMemberOfLandRequest
{

	public BeMemberOfLandRequest()
	{
		super();
	}

	public BeMemberOfLandRequest(String username, String landname)
	{
		super();
		this.username = username;
		this.landname = landname;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getLandname()
	{
		return landname;
	}

	public void setLandname(String landname)
	{
		this.landname = landname;
	}

	private String username;
	private String landname;
}
