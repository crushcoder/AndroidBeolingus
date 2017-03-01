package de.develcab.beolingus.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by jb on 17.02.17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Translation {
    private String known;
    private String foreign;
    @JsonIgnore
    private String knownPronounceUrl;
    @JsonIgnore
    private String foreignPronounceUrl;
    @JsonIgnore
    private String knownDescription;
    @JsonIgnore
    private String foreignDescription;

    public String getKnown() {
        return known;
    }

    public void setKnown(String known) {
        this.known = known;
    }

    public String getForeign() {
        return foreign;
    }

    public void setForeign(String foreign) {
        this.foreign = foreign;
    }

    public String getKnownPronounceUrl() {
        return knownPronounceUrl;
    }

    public void setKnownPronounceUrl(String knownPronounceUrl) {
        this.knownPronounceUrl = knownPronounceUrl;
    }

    public String getForeignPronounceUrl() {
        return foreignPronounceUrl;
    }

    public void setForeignPronounceUrl(String foreignPronounceUrl) {
        this.foreignPronounceUrl = foreignPronounceUrl;
    }

    public String getKnownDescription() {
        return knownDescription;
    }

    public void setKnownDescription(String knownDescription) {
        this.knownDescription = knownDescription;
    }

    public String getForeignDescription() {
        return foreignDescription;
    }

    public void setForeignDescription(String foreignDescription) {
        this.foreignDescription = foreignDescription;
    }
}
