package ru.svetobit.svetokit.framework;

import java.util.UUID;

public abstract class Something implements SaveLoad
{
	UUID mUID = UUID.randomUUID();
	String mEngName;

	public Something(String eng_name)
	{
		this(eng_name, UUID.randomUUID());
	}
	public Something(String eng_name, UUID uid)
	{
		mUID = uid;
		mEngName = eng_name;
	}

	public UUID getUID()
	{
		return mUID;
	}
	public String getEngName()
	{
		return mEngName;
	}
	public void setEngName(String name)
	{
		mEngName = name;
	}
}
