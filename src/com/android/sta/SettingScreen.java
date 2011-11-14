package com.android.sta;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class SettingScreen extends PreferenceActivity implements Preference.OnPreferenceChangeListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/** Load the preferences from an XML resource */
		addPreferencesFromResource( R.xml.preferences);
		
		EditTextPreference server_host = (EditTextPreference) this.findPreference("server_host");
		server_host.setSummary( server_host.getText());
		server_host.setOnPreferenceChangeListener(this);

		EditTextPreference server_port = (EditTextPreference) this.findPreference("server_port");
		server_port.setSummary( server_port.getText());
		server_port.setOnPreferenceChangeListener(this);
}
	
	public boolean onPreferenceChange( Preference preference, Object newValue){
		preference.setSummary((CharSequence)newValue);
		return true;
	}

}
