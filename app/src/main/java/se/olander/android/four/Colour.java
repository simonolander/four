package se.olander.android.four;

public enum Colour {
    COLOUR_1,
    COLOUR_2,
    COLOUR_3,
    COLOUR_4;

    public static int chooseColour(Colour colour, int color1, int color2, int color3, int color4, int defaultColor) {
        if (colour == null) {
            return defaultColor;
        }

        switch (colour) {
            case COLOUR_1:
                return color1;
            case COLOUR_2:
                return color2;
            case COLOUR_3:
                return color3;
            case COLOUR_4:
                return color4;
            default:
                return defaultColor;
        }
    }
}
