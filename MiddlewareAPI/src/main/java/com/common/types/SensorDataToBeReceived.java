package com.common.types;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SensorDataToBeReceived
{
	public Integer subscriptionID;
	public Double value;
}