package ru.svetobit.svetoportal.framework;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.UUID;

public class Notion extends Something
{
	String mCustomName;
	ArrayList<Something> mContent = new ArrayList<>();

	public Notion(String eng_name)
	{
		super(eng_name);
	}
	public Notion(String eng_name, UUID uid)
	{
		super(eng_name, uid);
	}

	public String getCustomName()
	{
		return mCustomName;
	}
	public void setCustomName(String name){ mCustomName = name; }

//Подгрузка XML
	@Override
	public Element getXML()
	{
		Element notion = DocumentHelper.createElement("notion")
				.addAttribute("eng_name", getEngName())
				.addAttribute("custom_name", getCustomName())
				.addAttribute("uid", getUID().toString());;


		for(Something thing : mContent)
		{
			notion.add(thing.getXML());
		}

		return notion;
	}
	@Override
	public void loadFromXML(Element element)
	{

	}
}
