package gw.resource.dataproducer;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RegisterDS
{

	public RegisterDS()
	{
		super();
	}

	public RegisterDS(Location location, ArrayList<DataType> dataType)
	{
		super();
		this.location = location;
		this.dataType = dataType;
	}

	public Location location;
	public ArrayList<DataType> dataType;
}