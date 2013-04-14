package com.common.types;

import javax.xml.bind.annotation.*;

/*
 *	<id>
 *		<id>15</id>
 *	</id>
 */

@XmlRootElement
public class ID
{
	@XmlElement(name = "id")
	public int id;

	public ID(int id) {
		super();
		this.id = id;
	}	
}