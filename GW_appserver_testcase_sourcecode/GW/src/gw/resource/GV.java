package gw.resource;

import gw.resource.dataproducer.SensorData;
import gw.resource.dataproducer.SubscriptionLocalStorage;

import java.util.HashMap;

public class GV
{
	public GV()
	{
	}

	// indicator of next current subscription number
	static public Integer currentSubscriptionId = 0;

	// local subscription pool of data producer
	static public HashMap<Integer, SubscriptionLocalStorage> subscriptionMap = new HashMap<Integer, SubscriptionLocalStorage>();

	// local data pool of data producer
	static public HashMap<Integer, SensorData> sensorDataMap = new HashMap<Integer, SensorData>();

	public static String[] parameterType = { "temperature", "humidity", "no", "no2", "co", "so2" };

	public final static Integer TEMPERATURE = 0;
	public final static Integer HUMIDITY = 1;
	public final static Integer NO = 2;
	public final static Integer NO2 = 3;
	public final static Integer CO = 4;
	public final static Integer SO2 = 5;

	// ip address of middle ware
	public final static String MWIP = "192.168.82.50";

	// interfaces of middle ware
	public final static String MWUrlNewData = "callback";
	public final static String MWUrlDescribedatatype = "describedatatype";
	public final static String MWUrlGetdatatypes = "getdatatypes";
	public final static String MWUrlSetSubscription = "setsubscription";
	public final static String MWUrlRemoveSubscription = "removesubscription";
	public final static String MWUrlRegisterDataStream = "registerdatastream";
	public final static String MWUrlRemoveDataStream = "removedatastream/";

	// url prefix of middle ware
	public final static String MWUrl = "http://" + MWIP + ":8080/middleware/rest/";
}
