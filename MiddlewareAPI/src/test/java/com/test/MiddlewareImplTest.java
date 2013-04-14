package com.test;

import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

import com.middleware.*;
import com.common.types.*;
import com.geolocation.GeoLocator;

public class MiddlewareImplTest {

	@Test
	public void testGetDataTypes() {
		MiddlewareImpl mw = new MiddlewareImpl();		
		DescribeDataTypeReply reply = mw.DescribeDataType("sometype");
		assertTrue(reply == null);
	}
	
	@Test
	public void testGetDataSourcesForPlace() {
		MiddlewareImpl mw = new MiddlewareImpl();
//		RegisterDSRequest request = new RegisterDSRequest();
//		DataType type = new DataType();		
//		
//		mw.RegisterDataStream(request, "not matter");
		List<DataProducerInfo> list = mw.getDataSourcesForPlace("humidity", "Dublin");
		for (int i = 0; i < list.size(); ++i) {
			DataProducerInfo info = list.get(i);
			System.out.println(info.dataStreamID + " at " + info.dataProducerURI);
		}
	}
	
	@Test
	public void testGetDataSourcesForCoordinates() {
		MiddlewareImpl mw = new MiddlewareImpl();
//		RegisterDSRequest request = new RegisterDSRequest();
//		DataType type = new DataType();		
//		
//		mw.RegisterDataStream(request, "not matter");
		Location loc = GeoLocator.getCenterOf("Trinity College Dublin");
		String coordinates = "" + loc.latitude + ";" + loc.longitude + ";" + loc.altitude;
		List<DataProducerInfo> list = mw.getDataSourcesForCoordinates("humidity", coordinates);
		for (int i = 0; i < list.size(); ++i) {
			DataProducerInfo info = list.get(i);
			System.out.println(info.dataStreamID + " at " + info.dataProducerURI);
		}
	}
}
