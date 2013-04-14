package com.common.types;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Location {
	@XmlElement(name = "latitude")
	public double latitude;
	
	@XmlElement(name = "longitude")
	public double longitude;	
	
	@XmlElement(name = "altitude")
	public double altitude;
}
