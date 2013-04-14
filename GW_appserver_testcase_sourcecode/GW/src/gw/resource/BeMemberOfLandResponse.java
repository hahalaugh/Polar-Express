package gw.resource;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BeMemberOfLandResponse
{
	public BeMemberOfLandResponse()
	{
		super();
	}

	public BeMemberOfLandResponse(Boolean flag)
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
