package gw.resource;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PullSensorDataResponse
{
	public PullSensorDataResponse()
	{
		super();
	}

	public PullSensorDataResponse(Double value)
	{
		super();
		this.value = value;
	}

	public Double getValue()
	{
		return value;
	}

	public void setValue(Double value)
	{
		this.value = value;
	}

	private Double value;
}
