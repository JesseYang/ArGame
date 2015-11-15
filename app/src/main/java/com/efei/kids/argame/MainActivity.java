package com.efei.kids.argame;

import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.WindowManager;

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


    private Camera mCamera;
    private GLSurfaceView mSurfaceView;
    private MyGLSurfaceView mGLView;
    private CameraBridgeViewBase mOpenCvCameraView;
    Renderer renderer;

    static {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
            Log.i("error", "static initialization failed");
        }
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat m = inputFrame.gray();
        Mat c = new Mat();
        // Mat e = new Mat();
        // Imgproc.Canny(m, e, 50, 60);
        Imgproc.HoughCircles(m, c, Imgproc.HOUGH_GRADIENT, 1, m.rows() / 4);
        return m;
        // return inputFrame.gray();
        // return inputFrame.rgba();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);

        final RajawaliSurfaceView surface = new RajawaliSurfaceView(this);
        surface.setFrameRate(60.0);
        surface.setRenderMode(IRajawaliSurface.RENDERMODE_WHEN_DIRTY);

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
        mGLView = new MyGLSurfaceView(this);
        // setContentView(mGLView);

        mOpenCvCameraView = new JavaCameraView(this, -1);
        // mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.HelloOpenCvView);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        addContentView(mOpenCvCameraView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    protected void onResume() {
        super.onResume();
        // OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        /*
         * The activity must call the GL surface view's
         * onResume() on activity onResume().
         */
        if (mSurfaceView != null) {
            mSurfaceView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        /*
         * The activity must call the GL surface view's
         * onPause() on activity onPause().
         */
        if (mSurfaceView != null) {
            mSurfaceView.onPause();
        }
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
