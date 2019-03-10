package com.example.martin.gmboard;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.ArrayList;
import java.util.List;


public class PinView extends SubsamplingScaleImageView {

        private final Paint paint = new Paint();
        private final PointF vPin = new PointF();
        private PointF sPin;
        private Bitmap pin;
        private Paint paintPin = new Paint();
        private List<Point> circlePoints;
        private List<Integer> typePoints;
        public PinView(Context context) {
            this(context, null);
        }

        public PinView(Context context, AttributeSet attr) {
            super(context, attr);
            circlePoints = new ArrayList<Point>();
            typePoints = new ArrayList<Integer>();
            initialise();
        }

        public void setPin(PointF sPin) {
            this.sPin = sPin;
            initialise();
            invalidate();
        }

        private void initialise() {
            float density = getResources().getDisplayMetrics().densityDpi;
            pin = BitmapFactory.decodeResource(this.getResources(), R.drawable.swordcoastmaplowres);
             float w = (density/420f) * pin.getWidth();
            float h = (density/420f) * pin.getHeight();
            pin = Bitmap.createScaledBitmap(pin, (int)w, (int)h, true);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            paintPin.setColor(Color.BLACK);
            paintPin.setAntiAlias(true);
            paintPin.setStrokeWidth(5);
            paintPin.setStyle(Paint.Style.STROKE);
            paintPin.setStrokeJoin(Paint.Join.ROUND);
            paintPin.setStrokeCap(Paint.Cap.ROUND);

            // Don't draw pin before image is ready so it doesn't move around during setup.
            if (!isReady()) {
                return;
            }

            for (int i=0;i<circlePoints.size();i++) {

                paintPin.setColor(typePoints.get(i));

                Point p = circlePoints.get(i);
                canvas.drawCircle(p.x, p.y, 5, paintPin);
            }

        }

    public List<Point> getCirclePoints() {
        return circlePoints;
    }
    public void setCirclePoints(List<Point> c){
        circlePoints=c;
    }

    public boolean touch(MotionEvent event, int type) {
            float touchX = event.getX();
            float touchY = event.getY();
            circlePoints.add(new Point(Math.round(touchX), Math.round(touchY)));
            int color = Color.BLACK;
            Log.d("type",String.valueOf(type));
            if (type==1)
                color=Color.YELLOW;
            if (type==2)
                color=Color.GREEN;
            if (type==3)
                color=Color.RED;
            typePoints.add(color);

            // indicate view should be redrawn
            invalidate();
            return true;
    }

    }


