package com.efei.kids.argame.data;

/**
 * Created by jesse on 15-11-16.
 */
public class HoughConditionData {
    public int accumelator_reso;
    public int canny_threshold;
    public int accumelator_threshold;
    public int radius_lower;
    public int radius_higher;

    public int last_x;
    public int last_y;
    public int last_r;
    public int last_circleNumber;

    // 0 for full image, 1 for part image
    public int image_range;

    // 0 for original image, 1 for grey image， 2 for edge image
    public int show_image_type;

    public HoughConditionData() {
        this.accumelator_reso = 1;
        this.canny_threshold = 100;
        this.accumelator_threshold = 30;
        this.radius_lower = 0;
        this.radius_higher = 0;
        this.show_image_type = 1;

        this.last_x = -1;
        this.last_y = -1;
        this.last_r = -1;
        this.image_range = 0;

        this.last_circleNumber = 0;
    }
}
