package gw.webservice;

import java.util.HashMap;

import gw.resource.GV;
import gw.resource.dataconsumer.DescribeDataTypeReply;
import gw.resource.dataconsumer.GetDataTypesReply;
import gw.resource.dataconsumer.PullSensorDataRequest;
import gw.resource.dataconsumer.PullSensorDataResponse;
import gw.resource.dataconsumer.SensorDataToBeReceived;
import gw.resource.dataconsumer.SetSubscriptionRequest;
import gw.resource.dataproducer.ID;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import javax.ws.rs.core.Response;

@Path("/DC")
public class DataConsumerWebService
{
	/**
	 * @param request
	 * @return the required value data described in the request.
	 */
	@Path("/pull")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public PullSensorDataResponse processPullSensorDataRequest(PullSensorDataRequest request)
	{
		// get data type from middle ware to know the capacity set of data
		// provided by middle ware.
		GetDataTypesReply dataType = getDataTypes();

		System.out.println("size of datatype is " + dataType.types.size());
		Double result = 0.0;
		if (dataType.types.contains(request.getValueName()))
		{
			// if the required value is included in the data type list(it's
			// supported by data producers)

			// generate a reply.
			DescribeDataTypeReply dataTypeDescription = getDataTypeDescription(request.getValueName());
			System.out.println("datatype description mode is : " + dataTypeDescription.model + "    datatype name"
					+ dataTypeDescription.name);
			// ready to set subscription to middle ware for certain data type.
			SetSubscriptionRequest setSubscritpionRequest = generateSSR(request);

			// set subscription from middle ware and get the subscription id.
			Integer subscriptionId = setSubscription(setSubscritpionRequest);

			System.out.println("subscription id from mw = " + subscriptionId);
			if (subscriptionId >= 0)
			{
				// if the subscription is set successfully, join into local
				// subscription pool.
				subscriptionMap.put(subscriptionId, setSubscritpionRequest);
				while (true)
				{
					// waiting for middle ware call the call back function of
					// data consumer to push data.
					System.out.println("DC waiting data from MW...");
					if (mutex)
					{
						// if new data is pushed, leave the dead loop.
						result = receivedData.value;
						System.out.println("value from mw is " + result);
						receivedData = new SensorDataToBeReceived();
						mutex = false;
						break;
					}
					try
					{
						Thread.sleep(1000);
					}
					catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			// remove subscription from middle ware after data has been
			// received.
			removeSubscription(subscriptionId);

			// return data to android client.
			return new PullSensorDataResponse(result);
		}
		else
		{

		}
		return null;
	}

	/**
	 * @param data
	 * @return
	 * 
	 *         a response to middle ware indicates if the callback function has
	 *         been invoked successfully.
	 */
	@Path("/callback")
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public Response receiveFromMW(SensorDataToBeReceived data)
	{
		// store the data to local data cache
		receivedData = data;
		// release the mutex and let set subscription thread going on.
		mutex = true;
		return Response.status(200).build();
	}

	/**
	 * @return return GetDataTypesReply typed value which indicates supported
	 *         data type get from middle ware
	 */
	public GetDataTypesReply getDataTypes()
	{
		WebResource resource = Client.create().resource(GV.MWUrl + GV.MWUrlGetdatatypes);
		// send http get request to MWUrlGetdatatypes interface of middleware
		ClientResponse response = resource.type(MediaType.APPLICATION_XML).get(ClientResponse.class);
		// rsp include the data type list.
		GetDataTypesReply rsp = (GetDataTypesReply) response.getEntity(GetDataTypesReply.class);

		return rsp;
	}

	/**
	 * @param type
	 * @return DescribeDataTypeReply include the DTD Schema describe the data
	 *         middle ware could provide.
	 */
	public DescribeDataTypeReply getDataTypeDescription(String type)
	{
		WebResource resource = Client.create().resource(GV.MWUrl + GV.MWUrlDescribedatatype + "/" + type);
		// send http get request to MWUrlDescribedatatype interface of middle
		// ware.
		ClientResponse response = resource.type(MediaType.APPLICATION_XML).get(ClientResponse.class);
		DescribeDataTypeReply rsp = (DescribeDataTypeReply) response.getEntity(DescribeDataTypeReply.class);

		return rsp;
	}

	/**
	 * @param request
	 * @return
	 * 
	 *         return a new generated set subscription request
	 */
	public SetSubscriptionRequest generateSSR(PullSensorDataRequest request)
	{
		SetSubscriptionRequest setSubscritpionRequest = new SetSubscriptionRequest();

		setSubscritpionRequest.dataType = request.getValueName();
		setSubscritpionRequest.location.type = "placename";
		setSubscritpionRequest.location.value = request.getLocation();
		setSubscritpionRequest.subscription.setSubscriptionType("now");
		setSubscritpionRequest.subscription.setSubscriptionTypeValue(null);
		setSubscritpionRequest.callbackURI = "192.168.135.1";

		return setSubscritpionRequest;
	}

	/**
	 * @param request
	 * @return subscription id from middle ware.
	 */
	public Integer setSubscription(SetSubscriptionRequest request)
	{
		WebResource resource = Client.create().resource(GV.MWUrl + GV.MWUrlSetSubscription);
		// send http put request to MWUrlSetSubscription interface of middle
		// ware
		ClientResponse response = resource.type(MediaType.APPLICATION_XML).put(ClientResponse.class, request);

		// get subscription id from middle ware.
		ID rsp = (ID) response.getEntity(ID.class);

		return rsp.id;
	}

	/**
	 * @param subscriptionID
	 * 
	 *            send remove subscription request to middle ware to stop
	 *            requiring data.
	 */
	public void removeSubscription(Integer subscriptionID)
	{
		WebResource resource = Client.create().resource(
				GV.MWUrl + GV.MWUrlRemoveSubscription + "/" + subscriptionID.toString());

		// send http delete request to middle ware to remove the subscription =
		// subscriptionID
		resource.type(MediaType.APPLICATION_XML).delete(ClientResponse.class);
	}

	// local subscription pool
	static HashMap<Integer, SetSubscriptionRequest> subscriptionMap = new HashMap<Integer, SetSubscriptionRequest>();
	// mutex to synchronize data receiving.
	static Boolean mutex = false;
	static SensorDataToBeReceived receivedData = new SensorDataToBeReceived();
}