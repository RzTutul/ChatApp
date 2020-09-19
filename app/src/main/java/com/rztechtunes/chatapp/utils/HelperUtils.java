package com.rztechtunes.chatapp.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HelperUtils {

    public static String getDateWithTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd MMM hh:mm aa");
        Date date = new Date();
        return dateFormat.format(date);
    }  public static String getDateYearTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy hh:mm aa");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
