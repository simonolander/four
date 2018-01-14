package se.olander.android.four;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
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

    public static int getColor(Colour colour) {
        if (colour == null) {
            return Color.WHITE;
        }

        switch (colour) {
            case COLOUR_1:
                return getColor1();
            case COLOUR_2:
                return getColor2();
            case COLOUR_3:
                return getColor3();
            case COLOUR_4:
                return getColor4();
            default:
                return Color.WHITE;
        }
    }

    public static int getColor1() {
        return 0xffff867c;
    }

    public static int getColor2() {
        return 0xff99d066;
    }

    public static int getColor3() {
        return 0xfffff263;
    }

    public static int getColor4() {
        return 0xff5eb8ff;
    }

    private static class LevelDtoMeta {
        LevelDto.Type type;
    }
}
