package com.bcauction.domain;

import java.math.BigDecimal;

public class Wallet
{
	private long id;
	private long owner_id;
	private String address;
	private BigDecimal balance = BigDecimal.valueOf(0);
	private int charge_count = 0;

	public long getOwner_id()
	{
		return owner_id;
	}

	public void setOwner_id(final long owner_id)
	{
		this.owner_id = owner_id;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(final String address)
	{
		this.address = address;
	}

	public BigDecimal getBalance()
	{
		return balance;
	}

	public void setBalance(final BigDecimal balance)
	{
		this.balance = balance;
	}

	public long getId()
	{
		return id;
	}

	public void setId(final long id)
	{
		this.id = id;
	}

	public int getCharge_count()
	{
		return charge_count;
	}

	public void setCharge_count(final int charge_count)
	{
		this.charge_count = charge_count;
	}

	public boolean canCharge(){
		return this.charge_count < 10;
	}
}
