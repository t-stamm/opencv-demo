package com.catira.opencvdemo.services;


import android.content.Context;
import android.util.Log;

import com.catira.opencvdemo.model.BikeDimensions;
import com.catira.opencvdemo.model.Circle;

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


    public BikeImageIdentifier(Context c) {

        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, c, this);
    }

    public BikeDimensions test(Mat frame, Point center) {
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
                    72, (frame.width() / 12), ((frame.width() / 5)));
            System.out.println(" cols: " + circlesMat.cols() + ", rows: " + circlesMat.rows());
            Circle bestMatch = null;
            int bestMatchDistance = Integer.MAX_VALUE;
            int bestMatchRadius = 0;
            if (circlesMat.cols() > 0) {
                System.out.println("Looking for circles at "+center.x+"/"+center.y);
                for (int x = 0; x < circlesMat.cols(); x++) {
                    double circle[] = circlesMat.get(0, x);
                    Point foundCenter = new Point((int) Math.round(circle[0]), (int) Math.round(circle[1]));
                    int radius = (int) Math.round(circle[2]);
                    if(Math.abs(foundCenter.x - center.x) < 20 && Math.abs(foundCenter.y - center.y) < 20) {
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
            return new BikeDimensions(foundWheels.get(preferedIndex)[0], foundWheels.get(preferedIndex)[1], null, null, null);
        }

        return null;
        /*
                    // get the circle which is the closest to the center and where the whole
                    // circle will not exceed the image boundaries
                    int newMatchDistance = getDistanceFromPoint(foundCenter, center);
                    if ((bestMatch == null
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
                        System.out.println("Found circle at " + foundCenter.x + " / " + foundCenter.y + " with " + radius + " r. (" + frame.width() + " / " + frame.height() + ")");
                    }
         */
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
        int bestMatchRadius = 0;
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
