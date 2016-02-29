package com.ryhupeja.znajdztrupa;

/**
 * Created by Piotr on 29.02.2016.
 */
public class TrupItem {
    private String description;
    private String pesel;

    public TrupItem(String description, String pesel) {
        this.description = description;
        this.pesel = pesel;
    }

    public String toString() {
        return description;
    }

    public String getPesel() {
        return pesel;
    }
}
