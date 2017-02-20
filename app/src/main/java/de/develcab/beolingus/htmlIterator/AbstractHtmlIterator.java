package de.develcab.beolingus.htmlIterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jb on 18.02.17.
 */

public abstract class AbstractHtmlIterator implements Iterator<String> {
    private static final String TAG_END = ">";
    private String startTag;
    private String endTag;

    protected String html;
    protected int pointer;

    public AbstractHtmlIterator(String html, int pointer, String startTag, String endTag) {
        this.html = html;
        this.pointer = pointer;
        this.startTag = startTag;
        this.endTag = endTag;
    }

    @Override
    public boolean hasNext() {
        return html.indexOf(startTag, pointer) != -1;

    }

    @Override
    public String next() {
        if (!hasNext()) {
            return null;
        }
        int start = html.indexOf(startTag, pointer);
        start = html.indexOf(TAG_END, start) + 1;
        int end = html.indexOf(endTag, start);
        String nextRow = html.substring(start, end + 20);
        this.pointer = end;
        return nextRow;
    }
}
