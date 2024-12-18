package mvc.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateFormatterUtil {

    public static String formatMessageDate(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();

        long secondsAgo = ChronoUnit.SECONDS.between(createdAt, now);
        if (secondsAgo < 60) {
            return getTimeString(secondsAgo, "секунду", "секунды", "секунд");
        }

        long minutesAgo = ChronoUnit.MINUTES.between(createdAt, now);
        if (minutesAgo < 60) {
            return getTimeString(minutesAgo, "минуту", "минуты", "минут");
        }

        long hoursAgo = ChronoUnit.HOURS.between(createdAt, now);
        if (hoursAgo < 24) {
            return getTimeString(hoursAgo, "час", "часа", "часов");
        }

        long dayAgo = ChronoUnit.DAYS.between(createdAt, now);
        if (dayAgo < 2) {
            return "вчера в " + createdAt.format(DateTimeFormatter.ofPattern("HH:mm"));
        }

        long daysAgo = ChronoUnit.DAYS.between(createdAt, now);
        return createdAt.format(DateTimeFormatter.ofPattern("d MMMM"));
    }

    private static String getTimeString(long amount, String one, String twoFour, String other) {
        if (amount == 1 || amount == 21 || amount ==31 || amount ==41 || amount ==51) {
            return amount + " " + one + " назад";
        } else if (amount >= 2 && amount <= 4 || amount >= 22 && amount <= 24 || amount >= 32 && amount <= 34 || amount >= 42 && amount <= 44 || amount >= 52 && amount <= 54) {
            return amount + " " + twoFour + " назад";
        } else {
            return amount + " " + other + " назад";
        }
    }

}





