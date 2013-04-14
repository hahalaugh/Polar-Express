package gw.resource;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MyCommunityTaskResponse
{

	public MyCommunityTaskResponse()
	{
		super();
	}

	public MyCommunityTaskResponse(String nameArray, String descriptionArray)
	{
		super();
		this.nameArray = nameArray;
		this.descriptionArray = descriptionArray;
	}

	public String getNameArray()
	{
		return nameArray;
	}

	public void setNameArray(String nameArray)
	{
		this.nameArray = nameArray;
	}

	public String getDescriptionArray()
	{
		return descriptionArray;
	}

	public void setDescriptionArray(String descriptionArray)
	{
		this.descriptionArray = descriptionArray;
	}

	private String nameArray;
	private String descriptionArray;
}
