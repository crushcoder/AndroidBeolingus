package de.develcab.beolingus.util;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.util.ArrayList;
import java.util.List;

import de.develcab.beolingus.dto.Translation;
import de.develcab.beolingus.htmlIterator.RowIterator;

/**
 * Created by jb on 21.02.17.
 */

public class HtmlMapper {

    private HtmlMapper() {
        super();
    }

    public static List<Translation> mapHtml(String html) {
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
