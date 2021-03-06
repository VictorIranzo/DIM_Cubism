package dim.vici.vectors;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Hashtable;
import java.util.Map;

public class MyView extends View {

    // Stores graphic properties of the drawn line.
    Paint paint = new Paint();
    Paint textPaint = new Paint();

    Hashtable<Integer, Point> contactPoints = new Hashtable<Integer, Point>();

    Triangle triangle;

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Establish the graphic properties of the draw.
        paint.setAntiAlias(true);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);

        textPaint.setTextSize(30);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawText("Number of contacts: " + contactPoints.size(), 50, 50, textPaint);

        if (contactPoints.size() > 3)
        {
            canvas.drawText("Not a triangle!!!", 100, 100, textPaint);
        }

        if (contactPoints.size() == 3)
        {
            Triangle triangle = new Triangle(contactPoints.values());

            canvas.drawText("Triangle: " + triangle.getTriangleType(), 100, 100, textPaint);

            Line line = triangle.GetOrientedLine();

            canvas.drawLine(line.p1.x, line.p1.y, line.p2.x, line.p2.y, this.paint);
        }

        for (Point point : contactPoints.values())
        {
            canvas.drawPoint(point.x, point.y, this.paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointerIndex = event.getPointerId(event.getActionIndex());

        switch(event.getActionMasked())
        {
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_DOWN:
                contactPoints.put(pointerIndex, new Point(event.getX(pointerIndex), event.getY(pointerIndex)));

                break;
            case MotionEvent.ACTION_MOVE:
                for (Integer pointKey : contactPoints.keySet())
                {
                    try {
                        contactPoints.put(pointKey, new Point(event.getX(pointKey), event.getY(pointKey)));

                        this.invalidate();
                    }
                    catch (IllegalArgumentException args) {}
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                contactPoints.remove(pointerIndex);

                this.invalidate();

                break;
        }

        return true;
    }
}
