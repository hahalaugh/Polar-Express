package gw.resource.dataproducer;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PushSensorDataResponse
{
	public PushSensorDataResponse()
	{
		super();
	}

	public PushSensorDataResponse(Boolean flag)
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
