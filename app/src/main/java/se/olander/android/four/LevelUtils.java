package se.olander.android.four;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by sios on 2017-12-23.
 */

class LevelUtils {
    @NonNull
    public static List<LevelDto> getLevels(@NonNull Context context) {
        ArrayList<LevelDto> levels = new ArrayList<>();
        try {
            AssetManager assets = context.getAssets();
            String[] fileNames = assets.list("levels");
            for (String fileName : fileNames) {
                try {
                    InputStream in = assets.open("levels/" + fileName);
                    InputStreamReader reader = new InputStreamReader(in);
                    Gson gson = new Gson();
                    LevelDto level = gson.fromJson(reader, LevelDto.class);
                    levels.add(level);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(levels);
        return levels;
    }
}
