package de.develcab.beolingus.util;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;

import de.develcab.beolingus.dto.CachedResult;
import de.develcab.beolingus.dto.Translation;

/**
 * Created by jb on 23.02.17.
 */

public class JsonMapper {
    private static final String TAG  = JsonMapper.class.getName();

    // only static methods
    private JsonMapper() {
        super();
    }

    public static List<Translation> mapJson(String html) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            CachedResult result = objectMapper.readValue(html, CachedResult.class);
            return result.getTranslations();
        } catch (IOException e) {
            Log.e(TAG, "Json couldn't be deserialized", e);
            return Collections.emptyList();
        }
    }

    public static String createJson(List<Translation> translations) {
        CachedResult cachedResult = new CachedResult();
        cachedResult.setTranslations(translations);
        ObjectMapper objectMapper = new ObjectMapper();
        StringWriter writer = new StringWriter();
        try {
            objectMapper.writeValue(writer, cachedResult);
        } catch (IOException e) {
            Log.e(TAG, "Json couldn't be serialized", e);
        }

        return writer.toString();
    }

}
