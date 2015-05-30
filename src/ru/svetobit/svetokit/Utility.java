package ru.svetobit.svetokit;

/**
 * Created by Иван on 28.05.2015.
 */
public class Utility
{
	public static class StringChecker
	{
		StringCheckHandler mHandler;
		String mMessage;

		public StringChecker(StringCheckHandler handler, String error_message)
		{
			mHandler = handler;
			mMessage = error_message;
		}
		public String getError()
		{
			return mMessage;
		}

		public boolean check(String input)
		{
			return mHandler.check(input);
		}
	}
	public interface StringCheckHandler
	{
		boolean check(String input);
	}
}
