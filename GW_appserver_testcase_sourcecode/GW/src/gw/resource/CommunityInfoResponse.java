package gw.resource;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CommunityInfoResponse
{

	public CommunityInfoResponse()
	{
		super();
	}

	public CommunityInfoResponse(String communityInfo)
	{
		super();
		this.communityInfo = communityInfo;
	}

	public String getCommunityInfo()
	{
		return communityInfo;
	}

	public void setCommunityInfo(String communityInfo)
	{
		this.communityInfo = communityInfo;
	}

	private String communityInfo;
}
