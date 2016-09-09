package com.b2infosoft.giftcardup.app;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by rajesh on 8/5/2016.
 */
public class Format {
    private Format() {

    }

    public static Format getInstance() {
        return new Format();
    }

    public String getDate(String string) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        if (string.contains(" ")) {
            String data[] = string.substring(0, string.indexOf(" ")).split("-");
            Date date = new GregorianCalendar(Integer.parseInt(data[0]), Integer.parseInt(data[1]) - 1, Integer.parseInt(data[2])).getTime();
            return format.format(date);
        }
        return string;
    }

    public String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        return format.format(date);
    }

    public String getStringFloat(String string) {

        if (string == null || string.length() == 0) {
            return "0";
        }
        if(string.contains("%")){
            string = string.replaceAll("%","");
        }
        try {
            float f = Float.parseFloat(string);
            return String.format("%.2f", f);
        } catch (NumberFormatException e) {
            return "0";
        }
    }

    public String getStringFloat(float string) {
        return String.format("%.2f", string);
    }
}
