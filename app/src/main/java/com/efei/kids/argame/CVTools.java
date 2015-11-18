package com.efei.kids.argame;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Created by jesse on 15-11-16.
 */
public class CVTools {

    public static HoughResultData find_circle(Mat grayMat, HoughConditionData condition) {
        HoughResultData data = new HoughResultData();

        long functionStartTime = System.nanoTime();
        Mat circleMat = new Mat();
        int accumelator_reso = condition.accumelator_reso;
        int canny_threshold = condition.canny_threshold;
        int accumelator_threshold = condition.accumelator_threshold;
        int radius_lower = condition.radius_lower;
        int radius_higher = condition.radius_higher;

        long houghStartTime = System.nanoTime();
        int col_start = 0;
        int col_end = 0;
        int row_start = 0;
        int row_end = 0;
        if (condition.image_range == 1 && condition.last_circleNumber > 0) {
            col_start = Math.max(condition.last_x - 2 * condition.last_r, 0);
            col_end = Math.min(condition.last_x + 2 * condition.last_r, grayMat.cols());
            row_start = Math.max(condition.last_y - 2 * condition.last_r, 0);
            row_end = Math.min(condition.last_y + 2 * condition.last_r, grayMat.rows());
            radius_lower = (int)Math.round(condition.last_r * 0.5);
            radius_higher = condition.last_r * 2;
            Imgproc.HoughCircles(grayMat.submat(row_start, row_end, col_start, col_end), circleMat, Imgproc.HOUGH_GRADIENT, accumelator_reso, 100, canny_threshold, accumelator_threshold, radius_lower, radius_higher);
        } else {
            Imgproc.HoughCircles(grayMat, circleMat, Imgproc.HOUGH_GRADIENT, accumelator_reso, 100, canny_threshold, accumelator_threshold, radius_lower, radius_higher);
        }
        data.functionTime = Math.round((System.nanoTime() - functionStartTime) * 1.0 / 1000000L * 100) * 1.0 / 100;
        data.houghTime = Math.round((System.nanoTime() - houghStartTime) * 1.0 / 1000000L * 100) * 1.0 / 100;
        data.circleNumber = circleMat.cols();
        if (data.circleNumber > 0) {
            data.x = (int)Math.round(circleMat.get(0, 0)[0]) + col_start;
            data.y = (int)Math.round(circleMat.get(0, 0)[1]) + row_start;
            data.r = (int)Math.round(circleMat.get(0, 0)[2]);
            condition.last_x = data.x;
            condition.last_y = data.y;
            condition.last_r = data.r;
            condition.last_circleNumber = data.circleNumber;
        }
        data.width = grayMat.cols();
        data.height = grayMat.rows();
        return data;
    }
}
