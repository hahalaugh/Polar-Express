package gw.resource;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JoinCommunityRequest
{

	public JoinCommunityRequest()
	{
		super();
	}

	public JoinCommunityRequest(String username, String communityName)
	{
		super();
		this.username = username;
		this.communityName = communityName;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getCommunityName()
	{
		return communityName;
	}

	public void setCommunityName(String communityName)
	{
		this.communityName = communityName;
	}

	private String username;
	private String communityName;

}
