package gw.resource.dataproducer;

public class SensorData
{
	public SensorData()
	{
		super();
	}

	public SensorData(PushSensorDataRequest request)
	{
		this.co = request.getCo();
		this.Humidity = request.getHumidity();
		this.latitude = request.getLatitude();
		this.location = request.getLocation();
		this.longitude = request.getLongitude();
		this.no = request.getNo();
		this.no2 = request.getNo2();
		this.so2 = request.getSo2();
		this.temperature = request.getTemperature();
		this.datetime = request.getDatetime();
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public Double getLatitude()
	{
		return latitude;
	}

	public void setLatitude(Double latitude)
	{
		this.latitude = latitude;
	}

	public Double getLongitude()
	{
		return longitude;
	}

	public void setLongitude(Double longitude)
	{
		this.longitude = longitude;
	}

	public Double getTemperature()
	{
		return temperature;
	}

	public void setTemperature(Double temperature)
	{
		this.temperature = temperature;
	}

	public Double getHumidity()
	{
		return Humidity;
	}

	public void setHumidity(Double humidity)
	{
		Humidity = humidity;
	}

	public Double getNo()
	{
		return no;
	}

	public void setNo(Double no)
	{
		this.no = no;
	}

	public Double getNo2()
	{
		return no2;
	}

	public void setNo2(Double no2)
	{
		this.no2 = no2;
	}

	public Double getCo()
	{
		return co;
	}

	public void setCo(Double co)
	{
		this.co = co;
	}

	public Double getSo2()
	{
		return so2;
	}

	public void setSo2(Double so2)
	{
		this.so2 = so2;
	}

	public String getDatetime()
	{
		return datetime;
	}

	public void setDatetime(String datetime)
	{
		this.datetime = datetime;
	}

	private String location;
	private Double latitude;
	private Double longitude;
	private Double temperature;
	private Double Humidity;
	private Double no;
	private Double no2;
	private Double co;
	private Double so2;
	private String datetime;
}
