package de.develcab.beolingus.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.develcab.beolingus.MainActivity;
import de.develcab.beolingus.R;
import de.develcab.beolingus.service.PreferencesService;

/**
 * Created by jb on 20.02.17.
 */

public class TranslationStorage {
    private static final String TAG = TranslationStorage.class.getName();
    private static final String FILE_SFX = ".json";
    private static final String SERVICE_DIVIDER = "_";

    private int maxCachedSearches = 100;

    private Context context;
    private PreferencesService preferencesService;

    public TranslationStorage(Context context) {
        this.context = context;
        this.preferencesService = new PreferencesService(context);
        maxCachedSearches = preferencesService.getMaxCachedSearches();
    }

    public void storeSearch(String searchTerm, String serviceName, String html) {
        File searchTermFile = toFile(searchTerm, serviceName);
        if(searchTermFile.exists()) {
            Log.d(TAG, "File already exists " + getFilename(searchTerm, serviceName));
        } else {
            try(FileOutputStream outputStream = new FileOutputStream(searchTermFile)) {
                outputStream.write(html.getBytes());
            } catch (IOException e) {
                Log.e(TAG, "File couldn't be written", e);
            }
        }
    }

    private String getFilename(String searchTerm, String serviceName) {
        return searchTerm + SERVICE_DIVIDER + serviceName + FILE_SFX;
    }

    public boolean isCached(String searchTerm, String serviceName) {
        File searchTermFile = toFile(searchTerm, serviceName);
        return searchTermFile.exists() && searchTermFile.isFile();
    }

    private File toFile(String searchTerm, String serviceName) {
        File dir = context.getFilesDir();
        File searchTermFile = new File(dir, getFilename(searchTerm, serviceName));
        return searchTermFile;
    }

    public String loadHtml(String searchTerm, String serviceName) {
        if(!isCached(searchTerm, serviceName)) {
            return "Error, searchTerm not cached? " + searchTerm;
        } else {
            File searchTermFile = toFile(searchTerm, serviceName);
            StringBuilder builder = new StringBuilder();
            try (BufferedReader inputStream = new BufferedReader(new FileReader(searchTermFile))) {
                String s;
                while((s = inputStream.readLine()) != null) {
                    builder.append(s);
                }
            } catch (IOException e) {
                Log.e(TAG, "File couldn't be read: " + searchTerm + ":"+ searchTermFile.getAbsolutePath());
                e.printStackTrace();
            }

            return builder.toString();
        }
    }

    public List<String> getCachedSearches(final String serviceName) {
        File[] allSearches = context.getFilesDir().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(SERVICE_DIVIDER + serviceName + FILE_SFX);
            }
        });


        List<File> files = Arrays.asList(allSearches);
        Collections.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return Long.valueOf(o1.lastModified()).compareTo(Long.valueOf(o2.lastModified()));
            }
        });
        List<String> searches = new ArrayList<>();
        for (int i = 0; i < allSearches.length; i++) {
            if(i < maxCachedSearches) {
                int searchStringEndIndex = allSearches[i].getName().lastIndexOf(SERVICE_DIVIDER);
                String searchName = allSearches[i].getName().substring(0, searchStringEndIndex);
                searches.add(searchName);
            } else {
                allSearches[i].deleteOnExit();
            }
        }

        return searches;
    }
}
