package locale;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import utils.PreferencesUtils;

public class MessageSource
{
	private static final String BUNDLE_NAME = "locale.messages";

	private ResourceBundle resourceBundle;
	
	public static Locale ACTUAL_LOCALE;

	private static MessageSource INSTANCE;

	private MessageSource()
	{
		resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);
	}

	private MessageSource(Locale locale)
	{
		if (locale.getCountry().equals("BR") && locale.getLanguage().equals("pt"))
			resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);
		else
			resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
	}

	public static MessageSource getInstance()
	{
		String language = PreferencesUtils.get().get(PreferencesUtils.PREFERENCE_LANGUAGE, PreferencesUtils.DEFAULT_PREFERENCE_LANGUAGE);
		String[] languageSplitted = language.split("_");
		Locale newLocale = new Locale(languageSplitted[0], languageSplitted[1]);
		if (INSTANCE == null)
			INSTANCE = new MessageSource(newLocale);
		else if (!ACTUAL_LOCALE.equals(newLocale))
			INSTANCE = new MessageSource(newLocale);
		
		ACTUAL_LOCALE = newLocale;

		return INSTANCE;
	}

	public String getString(String key)
	{
		try
		{
			return resourceBundle.getString(key);
		}
		catch (MissingResourceException e)
		{
			return '!' + key + '!';
		}
	}

	public String getString(String key, Object[] parameters)
	{
		try
		{
			return String.format(resourceBundle.getString(key), parameters);
		}
		catch (MissingResourceException e)
		{
			return '!' + key + '!';
		}
	}

	public static Locale[] getAvailableLocales()
	{
		Locale[] locales = new Locale[2];

		locales[0] = Locale.US;
		locales[1] = new Locale("pt", "BR");
		
		return locales;
	}
}
