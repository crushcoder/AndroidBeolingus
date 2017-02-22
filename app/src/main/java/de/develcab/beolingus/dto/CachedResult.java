package de.develcab.beolingus.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jb on 21.02.17.
 */

public class CachedResult {

    private List<Translation> translations = new ArrayList<>();

    public List<Translation> getTranslations() {
        return translations;
    }

    public void setTranslations(List<Translation> translations) {
        this.translations = translations;
    }
}
