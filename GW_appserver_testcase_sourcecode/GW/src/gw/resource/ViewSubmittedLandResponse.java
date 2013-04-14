package gw.resource;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ViewSubmittedLandResponse
{
	public ViewSubmittedLandResponse()
	{
		super();
	}

	public ViewSubmittedLandResponse(String landLocation, String landUploader)
	{
		super();
		this.landLocation = landLocation;
		this.landUploader = landUploader;
	}

	public String getLandLocation()
	{
		return landLocation;
	}

	public void setLandLocation(String landLocation)
	{
		this.landLocation = landLocation;
	}

	public String getLandUploader()
	{
		return landUploader;
	}

	public void setLandUploader(String landUploader)
	{
		this.landUploader = landUploader;
	}

	private String landLocation;
	private String landUploader;
}
