package io.github.mortuusars.mortaar.util;

import java.time.LocalDate;
import java.time.Month;

public abstract class Time {
    public static boolean isHalloween() {
        LocalDate localDate = LocalDate.now();
        int day = localDate.getDayOfMonth();
        Month month = localDate.getMonth();
        return month == Month.OCTOBER && day >= 20 || month == Month.NOVEMBER && day <= 3;
    }

    public static boolean isChristmas() {
        LocalDate localDate = LocalDate.now();
        int day = localDate.getDayOfMonth();
        Month month = localDate.getMonth();
        return month == Month.DECEMBER && day >= 24 && day <= 26;
    }
}
