package se.olander.android.four;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class SaveGameUtils {
    private static final String SAVED_GAME_KEY_PREFIX = "SAVED_GAME__";
    private final static String TIME_PLAYED_KEY = "TIME_PLAYED_KEY";
    private final static String COLOR_ARRAY_KEY = "COLOR_ARRAY_KEY";

    private static String getSavedGameSharedPreferenceName(@NonNull LevelDto level) {
        return SAVED_GAME_KEY_PREFIX + level.id;
    }

    public static void saveGame(@NonNull Context context, @NonNull SavedGame game) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(getSavedGameSharedPreferenceName(game.level), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(TIME_PLAYED_KEY, game.timePlayed);
        for (int i = 0; i < game.colorArray.length; i++) {
            editor.putString(COLOR_ARRAY_KEY + i, game.colorArray[i].name());
        }
        editor.apply();
    }

    @Nullable
    public static SavedGame loadGame(@NonNull Context context, @NonNull LevelDto level) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(getSavedGameSharedPreferenceName(level), Context.MODE_PRIVATE);

            long timePlayed = sharedPreferences.getLong(TIME_PLAYED_KEY, -1);
            if (timePlayed == -1) {
                return null;
            }

            Colour[] colorArray = new Colour[level.getPainting().regions.size()];
            for (int i = 0; i < colorArray.length; i++) {
                String name = sharedPreferences.getString(COLOR_ARRAY_KEY + i, null);
                colorArray[i] = Colour.valueOf(name);
            }

            return new SavedGame(
                level,
                timePlayed,
                colorArray
            );
        }
        catch (Exception e) {
            return null;
        }
    }
}
