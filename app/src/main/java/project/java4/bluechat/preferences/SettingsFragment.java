package project.java4.bluechat.preferences;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import project.java4.bluechat.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_main);
    }
}
