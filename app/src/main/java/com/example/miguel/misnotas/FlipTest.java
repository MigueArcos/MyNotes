package com.example.miguel.misnotas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 79812 on 03/05/2018.
 */

public class FlipTest extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.test, container, false);
        MyBackground myBackground = new MyBackground(getContext());
        container.addView(myBackground);
        return rootView;
    }

    class Point3D {
        private float x;
        private float y;
        private float z;

        public Point3D(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }


        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public float getZ() {
            return z;
        }
    }

    class MyBackground extends View {
        private final List<Point3D> originalPolygon = new ArrayList<>();
        private List<Point3D> polygon;
        private Point3D pivot;

        private Handler handler = new Handler();
        private Runnable handlerCallback;
        private boolean isRed = true;
        Paint paint = new Paint();

        private void initializeStuff() {

            originalPolygon.add(new Point3D(100, 300, 0));
            originalPolygon.add(new Point3D(300, 100, 0));
            originalPolygon.add(new Point3D(700, 300, 0));
            originalPolygon.add(new Point3D(300, 500, 0));

            /*
            originalPolygon.add(new Point3D(100, 300, 0));
            originalPolygon.add(new Point3D(200, 150, 0));
            originalPolygon.add(new Point3D(300, 220, 0));
            originalPolygon.add(new Point3D(500, 130, 0));
            originalPolygon.add(new Point3D(420, 550, 0));
            originalPolygon.add(new Point3D(270, 260, 0));
            */
            polygon = originalPolygon;

            pivot = calculatePolygonCenter(polygon);

            paint.setARGB(255, 255, 0, 0);

            handlerCallback = new Runnable() {
                private double angle = 0;
                private int degreesDelta = 1;
                private int degrees;
                private double delta = Math.PI / (180 / degreesDelta);

                @Override
                public void run() {
                    angle += delta;
                    //This method redraws this view
                    polygon = rotatePolygonXZ(originalPolygon, pivot, angle);
                    //Only changes color in 90 degrees and 270, 450, 630, ... etc
                    if ((degrees + 90) % 180 == 0) {
                        changeColor();
                    }
                    postInvalidate();
                    degrees += degreesDelta;
                    handler.postDelayed(this, 5);
                }

            };

        }

        private Point3D calculatePolygonCenter(List<Point3D> polygon) {
            float maximumX = 0, maximumY = 0, maximumZ = 0;
            float minimumX = 0, minimumY = 0, minimumZ = 0;
            for (Point3D point : polygon) {
                maximumX = point.getX() > maximumX ? point.getX() : maximumX;
                maximumY = point.getY() > maximumY ? point.getY() : maximumY;
                maximumZ = point.getZ() > maximumZ ? point.getZ() : maximumZ;

                minimumX = point.getX() < minimumX ? point.getX() : minimumX;
                minimumY = point.getY() < minimumY ? point.getY() : minimumY;
                minimumZ = point.getZ() < minimumZ ? point.getZ() : minimumZ;
            }
            return new Point3D((minimumX + maximumX) / 2, (minimumY + maximumY) / 2, (minimumZ + maximumZ) / 2);
        }

        public MyBackground(Context context) {
            super(context);

            initializeStuff();

            handler.postDelayed(handlerCallback, 50);
        }

        private void changeColor() {
            isRed = !isRed;
            paint.setARGB(255, isRed ? 255 : 0, 0, isRed ? 0 : 255);
        }


        protected void onDraw(final Canvas canvas) {
            canvas.drawRGB(70, 70, 70);
            drawPolygon(polygon, canvas, paint);
        }

        //Only works in 2D
        private void drawPolygon(List<Point3D> points, Canvas canvas, Paint paint) {
            Path path = new Path();
            path.reset(); // only needed when reusing this path for a new build
            path.moveTo(points.get(0).getX(), points.get(0).getY()); // used for first point
            int vertexes = points.size();
            for (int i = 0; i < vertexes; i++) {
                path.lineTo(points.get((i + 1) % vertexes).getX(), points.get((i + 1) % vertexes).getY());
            }
            canvas.drawPath(path, paint);
        }

        private List<Point3D> rotatePolygonXY(List<Point3D> points, Point3D pivot, double angle) {
            final List<Point3D> rotatedPolygon = new ArrayList<>();
            if (pivot == null) {
                pivot = new Point3D(0, 0, 0);
            }
            for (Point3D point : points) {
                Point3D rotatedPoint = new Point3D(
                        (float) ((point.getX() - pivot.getX()) * Math.cos(angle) - (point.getY() - pivot.getY()) * Math.sin(angle) + pivot.getX()),
                        (float) ((point.getY() - pivot.getY()) * Math.cos(angle) + (point.getX() - pivot.getX()) * Math.sin(angle) + pivot.getY()),
                        point.getZ()
                );
                rotatedPolygon.add(rotatedPoint);
            }
            return rotatedPolygon;
        }

        private List<Point3D> rotatePolygonXZ(List<Point3D> points, Point3D pivot, double angle) {
            final List<Point3D> rotatedPolygon = new ArrayList<>();
            if (pivot == null) {
                pivot = new Point3D(0, 0, 0);
            }
            for (Point3D point : points) {
                Point3D rotatedPoint = new Point3D(
                        (float) ((point.getX() - pivot.getX()) * Math.cos(angle) - (point.getZ() - pivot.getZ()) * Math.sin(angle) + pivot.getX()),
                        point.getY(),
                        (float) ((point.getZ() - pivot.getZ()) * Math.cos(angle) + (point.getX() - pivot.getX()) * Math.sin(angle) + pivot.getZ())
                );
                rotatedPolygon.add(rotatedPoint);
            }
            return rotatedPolygon;
        }
    }

}
