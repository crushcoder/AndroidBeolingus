package de.develcab.beolingus;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public BeolingusRestService() {
    }

    public List<Translation> loadTranslation(String searchTerm) {
        Log.d(TAG, "start translation");
        String urlStr = String.format(URL_TEMPLATE, searchTerm, SERVICE_DEEN);
        try {
            URL url = new URL(urlStr);
            String html = loadHtml(url);
            Log.d(TAG, "translation loaded success: " + html);

            return mapHtml(html);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Url was wrong: " + urlStr);
            return Collections.emptyList();
        }
    }

    private String loadHtml(URL url) {
        try(BufferedReader beoStream = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.ISO_8859_1))) {
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

    private List<Translation> mapHtml(String html) {
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

    String rowStr = "<tr class=\"s1 c\">\n" +
            "<td align=\"right\"><br /></td>\n" +
            "<td class=\"f\"> <a href=\"/deutsch-englisch/Datenabruf.html;m\">Datenabruf</a> <span title=\"Substantiv, männlich (der)\">{m}</span>; <a href=\"/deutsch-englisch/Datenabfrage.html;m\">Daten<b>abfrage</b></a> <span title=\"Substantiv, weiblich (die)\">{f}</span> </td>\n" +
            "<td class=\"f\"> <a href=\"/english-german/data.html;m\">data</a> <a href=\"/english-german/retrieval.html;m\">retrieval</a> </td>\n" +
            "</tr>\n";

    private Translation mapRow(String htmlRow) {
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
