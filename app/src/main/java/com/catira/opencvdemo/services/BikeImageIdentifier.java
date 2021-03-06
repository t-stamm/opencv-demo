package com.catira.opencvdemo.services;


import android.content.Context;
import android.graphics.Matrix;
import android.util.Log;

import com.catira.opencvdemo.R;
import com.catira.opencvdemo.model.BikePartPositions;
import com.catira.opencvdemo.model.BikeSize;
import com.catira.opencvdemo.model.Circle;
import com.catira.opencvdemo.model.CyclingPosition;

import org.opencv.android.InstallCallbackInterface;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

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

    public BikePartPositions createPrediction(Mat frame, Point frontWheelCenter, BikeSize bikeSize, int wheelSize, CyclingPosition cyclingPosition) {
        int colorChannels = (frame.channels() == 3) ? Imgproc.COLOR_BGR2GRAY
                : ((frame.channels() == 4) ? Imgproc.COLOR_BGRA2GRAY : 1);

        Imgproc.cvtColor(frame, frame, colorChannels);
        Imgproc.threshold(frame, frame, 140, 255, Imgproc.THRESH_BINARY);
        Imgproc.GaussianBlur(frame, frame, new Size(5, 5), 2, 2);

        // crop image to a part where the front wheel is expected
        int height = (int)Math.ceil(frame.height() / 1.5);
        int width = (int)Math.ceil(frame.width() / 2);
        int startColFront = 0;
        int startColBack = width;
        if(frontWheelCenter.x > frame.width() / 2) {
            startColFront = width;
            startColBack = 0;
        }
        int startRow = (frame.height() - height);
        Mat frontWheelMat = frame.submat(startRow, Math.min(startRow + height, frame.rows()), startColFront, Math.min(startColFront + width, frame.cols()));

        Circle frontWheelCircle = findWheelAroundCenterPoint(frontWheelMat, new Point(frontWheelCenter.x - startColFront, frontWheelCenter.y - startRow));
        // abort if nothing has been found
        if(frontWheelCircle == null) {
            return null;
        }
        frontWheelCircle.getCenter().x += startColFront;
        frontWheelCircle.getCenter().y += startRow;

        Mat backWheelMat = frame.submat(startRow, Math.min(startRow + height, frame.rows()), startColBack, Math.min(startColBack + width, frame.cols()));

        Circle backWheelCircle = findOtherWheel(backWheelMat, frontWheelCenter.y - startRow, frontWheelCircle.getRadius());
        if(backWheelCircle == null) {
            Log.w(getClass().getName(), "Could not find other wheel. Creating default.");
            if(frontWheelCenter.x > frame.width() / 2) {
                backWheelCircle = new Circle((int)(frontWheelCircle.getCenter().x - frontWheelCircle.getRadius() * 2), (int)frontWheelCircle.getCenter().y, frontWheelCircle.getRadius());
            } else {
                backWheelCircle = new Circle((int)(frontWheelCircle.getCenter().x + frontWheelCircle.getRadius() * 2), (int)frontWheelCircle.getCenter().y, frontWheelCircle.getRadius());
            }
        }
        backWheelCircle.getCenter().x += startColBack;
        backWheelCircle.getCenter().y += startRow;

        //List<Circle[]> foundWheels = new ArrayList<>();

        /*Circle bestFrontWheel = null;
        Circle bestBackWheel = null;
        double bestScore = Double.MAX_VALUE;

        for(int i = 0; i < frontWheelCircles.size(); i++) {
            for(int j = 0; j < circlesMat.cols(); j++) {
                double circle[] = circlesMat.get(0, j);
                Circle foundCircle = new Circle((int) Math.round(circle[0]), (int) Math.round(circle[1]), (int)Math.round(circle[2]));

                if(horizontalPositionIsOk(frontWheelCircles.get(i), foundCircle)) {
                    Double score = getFoundWheelsScore(frontWheelCenter, frontWheelCircles.get(i), foundCircle);
                    if(score < bestScore) {
                        bestScore = score;
                        bestFrontWheel = frontWheelCircles.get(i);
                        bestBackWheel = foundCircle;
                    }
                }
                //if (radiusMatches(frontWheelCircles.get(i), foundCircle) && verticalPositionMatches(frontWheelCircles.get(i), foundCircle) && horizontalPositionIsOk(frontWheelCircles.get(i), foundCircle)) {
                //    foundWheels.add(new Circle[]{frontWheelCircles.get(i), foundCircle});
                //}
                //System.out.println("");
            }
        }*/

        if(frontWheelCircle != null && backWheelCircle != null) {
            return createBikeDimensions(frontWheelCircle, backWheelCircle, bikeSize, wheelSize, cyclingPosition, frame.width(), frame.height());
        }
/*
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
        }*/

        return null;
    }

    private Circle findOtherWheel(Mat frame, double y, int frontWheelRadius) {
        Mat circlesMat = new Mat(frame.width(),
                frame.height(), CvType.CV_8UC1);

        Imgproc.HoughCircles(frame, circlesMat,
                Imgproc.CV_HOUGH_GRADIENT, 2, 5, 70,
                72, (int)Math.floor(frontWheelRadius *.8), (int)Math.ceil(frontWheelRadius * 1.2));
        System.out.println(" cols: " + circlesMat.cols() + ", rows: " + circlesMat.rows());
        Circle bestMatch = null;
        double bestMatchDistance = Integer.MAX_VALUE;
        if (circlesMat.cols() > 0) {
            System.out.println("Looking for back wheel at y: "+y+" with radius "+frontWheelRadius);
            for (int x = 0; x < circlesMat.cols(); x++) {
                double circle[] = circlesMat.get(0, x);
                Point foundCenter = new Point((int) Math.round(circle[0]), (int) Math.round(circle[1]));
                int radius = (int) Math.round(circle[2]);
                System.out.println("y difference: "+Math.abs(foundCenter.y - y)+", radius: "+Math.abs(radius - frontWheelRadius));

                double matchedDistance = Math.abs(foundCenter.y - y) + Math.abs(radius - frontWheelRadius);
                if(matchedDistance < bestMatchDistance) {
                    bestMatch = new Circle(foundCenter, radius);
                }

                /*if(Math.abs(foundCenter.x - frontWheelCenter.x) < frame.width() * tolerance
                        && Math.abs(foundCenter.y - frontWheelCenter.y) < frame.height() * tolerance
                        && foundCenter.x + radius < frame.width()
                        && foundCenter.x - radius > 0
                        && foundCenter.y + radius < frame.height()
                        && foundCenter.y - radius > 0) {
                    System.out.println("Found Circle at "+foundCenter.x+"/"+foundCenter.y+" with r "+radius);
                    frontWheelCircles.add(new Circle(foundCenter, radius));
                }*/
            }
        }
        //}

        return bestMatch;
    }

    private Circle findWheelAroundCenterPoint(Mat frame, Point frontWheelCenter) {
        Mat circlesMat = new Mat(frame.width(),
                frame.height(), CvType.CV_8UC1);

        int maxWheelSize = frame.height();
        int minWheelSize = maxWheelSize / 4;
        Imgproc.HoughCircles(frame, circlesMat,
                Imgproc.CV_HOUGH_GRADIENT, 2, 5, 70,
                72, minWheelSize, maxWheelSize);
        System.out.println(" cols: " + circlesMat.cols() + ", rows: " + circlesMat.rows());
        Circle bestMatch = null;
        double bestMatchDistance = Integer.MAX_VALUE;
        if (circlesMat.cols() > 0) {
            System.out.println("Looking for frontWheelCircles at "+frontWheelCenter.x+"/"+frontWheelCenter.y);
            for (int x = 0; x < circlesMat.cols(); x++) {
                double circle[] = circlesMat.get(0, x);
                Point foundCenter = new Point((int) Math.round(circle[0]), (int) Math.round(circle[1]));
                int radius = (int) Math.round(circle[2]);
                System.out.println("Center difference: "+Math.abs(foundCenter.x - frontWheelCenter.x)+" / "+Math.abs(foundCenter.y - frontWheelCenter.y));

                double matchedDistance = Math.abs(foundCenter.x - frontWheelCenter.x) + Math.abs(foundCenter.y - frontWheelCenter.y);
                if(matchedDistance < bestMatchDistance) {
                    bestMatch = new Circle(foundCenter, radius);
                }

                /*if(Math.abs(foundCenter.x - frontWheelCenter.x) < frame.width() * tolerance
                        && Math.abs(foundCenter.y - frontWheelCenter.y) < frame.height() * tolerance
                        && foundCenter.x + radius < frame.width()
                        && foundCenter.x - radius > 0
                        && foundCenter.y + radius < frame.height()
                        && foundCenter.y - radius > 0) {
                    System.out.println("Found Circle at "+foundCenter.x+"/"+foundCenter.y+" with r "+radius);
                    frontWheelCircles.add(new Circle(foundCenter, radius));
                }*/
            }
        }        //}

        return bestMatch;
    }

    /*private double getFoundWheelsScore(Point center, Circle frontWheel, Circle circle) {
        double centerDiff = Math.sqrt(Math.pow(center.x - frontWheel.getCenter().x, 2) + Math.pow(center.y - frontWheel.getCenter().y, 2));
        double verticalDiff = verticalPositionDiff(frontWheel, circle);
        double radiusDiff = radiusDiff(frontWheel, circle);

        return centerDiff * 2 + verticalDiff + radiusDiff;
    }*/

    public BikePartPositions updateBikeDimensions(BikePartPositions positions, BikeSize bikeSize, int pointsToIgnore) {
        pointsToIgnore = Math.max(0, Math.min(7, pointsToIgnore));

        Circle frontWheel = positions.getFrontWheel();
        Circle rearWheel = positions.getBackWheel();
        int wheelSize = positions.getWheelSize();

        int directionMultiplier = (frontWheel.getCenter().x > rearWheel.getCenter().x) ? 1 : -1;
        // mContext.getResources().getDisplayMetrics().densityDpi * 25.4)
        double imageScale = frontWheel.getRadius() * 2 / ((double) wheelSize / 10); // wheelsize is in mm. Bikecalculation returns values in cm;
        BikeSizeCalculator calc = new BikeSizeCalculator();

        // frame height from calc
        // scaledSteering = frame height + cycling position
        // scaledSteering and frame height 73°
        // frame length horizontally between those to points

        double pedalFactor = (frontWheel.getCenter().x > rearWheel.getCenter().x ? .33 : .66);
        double leftWheelX = Math.min(frontWheel.getCenter().x, rearWheel.getCenter().x);
        Point pedal = positions.getPaddles();
        if (pointsToIgnore < 1) {
            positions.getPaddles().x = (leftWheelX + (Math.abs(frontWheel.getCenter().x - rearWheel.getCenter().x) * pedalFactor));
            positions.getPaddles().y = (frontWheel.getCenter().y + rearWheel.getCenter().y) * .5 + (7 * imageScale);
        }

        double scaledFrameHeight = bikeSize.getFrameHeight() * imageScale;
        if (pointsToIgnore < 2) {
            Point scaledFrameBack = rotatePoint(pedal, new Point(pedal.x, pedal.y - scaledFrameHeight), DEFAULT_DEGREES);
            positions.getFrameBack().x = scaledFrameBack.x;
            positions.getFrameBack().y = scaledFrameBack.y;
        }
        if (pointsToIgnore < 3) {
            double scaledSaddleHeight = bikeSize.getSaddleHeight() * imageScale;
            Point scaledSaddle = rotatePoint(pedal, new Point(pedal.x, pedal.y - scaledSaddleHeight), DEFAULT_DEGREES);
            positions.getSaddle().x = scaledSaddle.x;
            positions.getSaddle().y = scaledSaddle.y;
        }

        Point scaledFrameFront = rotatePoint(frontWheel.getCenter(), new Point(frontWheel.getCenter().x, frontWheel.getCenter().y - scaledFrameHeight), DEFAULT_DEGREES);
        if(pointsToIgnore < 4) {
            scaledFrameFront.y = positions.getFrameBack().y;
            positions.getFrameFront().x = scaledFrameFront.x;
            positions.getFrameFront().y = scaledFrameFront.y;
        }

        if(pointsToIgnore < 5) {
            Point scaledSteering = rotatePoint(scaledFrameFront, new Point(scaledFrameFront.x, scaledFrameFront.y - 10 * imageScale), DEFAULT_DEGREES);
            Point steeringLength = new Point(scaledSteering.x + (10 * imageScale) * directionMultiplier, scaledSteering.y - 3 * imageScale);
            positions.getSteeringLength().x = steeringLength.x;
            positions.getSteeringLength().y = steeringLength.y;
        }
        return positions;
    }

    private BikePartPositions createBikeDimensions(Circle frontWheel, Circle rearWheel, BikeSize bikeSize, int wheelSize, CyclingPosition cyclingPosition, int width, int height) {
        int directionMultiplier = (frontWheel.getCenter().x > rearWheel.getCenter().x) ? 1 : -1;
        // mContext.getResources().getDisplayMetrics().densityDpi * 25.4)
        double imageScale = frontWheel.getRadius() * 2 / ((double)wheelSize / 10); // wheelsize is in mm. Bikecalculation returns values in cm;
        BikeSizeCalculator calc = new BikeSizeCalculator();

        // frame height from calc
        // scaledSteering = frame height + cycling position
        // scaledSteering and frame height 73°
        // frame length horizontally between those to points

        double pedalFactor = (frontWheel.getCenter().x > rearWheel.getCenter().x?.33:.66);
        double leftWheelX = Math.min(frontWheel.getCenter().x, rearWheel.getCenter().x);
        Point pedal = new Point((leftWheelX + (Math.abs(frontWheel.getCenter().x - rearWheel.getCenter().x) * pedalFactor)), (frontWheel.getCenter().y + rearWheel.getCenter().y) * .5 + (7 * imageScale));
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
                scaledSaddle, wheelSize, width, height);
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

    private boolean horizontalPositionIsOk(Circle circle, Circle circle1) {
        Circle a,b;
        if(circle.getCenter().x > circle1.getCenter().x) {
            a = circle;
            b = circle1;
        } else {
            b = circle;
            a = circle1;
        }

        boolean success = (a.getCenter().x - a.getRadius()) > (b.getCenter().x + b.getRadius());
        System.out.print("horizontal diff: "+ ((b.getCenter().x - b.getRadius()) - (a.getCenter().x - a.getRadius())) +" : "+success);
        return success;
    }

    private double verticalPositionDiff(Circle circle, Circle circle1) {
        /*boolean success = Math.abs(circle.getCenter().y - circle1.getCenter().y) < Math.max(circle.getCenter().y, circle1.getCenter().y) * .2;
        System.out.print("vertical diff: "+ Math.abs(circle.getCenter().y - circle1.getCenter().y)+": "+success);*/

        return Math.abs(circle.getCenter().y - circle1.getCenter().y);
    }

    private double radiusDiff(Circle circle, Circle circle1) {
        /*boolean success = Math.abs(circle.getRadius() - circle1.getRadius()) < Math.max(circle.getRadius(), circle1.getRadius()) * .1;
        System.out.print("radius diff: "+Math.abs(circle.getRadius() - circle1.getRadius())+": "+success+". ");*/
        return Math.abs(circle.getRadius() - circle1.getRadius());
    }
/*
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
    }*/

    @Override
    public void onManagerConnected(int status) {
        Log.i("BikeImageIdentifier", "initialized with "+String.valueOf(status));
    }

    @Override
    public void onPackageInstall(int operation, InstallCallbackInterface callback) {
        callback.install();
    }

    public BikePartPositions createDefault(Mat m, Point center, BikeSize bikeSize, int currentWheelSize, CyclingPosition currentCyclingPosition) {
        Circle frontWheel, backWheel;
        double radius = center.y / 3;
        if(center.x > m.width() / 2) {
            frontWheel = new Circle((int)center.x, (int)center.y, (int)radius);
            backWheel = new Circle((int)Math.max(radius, center.x - radius * 4), (int)center.y, (int)radius);
        } else {
            backWheel = new Circle((int)center.x, (int)center.y, (int)radius);
            frontWheel = new Circle((int)Math.min(m.width() - radius, center.x + radius * 4), (int)center.y, (int)radius);
        }

        return createBikeDimensions(frontWheel, backWheel, bikeSize, currentWheelSize, currentCyclingPosition, m.width(), m.height());
    }
}
