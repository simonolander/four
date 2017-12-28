package se.olander.android.four;

import android.support.annotation.NonNull;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public abstract class FormatUtils {

    @NonNull
    public static String formatNumber(int number) {
        return String.format(Locale.getDefault(), "%d", number);
    }

    @NonNull
    public static String formatHHmmssms(long millis) {
        return String.format(
            Locale.getDefault(),
            "%02d:%02d:%02d.%03d",
            TimeUnit.MILLISECONDS.toHours(millis),
            TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
            TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1),
            TimeUnit.MILLISECONDS.toMillis(millis) % TimeUnit.SECONDS.toMillis(1)
        );
    }
}
