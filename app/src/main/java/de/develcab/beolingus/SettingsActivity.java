package de.develcab.beolingus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import de.develcab.beolingus.service.DingServiceName;
import de.develcab.beolingus.service.PreferencesService;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = SettingsActivity.class.getName();

    private PreferencesService preferencesService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.preferencesService = new PreferencesService(this);

        // fill settings with values
        // set number of cached files
        EditText cacheNumber = (EditText) findViewById(R.id.settings_cache_size);
        cacheNumber.setText(String.valueOf(preferencesService.getMaxCachedSearches()));
        cacheNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String text = v.getText().toString();
                try {
                    Jsoup.clean(text, Whitelist.none());
                    int cacheNumber = Integer.parseInt(text);
                    preferencesService.setMaxCachedSearches(cacheNumber);
                } catch (NumberFormatException e) {
                    Log.w(TAG, "Number couldn't be parsed: " + text);
                }
                return false;
            }
        });

        // fill ding services dropdown and set current set as default
        final Spinner spinner = (Spinner) findViewById(R.id.ding_service_dropdown);
        // Specify the layout to use when the list of choices appears
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ding_service_names, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(DingServiceName.findPosition(preferencesService.getServiceName()));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getSelectedItem();
                Log.d(TAG, "Service chosen: " + selectedItem);
                DingServiceName serviceName = DingServiceName.findByValue(selectedItem);
                if (serviceName != null) {
                    preferencesService.serServiceName(serviceName.getName());

                } // else do nothing, old state stays
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing, keep old value
            }
        });
    }
}
