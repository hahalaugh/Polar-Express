package gw.resource.dataproducer;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DataToBeSent
{
	public DataToBeSent()
	{
	}

	public DataToBeSent(Integer subscriptionID, Integer dataStreamID, Double value)
	{
		super();
		this.subscriptionID = subscriptionID;
		this.dataStreamID = dataStreamID;
		this.value = value;
	}

	@XmlAttribute
	public Integer subscriptionID;
	@XmlAttribute
	public Integer dataStreamID;

	public Double value;
}
