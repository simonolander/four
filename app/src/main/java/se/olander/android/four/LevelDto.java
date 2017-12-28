package se.olander.android.four;

import android.graphics.PointF;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LevelDto implements Comparable<LevelDto>, Serializable {
    public int version;
    public int ordinal;
    public String name;
    public List<PaintRegionDto> regions;
    public List<List<Integer>> neighboursList;

    @Override
    public int compareTo(@NonNull LevelDto other) {
        return Integer.compare(this.ordinal, other.ordinal);
    }

    public Painting getPainting() {
        List<Painting.PaintRegion> regions = new ArrayList<>();
        for (PaintRegionDto regionDto : this.regions) {
            Painting.Polygon base = new Painting.Polygon(regionDto.base.points);

            Painting.Polygon[] holes = new Painting.Polygon[regionDto.holes != null ? regionDto.holes.length : 0];
            if (regionDto.holes != null) {
                for (int i = 0; i < regionDto.holes.length; i++) {
                    holes[i] = new Painting.Polygon(regionDto.holes[i].points);
                }
            }
            regions.add(new Painting.PaintRegion(base, holes));
        }
        return new Painting(regions, neighboursList);
    }

    @Override
    public String toString() {
        return "LevelDto{" +
            "version=" + version +
            ", ordinal=" + ordinal +
            ", name='" + name + '\'' +
            ", regions=" + regions +
            ", neighboursList=" + neighboursList +
            '}' ;
    }

    public class PaintRegionDto {
        PolygonDto base;
        PolygonDto[] holes;
    }

    public class PolygonDto {
        List<PointF> points;
    }
}
