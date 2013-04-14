package gw.resource.dataproducer;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DataType
{
	public DataType()
	{
	}

	public DataType(String name, Double samplingRate)
	{
		super();
		this.name = name;
		this.dataModel = new MetaData(samplingRate);
	}

	@XmlAttribute
	public String name;
	
	@XmlElement(name="metadata")
	public MetaData dataModel = new MetaData(); // XMLSchema or DTD which describes dataParam
}
