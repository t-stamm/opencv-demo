package com.catira.opencvdemo.services;


import android.content.Context;
import android.graphics.Matrix;
import android.util.Log;

import com.catira.opencvdemo.model.BikePartPositions;
import com.catira.opencvdemo.model.BikeSize;
import com.catira.opencvdemo.model.Circle;
import com.catira.opencvdemo.model.CyclingPosition;

import org.opencv.android.InstallCallbackInterface;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Timo on 26.10.2016.
 */

public class BikeImageIdentifier implements LoaderCallbackInterface {

    private static final int DEFAULT_DEGREES = -17; // 73° to left
    private Context mContext;
    public BikeImageIdentifier(Context c) {
        this.mContext = c;
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, c, this);
    }

    public BikePartPositions createPrediction(Mat frame, Point center, BikeSize bikeSize, int wheelSize, CyclingPosition cyclingPosition) {
        int colorChannels = (frame.channels() == 3) ? Imgproc.COLOR_BGR2GRAY
                : ((frame.channels() == 4) ? Imgproc.COLOR_BGRA2GRAY : 1);
        Imgproc.cvtColor(frame, frame, colorChannels);
        Imgproc.threshold(frame, frame, 140, 255, Imgproc.THRESH_BINARY);
        Imgproc.GaussianBlur(frame, frame, new Size(5, 5), 2, 2);

        Mat circlesMat = new Mat(frame.width(),
                frame.height(), CvType.CV_8UC1);
        List<Circle> circles = new ArrayList<>();

        //for(int i = 1; i < 10; i++) {
        Imgproc.HoughCircles(frame, circlesMat,
                    Imgproc.CV_HOUGH_GRADIENT, 2, 5, 70,
                    72, (frame.width() / 10), ((frame.width() / 5)));
            System.out.println(" cols: " + circlesMat.cols() + ", rows: " + circlesMat.rows());
            /*Circle bestMatch = null;
            int bestMatchDistance = Integer.MAX_VALUE;
            int bestMatchRadius = 0;*/
            double tolerance = .1;
            if (circlesMat.cols() > 0) {
                System.out.println("Looking for circles at "+center.x+"/"+center.y);
                for (int x = 0; x < circlesMat.cols(); x++) {
                    double circle[] = circlesMat.get(0, x);
                    Point foundCenter = new Point((int) Math.round(circle[0]), (int) Math.round(circle[1]));
                    int radius = (int) Math.round(circle[2]);
                    if(Math.abs(foundCenter.x - center.x) < frame.width() * tolerance && Math.abs(foundCenter.y - center.y) < frame.height() * tolerance) {
                        System.out.println("Found Circle at "+foundCenter.x+"/"+foundCenter.y+" with r "+radius);
                        circles.add(new Circle(foundCenter, radius));
                    }
                }
            }
        //}

        List<Circle[]> foundWheels = new ArrayList<>();

        for(int i = 0; i < circles.size(); i++) {
            for(int j = 0; j < circlesMat.cols(); j++) {
                double circle[] = circlesMat.get(0, j);
                Circle foundCircle = new Circle((int) Math.round(circle[0]), (int) Math.round(circle[1]), (int)Math.round(circle[2]));
                 if (radiusMatches(circles.get(i), foundCircle) && verticalPositionMatches(circles.get(i), foundCircle) && horizontalPositionDoesNotMatch(circles.get(i), foundCircle)) {
                    foundWheels.add(new Circle[]{circles.get(i), foundCircle});
                 }
                System.out.println("");
            }
        }

        int preferedRadius = 0;
        int preferedIndex = -1;
        for(int i = 0; i < foundWheels.size(); i++) {
            int r = Math.max(foundWheels.get(i)[0].getRadius(), foundWheels.get(i)[1].getRadius());
            if(preferedRadius < r) {
                preferedIndex = i;
                preferedRadius = r;
            }
        }
        if(preferedIndex >= 0) {
            return createBikeDimensions(foundWheels.get(preferedIndex)[0], foundWheels.get(preferedIndex)[1], bikeSize, wheelSize, cyclingPosition);
        }

        return null;
    }

    private BikePartPositions createBikeDimensions(Circle frontWheel, Circle rearWheel, BikeSize bikeSize, int wheelSize, CyclingPosition cyclingPosition) {
        int directionMultiplier = (frontWheel.getCenter().x > rearWheel.getCenter().x) ? 1 : -1;
        // mContext.getResources().getDisplayMetrics().densityDpi * 25.4)
        double imageScale = frontWheel.getRadius() * 2 / ((double)wheelSize / 10); // wheelsize is in mm. Bikecalculation returns values in cm;
        BikeSizeCalculator calc = new BikeSizeCalculator();

        // frame height from calc
        // scaledSteering = frame height + cycling position
        // scaledSteering and frame height 73°
        // frame length horizontally between those to points

        double leftWheelX = Math.min(frontWheel.getCenter().x, rearWheel.getCenter().x);
        Point pedal = new Point(((frontWheel.getCenter().x + rearWheel.getCenter().x - leftWheelX) * 0.33) + leftWheelX, (frontWheel.getCenter().y + rearWheel.getCenter().y) * .5 + (7 * imageScale));
        double scaledFrameHeight = bikeSize.getFrameHeight() * imageScale;

        Point scaledFrameBack = rotatePoint(pedal, new Point(pedal.x, pedal.y - scaledFrameHeight), DEFAULT_DEGREES);

        double scaledSaddleHeight = bikeSize.getSaddleHeight() * imageScale;

        Point scaledSaddle = rotatePoint(pedal, new Point(pedal.x, pedal.y - scaledSaddleHeight), DEFAULT_DEGREES);
        Point scaledFrameFront = rotatePoint(frontWheel.getCenter(), new Point(frontWheel.getCenter().x, frontWheel.getCenter().y - scaledFrameHeight), DEFAULT_DEGREES);
        scaledFrameFront.y = scaledFrameBack.y;

        Point scaledSteering = rotatePoint(scaledFrameFront, new Point(scaledFrameFront.x, scaledFrameFront.y - 10 * imageScale), DEFAULT_DEGREES);
        Point steeringLength = new Point(scaledSteering.x + (10 * imageScale) * directionMultiplier, scaledSteering.y - 3 * imageScale);
        return new BikePartPositions(frontWheel, rearWheel,
                scaledFrameFront,
                scaledFrameBack,
                scaledSteering, steeringLength,
                pedal,
                new Point((pedal.x + bikeSize.getCrankLength()) * imageScale, pedal.y),
                scaledSaddle, wheelSize);
    }

    private Point rotatePoint(Point pointSource, Point pointTarget, int degrees) {
        Matrix transform = new Matrix();
        transform.setRotate(degrees, (float)pointSource.x, (float)pointSource.y);
        float[] values = new float[2];
        values[0] = (float)pointTarget.x;
        values[1] = (float)pointTarget.y;
        transform.mapPoints(values);
        return new Point((double)values[0], (double) values[1]);
    }

    private boolean horizontalPositionDoesNotMatch(Circle circle, Circle circle1) {
        Circle a,b;
        if(circle.getCenter().x > circle1.getCenter().x) {
            a = circle;
            b = circle1;
        } else {
            b = circle;
            a = circle1;
        }

        System.out.print("horizontal diff: "+ ((b.getCenter().x - b.getRadius()) - (a.getCenter().x - a.getRadius())) +"  ");
        return (a.getCenter().x - a.getRadius()) > (b.getCenter().x + b.getRadius());
    }

    /**
     * If difference of vertical position is < 10% of bigger radius
     * @param circle
     * @param circle1
     * @return
     */
    private boolean verticalPositionMatches(Circle circle, Circle circle1) {
        System.out.print("vertical diff: "+ Math.abs(circle.getCenter().y - circle1.getCenter().y)+"  ");
        return Math.abs(circle.getCenter().y - circle1.getCenter().y) < Math.max(circle.getCenter().y, circle1.getCenter().y) * .2;
    }


    /**
     * If difference of radius is < 10% of bigger radius
     * @param circle
     * @param circle1
     * @return
     */
    private boolean radiusMatches(Circle circle, Circle circle1) {
        System.out.print("radius diff: "+Math.abs(circle.getRadius() - circle1.getRadius())+"  ");
        return Math.abs(circle.getRadius() - circle1.getRadius()) < Math.max(circle.getRadius(), circle1.getRadius()) * .1;
    }

    public Circle getWheel(Mat frame, Point center) {
        int colorChannels = (frame.channels() == 3) ? Imgproc.COLOR_BGR2GRAY
                : ((frame.channels() == 4) ? Imgproc.COLOR_BGRA2GRAY : 1);
        Imgproc.cvtColor(frame, frame, colorChannels);
        Imgproc.GaussianBlur(frame, frame, new Size(9, 9), 2, 2);
        System.out.println("Try to find wheel at point "+center.x+" / "+center.y);
        return tryToFindWheel(frame, center, 1);
    }

    private Circle tryToFindWheel(Mat frame, Point center, int i) {
        if( i < 1) {
            i = 1;
        }

        Mat circles = new Mat(frame.width(),
                frame.height(), CvType.CV_8UC1);

        System.out.print(i+": "+ (1 + (i * .2))+", "+(int)((frame.width() / (5 - i * .2)))+", "+(int)((frame.width() / 2) * .9));

        Imgproc.HoughCircles(frame, circles,
                Imgproc.CV_HOUGH_GRADIENT, 1 + (i * .2), 20, 70,
                72, (int)(frame.width() / (5 - i * .2)), (int)((frame.width() / 2) * .9));
        System.out.println(" cols: "+circles.cols()+", rows: "+circles.rows());
        Circle bestMatch = null;
        int bestMatchDistance = Integer.MAX_VALUE;
        if(circles.cols() > 0) {
            for (int x = 0; x < circles.cols(); x++) {
                double circle[] = circles.get(0, x);
                Point foundCenter = new Point((int)Math.round(circle[0]), (int)Math.round(circle[1]));
                int radius = (int) Math.round(circle[2]);
                // get the circle which is the closest to the center and where the whole
                // circle will not exceed the image boundaries
                int newMatchDistance = getDistanceFromPoint(foundCenter, center);
                if((bestMatch == null
                        || newMatchDistance < bestMatchDistance)
                        && (
                        (foundCenter.x - radius) >= 0
                            && (foundCenter.x + radius) <= frame.width()
                            && (foundCenter.y - radius) >= 0
                            && (foundCenter.y + radius) <= frame.height()
                            )
                        ) {
                    bestMatchDistance = newMatchDistance;
                    bestMatch = new Circle(foundCenter, radius);
                    System.out.println("Found circle at " + foundCenter.x + " / " + foundCenter.y + " with " + radius + " r. ("+frame.width()+" / "+frame.height()+")");
                }
            }
        }
        if(i < 10) {
            Circle otherMatch = tryToFindWheel(frame, center, i+1);
            if(otherMatch != null && getDistanceFromPoint(otherMatch.getCenter(), center) < bestMatchDistance) {
                System.out.println("Found other circle at " + otherMatch.getCenter().x + " / " + otherMatch.getCenter().y + " with " + otherMatch.getRadius() + " r.");
                return otherMatch;
            }
        }
        return bestMatch;
    }

    private int getDistanceFromPoint(Point foundCenter, Point center) {
        return (int) (Math.abs(foundCenter.x - center.x) + Math.abs(foundCenter.y - center.y));
    }

    @Override
    public void onManagerConnected(int status) {
        Log.i("BikeImageIdentifier", "initialized with "+String.valueOf(status));
    }

    @Override
    public void onPackageInstall(int operation, InstallCallbackInterface callback) {
        callback.install();
    }
}
