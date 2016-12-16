package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import locale.MessageSource;

public enum Gender
{
	ACTION(0), ADULT(1), ADVENTURE(2), COMEDY(3), DRAMA(4), ECCHI(5), FANTASY(6), GENDER_BENDER(7), HAREM(8), HENTAI(9), HISTORICAL(10), HORROR(11), JOSEI(12), MARTIAL_ARTS(13), MATURE(14), MECHA(15), MYSTERY(16), PSYCHOLOGICAL(17), ROMANCE(18), SCHOOL(19), SCI_FI(20), SEINEN(21), SHOUJO(22), SHOUJO_AI(23), SHOUNEN(24), SHOUNEN_AI(25), SLICE_OF_LIFE(26), SPORTS(27), SUPERNATURAL(28), THRILLER(29), TRAGEDY(30), YAOI(31), YURI(32), CARS(33), DEMENTIA(34), DEMONS(35), GAME(36), KIDS(37), MAGIC(38), MILITARY(39), MUSIC(40), PARODY(41), POLICE(42), SAMURAI(43), SPACE(44), SUPER_POWER(45), VAMPIRE(46);

	private int value;

	private Gender(int value)
	{
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	public static Gender fromValue(int value)
	{
		return Gender.values()[value];
	}

	public static Gender[] getValuesOrdered()
	{
		List<Gender> genders = Arrays.asList(values());
		Collections.sort(genders, new Comparator<Gender>() {

			@Override
			public int compare(Gender o1, Gender o2)
			{
				return o1.toString().compareTo(o2.toString());
			}
		});
		Gender[] result = new Gender[genders.size()];
		genders.toArray(result);
		return result;
	}

	@Override
	public String toString()
	{
		switch (this)
		{
			case ACTION:
				return MessageSource.getInstance().getString("Gender.action");
			case ADULT:
				return MessageSource.getInstance().getString("Gender.adult");
			case ADVENTURE:
				return MessageSource.getInstance().getString("Gender.adventure");
			case COMEDY:
				return MessageSource.getInstance().getString("Gender.comedy");
			case DRAMA:
				return MessageSource.getInstance().getString("Gender.drama");
			case ECCHI:
				return MessageSource.getInstance().getString("Gender.ecchi");
			case FANTASY:
				return MessageSource.getInstance().getString("Gender.fantasy");
			case GENDER_BENDER:
				return MessageSource.getInstance().getString("Gender.genderBender");
			case HAREM:
				return MessageSource.getInstance().getString("Gender.harem");
			case HENTAI:
				return MessageSource.getInstance().getString("Gender.hentai");
			case HISTORICAL:
				return MessageSource.getInstance().getString("Gender.historical");
			case HORROR:
				return MessageSource.getInstance().getString("Gender.horror");
			case JOSEI:
				return MessageSource.getInstance().getString("Gender.josei");
			case MARTIAL_ARTS:
				return MessageSource.getInstance().getString("Gender.martialArts");
			case MATURE:
				return MessageSource.getInstance().getString("Gender.mature");
			case MECHA:
				return MessageSource.getInstance().getString("Gender.mecha");
			case MYSTERY:
				return MessageSource.getInstance().getString("Gender.mystery");
			case PSYCHOLOGICAL:
				return MessageSource.getInstance().getString("Gender.psychological");
			case ROMANCE:
				return MessageSource.getInstance().getString("Gender.romance");
			case SCHOOL:
				return MessageSource.getInstance().getString("Gender.school");
			case SCI_FI:
				return MessageSource.getInstance().getString("Gender.sciFi");
			case SEINEN:
				return MessageSource.getInstance().getString("Gender.seinen");
			case SHOUJO:
				return MessageSource.getInstance().getString("Gender.shoujo");
			case SHOUJO_AI:
				return MessageSource.getInstance().getString("Gender.shoujoAi");
			case SHOUNEN:
				return MessageSource.getInstance().getString("Gender.shounen");
			case SHOUNEN_AI:
				return MessageSource.getInstance().getString("Gender.shounenAi");
			case SLICE_OF_LIFE:
				return MessageSource.getInstance().getString("Gender.sliceOfLife");
			case SPORTS:
				return MessageSource.getInstance().getString("Gender.sports");
			case SUPERNATURAL:
				return MessageSource.getInstance().getString("Gender.supernatural");
			case THRILLER:
				return MessageSource.getInstance().getString("Gender.thriller");
			case TRAGEDY:
				return MessageSource.getInstance().getString("Gender.tragedy");
			case YAOI:
				return MessageSource.getInstance().getString("Gender.yaoi");
			case YURI:
				return MessageSource.getInstance().getString("Gender.yuri");
			case CARS:
				return MessageSource.getInstance().getString("Gender.cars");
			case DEMENTIA:
				return MessageSource.getInstance().getString("Gender.dementia");
			case DEMONS:
				return MessageSource.getInstance().getString("Gender.demons");
			case GAME:
				return MessageSource.getInstance().getString("Gender.game");
			case KIDS:
				return MessageSource.getInstance().getString("Gender.kids");
			case MAGIC:
				return MessageSource.getInstance().getString("Gender.magic");
			case MILITARY:
				return MessageSource.getInstance().getString("Gender.military");
			case MUSIC:
				return MessageSource.getInstance().getString("Gender.music");
			case PARODY:
				return MessageSource.getInstance().getString("Gender.parody");
			case POLICE:
				return MessageSource.getInstance().getString("Gender.police");
			case SAMURAI:
				return MessageSource.getInstance().getString("Gender.samurai");
			case SPACE:
				return MessageSource.getInstance().getString("Gender.space");
			case SUPER_POWER:
				return MessageSource.getInstance().getString("Gender.superPower");
			case VAMPIRE:
				return MessageSource.getInstance().getString("Gender.vampire");
			default:
				return "";
		}
	}

	public String toString(Locale locale)
	{
		switch (this)
		{
			case ACTION:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.action");
			case ADULT:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.adult");
			case ADVENTURE:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.adventure");
			case COMEDY:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.comedy");
			case DRAMA:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.drama");
			case ECCHI:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.ecchi");
			case FANTASY:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.fantasy");
			case GENDER_BENDER:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.genderBender");
			case HAREM:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.harem");
			case HENTAI:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.hentai");
			case HISTORICAL:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.historical");
			case HORROR:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.horror");
			case JOSEI:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.josei");
			case MARTIAL_ARTS:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.martialArts");
			case MATURE:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.mature");
			case MECHA:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.mecha");
			case MYSTERY:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.mystery");
			case PSYCHOLOGICAL:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.psychological");
			case ROMANCE:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.romance");
			case SCHOOL:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.school");
			case SCI_FI:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.sciFi");
			case SEINEN:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.seinen");
			case SHOUJO:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.shoujo");
			case SHOUJO_AI:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.shoujoAi");
			case SHOUNEN:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.shounen");
			case SHOUNEN_AI:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.shounenAi");
			case SLICE_OF_LIFE:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.sliceOfLife");
			case SPORTS:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.sports");
			case SUPERNATURAL:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.supernatural");
			case THRILLER:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.thriller");
			case TRAGEDY:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.tragedy");
			case YAOI:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.yaoi");
			case YURI:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.yuri");
			case CARS:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.cars");
			case DEMENTIA:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.dementia");
			case DEMONS:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.demons");
			case GAME:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.game");
			case KIDS:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.kids");
			case MAGIC:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.magic");
			case MILITARY:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.military");
			case MUSIC:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.music");
			case PARODY:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.parody");
			case POLICE:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.police");
			case SAMURAI:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.samurai");
			case SPACE:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.space");
			case SUPER_POWER:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.superPower");
			case VAMPIRE:
				return MessageSource.getResourceBundleInLocale(Locale.US).getString("Gender.vampire");
			default:
				return "";
		}
	}
}
