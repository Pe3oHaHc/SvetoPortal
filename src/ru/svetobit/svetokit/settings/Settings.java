package ru.svetobit.svetokit.settings;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import ru.svetobit.svetokit.SvetoKit;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Иван on 28.05.2015.
 */
public class Settings
{
	private static ArrayList<LetterType> mLetterTypes;

	public static void loadSettings()
	{
		//loadLetterTypes();
	}

	public static ArrayList<LetterType> getLetterTypes(){ return mLetterTypes; }
	public static LetterType getLetterType(String id)
	{
		for(LetterType type : mLetterTypes)
		{
			if(type.mID.equalsIgnoreCase(id))
				return type;
		}

		return null;
	}

	private static void loadLetterTypes()
	{
		SAXReader reader = new SAXReader();
		Document doc = null;
		try
		{
			doc = reader.read(SvetoKit.class.getResourceAsStream("settings/letters.xml"));
		}catch(DocumentException e){e.printStackTrace(); return;}

		Element root = doc.getRootElement();
		Element letter_types = root.element("letter_types");

		for(Iterator i = letter_types.elementIterator(); i.hasNext();)
		{
			Element type = (Element)i.next();
			mLetterTypes.add(new LetterType(type.attribute("id").getValue(), type.attribute("name").getValue(), type.getText()));
		}
	}
	public static class LetterType
	{
		public String mID, mName, mRegExp;

		public LetterType(String id, String name, String regex)
		{
			mID = id;
			mName = name;
			mRegExp = regex;
		}
	}
}
