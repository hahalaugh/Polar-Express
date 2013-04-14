/*
 * This package parses geolocation data
 */
package com.geolocation;

import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;

public class XMLParser {
	
	public static String getCharacterDataFromElement(Element e) {
	    Node child = e.getFirstChild();
	    if (child instanceof CharacterData) {
	       CharacterData cd = (CharacterData) child;
	       return cd.getData();
	    }
	    return "?";
	  }
	
	/*
	 * Returns DOM document containing XML retrieved from URL 
	 */
	public static Document getXML(String url){
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url); 
		Document parsedXML = null;
		try {
			
			HttpResponse response = httpclient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() != 200){
				System.out.println("Wrong status code");
			} else {			
				HttpEntity entity = response.getEntity();			
				String xmlContents = EntityUtils.toString(entity);
				
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				InputSource is = new InputSource();
				is.setCharacterStream(new StringReader(xmlContents));
				parsedXML = db.parse(is);

				EntityUtils.consume(entity);
			}
		} catch (ClientProtocolException e) {			
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Exception occured. getXML() have no internet access, or wrong proxy");
		} catch (SAXException e) {
			System.out.println("Parsing of XML failed");
		} catch (ParserConfigurationException e) {
			System.out.println("Parser configuration failed");			
		} finally {
			httpGet.releaseConnection();
		}
		return parsedXML;
	}
}
