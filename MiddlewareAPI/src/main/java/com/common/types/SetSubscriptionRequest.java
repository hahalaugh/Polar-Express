package com.common.types;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "setSubscription")
public class SetSubscriptionRequest {
	@XmlElement(name = "dataType")
	public String dataType;
	
	@XmlElement(name = "subscription")
	public Subscription subscription = new Subscription();
	
	@XmlElement(name = "location")
	public LocationNamed location = new LocationNamed();
	
	@XmlElement(name = "callbackURI")
	public String callbackURI = null;
}
