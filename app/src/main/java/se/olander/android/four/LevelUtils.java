package se.olander.android.four;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by sios on 2017-12-23.
 */

class LevelUtils {
    private static LevelDto parserLevelDto(Reader reader) throws IOException {
        Gson gson = new Gson();
        String json = IOUtils.toString(reader);
        LevelDtoMeta levelDtoMeta = gson.fromJson(json, LevelDtoMeta.class);
        switch (levelDtoMeta.type) {
            case RAW:
                return gson.fromJson(json, RawLevelDto.class);
            case SQUARE_DFS_MAZE:
                return gson.fromJson(json, SquareMazeLevelDto.class);
            default:
                throw new JsonParseException("Unknown level type: " + levelDtoMeta.type);
        }
    }

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
                    LevelDto level = parserLevelDto(reader);
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

    @Nullable
    public static LevelDto getNextLevel(@NonNull Context context, LevelDto level) {
        boolean next = false;
        for (LevelDto lvl : getLevels(context)) {
            if (next) {
                return lvl;
            }
            if (lvl.equals(level)) {
                next = true;
            }
        }
        return null;
    }

    private static class LevelDtoMeta {
        LevelDto.Type type;
    }
}
