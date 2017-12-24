package se.olander.android.four;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

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

    private Painting painting;

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
    }

    public Painting getPainting() {
        return painting;
    }

    public void setPainting(Painting painting) {
        this.painting = painting;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawPainting(canvas, painting);
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
            canvas.clipPath(region.base.getPath());
            for (Painting.Polygon hole : region.holes) {
                canvas.clipOutPath(hole.getPath());
            }
            canvas.drawPath(region.base.getPath(), colors[color]);
            canvas.restore();
        }
    }

    private void strokeRegion(Canvas canvas, Painting.PaintRegion region) {
        canvas.drawPath(region.base.getPath(), border);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        initPaths();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent: " + event);
        return true;
    }
}
