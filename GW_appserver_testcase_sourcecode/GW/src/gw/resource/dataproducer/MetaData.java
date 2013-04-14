package gw.resource.dataproducer;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MetaData
{
	public MetaData()
	{
	}

	final public String XSDofData = "<xs:schema attributeFormDefault=\"unqualified\" elementFormDefault=\"qualified\""
			+ " xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"><xs:element name=\"data\">"
			+ "<xs:complexType><xs:sequence><xs:element type=\"xs:float\" name=\"value\"/>"
			+ "</xs:sequence><xs:attribute type=\"xs:byte\" name=\"subscriptionID\"/>"
			+ "<xs:attribute type=\"xs:short\" name=\"dataStreamID\"/></xs:complexType>" + "</xs:element></xs:schema>";

	public MetaData(Double samplingRate)
	{
		System.out.println("Creating meta data");
		this.samplingRate = samplingRate;
		dataModel = XSDofData;
		System.out.println(dataModel);
	}

	public String dataModel = new String(XSDofData); // XMLSchema or DTD.
	public Double samplingRate = 0.0;

}
