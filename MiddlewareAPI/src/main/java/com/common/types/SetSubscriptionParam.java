package com.common.types;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SetSubscriptionParam {
	private String dataTypeName;
	private String subscriptionType;
	private String subscriptionTypeValue;
	
	public String getDataType(){
		return dataTypeName;
	}
	
	@XmlElement
	public void setDataType(String dataTypeName){
		this.dataTypeName = dataTypeName;
	}
	
	public String getSubscriptionType(){
		return subscriptionType;
	}
	
	@XmlElement
	public void setSubscriptionType(String subscriptionType){
		this.subscriptionType = subscriptionType;
	}
	
	public String getSubscriptionTypeValue(){
		return subscriptionTypeValue;
	}
	
	@XmlElement
	public void setSubscriptionTypeValue(String subscriptionTypeValue){
		this.subscriptionTypeValue = subscriptionTypeValue;
	}
}
