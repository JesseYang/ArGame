package com.efei.kids.argame;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.efei.kids.argame.views.ControlView;
import com.efei.kids.argame.views.InfoView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.rajawali3d.surface.IRajawaliSurface;
import org.rajawali3d.surface.RajawaliSurfaceView;


public class MainActivity extends ActionBarActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private CameraBridgeViewBase mOpenCvCameraView;
    private InfoView infoView;
    private ControlView controlView;
    private Renderer renderer;
    private long lastFrameTime;

    public boolean isCalHough;

    private HoughResultData data;
    private HoughConditionData condition;

    private Mat edgeMat;

    static {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
            Log.i("error", "static initialization failed");
        }
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        long curFrameTime = System.nanoTime();
        if (lastFrameTime != 0L) {
            long internalTime = curFrameTime - lastFrameTime;
            updateInternalTime(internalTime);
        }
        lastFrameTime = curFrameTime;

        if (data != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    infoView.setHoughResultData(data);
                }
            });
        }

        final Mat grayMat = inputFrame.gray();

        if (isCalHough == false) {
            isCalHough = true;
            new Thread(new Runnable() {
                public void run() {
                    data = CVTools.find_circle(grayMat, condition);
                    isCalHough = false;
                }
            }).start();
        }

        if (condition.show_image_type == 0) {
            return inputFrame.rgba();
        } else if (condition.show_image_type == 1) {
            return grayMat;
        } else {
            Imgproc.Canny(grayMat, edgeMat, condition.canny_threshold / 2, condition.canny_threshold);
            return edgeMat;
        }
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
        edgeMat = new Mat();

        final RajawaliSurfaceView surface = new RajawaliSurfaceView(this);
        surface.setFrameRate(30.0);
        surface.setRenderMode(IRajawaliSurface.RENDERMODE_CONTINUOUSLY);

        setContentView(surface);

        renderer = new Renderer(this);
        surface.setSurfaceRenderer(renderer);

        // When working with the camera, it's useful to stick to one orientation.
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

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

        infoView = new InfoView(this);
        infoView.setAnchorView();

        controlView = new ControlView(this);
        controlView.setAnchorView();
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
}
