package de.develcab.beolingus;

import android.graphics.Color;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import de.develcab.beolingus.dto.Translation;
import de.develcab.beolingus.service.BeolingusRestService;

/**
 * Created by jb on 19.02.17.
 */

public class DownloadTranslationTask extends AsyncTask<String, Void, Long> {
    private static final String TAG = DownloadTranslationTask.class.getName();
    private static final int BEO_COLOR = Color.rgb(229, 255, 204);

    private static TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
    private static TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);


    private TableLayout table;
    private BeolingusRestService beo;

    private List<Translation> translations = Collections.emptyList();

    public DownloadTranslationTask(TableLayout table, BeolingusRestService beo) {
        this.table = table;
        table.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));// assuming the parent view is a LinearLayout
        table.setStretchAllColumns(true);

        this.beo = beo;
    }

    protected Long doInBackground(String... searchStrings) {
        translations = beo.loadTranslation(searchStrings[0], searchStrings[1]);

        return Long.valueOf(translations.size());
    }

    protected void onPostExecute(Long result) {

        if (translations.isEmpty()) {
        } else {
            Log.d(TAG, "translations found: " + translations.size());
            for (Translation translation : translations) {
                TableRow row = new TableRow(table.getContext());
                row.setLayoutParams(tableParams); // TableLayout is the parent view
                TextView left  = createView(translation.getKnown());
                left.setBackgroundColor(BEO_COLOR);
                row.addView(left);
                TextView right = createView(translation.getForeign());
                row.addView(right);
                table.addView(row);
            }
        }

    }

    private TextView createView(String text) {
        TextView textView = new TextView(table.getContext());
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setText(text);
        textView.setWidth(250);
        textView.setLayoutParams(rowParams);// TableRow is the parent view
        textView.setGravity(Gravity.CENTER | Gravity.CENTER);
        textView.setPadding(3, 3, 3, 3);


        return textView;
    }
}