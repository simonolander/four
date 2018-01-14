package se.olander.android.four;

class SquareMazeLevelDto extends LevelDto {

    private int width;
    private int height;
    private int seed;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getSeed() {
        return seed;
    }

    @Override
    public Painting getPainting() {
        return Graph.bfsMazeGraph(width, height, seed)
            .build()
            .computePainting();
    }
}
