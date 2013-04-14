package com.common.types;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/*
 * <RegisterDS>
 * 		<location>
 * 			<longitude></longitude>
 * 			<latitude></latitude>
 * 			<altitude></altitude>
 * 		</location>
 * 		<dataType name="temperature">
 * 			<metadata>
 * 				<datamodel></datamodel>
 * 				<sampling_rate>0.1</sampling_rate>
 * 			</metadata>
 * 		</dataType>
 * 		<dataType name="humidity">
 * 			<metadata>
 * 				<datamodel></datamodel>
 * 				<sampling_rate>0.1</sampling_rate>
 * 			</metadata>
 * 		</dataType>
 * </RegisterDS>
 */

@XmlRootElement
@XmlType(propOrder={"location", "dataTypes"})
public class RegisterDSRequest {
	@XmlElement(name = "location")
	public Location location;
	
	@XmlElement(name = "dataType")
	public ArrayList<DataType> dataTypes;	
}