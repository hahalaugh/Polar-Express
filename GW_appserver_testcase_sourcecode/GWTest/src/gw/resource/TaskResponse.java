package gw.resource;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TaskResponse
{
	public TaskResponse()
	{
		super();
	}

	public TaskResponse(String[] taskName, String[] taskDescription)
	{
		super();
		this.taskName = taskName;
		this.taskDescription = taskDescription;
	}

	public String[] getTaskName()
	{
		return taskName;
	}

	public void setTaskName(String[] taskName)
	{
		this.taskName = taskName;
	}

	public String[] getTaskDescription()
	{
		return taskDescription;
	}

	public void setTaskDescription(String[] taskDescription)
	{
		this.taskDescription = taskDescription;
	}

	private String[] taskName;
	private String[] taskDescription;
}
