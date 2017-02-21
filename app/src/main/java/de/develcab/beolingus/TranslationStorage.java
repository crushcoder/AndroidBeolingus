package de.develcab.beolingus;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.develcab.beolingus.dto.Translation;

/**
 * Created by jb on 20.02.17.
 */

public class TranslationStorage {
    private static final String TAG = TranslationStorage.class.getName();
    private static final String FILE_SFX = ".html";

    private int MAX_CACHED_SEARCHES = 100;

    private Context context;

    public TranslationStorage(Context context) {
        this.context = context;
    }

    public void storeSearch(String searchTerm, String html) {
        File dir = context.getFilesDir();
        File searchTermFile = new File(dir, getFilename(searchTerm));
        if(searchTermFile.exists()) {
            Log.d(TAG, "File already exists " + getFilename(searchTerm));
        } else {
            try(FileOutputStream outputStream = new FileOutputStream(searchTermFile)) {
                outputStream.write(html.getBytes());
            } catch (IOException e) {
                Log.e(TAG, "File couldn't be written", e);
            }
        }
    }

    private String getFilename(String searhTerm) {
        return searhTerm + FILE_SFX;
    }

    public boolean isCached(String searchTerm) {
        File searchTermFile = toFile(searchTerm);
        return searchTermFile.exists() && searchTermFile.isFile();
    }

    private File toFile(String searchTerm) {
        File dir = context.getFilesDir();
        File searchTermFile = new File(dir, getFilename(searchTerm));
        return searchTermFile;
    }

    public String loadHtml(String searchTerm) {
        if(!isCached(searchTerm)) {
            return "Error, searchTerm not cached? " + searchTerm;
        } else {
            File searchTermFile = toFile(searchTerm);
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

    public List<String> getCachedSearches() {
        File[] allSearches = context.getFilesDir().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(FILE_SFX);
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
            if(i < MAX_CACHED_SEARCHES) {
                searches.add(allSearches[i].getName().substring(0, allSearches[i].getName().length() - FILE_SFX.length()));
            } else {
                allSearches[i].deleteOnExit();
            }
        }

        return searches;
    }
}
