package com;
import java.util.Locale;
import java.util.ResourceBundle;

public class SetLocale {
    public static void execute(Locale newLocale, ResourceBundle messages) {
        String message = messages.getString("locale.set");
        System.out.println(java.text.MessageFormat.format(message, newLocale.getDisplayName(newLocale)));
    }
}