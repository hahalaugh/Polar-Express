package gw.test;

import static org.junit.Assert.*;
import gw.resource.PushSensorDataRequest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WebServiceTestTest
{

	@Before
	public void setUp() throws Exception
	{
		test = new WebServiceTest();
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testLoginTest()
	{
		assertEquals(test.loginTest("a", "a"),true);
	}

	@Test
	public void testRegisterTest()
	{
		Double d = Math.random();
		String username = d.toString().substring(0, 5);
		assertEquals(test.registerTest(username), true);
	}
	
	@Test
	public void profileRegisterTest()
	{
		String username = "juntao";
		assertEquals(test.profileTest(username), true);
	}
	
	@Test
	public void createTaskTest()
	{
		Double d = Math.random();
		Double dd = Math.random();
		String taskname = d.toString();
		String taskDescription = dd.toString();
		assertEquals(test.createTaskTest(taskname, taskDescription), true);
	}
	
	@Test
	public void viewTaskTest()
	{
		assertEquals(test.viewTaskTest(), true);
	}
	
	@Test
	public void createCommunityTest()
	{
		Double d = Math.random();
		String communityName = d.toString();
		assertEquals(test.createCommunityTest(communityName), true);
	}
	
	@Test
	public void viewSubmittedLandTest()
	{
		assertEquals(test.viewSubmittedLand(), true);
	}
	
	@Test
	public void submitLandTest()
	{
		Double d = Math.random();
		Double dd = Math.random();
		String username = d.toString();
		String location = dd.toString();
		assertEquals(test.submitLand(username, location), true);
	}
	
	@Test
	public void pullTest()
	{
		assertEquals(test.pull("temperature", "Trinity College Dublin"), true);
	}
	
	@Test
	public void pushTest()
	{
		PushSensorDataRequest request = new PushSensorDataRequest();
		
		request.setCo(Math.random());
		request.setHumidity(Math.random());
		request.setLatitude(Math.random());
		request.setLocation("Trinity College Dublin");
		request.setLongitude(Math.random());
		request.setNo(Math.random());
		request.setNo2(Math.random());
		request.setSo2(Math.random());
		request.setTemperature(Math.random());
		request.setSensorId("S1");
		
		assertEquals(test.push(request), true);
	}
	
	
	WebServiceTest test = new WebServiceTest();
}
