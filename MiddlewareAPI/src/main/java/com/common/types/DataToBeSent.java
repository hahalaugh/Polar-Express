package com.common.types;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DataToBeSent
{
	@XmlAttribute
	public Integer subscriptionID;
	@XmlAttribute
	public Integer dataStreamID;
	
	public Double value;
}