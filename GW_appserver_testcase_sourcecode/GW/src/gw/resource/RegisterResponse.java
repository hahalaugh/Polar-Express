package gw.resource;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RegisterResponse
{
	public RegisterResponse()
	{
		super();
	}

	public RegisterResponse(Boolean flag)
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
