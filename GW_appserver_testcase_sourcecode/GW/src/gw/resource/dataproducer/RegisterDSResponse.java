package gw.resource.dataproducer;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RegisterDSResponse
{
	public RegisterDSResponse()
	{
		super();
	}

	public RegisterDSResponse(String dataStreamId)
	{
		super();
		this.dataStreamId = dataStreamId;
	}

	public String getDataStreamId()
	{
		return dataStreamId;
	}

	public void setDataStreamId(String dataStreamId)
	{
		this.dataStreamId = dataStreamId;
	}

	private String dataStreamId;
}
