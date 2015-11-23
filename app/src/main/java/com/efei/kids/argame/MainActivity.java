package com.efei.kids.argame;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.efei.kids.argame.data.EulerAngleData;
import com.efei.kids.argame.data.HoughConditionData;
import com.efei.kids.argame.data.HoughResultData;
import com.efei.kids.argame.data.SensorData;
import com.efei.kids.argame.views.AngleView;
import com.efei.kids.argame.views.ControlView;
import com.efei.kids.argame.views.InfoView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.rajawali3d.surface.IRajawaliSurface;
import org.rajawali3d.surface.RajawaliSurfaceView;


public class MainActivity extends ActionBarActivity implements CameraBridgeViewBase.CvCameraViewListener2, SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mRotationVectorSensor;
    public SensorData mSensorData;


    private CameraBridgeViewBase mOpenCvCameraView;
    private InfoView infoView;
    private ControlView controlView;
    private AngleView angleView;
    private Renderer renderer;
    private long lastFrameTime;

    public boolean isCalHough;

    private HoughResultData data;
    private HoughConditionData condition;
    private EulerAngleData eulerAngleData;

    private Mat edgeMat;

    private CameraBridgeViewBase.CvCameraViewFrame currentFrame;
    private int currentFrameIndex;

    static {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
            Log.i("error", "static initialization failed");
        }
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        currentFrame = inputFrame;
        currentFrameIndex = (currentFrameIndex + 1) % 1000;
        return inputFrame.rgba();

        /****** RETURN COLOER FILTERED IMAGE ******/

        /*
        Mat colorMat = inputFrame.rgba();
        Mat hsvMat = new Mat();
        Mat threshold1Mat = new Mat();
        Mat threshold2Mat = new Mat();
        Mat thresholdMat = new Mat();
        Imgproc.cvtColor(colorMat, hsvMat, Imgproc.COLOR_RGB2HSV);
        Core.inRange(hsvMat, new Scalar(0, 100, 50), new Scalar(10, 255, 255), threshold1Mat);
        Core.inRange(hsvMat, new Scalar(170, 100, 50), new Scalar(180, 255, 255), threshold2Mat);
        Core.bitwise_or(threshold1Mat, threshold2Mat, thresholdMat);
        Imgproc.GaussianBlur(thresholdMat, thresholdMat, new Size(7, 7), 3, 3);
        Imgproc.dilate(thresholdMat, thresholdMat, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));
        Imgproc.erode(thresholdMat, thresholdMat, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));
        // return thresholdMat;
        return thresholdMat;
        */





        /****** SET FRAME INTERNAL TIME ******/
        /*
        long curFrameTime = System.nanoTime();
        if (lastFrameTime != 0L) {
            long internalTime = curFrameTime - lastFrameTime;
            updateInternalTime(internalTime);
        }
        lastFrameTime = curFrameTime;
        */

        /****** RETURN REQUIRED IMAGE ******/
        /*
        if (condition.show_image_type == 0) {
            return inputFrame.rgba();
        } else if (condition.show_image_type == 1) {
            return inputFrame.gray();
        } else {
            Imgproc.Canny(inputFrame.gray(), edgeMat, condition.canny_threshold / 2, condition.canny_threshold);
            return edgeMat;
        }
        */
    }

    public void updateInternalTime(final long internalTime) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                infoView.setInternalTime(internalTime);
            }
        });
    }

    public void onCameraViewStarted(int width, int height) {
    }

    public void onCameraViewStopped() {
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("success", "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public void setEulerAngleData(int yaw, int pitch, int roll) {
        this.eulerAngleData.yaw = yaw;
        this.eulerAngleData.pitch = pitch;
        this.eulerAngleData.roll = roll;
    }

    public EulerAngleData getEulerAngleData() {
        return this.eulerAngleData;
    }

    public void setHoughCondition(int canny_threshold, int accumelator_reso, int accumelator_threshold, int show_image_type, int image_range) {
        condition.canny_threshold = canny_threshold;
        condition.accumelator_reso = accumelator_reso;
        condition.accumelator_threshold = accumelator_threshold;
        condition.show_image_type = show_image_type;
        condition.image_range = image_range;
    }

    public HoughResultData getHoughResult() {
        return data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lastFrameTime = 0L;
        isCalHough = false;
        condition = new HoughConditionData();
        eulerAngleData = new EulerAngleData();
        edgeMat = new Mat();

        final RajawaliSurfaceView surface = new RajawaliSurfaceView(this);
        surface.setFrameRate(30.0);
        surface.setRenderMode(IRajawaliSurface.RENDERMODE_CONTINUOUSLY);

        setContentView(surface);

        renderer = new Renderer(this);
        surface.setSurfaceRenderer(renderer);

        // When working with the camera, it's useful to stick to one orientation.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Next, we disable the application's title bar...
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        // ...and the notification bar. That way, we can use the full screen.
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mOpenCvCameraView = new JavaCameraView(this, -1);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        addContentView(mOpenCvCameraView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        angleView = new AngleView(this);
        angleView.setAnchorView();
        /*
        infoView = new InfoView(this);
        infoView.setAnchorView();

        controlView = new ControlView(this);
        controlView.setAnchorView();
        */

        currentFrame = null;
        currentFrameIndex = 0;

        /*
        new Thread(new MyRunnable() {
            public void run() {
                while (true) {
                    if (currentFrame == null || currentFrameIndex == this.lastFrameIndex) {
                        SystemClock.sleep(10);
                        continue;
                    }
                    data = CVTools.find_circle(currentFrame, condition);
                    this.lastFrameIndex = currentFrameIndex;

                    if (data != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                infoView.setHoughResultData(data);
                            }
                        });
                    }
                }
            }
        }).start();
        */

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mRotationVectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        mSensorManager.registerListener(this, mRotationVectorSensor, 100000);
        mSensorData = new SensorData();
    }

    abstract class MyRunnable implements Runnable {
        public int lastFrameIndex;
        public MyRunnable() {
            lastFrameIndex = -1;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        mSensorData.setrVector(sensorEvent.values);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
