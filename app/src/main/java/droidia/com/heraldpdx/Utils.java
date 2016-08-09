package droidia.com.heraldpdx;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import droidia.com.heraldpdx.savedlocations.LocationListRecyclerViewAdapter;
import droidia.com.heraldpdx.trimetapis.arrivals.Arrival;
import timber.log.Timber;

/**
 * Created by froger_mcs on 05.11.14.
 */
public class Utils {
    private static int screenWidth = 0;
    private static int screenHeight = 0;

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int getScreenHeight(Context c) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }

        return screenHeight;
    }

    public static int getScreenWidth(Context c) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        return screenWidth;
    }

    public static boolean isAndroid5() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    @NonNull
    public static String getArrivalDescription(Arrival arrival) {
        int index = arrival.fullSign.toLowerCase().indexOf(" ");
        String fullsign = arrival.fullSign.substring(index).trim();

        // space in "to " is necessary to not accidentally catch words like downtown
        int toIndex = fullsign.toLowerCase().indexOf("to ");

        if (toIndex >= 0) {
            fullsign = fullsign.substring(toIndex + "to ".length()).trim();
        }
        fullsign = "to " + fullsign;
        return fullsign;
    }

    public static String getEstimatedTimeString(Arrival arrival) {
        // a is AM/PM marker.
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.US);
        Date scheduledDate = new Date(Long.parseLong(String.valueOf(arrival.scheduled)));
        sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        String scheduledTime = sdf.format(scheduledDate);

        if (arrival.status.equalsIgnoreCase(Arrival.STATUS_SCHEDULED)) {
            return scheduledTime;

        } else if (arrival.status.equalsIgnoreCase(Arrival.STATUS_ESTIMATED)) {

            int arrivalIn = (int) ((arrival.estimated - System.currentTimeMillis()) / 1000 / 60);
            if (arrivalIn > 0)
                return String.format("%d min", arrivalIn);
            else
                return "Due";

        } else {
            Timber.e("Not implemented handling of %s", arrival.status);
            return arrival.status;
        }
    }
}
