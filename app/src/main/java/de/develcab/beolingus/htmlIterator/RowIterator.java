package de.develcab.beolingus.htmlIterator;

/**
 * Created by jb on 19.02.17.
 */

public class RowIterator extends AbstractHtmlIterator {
    public static final String ROW_START = "<tr class=";
    public static final String ROW_END = "</tr>";

    public RowIterator(String html, int pointer) {
        super(html, pointer, ROW_START, ROW_END);
    }
}
