package ru.svetobit.svetoportal.framework;

import org.dom4j.Element;

public interface SaveLoad
{
	Element getXML();
	void loadFromXML(Element element);
}
