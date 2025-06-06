package com;
import java.util.Locale;
import java.util.ResourceBundle;

public class DisplayLocales {
    public static void execute(Locale currentLocale, ResourceBundle messages) {
        System.out.println(messages.getString("locales"));
        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            System.out.println(locale + " - " + locale.getDisplayName(currentLocale));
        }
    }
}