package se.olander.android.four;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class PaintingView extends View {
    private static final String TAG = PaintingView.class.getSimpleName();
    private static final long COLOR_ANIMATION_TIME_MILLIS = 2000;
    private static final long COLOR_ANIMATION_WAIT_MILLIS = 20;

    private final Paint
        fillPaint = new Paint(),
        border = new Paint();

    private int color1 = Color.RED;
    private int color2 = Color.GREEN;
    private int color3 = Color.BLUE;
    private int color4 = Color.YELLOW;

    private final ScaleGestureDetector scaleGestureDetector;
    private final GestureDetector gestureDetector;

    private Painting painting;

    private final float[] matrixValues = new float[9];
    private float minScale;
    private float maxScale;
    private Matrix matrix;
    private Matrix inverseMatrix = new Matrix();

    private Painting.PaintRegion currentSelectedRegion;

    private OnSelectedRegionChangedListener onSelectedRegionChangedListener;

    public PaintingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        fillPaint.setAntiAlias(true);
        border.setColor(Color.BLACK);
        border.setStyle(Paint.Style.STROKE);
        border.setStrokeWidth(5);
        border.setAntiAlias(true);

        matrix = new Matrix();
        scaleGestureDetector = new ScaleGestureDetector(getContext(), new PaintingOnScaleGestureListener());
        gestureDetector = new GestureDetector(getContext(), new PaintingOnGestureListener());
        gestureDetector.setOnDoubleTapListener(new PaintingOnDoubleTapListener());
        gestureDetector.setIsLongpressEnabled(false);
    }

    public Painting getPainting() {
        return painting;
    }

    public void setPainting(Painting painting) {
        this.painting = painting;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.setMatrix(matrix);
        drawPainting(canvas, painting);
    }

    private void drawPainting(Canvas canvas, Painting painting) {
        for (Painting.PaintRegion region : painting.regions) {
            if (region == currentSelectedRegion) {
                fillRegion(canvas, region, getCurrentSelectedRegionColor());
            }
            else if (painting.colors.get(region) != null) {
                fillRegion(canvas, region, getColor(painting.colors.get(region)));
            }
        }
        for (Painting.PaintRegion region : painting.regions) {
            strokeRegion(canvas, region);
        }
    }

    private void fillRegion(Canvas canvas, Painting.PaintRegion region, int color) {
        canvas.save();
        canvas.clipPath(region.base.path);
        for (Painting.Polygon hole : region.holes) {
            canvas.clipOutPath(hole.path);
        }
        fillPaint.setColor(color);
        canvas.drawPath(region.base.path, fillPaint);
        canvas.restore();
    }

    private void strokeRegion(Canvas canvas, Painting.PaintRegion region) {
        canvas.drawPath(region.base.path, border);
    }

    private int getCurrentSelectedRegionColor() {
        long currentCycleTime = System.currentTimeMillis() % COLOR_ANIMATION_TIME_MILLIS;
        double t = (double) currentCycleTime / COLOR_ANIMATION_TIME_MILLIS;
        int fromColor = getColor(painting.colors.get(currentSelectedRegion), Color.WHITE);
        int toColor = Color.LTGRAY;
        return MathUtils.interpolateRGB(fromColor, toColor, MathUtils.linearToFullSin(t));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        fitPaintingToScreen();
    }

    private void fitPaintingToScreen() {
        float screenWidth = getWidth();
        float screenHeight = getHeight();
        float screenRatio = screenHeight / screenWidth;
        float screenCenterX = getX() + screenWidth / 2;
        float screenCenterY = getY() + screenHeight / 2;
        float paintingMinX = painting.getMinX();
        float paintingMinY = painting.getMinY();
        float paintingWidth = painting.getWidth();
        float paintingHeight = painting.getHeight();
        float paintingRatio = paintingHeight / paintingWidth;
        float paintingCenterX = paintingMinX + paintingWidth / 2;
        float paintingCenterY = paintingMinY + paintingHeight / 2;


        float tx = screenCenterX - paintingCenterX;
        float ty = screenCenterY - paintingCenterY;
        float scale = screenRatio > paintingRatio
            ? screenWidth / paintingWidth
            : screenHeight / paintingHeight;

        matrix.setTranslate(tx, ty);
        matrix.setScale(scale, scale, paintingCenterX, paintingCenterY);
        minScale = scale;
        maxScale = scale * 4;
    }

    private void translate(float dx, float dy) {
        matrix.postTranslate(dx, dy);
        postInvalidate();
    }

    private void scale(float factor, float fx, float fy) {
        float currentScale = getScale();
        float newScale = currentScale * factor;
        if (newScale < minScale) {
            factor = minScale / currentScale;
        }
        else if (newScale > maxScale) {
            factor = maxScale / currentScale;
        }
        matrix.postScale(factor, factor, fx, fy);
        postInvalidate();
    }

    private float getScale() {
        matrix.getValues(matrixValues);
        return (matrixValues[Matrix.MSCALE_X] + matrixValues[Matrix.MSCALE_Y]) / 2;
    }

    private void onClick(float x, float y) {
        PointF point = toPaintingPoint(x, y);
        setCurrentSelectedRegion(painting.getRegion(point));
        postInvalidate();
    }

    public void setCurrentSelectedRegion(Painting.PaintRegion currentSelectedRegion) {
        this.currentSelectedRegion = currentSelectedRegion;

        removeCallbacks(animateRunnable);
        if (this.currentSelectedRegion != null) {
            post(animateRunnable);
        }
    }

    public void setSelectedRegionColor(Colour colour) {
        if (this.currentSelectedRegion == null) {
            return;
        }

        painting.colors.put(this.currentSelectedRegion, colour);
        postInvalidate();
    }

    private int getColor(Colour colour) {
        return getColor(colour, Color.BLACK);
    }

    private int getColor(Colour colour, int defaultColor) {
        return Colour.chooseColour(
            colour,
            color1,
            color2,
            color3,
            color4,
            defaultColor
        );
    }

    private PointF toPaintingPoint(float x, float y) {
        float point[] = new float[] {x, y};
        matrix.invert(inverseMatrix);
        inverseMatrix.mapPoints(point);
        return new PointF(point[0], point[1]);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean retVal = scaleGestureDetector.onTouchEvent(event);
        retVal = gestureDetector.onTouchEvent(event) || retVal;
        return retVal || super.onTouchEvent(event);
    }

    public void setColors(int color1, int color2, int color3, int color4) {
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.color4 = color4;
    }

    public void setOnSelectedRegionChangedListener(OnSelectedRegionChangedListener listener) {
        this.onSelectedRegionChangedListener = listener;
    }

    private class PaintingOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        private final String TAG = PaintingOnGestureListener.class.getSimpleName();

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            translate(-distanceX, -distanceY);
            return true;
        }
    }

    private class PaintingOnDoubleTapListener implements GestureDetector.OnDoubleTapListener {
        private final String TAG = PaintingOnDoubleTapListener.class.getSimpleName();

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            onClick(e.getX(), e.getY());
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }
    }

    private class PaintingOnScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private final String TAG = PaintingOnScaleGestureListener.class.getSimpleName();

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale(detector.getScaleFactor(), detector.getFocusX(), detector.getFocusY());
            return true;
        }
    }

    private final Runnable animateRunnable =  new Runnable() {

        @Override
        public void run() {
            postInvalidate();
            postDelayed(this, COLOR_ANIMATION_WAIT_MILLIS);
        }
    };

    public interface OnSelectedRegionChangedListener {
        void onSelectedRegionChanged();
    }
}
