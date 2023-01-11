package app.krista.extensions.crm.marketing_management.salesforce_marketing.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DatePlus {

    /**
     * This function will return time stamp in required date format
     *
     * @param date timestamp value
     * @return formatted date string
     */
    public static String getYYYYMMDD(long date) {
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(date));
    }

    /**
     * This function will return {@link Date} object for given date string
     *
     * @param date Date String value
     * @return {@link Date} object
     */
    public static Date fromYYYYMMDD(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
}
