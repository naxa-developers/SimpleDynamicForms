package np.com.naxa.simpledynamicforms.uitils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created on 9/1/17
 * by nishon.tan@gmail.com
 */

public class CalendarUtils {

    public static Calendar toCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }
}
