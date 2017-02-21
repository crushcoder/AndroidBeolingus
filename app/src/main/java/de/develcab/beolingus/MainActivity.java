package de.develcab.beolingus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    public final static String TEXT_2_TRANSLATE = "de.develcab.beolingus.MESSAGE";


    private BeolingusRestService beo;

    private List<String> searchTerms = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editText = (EditText) findViewById(R.id.translationText);
//        editText.setOnKeyListener(new View.OnKeyListener() {
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                // If the event is a key-down event on the "enter" button
//                if ( event.getAction() == KeyEvent.ACTION_DOWN
//                        && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER
//                            || editText.getText().toString().endsWith("\n"))) { // Perform action on key press
//                    translateText(null);
//                    return true;
//                }
//                return false;
//            }
//        });
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d(TAG, "editorAction " + v.getText().toString());
                translateText(null);
                return true;
            }
        });

        TranslationStorage translationStorage = new TranslationStorage(editText.getContext());
        beo = new BeolingusRestService(translationStorage);
        fillDropdownlist(translationStorage);
        refreshAutocomplete();
    }

    private void fillDropdownlist(TranslationStorage translationStorage) {
        searchTerms = translationStorage.getCachedSearches();
    }

    private void refreshAutocomplete() {
        // Autocomplete
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
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
