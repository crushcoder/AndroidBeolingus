package de.develcab.beolingus;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Downloads de-en translations if not newest one already downloaded.
 *
 * Created by jb on 21.02.17.
 */

public class DownloadDictionaryTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = DownloadDictionaryTask.class.getName();
    private static final String DING_FILE_NAME = "de-en.txt";
    private static final String DING_ZIP_FILE_NAME = "de-en.txt.zip";
    private static final String DING_URL = "http://ftp.tu-chemnitz.de/pub/Local/urz/ding/de-en/de-en.txt.zip";

    private Context context;
    private RestTemplate restTemplate;
    private AtomicBoolean cancelled;

    public DownloadDictionaryTask(Context context, RestTemplate restTemplate) {
        this.context = context;
        this.restTemplate = restTemplate;
        this.cancelled = new AtomicBoolean(false);
    }

    @Override
    protected Void doInBackground(Void... params) {
        // Look for an old dictionary file
        File ding = getDing();
        if(!ding.exists() || !ding.isFile()) {
            // download ding
            File dingZip = new File(context.getFilesDir(), DING_ZIP_FILE_NAME);
            restTemplate.loadTo(DING_URL, StandardCharsets.UTF_8, dingZip);

            // decompress ding
            try (FileWriter fileWriter = new FileWriter(ding); ZipInputStream inputStream = new ZipInputStream(new FileInputStream(dingZip))) {
                int sign;
                while ((sign = inputStream.read()) != -1 && !cancelled.get()) {
                    fileWriter.write(sign);
                }
            } catch (FileNotFoundException e) {
                Log.e(TAG, "File not found? " + DING_FILE_NAME, e);
                return null;
            } catch (IOException e) {
                Log.e(TAG, "Error reading file. " + DING_FILE_NAME, e);
                return null;
            }
        }

        return null;
    }

    private File getDing() {
        return new File(context.getFilesDir(), DING_FILE_NAME);
    }

    @Override
    protected void onCancelled(Void aVoid) {
        this.cancelled.set(true);
    }

}
