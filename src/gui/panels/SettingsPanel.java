package gui.panels;

import gui.Main;
import gui.Splash;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.PreferencesUtils;
import javax.swing.JButton;
import locale.MessageSource;

public class SettingsPanel extends JPanel
{
	private JComboBox<String> cbLanguages;
	private Locale[] availableLanguages;

	public SettingsPanel()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		Box hbLanguage = Box.createHorizontalBox();
		add(hbLanguage);
		
		JLabel lbLanguage = new JLabel(MessageSource.getInstance().getString("SettingsPanel.lbl.language"));
		hbLanguage.add(lbLanguage);
		
		Component rigidArea = Box.createRigidArea(new Dimension(5, 1));
		hbLanguage.add(rigidArea);
		
		cbLanguages = new JComboBox<>();
		cbLanguages.setMaximumSize(new Dimension(100, 26));
		cbLanguages.setModel(new DefaultComboBoxModel<String>());
		hbLanguage.add(cbLanguages);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		hbLanguage.add(horizontalGlue);
		
		Component verticalGlue = Box.createVerticalGlue();
		add(verticalGlue);
		
		Box horizontalBox = Box.createHorizontalBox();
		add(horizontalBox);
		
		Component horizontalGlue_1 = Box.createHorizontalGlue();
		horizontalBox.add(horizontalGlue_1);
		
		JButton btOK = new JButton(MessageSource.getInstance().getString("Basics.ok"));
		btOK.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				PreferencesUtils.get().put(PreferencesUtils.PREFERENCE_LANGUAGE, availableLanguages[cbLanguages.getSelectedIndex()].toString());
				Splash.MAIN.init();
				Main.showPanel(Panels.SETTINGS);
			}
		});
		horizontalBox.add(btOK);
		
		fillValues();
		selectValues();
	}
	
	private void fillValues()
	{
		availableLanguages = MessageSource.getAvailableLocales();
		for(Locale l : availableLanguages)
			cbLanguages.addItem(l.getDisplayLanguage(l).substring(0,1).toUpperCase()+l.getDisplayLanguage(l).substring(1));
	}
	
	private void selectValues()
	{
		selectLanguage();
	}
	
	private void selectLanguage()
	{
		String language = PreferencesUtils.get().get(PreferencesUtils.PREFERENCE_LANGUAGE, PreferencesUtils.DEFAULT_PREFERENCE_LANGUAGE);
		String[] languageSplitted = language.split("_");
		
		for(int i=0;i<availableLanguages.length;i++)
		{
			Locale locale = availableLanguages[i];
			if(locale.getLanguage().equals(languageSplitted[0])&&locale.getCountry().equals(languageSplitted[1]))
			{
				cbLanguages.setSelectedIndex(i);
				break;
			}
		}
	}

}
