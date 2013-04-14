package gw.resource.dataproducer;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SetSubscriptionParam
{
	public SetSubscriptionParam(){}
	
	@XmlAttribute
	public String name;
	public Subscription subscription;
}
