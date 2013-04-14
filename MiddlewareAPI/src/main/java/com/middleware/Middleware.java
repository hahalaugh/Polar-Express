package com.middleware;

import com.common.types.*;

public interface Middleware {

	// returns XML representation of data types available to middleware
	public GetDataTypesReply GetDataTypes();

	// returns schema description of specific data type
	public DescribeDataTypeReply DescribeDataType(String dataType);

	/* check the availability of requested data type and if such type presents -
	 * subscribe to this type
	 */
	public int SetSubscription(SetSubscriptionRequest request);

	// removes subscription by its id
	public int RemoveSubscription(int subscriptionID);

	// saves information about data producer
	public int RegisterDataStream(RegisterDSRequest request, String dataProducerURI);

	// removes data producer from list of available data producers
	public int RemoveDataStream(int dataStreamID);
	
	// receives data from data producer
	public int OnCallback(DataToBeSent data);
}
