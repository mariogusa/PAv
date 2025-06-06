package app;
import com.DisplayLocales;
import com.Info;
import com.SetLocale;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class LocaleExplore {
    public static void main(String[] args) {
        Locale currentLocale = Locale.getDefault();
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            ResourceBundle messages = ResourceBundle.getBundle("res.Messages", currentLocale);
            System.out.print(messages.getString("prompt"));
            String command = scanner.nextLine().trim();
            
            if (command.equals("locales")) {
                DisplayLocales.execute(currentLocale, messages);
            } else if (command.startsWith("set locale ")) {
                String localeTag = command.substring(11);
                currentLocale = Locale.forLanguageTag(localeTag);
                SetLocale.execute(currentLocale, messages);
            } else if (command.equals("info")) {
                Info.execute(currentLocale, currentLocale, messages);
            } else if (command.startsWith("info ")) {
                String localeTag = command.substring(5);
                Locale targetLocale = Locale.forLanguageTag(localeTag);
                Info.execute(currentLocale, targetLocale, messages);
            } else if (command.equals("exit")) {
                break;
            } else {
                System.out.println(messages.getString("invalid"));
            }
        }
        scanner.close();
    }
}