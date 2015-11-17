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

    public int last_x;
    public int last_y;
    public int last_r;
    public boolean use_last_data;

    // 0 for original image, 1 for grey image， 2 for edge image
    public int show_image_type;

    public HoughConditionData() {
        this.accumelator_reso = 8;
        this.canny_threshold = 100;
        this.accumelator_threshold = 100;
        this.radius_lower = 0;
        this.radius_higher = 0;
        this.show_image_type = 1;
        
    }
}
