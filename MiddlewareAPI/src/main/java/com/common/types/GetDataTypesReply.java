package com.common.types;

import java.util.ArrayList;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "dataTypes")
public class GetDataTypesReply {
	@XmlElement(name = "type")
	public ArrayList<String> types = new ArrayList<String>();	
}
