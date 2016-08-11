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
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        if (string.contains("T")) {
            String data[] = string.substring(0, string.indexOf("T")).split("-");
            Date date = new GregorianCalendar(Integer.parseInt(data[0]),Integer.parseInt(data[1])-1,Integer.parseInt(data[2])).getTime();
            return format.format(date);
        }
        return string;
    }
    public String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        return format.format(date);
    }
}
