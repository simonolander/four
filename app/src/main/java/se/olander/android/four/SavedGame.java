package se.olander.android.four;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class SavedGame implements Serializable {

    public final LevelDto level;
    public final long timePlayed;
    public final Colour[] colorArray;

    public SavedGame(@NonNull LevelDto level, long timePlayed, @NonNull Colour[] colorArray) {
        this.level = level;
        this.timePlayed = timePlayed;
        this.colorArray = colorArray;
    }
}
