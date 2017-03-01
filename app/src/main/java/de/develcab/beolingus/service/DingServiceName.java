package de.develcab.beolingus.service;

import java.util.EnumMap;

/**
 * Created by jb on 24.02.17.
 */

public enum DingServiceName {
    DEEN("deen" , "De↔En Wörterbuch", true),
    DEES("dees", "De↔Es Wörterbuch"),
    DEPT("dept", "De↔Pt Wörterbuch"),
    DE_EN_EX("de-en-ex", "De↔En Beispielsätze"),
    DICT_EN("dict-en", "Erklärungen En"),
    DICT_DE("dict-de", "Synonyme De"),
    FORTUNE_EN("fortune-en", "Sprüche En"),
    FORTUNE_DE("fortune-de", "Sprüche De"),
    DE_ES_EX("de-es-ex", "De↔Es Beispielsätze"),
    FORTUNE_ES("fortune-es", "Sprüche Es"),
    DE_PT_EX("de-pt-ex", "De↔Pt Beispielsätze"),
    ;

    private String name;
    private String value;
    private boolean isDefault;

    DingServiceName(String name, String value) {
        this.name = name;
        this.value = value;
        this.isDefault = false;
    }

    DingServiceName(String name, String value, boolean isDefault) {
        this.name = name;
        this.value = value;
        this.isDefault = isDefault;
    }

    public static DingServiceName findByValue(String value) {
        for(DingServiceName currName : DingServiceName.values()) {
            if(currName.value.equals(value)) {
                return currName;
            }
        }
        return null;
    }

    private static final EnumMap<DingServiceName, Integer> NAME_TO_POSITION = new EnumMap(DingServiceName.class);

    private static void fillEnumMap() {
        for (int i = 0; i < DingServiceName.values().length; i++) {
            NAME_TO_POSITION.put(DingServiceName.values()[i], Integer.valueOf(i));
        }
    }

    public static int findPosition(DingServiceName ding) {
        fillMapIfNeeded();
        return NAME_TO_POSITION.get(ding);

    }

    private static void fillMapIfNeeded() {
        if(NAME_TO_POSITION.size() == 0) {
            fillEnumMap();
        }
    }

    public static DingServiceName findByName(String name) {
        fillMapIfNeeded();
        for(DingServiceName currName : DingServiceName.values()) {
            if(currName.getName().equals(name)) {
                return currName;
            }
        }
        return null;
    }

    public static int findPosition(String name) {

        return findPosition(findByName(name));
    }

    @Override
    public String toString() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public boolean isDefault() {
        return isDefault;
    }
}
