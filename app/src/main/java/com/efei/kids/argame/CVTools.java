package com.efei.kids.argame;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Created by jesse on 15-11-16.
 */
public class CVTools {

    public static HoughResultData find_circle(Mat greyMat, HoughConditionData condition) {
        HoughResultData data = new HoughResultData();

        long functionStartTime = System.nanoTime();
        Mat circleMat = new Mat();
        int accumelator_reso = condition.accumelator_reso;
        int canny_threshold = condition.canny_threshold;
        int accumelator_threshold = condition.accumelator_threshold;
        int radius_lower = condition.radius_lower;
        int radius_higher = condition.radius_higher;
        long houghStartTime = System.nanoTime();
        Imgproc.HoughCircles(greyMat, circleMat, Imgproc.HOUGH_GRADIENT, accumelator_reso, 100, canny_threshold, accumelator_threshold, radius_lower, radius_higher);

        data.functionTime = Math.round((System.nanoTime() - functionStartTime) * 1.0 / 1000000L * 100) * 1.0 / 100;
        data.houghTime = Math.round((System.nanoTime() - houghStartTime) * 1.0 / 1000000L * 100) * 1.0 / 100;
        data.circleNumber = circleMat.cols();
        if (data.circleNumber > 0) {
            data.x = (int)Math.round(circleMat.get(0, 0)[0]);
            data.y = (int)Math.round(circleMat.get(0, 0)[1]);
            data.r = (int)Math.round(circleMat.get(0, 0)[2]);
        }
        data.width = greyMat.cols();
        data.height = greyMat.rows();
        return data;
    }
}
