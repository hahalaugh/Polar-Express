package gw.webservice;

import gw.resource.CommunityInfoRequest;
import gw.resource.CommunityInfoResponse;
import gw.resource.CreateCommunityRequest;
import gw.resource.CreateCommunityResponse;
import gw.resource.CreateTaskRequest;
import gw.resource.CreateTaskResponse;
import gw.resource.DBOperator;
import gw.resource.LoginRequest;
import gw.resource.LoginResponse;
import gw.resource.ProfileRequest;
import gw.resource.ProfileResponse;
import gw.resource.RegisterRequest;
import gw.resource.RegisterResponse;
import gw.resource.SubmitLandRequest;
import gw.resource.SubmitLandResponse;
import gw.resource.TaskRequest;
import gw.resource.TaskResponse;
import gw.resource.ViewSubmittedLandRequest;
import gw.resource.ViewSubmittedLandResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/userinfo")
public class UserInfoWebService
{

	/**
	 * @param request
	 * @return a Boolean value indicates if logs in successfully
	 */
	@Path("/login")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public LoginResponse processLoginRequest(LoginRequest request)
	{
		System.out.println(request.getUsername() + " try to login.");
		return new LoginResponse(DBOperator.isValidUser(request.getUsername(), request.getPassword()));
	}

	/**
	 * @param request
	 * @return a Boolean value indicates if registration successful
	 */
	@Path("/register")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RegisterResponse processRegisterRequest(RegisterRequest request)
	{
		System.out.println(request.getUsername() + " try to register.");
		return new RegisterResponse(DBOperator.register(request));
	}

	/**
	 * @param request
	 * @return ProfileResponse includes username, password, email and ability
	 *         information of certain user.
	 */
	@Path("/profile")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ProfileResponse processProfileRequest(ProfileRequest request)
	{
		System.out.println(request.getUsername() + " try to get profile.");
		return DBOperator.getUserProfile(request.getUsername());
	}

	/**
	 * @param request
	 * @return a Boolean value indicates if the task created successfully
	 */
	@Path("/createtask")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public CreateTaskResponse processCreateTaskRequest(CreateTaskRequest request)
	{
		System.out.println("task creating");
		return new CreateTaskResponse(DBOperator.createTask(request));
	}

	/**
	 * @param request
	 * @return all the task name and task description are returned to the client
	 */
	@Path("/task")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public TaskResponse processTaskRequest(TaskRequest request)
	{
		System.out.println("taski information is required");
		return new TaskResponse(DBOperator.getTaskInfo());
	}

	/**
	 * @param request
	 * @return a Boolean value indicates if the community created successfully
	 */
	@Path("/createcommunity")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public CreateCommunityResponse processCreateCommunityRequest(CreateCommunityRequest request)
	{
		System.out.println(request.getUsername() + "create community");
		return new CreateCommunityResponse(DBOperator.createCommunity(request));
	}

	/**
	 * @param request
	 * @return all the submittedland and uploaders are returned to client.
	 */
	@Path("/viewland")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ViewSubmittedLandResponse processViewLandRequest(ViewSubmittedLandRequest request)
	{
		return DBOperator.getLandInfo();
	}

	/**
	 * @param request
	 * @return a Boolean value indicates if the land submission sucessful
	 */
	@Path("/submitland")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SubmitLandResponse processSubmitLandRequest(SubmitLandRequest request)
	{
		return DBOperator.submitLand(request);
	}
	
	/**
	 * @param request
	 * @return a Boolean value indicates if the land submission sucessful
	 */
	@Path("/communityinfo")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public CommunityInfoResponse processCommunityInfoRequest(CommunityInfoRequest request)
	{
		System.out.println("some one try to get community information");
		return DBOperator.getCommunityInfo();
	}
}
