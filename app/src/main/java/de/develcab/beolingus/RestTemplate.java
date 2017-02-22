package de.develcab.beolingus;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created by jb on 21.02.17.
 */

public class RestTemplate {
    private static final String TAG = RestTemplate.class.getName();

    public String load(String urlStr, Charset charset) {
        URL url = createUrl(urlStr);

        try (BufferedReader beoStream = new BufferedReader(new InputStreamReader(url.openStream(), charset))) {
            StringBuilder htmlBuilder = new StringBuilder();
            String s;
            while ((s = beoStream.readLine()) != null) {
                htmlBuilder.append(s);
            }

            return htmlBuilder.toString();
        } catch (IOException e) {
            Log.e(TAG, "Stream couldn't be read", e);
            return null;
        }
    }

    public void loadTo(String urlStr, Charset charset, File toFile) {
        try(FileWriter fileWriter = new FileWriter(toFile); BufferedReader beoStream = new BufferedReader(new InputStreamReader(createUrl(urlStr).openStream(), charset))) {
            String line;
            while ((line = beoStream.readLine()) != null) {
                fileWriter.write(line);
            }
        } catch (IOException e) {
            Log.e(TAG, "Dictionary couldn't be written. " + toFile.getAbsolutePath(), e);
        }
    }

    private URL createUrl(String urlStr) {
        URL url;
        try {
            return new URL(urlStr);
        } catch (MalformedURLException e) {
            Log.e(TAG, "URL not correct " + urlStr, e);
            return null;
        }
    }
}