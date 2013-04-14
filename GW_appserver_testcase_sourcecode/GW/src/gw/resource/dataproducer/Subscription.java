package gw.resource.dataproducer;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;
import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlElement
public class Subscription
{
	public Subscription(){}
	
	@XmlAttribute
	public String type;
	@XmlValue
	public String value;
}
