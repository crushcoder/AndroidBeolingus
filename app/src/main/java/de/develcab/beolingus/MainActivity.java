package de.develcab.beolingus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.develcab.beolingus.service.BeolingusRestService;
import de.develcab.beolingus.service.PreferencesService;
import de.develcab.beolingus.util.RestTemplate;
import de.develcab.beolingus.util.TranslationStorage;

public class MainActivity extends AppCompatActivity implements LastSearchesDialogFragment.LastSearchChosenListener {
    private static final String TAG = MainActivity.class.getName();
    public static final String SEARCH_TERMS_ARGUMENT_KEY = "searchTerms";

    private BeolingusRestService beo;
    private PreferencesService preferencesService;


    private Context context;

    private List<String> searchTerms = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editText = (EditText) findViewById(R.id.translationText);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d(TAG, "editorAction " + v.getText().toString());
                View view = null;
                translateText(view);
                return false;
            }
        });

        this.context = editText.getContext();
        TranslationStorage translationStorage = new TranslationStorage(editText.getContext());
        RestTemplate restTemplate = new RestTemplate();
        beo = new BeolingusRestService(translationStorage, restTemplate);
        preferencesService = new PreferencesService(context);
        fillDropdownlist(translationStorage);
        refreshAutocomplete();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Log.d(TAG, "Preferences button pressed.");
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);

                return true;
            case R.id.action_favorite:
                Log.d(TAG, "Favorites/Recent searches button pressed.");
                DialogFragment dialog = new LastSearchesDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(SEARCH_TERMS_ARGUMENT_KEY, new ArrayList<String>(searchTerms));
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(), "LastSearchesFragment");

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fillDropdownlist(TranslationStorage translationStorage) {
        searchTerms = translationStorage.getCachedSearches(preferencesService.getServiceName());
    }

    private void refreshAutocomplete() {
        // Autocomplete
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.context,
                android.R.layout.simple_dropdown_item_1line, searchTerms);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.translationText);
        textView.setAdapter(adapter);
    }

    public void translateText(View view) {
        EditText editText = (EditText) findViewById(R.id.translationText);
        String searchTerm = editText.getText().toString().trim();
        translateText(searchTerm);
    }

    public void translateText(String searchTerm) {
        Log.d(TAG, searchTerm);

        TableLayout tableLayout = (TableLayout) findViewById(R.id.resultTable);
        tableLayout.removeAllViews();

        DownloadTranslationTask task = new DownloadTranslationTask(tableLayout, beo);
        String serviceName = preferencesService.getServiceName();
        task.execute(searchTerm, serviceName);

        synchronized (searchTerms) {
            if (!searchTerms.contains(searchTerm)) {
                searchTerms.add(searchTerm);
            }
        }
        refreshAutocomplete();
    }

    @Override
    public void searchTermChosen(String searchTerm) {
        EditText editText = (EditText) findViewById(R.id.translationText);
        editText.setText(searchTerm);
        translateText(searchTerm);
    }
}
