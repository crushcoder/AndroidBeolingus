package de.develcab.beolingus.service;

import android.content.Context;
import android.content.SharedPreferences;

import de.develcab.beolingus.MainActivity;
import de.develcab.beolingus.R;

/**
 * Created by jb on 26.02.17.
 */

public class PreferencesService {
    private static final String PREFERENCES_NAME = "de.develcab.beolingus.sharedpreferences";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String servicePrefName;
    private String serviceDefaultValue;
    private String maxCachedSearchesPrefName;
    private Integer maxCachedSearchesDefaultValue;

    public PreferencesService(Context context) {
        preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        servicePrefName = context.getString(R.string.ding_service_pref_name);
        serviceDefaultValue = context.getString(R.string.ding_serivce_default_value);
        maxCachedSearchesPrefName = context.getString(R.string.number_of_cached_files_pref_name);
        maxCachedSearchesDefaultValue = context.getResources().getInteger(R.integer.number_of_cached_files_default_value);
    }

    public String getServiceName() {
        String serviceName = preferences.getString(servicePrefName, serviceDefaultValue);
        return serviceName;
    }

    public void serServiceName(String serviceName) {
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putString(servicePrefName, serviceName);
        preferencesEditor.commit();
    }

    public int getMaxCachedSearches() {
        int maxCachedSearches = preferences.getInt(maxCachedSearchesPrefName, maxCachedSearchesDefaultValue);
        return maxCachedSearches;
    }

    public void setMaxCachedSearches(int maxCachedSearches) {
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putInt(maxCachedSearchesPrefName, maxCachedSearches);
        preferencesEditor.commit();
    }
}
