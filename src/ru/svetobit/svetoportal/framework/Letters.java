package ru.svetobit.svetoportal.framework;

import org.dom4j.Element;
import ru.svetobit.svetoportal.Utility;
import ru.svetobit.svetoportal.settings.Settings.LetterType;

import java.util.UUID;

public class Letters extends Primitive
{
	LetterType[] mOnlyLetters;
	OnlyLettersChecker mChecker;
	String mValue;

	public Letters(String eng_name, UUID uid)
	{
		super(eng_name, uid);
	}
	public void setOnlyLetters(LetterType... types)
	{
		mOnlyLetters = types;

		if(mOnlyLetters.length == 0)
			return;

		String error_message = "В этой строке допустимы только следующие символы:";
		for(LetterType type : mOnlyLetters)
		{
			error_message += " " + type.mName;
		}
		error_message += ".";

		mChecker = new OnlyLettersChecker(error_message);
	}
	public void setValue(String value) throws WrongSymbolException
	{
		if(mChecker == null)
		{
			mValue = value;
			return;
		}

		if(mChecker.check(value))
			mValue = value;
		else
			throw new WrongSymbolException(mChecker.getError());
	}

//Детали
	public class OnlyLettersChecker extends Utility.StringChecker
	{
		public OnlyLettersChecker(String error_message)
		{
			super((input) -> {
				String regex = "[";
				for (LetterType type : mOnlyLetters)
				{
					regex += type.mRegExp;
				}
				regex += "]*";

				return input.matches(regex);
			}, error_message);
		}
	}

//Исключения
	public static class WrongSymbolException extends Exception{public WrongSymbolException(String message){super(message);}}

//Работа с XML
	@Override
	public Element getXML()
	{
		return null;
	}
	@Override
	public void loadFromXML(Element element)
	{

	}
}
