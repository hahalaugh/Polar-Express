package com.common.types;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/*
 * <location type="place">Dublin</location>
 */

@XmlRootElement
public class LocationNamed {	
	@XmlAttribute(name = "type")
	public String type;
	
	@XmlValue
	public String value;	
}
