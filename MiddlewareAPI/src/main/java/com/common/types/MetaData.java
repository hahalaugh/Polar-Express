package com.common.types;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MetaData {	
	@XmlElement(name = "datamodel")
	public String dataModel;	// XML schema of the data
	
	@XmlElement(name = "sampling_rate")
	public int samplingRate;	// sampling rate of the data appearance	
}
