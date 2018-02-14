package se.olander.android.four;

import java.util.Random;

class SunflowerMazeLevelDto extends LevelDto {

    private int numberOfPoints;
    private float alpha;
    private int seed;

    public int getNumberOfPoints() {
        return numberOfPoints;
    }

    public float getAlpha() {
        return alpha;
    }

    public int getSeed() {
        return seed;
    }

    @Override
    public Painting getPainting() {
        return Graph.sunflower(numberOfPoints, alpha, new Random(seed))
            .build()
            .computePainting();
    }
}
