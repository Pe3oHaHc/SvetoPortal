package ru.svetobit.svetokit.framework;

import org.dom4j.Document;
import org.dom4j.Element;

public interface SaveLoad
{
	Element getXML();
	void loadFromXML(Element element);
}
