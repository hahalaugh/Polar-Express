package gw.resource;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CreateTaskResponse
{
	public CreateTaskResponse()
	{
		super();
	}

	public CreateTaskResponse(Boolean flag)
	{
		super();
		this.flag = flag;
	}

	public Boolean getFlag()
	{
		return flag;
	}

	public void setFlag(Boolean flag)
	{
		this.flag = flag;
	}

	private Boolean flag;
}
