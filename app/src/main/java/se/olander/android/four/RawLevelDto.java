package se.olander.android.four;

import android.graphics.PointF;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RawLevelDto extends LevelDto {

    public List<PaintRegionDto> regions;
    public List<List<Integer>> neighboursList;

    @Override
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
        return "RawLevelDto{" +
            "version=" + version +
            ", ordinal=" + ordinal +
            ", name='" + name + '\'' +
            ", regions=" + regions +
            ", neighboursList=" + neighboursList +
            '}' ;
    }

    public class PaintRegionDto implements Serializable {
        PolygonDto base;
        PolygonDto[] holes;
    }

    public class PolygonDto implements Serializable {
        List<PointF> points;
    }
}
