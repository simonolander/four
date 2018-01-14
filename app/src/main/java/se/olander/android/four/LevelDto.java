package se.olander.android.four;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by sios on 2018-01-13.
 */

public abstract class LevelDto implements Comparable<LevelDto>, Serializable {

    public enum Type {
        RAW,
        SQUARE_DFS_MAZE
    }

    public int version;
    public int ordinal;
    public String name;
    public Type type;

    @Override
    public int compareTo(@NonNull LevelDto other) {
        return Integer.compare(this.getOrdinal(), other.getOrdinal());
    }

    public int getVersion() {
        return version;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public abstract Painting getPainting();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LevelDto levelDto = (LevelDto) o;

        if (version != levelDto.version) return false;
        if (ordinal != levelDto.ordinal) return false;
        if (name != null ? !name.equals(levelDto.name) : levelDto.name != null) return false;
        return type == levelDto.type;
    }

    @Override
    public int hashCode() {
        int result = version;
        result = 31 * result + ordinal;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
