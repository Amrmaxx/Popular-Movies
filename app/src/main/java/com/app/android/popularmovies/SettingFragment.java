package com.app.android.popularmovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

public class SettingFragment extends
        PreferenceFragment implements
        SharedPreferences.OnSharedPreferenceChangeListener {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefrences);

        // Getting SharedPreferences and setting value
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        Preference p = getPreferenceScreen().getPreference(0);
        String value = sharedPreferences.getString(p.getKey(), "");
        setPrefSummary(p, value);
    }

    //  Method to Set preference summary
    private void setPrefSummary(Preference p, String value) {
        ListPreference listPreference = (ListPreference) p;
        int prefIndex = listPreference.findIndexOfValue(value);
        listPreference.setSummary(listPreference.getEntries()[prefIndex]);
    }

    // handling preferences changes
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if (preference != null) {
            String value = sharedPreferences.getString(preference.getKey(), "");
            setPrefSummary(preference, value);
        }
    }

    // Registering listener
    @Override
    public void onStart() {
        super.onStart();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    // un-registering listener
    @Override
    public void onStop() {
        super.onStop();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
