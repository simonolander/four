package se.olander.android.four;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.ArrayList;

public class PaintingView extends View {
    private static final String TAG = PaintingView.class.getSimpleName();

    private final Paint
        red = new Paint(),
        green = new Paint(),
        blue = new Paint(),
        yellow = new Paint(),
        border = new Paint();

    private final Paint[] colors = new Paint[] {
        red, green, blue, yellow
    };

    private final ScaleGestureDetector scaleGestureDetector;
    private final GestureDetector gestureDetector;

    private Painting painting;

    private final float[] matrixValues = new float[9];
    private float minScale;
    private float maxScale;
    private Matrix matrix;
    private Matrix inverseMatrix = new Matrix();

    private ArrayList<PointF> clicks = new ArrayList<>();

    public PaintingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        red.setColor(Color.RED);
        red.setAntiAlias(true);
        green.setColor(Color.GREEN);
        green.setAntiAlias(true);
        blue.setColor(Color.BLUE);
        blue.setAntiAlias(true);
        yellow.setColor(Color.YELLOW);
        yellow.setAntiAlias(true);
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

        for (PointF click : clicks) {
            canvas.drawCircle(click.x, click.y, 10, yellow);
        }
    }

    private void drawPainting(Canvas canvas, Painting painting) {
        for (Painting.PaintRegion region : painting.regions) {
            fillRegion(canvas, region, painting.colors.get(region));
        }
        for (Painting.PaintRegion region : painting.regions) {
            strokeRegion(canvas, region);
        }
    }

    private void fillRegion(Canvas canvas, Painting.PaintRegion region, Integer color) {
        if (color != null) {
            canvas.save();
            canvas.clipPath(region.base.path);
            for (Painting.Polygon hole : region.holes) {
                canvas.clipOutPath(hole.path);
            }
            canvas.drawPath(region.base.path, colors[color]);
            canvas.restore();
        }
    }

    private void strokeRegion(Canvas canvas, Painting.PaintRegion region) {
        canvas.drawPath(region.base.path, border);
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
//        clicks.add(point);
        Painting.PaintRegion region = painting.getRegion(point);
        if (region == null) {
            return;
        }
        Integer currentColor = painting.colors.get(region);
        if (currentColor == null) {
            painting.colors.put(region, 0);
        }
        else if (currentColor == 0) {
            painting.colors.put(region, 1);
        }
        else if (currentColor == 1) {
            painting.colors.put(region, 2);
        }
        else if (currentColor == 2) {
            painting.colors.put(region, 3);
        }
        else if (currentColor == 3) {
            painting.colors.put(region, null);
        }
        postInvalidate();
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
}
