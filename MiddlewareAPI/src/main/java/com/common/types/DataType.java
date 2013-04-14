package com.common.types;

import javax.xml.bind.annotation.*;

/*
 * 	<dataType name="temperature">
 * 		<metadata>
 * 			<datamodel></datamodel>
 * 			<sampling_rate>0.1</sampling_rate>
 * 		</metadata>
 * 	</dataType>
 */

@XmlRootElement
public class DataType {
	@XmlAttribute(name = "name")
	public String name;
	
	@XmlElement(name = "metadata")
	public MetaData metaData;
}
