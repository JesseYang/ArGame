package com.efei.kids.argame;

/**
 * Created by jesse on 15-11-16.
 */
public class HoughConditionData {
    public int accumelator_reso;
    public int canny_threshold;
    public int accumelator_threshold;
    public int radius_lower;
    public int radius_higher;

    public HoughConditionData() {
        this.accumelator_reso = 8;
        this.canny_threshold = 100;
        this.accumelator_threshold = 100;
        this.radius_lower = 0;
        this.radius_higher = 0;
    }
}
