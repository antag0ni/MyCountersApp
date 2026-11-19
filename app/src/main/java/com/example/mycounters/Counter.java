package com.example.mycounters;

import android.content.SharedPreferences;
public class Counter {
    private final SharedPreferences prefs;
    private final String key;
    private int value;
    private final String name;


    public Counter(SharedPreferences prefs, String name) {
        this.prefs = prefs;
        this.name = name;
        this.key = name;
        this.value = prefs.getInt(key, 0);
    }

    public SharedPreferences getPrefs() {
        return prefs;
    }

    private String getKey() {
        return key;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public void increment(int delta) {
        if (delta < 0) throw new IllegalArgumentException("delta must be >= 0");
        setValue(value + delta);
    }

    public void decrement(int delta) {
        if (delta < 0) throw new IllegalArgumentException("delta must be >= 0");
        setValue(Math.max(0, value - delta));
    }

    public void reset() {
        setValue(0);
    }

    public void removeFromPrefs() {
        prefs.edit().remove(this.key).apply();
    }

    private void setValue(int newValue) {
        this.value = newValue;
        prefs.edit().putInt(this.key, this.value).apply();
    }
}
