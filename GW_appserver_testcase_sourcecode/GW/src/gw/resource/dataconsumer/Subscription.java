package gw.resource.dataconsumer;

import javax.xml.bind.annotation.*;

@XmlRootElement
public class Subscription
{

	private String subscriptionType;
	private String subscriptionTypeValue;

	public Subscription()
	{
	}

	public String getSubscriptionType()
	{
		return subscriptionType;
	}

	@XmlAttribute(name = "type")
	public void setSubscriptionType(String subscriptionType)
	{
		this.subscriptionType = subscriptionType;
	}

	public String getSubscriptionTypeValue()
	{
		return subscriptionTypeValue;
	}

	@XmlValue
	public void setSubscriptionTypeValue(String subscriptionTypeValue)
	{
		this.subscriptionTypeValue = subscriptionTypeValue;
	}
}