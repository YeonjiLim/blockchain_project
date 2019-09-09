package com.bcauction.domain;

public class DigitalWork
{
	private long id;
	private String name;
	private String explanation;
	private String disclosure = "Y";
	private String status = "Y";
	private long member_id;

	public long getId()
	{
		return id;
	}

	public void setId(final long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public String getExplanation()
	{
		return explanation;
	}

	public void setExplanation(final String explanation)
	{
		this.explanation = explanation;
	}

	public String getDisclosure()
	{
		return disclosure;
	}

	public void setDisclosure(final String disclosure)
	{
		this.disclosure = disclosure;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(final String status)
	{
		this.status = status;
	}

	public long getMember_id()
	{
		return member_id;
	}

	public void setMember_id(final long member_id)
	{
		this.member_id = member_id;
	}

	@Override
	public String toString()
	{
		return new StringBuilder()
				.append("{ id: " + id)
				.append("\n\tname: " + name)
				.append("\n\texplanation: " + explanation)
				.append("\n\tdisclosure: " + disclosure)
				.append("\n\tstatus: " + status)
				.append("\n\tmember_id: " + member_id)
				.append(" }")
				.toString();
	}
}
