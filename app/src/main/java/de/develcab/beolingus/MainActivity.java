package de.develcab.beolingus;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    public final static String TEXT_2_TRANSLATE = "de.develcab.beolingus.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editText = (EditText) findViewById(R.id.translationText);
        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) { // Perform action on key press
                    translateText(null);
                    return true;
                }
                return false;
            }
        });
    }

    public void translateText(View view) {
        EditText editText = (EditText) findViewById(R.id.translationText);
        Log.d(TAG, editText.getText().toString());

        TextView outputText = (TextView) findViewById(R.id.outputTextView);
        TableLayout tableLayout = (TableLayout)findViewById(R.id.resultTable);
        tableLayout.removeAllViews();

        DownloadTranslationTask task = new DownloadTranslationTask(outputText, tableLayout);
        task.execute(editText.getText().toString());

    }



}
