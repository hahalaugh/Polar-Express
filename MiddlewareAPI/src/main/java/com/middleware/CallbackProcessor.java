package com.middleware;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.ws.rs.core.MediaType;

import com.common.types.DataToBeSent;
import com.common.types.LocationNamed;
import com.common.types.SetSubscriptionParam;
import com.common.types.SetSubscriptionRequest;
import com.common.types.Subscription;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/*
 * Assumption: data producer is dumb. Whenever we need data, we create
 * subscription to DP with type "now", it will call us back once with ready data.
 * 
 * Assumption: when type of subscription is expression or time interval,
 * requests to DP will be performed every 10 seconds.
 */

public class CallbackProcessor extends Thread {
	public int subscriptionID;
	
	// information about subscription being processed by this thread
	private String dataType;
	private Subscription subscription;
	private LocationNamed location;
	private String callbackURI;			// this is consumer's URI
	
	// middleware interface for requesting the list of data producers
	// and notify about created subscriptions to DPs
	MiddlewareImpl mw;
	
	// received data which waits for being forwarded
	private DataToBeSent data;

	// timer to control data requesting and forwarding
	Timer countdownTimer = new Timer(true); // will stop with thread
	long timerDuration = 10*1000; // 10 seconds	
	
	volatile boolean run;
	
	// create an instance of callback processor
	public CallbackProcessor(int subscriptionID, SetSubscriptionRequest req, MiddlewareImpl mw) {
		this.subscriptionID = subscriptionID;
		this.dataType = req.dataType;
		this.subscription = req.subscription;
		this.location = req.location;
		this.callbackURI = req.callbackURI;
		this.mw = mw;
		run = true;
		System.out.println("CallbackProcessor for subscription No" + subscriptionID + " created.");		
	}
	
	private synchronized void Request() {
		// create client and make request to data producer
		SetSubscriptionParam param = new SetSubscriptionParam();
		param.setDataType(dataType);
		param.setSubscriptionType("now");
		param.setSubscriptionTypeValue("");

		// this will be implemented when DP become more intellectual
		//param.setSubscriptionType(subscription.getSubscriptionType());
		//param.setSubscriptionTypeValue(subscription.getSubscriptionTypeValue());
		
		// for all data producers corresponding to current subscription
		// create subscriptions
		List<DataProducerInfo> DPs = mw.getDataSources(this.dataType, this.location);
		for (int i = 0; i < DPs.size(); ++i) {
			String request = DPs.get(i).dataProducerURI + "setSubscription/" + DPs.get(i).dataStreamID;			
			System.out.println("Creating subscription on DP: " + request);
			
			WebResource resource = Client.create().resource(request);
			ClientResponse response = resource.type(MediaType.APPLICATION_XML).post(ClientResponse.class, param);
			if (response.getStatus() != 200) {
				System.out.println("SetSubscription to DP failed.");
			} else {
				Integer sID = (Integer) response.getEntity(Integer.class);
				System.out.println("Subscription on DP side created. id = " + sID);
				
				// add current subscription into subscriptionID <-> dataStreamID map
				mw.updateSubscriptionDataStreams(this.subscriptionID, DPs.get(i).dataStreamID);

				// everything is fine, we created single time subscription, 
				// we have to wait now until onCallback is called
			}			
		}		
	}
	
	// selectively process new data from data producer
	public synchronized void onCallback(DataToBeSent data) {
		// check if this data is related to current thread
		int dataStreamID = data.dataStreamID;
		int producerSubscriptionID = data.subscriptionID;
		
		if (mw.pairExists(dataStreamID, producerSubscriptionID)) {
			// save data
			this.data = data;
			sendToConsumer(data);
			
			// remove subscription from table
			mw.removeSubscriptionDataStreams(producerSubscriptionID, dataStreamID);			
		}		
	}
	
	// consumer will receive DataToBeSent object
	private synchronized void sendToConsumer(DataToBeSent data) {
		// send received data to the consumer using post request	
		System.out.println("Sending to consumer. URI: " + callbackURI);
		WebResource resource = Client.create().resource(callbackURI);
		ClientResponse response = resource.type(MediaType.APPLICATION_XML).post(ClientResponse.class, data);
		if (response.getStatus() != 200) {
			System.out.println("Client notification failed.");
		} else {
			System.out.println("Client notification succeeded.");
		}
	}
	
	// start thread execution
	// request data from producers with frequency/time interval etc. depending on subscription type
	// considering dumb DP at this stage
	public void run() {
		String type = this.subscription.getSubscriptionType();
		String value = this.subscription.getSubscriptionTypeValue();
		if (type.equals("now")) {
			// request data once and wait for callback to send it to consumer		
			Request();
			System.out.println("Subscription with type = now created");
		} 
		else if (type.equals("frequently")) {
			// install timer for specified duration and restart after frequency*1000 msecs
			// start timer immediately
			countdownTimer.scheduleAtFixedRate(new TimerTask() {
				  @Override 
				  public void run() {
					  Request();
				  }
				}, 0, Integer.valueOf(value)*1000);
			System.out.println("Subscription with type = frequently created");
		} 
		else if (type.equals("time_interval")) {
			// send data only in time interval specified
			String[] tmp = value.split(";");
			final long start = Long.valueOf(tmp[0]);
			final long stop =  Long.valueOf(tmp[1]);
			// request every second in interval specified
			countdownTimer.scheduleAtFixedRate(new TimerTask() {
				  @Override 
				  public void run() {
					  Date cur = new Date();
					  long curTime = cur.getTime()/1000;
					  if (curTime > start && curTime < stop) {
						  Request();	  
					  }
				  }
				}, 0, 1*1000);
			System.out.println("Subscription thread with type = time interval created");
		}
		else if (type.equals("expression")) {
			// send data only when expression is true
			System.out.println("Subscription with type = expression unsopported.");
			return;
		}
		else {
			System.out.println("CallbackProcessor for subscription No" + subscriptionID + ": Wrong type of subscription: " + type);
			return;
		}
		
		// wait until signal to stop is received
		synchronized (this) {
			while (run) {
				try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
			}		
		}
		
		UnsubscribeAll();
		System.out.println("CallbackProcessor thread for subscription No" + subscriptionID + " finished.");
	}
	
	// send remove subscription call to all data producers 
	// it is possible that non active data producers will receive removesubscription() call
	private void UnsubscribeAll() {
		System.out.println("Unsubscription is not implemented yet.");
	}

	// stop thread execution
	public void terminate() {
		synchronized (this) {
			run = false;
			this.notifyAll();
			System.out.println("Terminate signal sent.");
		}
	}
//	countdownTimer.schedule(new TimerTask() {
//	  @Override
//	  public void run() {
//	  
//	  }
//	}, timerDuration);
}
