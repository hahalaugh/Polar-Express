package gw.resource;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProfileResponse
{
	public ProfileResponse()
	{
		super();
	}

	public ProfileResponse(String username, String password, String email, String ability)
	{
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.ability = ability;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getAbility()
	{
		return ability;
	}

	public void setAbility(String ability)
	{
		this.ability = ability;
	}

	private String username = null;
	private String password = null;
	private String email = null;
	private String ability = null;
}
