package utils;

import gui.components.borders.RoundedBorder;
import gui.components.borders.RoundedTitleBorder;

import java.awt.Color;

import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.bulenkov.darcula.DarculaUIUtil;
import com.bulenkov.darcula.ui.DarculaTextFieldUI;

public class BorderUtils
{
	public static final Color DEFAULT_LINE_COLOR = new Color(117,117,117);
	public static final Color DEFAULT_BACKGROUND_COLOR = new Color(70,96,132);
	
	public static final Color DEFAULT_BACKGROUND_TOP_COLOR = new Color(0,79,114);
	public static final Color DEFAULT_BACKGROUND_BOTTOM_COLOR = new Color(49,113,164);
	
	public static Border createRoundedBorder()
	{		
		return new RoundedBorder(DEFAULT_LINE_COLOR);
	}
	
	public static Border createRoundedTitleBorder(String title)
	{
		return new RoundedTitleBorder(title,DEFAULT_LINE_COLOR,new Color[]{DEFAULT_BACKGROUND_TOP_COLOR,DEFAULT_BACKGROUND_BOTTOM_COLOR}, 8, 8, 0, 0);
	}
	
	public static Border createRoundedTitleBorder(String title, String info)
	{
		return new RoundedTitleBorder(title, info, DEFAULT_LINE_COLOR, new Color[]{DEFAULT_BACKGROUND_TOP_COLOR,DEFAULT_BACKGROUND_BOTTOM_COLOR}, 8, 8, 0, 0);
	}
	
	public static Border createRoundedTitleBorder(String title, int br)
	{
		return new RoundedTitleBorder(title,DEFAULT_LINE_COLOR,new Color[]{DEFAULT_BACKGROUND_TOP_COLOR,DEFAULT_BACKGROUND_BOTTOM_COLOR}, br);
	}
	
	public static Border createRoundedTitleBorder(String title, int brt, int brb)
	{
		return new RoundedTitleBorder(title,DEFAULT_LINE_COLOR,new Color[]{DEFAULT_BACKGROUND_TOP_COLOR,DEFAULT_BACKGROUND_BOTTOM_COLOR}, brt, brt, brb, brb);
	}
	
	public static Border createRoundedTitleBorder(String title, int br1, int br2, int br3, int br4)
	{
		return new RoundedTitleBorder(title,DEFAULT_LINE_COLOR,new Color[]{DEFAULT_BACKGROUND_TOP_COLOR,DEFAULT_BACKGROUND_BOTTOM_COLOR}, br1, br2, br3, br4);
	}
}
