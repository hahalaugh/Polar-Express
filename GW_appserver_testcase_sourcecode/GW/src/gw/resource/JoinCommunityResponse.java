package gw.resource;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class JoinCommunityResponse
{
	
	public JoinCommunityResponse()
	{
		super();
	}

	public JoinCommunityResponse(Boolean flag)
	{
		super();
		this.flag = flag;
	}

	public Boolean getFlag()
	{
		return flag;
	}

	public void setFlag(Boolean flag)
	{
		this.flag = flag;
	}

	private Boolean flag;
}
