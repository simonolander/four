package se.olander.android.four;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class Level implements Comparable<Level>, Serializable {
    public int version;
    public int ordinal;
    public String name;
    public List<Painting.PaintRegion> regions;
    public List<List<Integer>> neighboursList;

    @Override
    public int compareTo(@NonNull Level other) {
        return Integer.compare(this.ordinal, other.ordinal);
    }

    @Override
    public String toString() {
        return "Level{" +
            "version=" + version +
            ", ordinal=" + ordinal +
            ", name='" + name + '\'' +
            ", regions=" + regions +
            ", neighboursList=" + neighboursList +
            '}' ;
    }
}
