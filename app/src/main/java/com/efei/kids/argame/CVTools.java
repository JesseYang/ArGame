package com.efei.kids.argame;

import com.efei.kids.argame.data.HoughConditionData;
import com.efei.kids.argame.data.HoughResultData;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Created by jesse on 15-11-16.
 */
public class CVTools {

    public static HoughResultData find_circle(CameraBridgeViewBase.CvCameraViewFrame inputFrame, HoughConditionData condition) {
        long functionStartTime = System.nanoTime();

        HoughResultData data = new HoughResultData();

        Mat colorMat = inputFrame.rgba();
        // Mat colorMat = new Mat();
        // Imgproc.GaussianBlur(inputFrame.rgba(), colorMat, new Size(7, 7), 3, 3);

        Mat hsvMat = new Mat();
        Mat threshold1Mat = new Mat();
        Mat threshold2Mat = new Mat();
        Mat thresholdMat = new Mat();

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
        int region_factor = 3;
        Size dilate_erode_size = new Size(3, 3);
        if (condition.image_range == 1 && condition.last_circleNumber > 0) {
            Mat grayMat = inputFrame.gray();
            col_start = Math.max(condition.last_x - region_factor * condition.last_r, 0);
            col_end = Math.min(condition.last_x + region_factor * condition.last_r, grayMat.cols());
            row_start = Math.max(condition.last_y - region_factor * condition.last_r, 0);
            row_end = Math.min(condition.last_y + region_factor * condition.last_r, grayMat.rows());
            radius_lower = (int)Math.round(condition.last_r * 0.5);
            radius_higher = condition.last_r * 2;
            Imgproc.cvtColor(colorMat.submat(row_start, row_end, col_start, col_end), hsvMat, Imgproc.COLOR_RGB2HSV);
            Core.inRange(hsvMat, new Scalar(0, 100, 50), new Scalar(10, 255, 255), threshold1Mat);
            Core.inRange(hsvMat, new Scalar(170, 100, 50), new Scalar(180, 255, 255), threshold2Mat);
            Core.bitwise_or(threshold1Mat, threshold2Mat, thresholdMat);
            Imgproc.GaussianBlur(thresholdMat, thresholdMat, new Size(7, 7), 3, 3);
            Imgproc.dilate(thresholdMat, thresholdMat, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, dilate_erode_size));
            Imgproc.erode(thresholdMat, thresholdMat, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, dilate_erode_size));
            Imgproc.HoughCircles(thresholdMat, circleMat, Imgproc.HOUGH_GRADIENT, accumelator_reso, 100, canny_threshold, accumelator_threshold, radius_lower, radius_higher);
        } else {
            Imgproc.cvtColor(colorMat, hsvMat, Imgproc.COLOR_RGB2HSV);
            Core.inRange(hsvMat, new Scalar(0, 100, 50), new Scalar(10, 255, 255), threshold1Mat);
            Core.inRange(hsvMat, new Scalar(170, 100, 50), new Scalar(180, 255, 255), threshold2Mat);
            Core.bitwise_or(threshold1Mat, threshold2Mat, thresholdMat);
            Imgproc.GaussianBlur(thresholdMat, thresholdMat, new Size(7, 7), 3, 3);
            Imgproc.dilate(thresholdMat, thresholdMat, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, dilate_erode_size));
            Imgproc.erode(thresholdMat, thresholdMat, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, dilate_erode_size));
            Imgproc.HoughCircles(thresholdMat, circleMat, Imgproc.HOUGH_GRADIENT, accumelator_reso, 100, canny_threshold, accumelator_threshold, radius_lower, radius_higher);
        }
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
        data.width = thresholdMat.cols();
        data.height = thresholdMat.rows();
        data.thresholdMat = thresholdMat;
        data.functionTime = Math.round((System.nanoTime() - functionStartTime) * 1.0 / 1000000L * 100) * 1.0 / 100;
        return data;
    }
}
