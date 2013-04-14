package gw.resource.dataproducer;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ID
{
	public ID(){}
	
	public ID(Integer id)
	{
		super();
		this.id = id;
	}

	public Integer id;
}
