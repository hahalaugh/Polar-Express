package com.geolocation;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.*;

import com.common.types.Location;

/*
 * This class is designed with purpose of converting GPS coordinates to textual 
 * name of area and back.
 * It uses GoogleMapAPI.
 */

public final class GeoLocator {
	
	/*
	 * Returns coordinates of center of the specified place
	 */
	public static Location getCenterOf(String placeName){
		String url = "http://maps.googleapis.com/maps/api/geocode/xml?sensor=true&language=en&address=ireland+";

		String finalUrl = null;
		try {
			finalUrl = url + URLEncoder.encode(placeName, "UTF-8");			
		} catch (UnsupportedEncodingException e) {
			System.out.println("getCenterOf: unsupported url encoding");
			return null;
		}

		Document doc = XMLParser.getXML(finalUrl);
		if (doc != null) {		
			NodeList nodes = doc.getElementsByTagName("location");
			Element location = (Element) nodes.item(0);
			Element latitude = (Element)location.getElementsByTagName("lat").item(0);	
			Element longitude = (Element)location.getElementsByTagName("lng").item(0);				    

			Location center = new Location();
			center.latitude = Double.parseDouble(getCharacterDataFromElement(latitude));
			center.longitude = Double.parseDouble(getCharacterDataFromElement(longitude));
			center.altitude = GeoLocator.getElevation(center.latitude, center.longitude);
			
			return center;
		}
		return null;
	}
	
	/*
	 * Request southwest and northeast coordinates of specified place
	 */
	public static List<Location> getViewPortOf(String placeName){
		String url = "http://maps.googleapis.com/maps/api/geocode/xml?sensor=true&language=en&address=ireland+";
		
		String finalUrl = null;
		try {
			finalUrl = url + URLEncoder.encode(placeName, "UTF-8");			
		} catch (UnsupportedEncodingException e) {
			System.out.println("getViewPortOf: unsupported url encoding");
			return null;
		}
		
		Document doc = XMLParser.getXML(finalUrl);
		if (doc == null) {
			return null;
		} else {
			NodeList nodes = doc.getElementsByTagName("viewport");
	    	Element viewport = (Element) nodes.item(0);	
	    	
	    	Element southwest = (Element)viewport.getElementsByTagName("southwest").item(0);
	    	Element swlatitude = (Element)southwest.getElementsByTagName("lat").item(0);
	    	Element swlongitude = (Element)southwest.getElementsByTagName("lng").item(0);	    
	    	
	    	Location sw = new Location();
	    	sw.latitude = Double.parseDouble(getCharacterDataFromElement(swlatitude));
	    	sw.longitude = Double.parseDouble(getCharacterDataFromElement(swlongitude));
	    	sw.altitude = GeoLocator.getElevation(sw.latitude, sw.longitude);
	    	    	
	    	Element northeast = (Element)viewport.getElementsByTagName("northeast").item(0);
	    	Element nelatitude = (Element)northeast.getElementsByTagName("lat").item(0);
	    	Element nelongitude = (Element)northeast.getElementsByTagName("lng").item(0);	    	
	    	
	    	Location ne = new Location();
	    	ne.latitude = Double.parseDouble(getCharacterDataFromElement(nelatitude));
	    	ne.longitude = Double.parseDouble(getCharacterDataFromElement(nelongitude));
	    	ne.altitude = GeoLocator.getElevation(ne.latitude, ne.longitude);
	    		    	
	    	ArrayList<Location> viewPort = new ArrayList<Location>();
	    	viewPort.add(sw);
	    	viewPort.add(ne);
	    	
	    	return viewPort;
		}
	}
	
	/*
	 * This function checks if position lies in between of two viewport 
	 * coordinates(southwest and northeast).
	 */
	public static boolean inViewPort(Location position, List<Location> viewPort){
		if (viewPort.size() >= 2) {
			Location sw = viewPort.get(0);
			Location ne = viewPort.get(1);	
			
			boolean fitsHorisontally = 
				(sw.latitude <= position.latitude && ne.latitude >= position.latitude) ||
				(sw.latitude >= position.latitude && ne.latitude <= position.latitude);
			boolean fitsVertically = 
				(sw.longitude <= position.longitude && ne.longitude >= position.longitude) ||
				(sw.longitude >= position.longitude && ne.longitude <= position.longitude);
			
			return fitsHorisontally && fitsVertically;
		}
		return false;
	}	
	
	/*
	 * Request altitude (elevation) on specified position
	 */
	public static double getElevation(double latitude, double longitude){
		String url = "http://maps.googleapis.com/maps/api/elevation/xml?sensor=false&locations=";
		String finalUrl = url + String.valueOf(latitude) + "," + String.valueOf(longitude);	

		Document doc = XMLParser.getXML(finalUrl);
		if (doc != null) {			
			NodeList nodes = doc.getElementsByTagName("elevation");
	    	Element elevation = (Element) nodes.item(0);	    	
	    	return Double.parseDouble(getCharacterDataFromElement(elevation));	    	
		}
		
		return 0;
	}

	/*
	 * Request the nearest to specified coordinates from Google Map API.
	 * Not yet required for our system.
	 */
	public static String getNameByLocation(Location coordinates){
		return "not yet implemented";
	}
	
	private static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return "?";
	}
}
