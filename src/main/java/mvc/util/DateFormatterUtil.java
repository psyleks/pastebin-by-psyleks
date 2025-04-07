package mvc.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateFormatterUtil {

    public static String formatMessageDate(OffsetDateTime createdAt, ZoneId zone) {
        ZonedDateTime createdInZone = createdAt.atZoneSameInstant(zone);
        ZonedDateTime now = ZonedDateTime.now(zone);

        long secondsAgo = ChronoUnit.SECONDS.between(createdInZone, now);
        if (secondsAgo < 60) {
            return getTimeString(secondsAgo, "секунду", "секунды", "секунд");
        }

        long minutesAgo = ChronoUnit.MINUTES.between(createdInZone, now);
        if (minutesAgo < 60) {
            return getTimeString(minutesAgo, "минуту", "минуты", "минут");
        }

        long hoursAgo = ChronoUnit.HOURS.between(createdInZone, now);
        if (hoursAgo < 24) {
            return getTimeString(hoursAgo, "час", "часа", "часов");
        }

        long dayAgo = ChronoUnit.DAYS.between(createdInZone.toLocalDate(), now.toLocalDate());
        if (dayAgo == 1) {
            return "вчера в " + createdInZone.format(DateTimeFormatter.ofPattern("HH:mm"));
        }

        return createdInZone.format(DateTimeFormatter.ofPattern("d MMMM")) +
                " в " + createdInZone.format(DateTimeFormatter.ofPattern("HH:mm"));
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





