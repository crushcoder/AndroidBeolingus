package de.develcab.beolingus.service;

import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import de.develcab.beolingus.util.HtmlMapper;
import de.develcab.beolingus.util.JsonMapper;
import de.develcab.beolingus.util.RestTemplate;
import de.develcab.beolingus.util.TranslationStorage;
import de.develcab.beolingus.dto.Translation;

/**
 * Class to load result from Beolingus Server to be shown in App.
 *
 * Created by jb on 17.02.17.
 */

public class BeolingusRestService {
    static final String TAG = BeolingusRestService.class.getName();
    static final String URL_TEMPLATE = "http://dict.tu-chemnitz.de/dings.cgi?lang=de&mini=1&count=50&query=%s&service=%s";

    private TranslationStorage storage;
    private RestTemplate restTemplate;

    public BeolingusRestService(TranslationStorage storage, RestTemplate restTemplate) {
        this.storage = storage;
        this.restTemplate = restTemplate;
    }

    public List<Translation> loadTranslation(String searchTerm, String service) {
        Log.d(TAG, "start translation");
        String urlStr = String.format(URL_TEMPLATE, searchTerm, service);

        String html;
        List<Translation> translations;
        if(storage.isCached(searchTerm, service)) {
            html = storage.loadHtml(searchTerm, service);
            Log.d(TAG, "translation loaded from cache: " + searchTerm + ": " + html);

            return JsonMapper.mapJson(html);
        } else {
            html = restTemplate.load(urlStr, StandardCharsets.ISO_8859_1);
            if(html == null) {
                Log.e(TAG, "Url was wrong: " + urlStr);

                return Collections.emptyList();
            } else {
                Log.d(TAG, "translation loaded from web success: " + html);
                translations = HtmlMapper.mapHtml(html);
                String json = JsonMapper.createJson(translations);
                storage.storeSearch(searchTerm, service, json);
                Log.d(TAG, "translation saved");

                return translations;
            }
        }
    }







}
