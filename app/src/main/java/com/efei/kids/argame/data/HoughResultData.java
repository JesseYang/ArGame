package com.efei.kids.argame.data;

import org.opencv.core.Mat;

/**
 * Created by jesse on 15-11-16.
 */
public class HoughResultData {
    public int circleNumber;
    public int x;
    public int y;
    public int r;
    public int width;
    public int height;

    public double functionTime;
    public double houghTime;

    public Mat thresholdMat;

    public HoughResultData() {
        this.circleNumber = 0;
        this.x = -1;
        this.y = -1;
        this.r = -1;
        this.width = -1;
        this.height = -1;
        this.functionTime = 0.0;
        this.houghTime = 0.0;
        this.thresholdMat = new Mat();
    }
}
