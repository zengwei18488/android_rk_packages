/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.deskclock.settings;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.TwoStatePreference;
import android.view.MenuItem;

import com.android.deskclock.R;
import com.android.deskclock.Utils;

/**
 * Settings for Clock screen saver
 */
public final class ScreensaverSettingsActivity extends AppCompatActivity {

    public static final String KEY_CLOCK_STYLE = "screensaver_clock_style";
    public static final String KEY_NIGHT_MODE = "screensaver_night_mode";
    public static final String PREFS_FRAGMENT_TAG = "prefs_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screensaver_settings);

        // Create the prefs fragment in code to ensure it's created before PreferenceDialogFragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main, new PrefsFragment(), PREFS_FRAGMENT_TAG)
                    .disallowAddToBackStack()
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public static class PrefsFragment extends PreferenceFragmentCompat
            implements Preference.OnPreferenceChangeListener {

        @Override
        @TargetApi(Build.VERSION_CODES.N)
        public void onCreatePreferences(Bundle bundle, String rootKey){
            if (Utils.isNOrLater()) {
                getPreferenceManager().setStorageDeviceProtected();
            }
            addPreferencesFromResource(R.xml.screensaver_settings);
        }

        @Override
        public void onResume() {
            super.onResume();
            refresh();
        }

        @Override
        public boolean onPreferenceChange(Preference pref, Object newValue) {
            if (KEY_CLOCK_STYLE.equals(pref.getKey())) {
                final ListPreference clockStylePref = (ListPreference) pref;
                final int index = clockStylePref.findIndexOfValue((String) newValue);
                clockStylePref.setSummary(clockStylePref.getEntries()[index]);
            }
            return true;
        }

        private void refresh() {
            final ListPreference clockStylePref = (ListPreference) findPreference(KEY_CLOCK_STYLE);
            clockStylePref.setSummary(clockStylePref.getEntry());
            clockStylePref.setOnPreferenceChangeListener(this);
        }
    }
}
