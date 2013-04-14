package com.common.types;

import java.util.ArrayList;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "dataTypeDescription")
public class DescribeDataTypeReply {
	@XmlAttribute(name = "name")
	public String name;
	
	@XmlElement(name = "model")
	public String model;
}
