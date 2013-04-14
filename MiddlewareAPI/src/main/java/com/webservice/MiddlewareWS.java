package com.webservice;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import javax.xml.bind.JAXBElement;
import javax.servlet.http.HttpServletRequest;

import com.common.types.*;
import com.middleware.Middleware;
import com.middleware.MiddlewareImpl;
 
@Path("/")
public class MiddlewareWS { 
	/*
	 * Important! Send data only with Context-Type: application/xml.
	 * I will receive:
	 * 
	 * <registerDS>
	 * 		<location>
	 * 			<longitude></longitude>
	 * 			<latitude></latitude>
	 * 			<altitude></altitude>
	 * 		</location>
	 * 		<dataType name="temperature">
	 * 			<metadata>
	 * 				<datamodel></datamodel>
	 * 				<sampling_rate>0.1</sampling_rate>	//samplingRate
	 * 			</metadata>
	 * 		</dataType>
	 * </registerDS> 
	 */
	@PUT
	@Path("/registerdatastream")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public ID registerDataStream(JAXBElement<RegisterDSRequest> registerDS, @Context HttpServletRequest inRequest) {		
		RegisterDSRequest rds = registerDS.getValue();	
		return new ID(mw.RegisterDataStream(rds, inRequest.getRemoteAddr()));
	}	
	
	/*
	 * Takes id information in string format
	 */
	@DELETE
	@Path("/removedatastream/{id}")
	@Produces(MediaType.APPLICATION_XML)
	public Response removeDataStream(@PathParam("id") String id) {
		int dataStreamId = Integer.valueOf(id);
		int res = mw.RemoveDataStream(dataStreamId);
		if (res == 1) {
			return Response.status(200).entity("Removed OK.").build();			
		} else {
			return Response.status(200).entity("Remove failed.").build();
		}
	}
	
	/*
	 * Important! Send data only with Context-Type: application/xml.
	 * Takes information about required subscription in format:
	 * 
	 *	<setSubscription>
	 *		<dataType>temperature</dataType>
	 *		<location type="place">Dublin</location>
	 *		<subscription type="frequently">10</subscription>
	 *	</setSubscription>
	 *
	 *  Location types: place/coordinates
	 *  Subscription types: now/frequently/time_interval/expression
	 */
	@PUT
	@Path("/setsubscription")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public ID setSubscription(JAXBElement<SetSubscriptionRequest> setSubscriptionP) {		
		SetSubscriptionRequest ssp = setSubscriptionP.getValue();
		return new ID(mw.SetSubscription(ssp));
	}
	
	/*
	 * Takes subscription id information in string format.
	 */
	@DELETE
	@Path("/removesubscription/{id}")	
	@Produces(MediaType.APPLICATION_XML)
	public Response RemoveSubscription(@PathParam("id") String id) {
		int subscriptionID = Integer.valueOf(id);
		int res = mw.RemoveSubscription(subscriptionID);
		if (res == 1) {
			return Response.status(200).entity("Removed OK.").build();			
		} else {
			return Response.status(200).entity("Remove failed.").build();
		}
	}	

	/*
	 * Returns information about all registered data types in format:
	 * 
	 *	<dataTypes>
	 *		<type>temperature</type>
	 *		<type>humidity</type>
	 *		<type>air_pollution</type>
	 *	</dataTypes>
	 */
	@GET
	@Path("/getdatatypes")
	@Produces(MediaType.APPLICATION_XML)
	public GetDataTypesReply getDataTypes() {
		GetDataTypesReply reply = mw.GetDataTypes();		
		return reply;
	}
	
	/*
	 * Returns information about particular data type in format:
	 * 
	 *	<dataTypeDescription name="temperature">
	 *		<model>some model</model>
	 *	</dataTypeDescription>
	 */
	@GET
	@Path("/describedatatype/{type}")
	@Produces(MediaType.APPLICATION_XML)
	public DescribeDataTypeReply describeDataType(@PathParam("type") String type) {
		DescribeDataTypeReply reply = mw.DescribeDataType(type);		
		return reply;
	}
	
	/*
	 * Important! Send data only with Context-Type: application/xml.
	 * Takes subscribed data in format:
	 * 
	 * <dataToBeSent subscriptionID="31" dataStreamID="13">
	 *		<value>567.76</value>
	 * 	</dataToBeSent>
	 */
	@PUT
	@Path("/callback")
	@Consumes(MediaType.APPLICATION_XML)
	public Response onCallback(JAXBElement<DataToBeSent> data) {
		int res = mw.OnCallback(data.getValue());
		if (res == 1) {
			return Response.status(200).entity("Removed OK.").build();			
		} else {
			return Response.status(200).entity("Remove failed.").build();
		}
	}
	
	private static Middleware mw = new MiddlewareImpl();	
}
