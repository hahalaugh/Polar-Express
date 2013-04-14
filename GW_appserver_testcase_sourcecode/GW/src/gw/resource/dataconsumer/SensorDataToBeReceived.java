package gw.resource.dataconsumer;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SensorDataToBeReceived
{
	public SensorDataToBeReceived()
	{
	}

	public Integer subscriptionID;
	public Double value;
}
