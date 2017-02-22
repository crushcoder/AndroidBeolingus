package de.develcab.beolingus;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    private BeolingusRestService beo;

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
                translateText(null);
                return true;
            }
        });

        this.context = editText.getContext();
        TranslationStorage translationStorage = new TranslationStorage(editText.getContext());
        RestTemplate restTemplate = new RestTemplate();
        beo = new BeolingusRestService(translationStorage, restTemplate);
        fillDropdownlist(translationStorage);
        refreshAutocomplete();
    }

    private void fillDropdownlist(TranslationStorage translationStorage) {
        searchTerms = translationStorage.getCachedSearches();
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
        Log.d(TAG, searchTerm);

        TextView outputText = (TextView) findViewById(R.id.outputTextView);
        TableLayout tableLayout = (TableLayout) findViewById(R.id.resultTable);
        tableLayout.removeAllViews();

        DownloadTranslationTask task = new DownloadTranslationTask(outputText, tableLayout, beo);
        task.execute(searchTerm);

        synchronized (searchTerms) {
            if (!searchTerms.contains(searchTerm)) {
                searchTerms.add(searchTerm);
            }
        }
        refreshAutocomplete();
    }


}
