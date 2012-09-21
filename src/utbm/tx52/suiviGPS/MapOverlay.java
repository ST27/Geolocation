package utbm.tx52.suiviGPS;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

/**
 * Class pour les points sur la google map
 */
public class MapOverlay extends Overlay {

    ArrayList<GeoPoint> route;

    public MapOverlay(ArrayList<GeoPoint> r) {
            route = new ArrayList<GeoPoint>();
            for (GeoPoint p: r) {
                    route.add(p);
            }
    }

    public void draw(Canvas canvas, MapView mapv, boolean shadow) {
            super.draw(canvas, mapv, shadow);

            Paint mPaint = new Paint();
            mPaint.setDither(true);
            mPaint.setColor(Color.rgb(128, 136, 231));
            mPaint.setAlpha(100);
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(6);

            Path path = new Path();

            GeoPoint start = route.get(0);
            for (int i = 1; i < route.size(); ++i) {
                    Point p1 = new Point();
                    Point p2 = new Point();

                    Projection projection = mapv.getProjection();
                    projection.toPixels(start, p1);
                    projection.toPixels(route.get(i), p2);

                    path.moveTo(p2.x, p2.y);
                    path.lineTo(p1.x, p1.y);

                    start = route.get(i);
            }
            canvas.drawPath(path, mPaint);
    }
}
