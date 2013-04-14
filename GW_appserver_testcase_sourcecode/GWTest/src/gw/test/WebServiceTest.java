package gw.test;

import gw.resource.CreateCommunityRequest;
import gw.resource.CreateCommunityResponse;
import gw.resource.CreateTaskRequest;
import gw.resource.CreateTaskResponse;
import gw.resource.LoginRequest;
import gw.resource.LoginResponse;
import gw.resource.ProfileRequest;
import gw.resource.ProfileResponse;
import gw.resource.PullSensorDataRequest;
import gw.resource.PullSensorDataResponse;
import gw.resource.PushSensorDataRequest;
import gw.resource.PushSensorDataResponse;
import gw.resource.RegisterRequest;
import gw.resource.RegisterResponse;
import gw.resource.SubmitLandRequest;
import gw.resource.SubmitLandResponse;
import gw.resource.TaskRequest;
import gw.resource.TaskResponse;
import gw.resource.ViewSubmittedLandRequest;
import gw.resource.ViewSubmittedLandResponse;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class WebServiceTest
{

	public Boolean loginTest(String username, String password)
	{
		WebResource resource = Client.create().resource("http://localhost:8080/GW/rest/userinfo/login");

		LoginRequest r = new LoginRequest();
		r.setUsername(username);
		r.setPassword(password);

		ClientResponse response = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, r);
		LoginResponse lr = (LoginResponse) response.getEntity(LoginResponse.class);

		return lr.getFlag();
	}

	public Boolean registerTest(String username)
	{
		WebResource resource = Client.create().resource("http://localhost:8080/GW/rest/userinfo/register");

		RegisterRequest r = new RegisterRequest();
		String[] s = { "eat", "drink" };
		r.setUsername(username);
		r.setPassword("1234567");
		r.setName("hahalaugh");
		r.setEmail("juntao.gu@gmail.com");
		r.setAbility(s);

		ClientResponse response = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, r);
		RegisterResponse lr = (RegisterResponse) response.getEntity(RegisterResponse.class);

		return lr.getFlag();
	}

	public Boolean profileTest(String username)
	{
		WebResource resource = Client.create().resource("http://localhost:8080/GW/rest/userinfo/profile");

		ProfileRequest r = new ProfileRequest();
		r.setUsername(username);

		ClientResponse response = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, r);
		ProfileResponse lr = (ProfileResponse) response.getEntity(ProfileResponse.class);

		return !lr.getPassword().isEmpty();
	}

	public Boolean createTaskTest(String taskname, String taskDescription)
	{
		WebResource resource = Client.create().resource("http://localhost:8080/GW/rest/userinfo/createtask");

		CreateTaskRequest r = new CreateTaskRequest(taskname, taskDescription);

		ClientResponse response = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, r);
		CreateTaskResponse lr = (CreateTaskResponse) response.getEntity(CreateTaskResponse.class);

		return lr.getFlag();
	}

	public Boolean viewTaskTest()
	{
		WebResource resource = Client.create().resource("http://localhost:8080/GW/rest/userinfo/task");

		TaskRequest r = new TaskRequest();

		ClientResponse response = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, r);
		TaskResponse lr = (TaskResponse) response.getEntity(TaskResponse.class);

		return lr.getTaskName().length > 0;
	}

	public Boolean createCommunityTest(String communityName)
	{
		WebResource resource = Client.create().resource("http://localhost:8080/GW/rest/userinfo/createcommunity");

		CreateCommunityRequest r = new CreateCommunityRequest();
		r.setCommunityName(communityName);
		r.setLocation("Dublin 12");
		r.setUsername("hahalaugh");

		ClientResponse response = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, r);
		CreateCommunityResponse lr = (CreateCommunityResponse) response.getEntity(CreateCommunityResponse.class);

		return lr.getFlag();
	}

	public Boolean viewSubmittedLand()
	{
		WebResource resource = Client.create().resource("http://localhost:8080/GW/rest/userinfo/viewland");

		ViewSubmittedLandRequest r = new ViewSubmittedLandRequest();

		ClientResponse response = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, r);
		ViewSubmittedLandResponse lr = (ViewSubmittedLandResponse) response.getEntity(ViewSubmittedLandResponse.class);

		return lr.getLandLocation().length > 0;
	}

	public Boolean submitLand(String username, String location)
	{
		WebResource resource = Client.create().resource("http://localhost:8080/GW/rest/userinfo/submitland");

		SubmitLandRequest r = new SubmitLandRequest(username, location);

		ClientResponse response = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, r);
		SubmitLandResponse lr = (SubmitLandResponse) response.getEntity(SubmitLandResponse.class);

		return lr.getFlag();
	}

	public Boolean pull(String value, String location)
	{
		WebResource resource = Client.create().resource("http://localhost:8080/GW/rest/DC/pull");

		PullSensorDataRequest r = new PullSensorDataRequest(location, value);

		ClientResponse response = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, r);
		PullSensorDataResponse lr = (PullSensorDataResponse) response.getEntity(PullSensorDataResponse.class);

		return lr.getValue() != 0;

	}
	
	public Boolean push(PushSensorDataRequest request)
	{
		WebResource resource = Client.create().resource("http://localhost:8080/GW/rest/DP/push");

		ClientResponse response = resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, request);
		PushSensorDataResponse lr = (PushSensorDataResponse) response.getEntity(PushSensorDataResponse.class);

		return lr.getFlag();
	}
}
