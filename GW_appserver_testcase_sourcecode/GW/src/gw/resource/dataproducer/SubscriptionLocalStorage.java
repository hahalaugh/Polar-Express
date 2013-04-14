package gw.resource.dataproducer;

public class SubscriptionLocalStorage
{

	public SubscriptionLocalStorage(Integer dataStreamId, String valueName)
	{
		super();
		this.dataStreamId = dataStreamId;
		this.valueName = valueName;
	}

	public Integer getDataStreamId()
	{
		return dataStreamId;
	}

	public void setDataStreamId(Integer dataStreamId)
	{
		this.dataStreamId = dataStreamId;
	}

	public String getValueName()
	{
		return valueName;
	}

	public void setValueName(String valueName)
	{
		this.valueName = valueName;
	}

	private Integer dataStreamId;
	private String valueName;
}
