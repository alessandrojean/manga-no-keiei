package utils;

import java.util.prefs.Preferences;

public class PreferencesUtils
{
	
	public static final String PREFERENCE_LANGUAGE = "language";
	
	public static final String DEFAULT_PREFERENCE_LANGUAGE = "pt_BR";
	
	public static Preferences get()
	{
		return Preferences.userNodeForPackage(PreferencesUtils.class);
	}
}
