package model;

import locale.MessageSource;

public enum Gender
{
	ACTION(0), ADULT(1), ADVENTURE(2), COMEDY(3), DRAMA(4), ECCHI(5), FANTASY(6), GENDER_BENDER(7), HAREM(8), HENTAI(9), HISTORICAL(10), HORROR(11), JOSEI(12), MARTIAL_ARTS(13), MATURE(14), MECHA(15), MISTERY(16), PSYCHOLOGICAL(17), ROMANCE(18), SCHOOL_LIFE(19), SCI_FI(20), SEINEN(21), SHOUJO(22), SHOUJO_AI(23), SHOUNEN(24), SHOUNEN_AI(25), SLICE_OF_LIFE(26), SPORTS(27), SUPERNATURAL(29), THRILLER(30), TRAGEDY(31), YAOI(32), YURI(33);

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
			case MISTERY:
				return MessageSource.getInstance().getString("Gender.mistery");
			case PSYCHOLOGICAL:
				return MessageSource.getInstance().getString("Gender.psychological");
			case ROMANCE:
				return MessageSource.getInstance().getString("Gender.romance");
			case SCHOOL_LIFE:
				return MessageSource.getInstance().getString("Gender.schoolLife");
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
			default:
				return "";

		}
	}
}
