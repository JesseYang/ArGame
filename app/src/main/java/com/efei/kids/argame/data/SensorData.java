package com.efei.kids.argame.data;

import android.hardware.SensorManager;

/**
 * Created by jesse on 15-11-22.
 */
public class SensorData {
    private float[] rVector;
    private float[] rMatrix;
    private float[] rAngles;
    private boolean isUpdate;

    public SensorData() {
        rVector = new float[3];
        rAngles = new float[3];
        rMatrix = new float[9];
        isUpdate = true;
    }

    public void setrVector(float[] data) {
        rVector = data;
        isUpdate = true;
    }

    public float[] calculateAngles() {

        if (isUpdate == false) {
            return rAngles;
        }

        isUpdate = true;

        //caculate rotation matrix from rotation vector first
        SensorManager.getRotationMatrixFromVector(rMatrix, rVector);

        //calculate Euler angles now
        SensorManager.getOrientation(rMatrix, rAngles);

        //The results are in radians, need to convert it to degrees
        convertToDegrees(rAngles);

        return rAngles;
    }

    private void convertToDegrees(float[] vector){
        for (int i = 0; i < vector.length; i++){
            vector[i] = Math.round(Math.toDegrees(vector[i]));
        }
    }
}
