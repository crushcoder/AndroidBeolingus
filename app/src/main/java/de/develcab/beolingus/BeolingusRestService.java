package de.develcab.beolingus;

import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.develcab.beolingus.dto.CachedResult;
import de.develcab.beolingus.dto.Translation;
import de.develcab.beolingus.htmlIterator.RowIterator;

/**
 * Class to load result from Beolingus Server to be shown in App.
 *
 * Created by jb on 17.02.17.
 */

public class BeolingusRestService {
    static final String TAG = BeolingusRestService.class.getName();
    static final String URL_TEMPLATE = "http://dict.tu-chemnitz.de/dings.cgi?lang=de&mini=1&count=50&query=%s&service=%s";
    static final String SERVICE_DEEN = "deen";

    private TranslationStorage storage;
    private RestTemplate restTemplate;

    public BeolingusRestService(TranslationStorage storage, RestTemplate restTemplate) {
        this.storage = storage;
        this.restTemplate = restTemplate;
    }

    public List<Translation> loadTranslation(String searchTerm) {
        Log.d(TAG, "start translation");
        String urlStr = String.format(URL_TEMPLATE, searchTerm, SERVICE_DEEN);

        String html;
        List<Translation> translations;
        if(storage.isCached(searchTerm)) {
            html = storage.loadHtml(searchTerm);
            Log.d(TAG, "translation loaded from cache: " + searchTerm + ": " + html);

            return mapJson(html);
        } else {
            html = restTemplate.load(urlStr, StandardCharsets.ISO_8859_1);
            if(html == null) {
                Log.e(TAG, "Url was wrong: " + urlStr);

                return Collections.emptyList();
            } else {
                Log.d(TAG, "translation loaded from web success: " + html);
                translations = mapHtml(html);
                String json = createJson(translations);
                storage.storeSearch(searchTerm, json);
                Log.d(TAG, "translation saved");

                return translations;
            }
        }
    }

    private List<Translation> mapJson(String html) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            CachedResult result = objectMapper.readValue(html, CachedResult.class);
            return result.getTranslations();
        } catch (IOException e) {
            Log.e(TAG, "Json couldn't be deserialized", e);
            return Collections.emptyList();
        }
    }

    private String createJson(List<Translation> translations) {
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

    private static List<Translation> mapHtml(String html) {
        List<Translation> translations = new ArrayList<>();
        int start = html.indexOf("<table id=\"result\" ");
        RowIterator iterator = new RowIterator(html, start);
        while (iterator.hasNext()) {
            String htmlRow = iterator.next();
            if(htmlRow.indexOf("<td class=\"tip\"") != 0) {
                Translation translation = mapRow(htmlRow);
                translations.add(translation);
            }
        }

        return translations;
    }

    private static Translation mapRow(String htmlRow) {
        Translation newTranslation = new Translation();

        int languageSplitIndex = htmlRow.indexOf("</td>");
        languageSplitIndex = htmlRow.indexOf("</td>", languageSplitIndex+5);
        String known = Jsoup.clean(htmlRow.substring(0, languageSplitIndex), Whitelist.none());
        String foreign = Jsoup.clean(htmlRow.substring(languageSplitIndex, htmlRow.length()-1), Whitelist.none());
        newTranslation.setKnown(known);
        newTranslation.setForeign(foreign);

        return newTranslation;
    }





}
