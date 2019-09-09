package com.bcauction.domain;

import java.time.LocalDateTime;

public class Ownership
{
	private long id;
	private long owner_id;
	private long item_id;
	private LocalDateTime possession_start_date;
	private LocalDateTime possession_end_date;

	public long getId()
	{
		return id;
	}

	public void setId(final long id)
	{
		this.id = id;
	}

	public long getOwner_id()
	{
		return owner_id;
	}

	public void setOwner_id(final long owner_id)
	{
		this.owner_id = owner_id;
	}

	public long getItem_id()
	{
		return item_id;
	}

	public void setItem_id(final long item_id)
	{
		this.item_id = item_id;
	}

	public LocalDateTime getPossession_start_date()
	{
		return possession_start_date;
	}

	public void setPossession_start_date(final LocalDateTime possession_start_date)
	{
		this.possession_start_date = possession_start_date;
	}

	public LocalDateTime getPossession_end_date()
	{
		return possession_end_date;
	}

	public void setPossession_end_date(final LocalDateTime possession_end_date)
	{
		this.possession_end_date = possession_end_date;
	}
}
