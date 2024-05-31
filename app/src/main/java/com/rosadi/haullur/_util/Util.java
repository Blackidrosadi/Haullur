package com.rosadi.haullur._util;

import java.text.NumberFormat;
import java.util.Locale;

public class Util {
    public static String rupiahFormat(String jumlah) {
        Locale local = new Locale("id", "id");
        String replaceable = String.format("[Rp,.\\s]",
                NumberFormat.getCurrencyInstance()
                        .getCurrency()
                        .getSymbol(local));
        String cleanString = jumlah.replaceAll(replaceable, "");

        double parsed;
        try {
            parsed = Double.parseDouble(cleanString);
        } catch (NumberFormatException e) {
            parsed = 0.00;
        }

        NumberFormat formatter = NumberFormat
                .getCurrencyInstance(local);
        formatter.setMaximumFractionDigits(0);
        formatter.setParseIntegerOnly(true);
        String formatted = formatter.format((parsed));

        String replace = String.format("[Rp\\s]",
                NumberFormat.getCurrencyInstance().getCurrency()
                        .getSymbol(local));
        String clean = formatted.replaceAll(replace, "");

        return clean;
    }
}
