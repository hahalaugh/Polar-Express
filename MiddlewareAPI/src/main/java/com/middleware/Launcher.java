package com.middleware;

import java.util.List;

import com.common.types.*;
import com.geolocation.GeoLocator;

public class Launcher {

	/**
	 * This file works for testing the middleware implementation 
	 */
	public static void main(String[] args) {
		MiddlewareImpl mw = new MiddlewareImpl();
		/*
		GetDataTypesReply reply = mw.GetDataTypes();
		System.out.println("Requested OK.");
		*/
		
		/*
		SetSubscriptionRequest sub = new SetSubscriptionRequest();
		sub.location.type = "place";
		sub.location.value = "Dublin";
		sub.callbackURI = "http://localhost:8080/middleware/rest/1";
		sub.dataType = "temperature";
		sub.subscription.setSubscriptionType("now");
		sub.subscription.setSubscriptionTypeValue("abc");
		*/
		
		
		
//		System.out.println(loc.latitude + " " + loc.longitude + " " + loc.altitude);
//		mw.removeSubscriptionDataStreams(11,12);
		
//		System.out.println(mw.getSubscriptionID(sub));
		
//		LocationNamed location = new LocationNamed();
//		location.type = "place";
//		location.value = "UCD";
//		int id = mw.insertNewLocation(location);
	}
}
