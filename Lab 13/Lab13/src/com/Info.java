package com;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Currency;
import java.util.Locale;
import java.util.ResourceBundle;

public class Info {
    public static void execute(Locale currentLocale, Locale targetLocale, ResourceBundle messages) {
        System.out.println(messages.getString("info") + " " + targetLocale.toString());

        // Country info
        System.out.println("Country: " + targetLocale.getDisplayCountry(currentLocale) + 
            " (" + targetLocale.getDisplayCountry(targetLocale) + ")");
        
        // Language info
        System.out.println("Language: " + targetLocale.getDisplayLanguage(currentLocale) + 
            " (" + targetLocale.getDisplayLanguage(targetLocale) + ")");
        
        // Currency info
        try {
            Currency currency = Currency.getInstance(targetLocale);
            System.out.println("Currency: " + currency.getCurrencyCode() + 
                " (" + currency.getDisplayName(currentLocale) + ")");
        } catch (IllegalArgumentException e) {
            System.out.println("Currency: N/A");
        }
        
        // Week days
        DateFormatSymbols dfs = DateFormatSymbols.getInstance(targetLocale);
        String[] weekdays = dfs.getWeekdays();
        System.out.println("Week Days: " + 
            String.join(", ", weekdays[2], weekdays[3], weekdays[4], weekdays[5], weekdays[6], weekdays[7], weekdays[1]));
        
        // Months
        String[] months = dfs.getMonths();
        System.out.println("Months: " + 
            String.join(", ", months[0], months[1], months[2], months[3], months[4], months[5], 
                        months[6], months[7], months[8], months[9], months[10], months[11]));
        
        // Today's date
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(targetLocale);
        System.out.println("Today: " + formatter.format(today));
    }
}