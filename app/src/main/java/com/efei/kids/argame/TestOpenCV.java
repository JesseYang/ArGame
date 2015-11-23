package com.efei.kids.argame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.FileOutputStream;


public class TestOpenCV extends ActionBarActivity {

    static {
        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
            Log.i("error", "static initialization failed");
        }
    }

    ImageView origin_iv;
    ImageView result_iv;
    TextView image_reso_tv;
    TextView hough_tv;
    TextView function_tv;
    TextView circle_number_tv;
    TextView circle_result_tv;
    EditText canny_threshold_et;
    EditText accumelator_threshold_et;
    EditText accumelator_reso_et;
    EditText radius_lower_et;
    EditText radius_higher_et;
    EditText in_sample_size_et;
    Button begin_btn;
    RadioGroup show_image_type_rg;
    RadioGroup show_result_type_rg;
    RadioGroup crop_image_rg;

    EditText crop_x_et;
    EditText crop_y_et;
    EditText crop_width_et;
    EditText crop_height_et;

    EditText hsv_lower_h_et;
    EditText hsv_lower_s_et;
    EditText hsv_lower_v_et;
    EditText hsv_higher_h_et;
    EditText hsv_higher_s_et;
    EditText hsv_higher_v_et;



    private static final String folder = "/storage/emulated/0/argame/";
    FileOutputStream out;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("success", "OpenCV loaded successfully");
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_open_cv);

        origin_iv = (ImageView) findViewById(R.id.origin_image);
        result_iv = (ImageView) findViewById(R.id.result_image);
        image_reso_tv = (TextView) findViewById(R.id.image_reso_tv);
        hough_tv = (TextView) findViewById(R.id.hough_time_tv);
        function_tv = (TextView) findViewById(R.id.function_time_tv);
        circle_number_tv = (TextView) findViewById(R.id.circle_number_tv);
        circle_result_tv = (TextView) findViewById(R.id.circle_result_tv);
        canny_threshold_et = (EditText) findViewById(R.id.canny_threshold_et);
        accumelator_threshold_et = (EditText) findViewById(R.id.accumelator_threshold_et);
        accumelator_reso_et = (EditText) findViewById(R.id.accumelator_reso_et);
        radius_lower_et = (EditText) findViewById(R.id.radius_lower_et);
        radius_higher_et = (EditText) findViewById(R.id.radius_higher_et);
        in_sample_size_et = (EditText) findViewById(R.id.in_sample_size_et);
        begin_btn = (Button) findViewById(R.id.begin_btn);
        show_image_type_rg = (RadioGroup) findViewById(R.id.show_image_type_rg);
        show_result_type_rg = (RadioGroup) findViewById(R.id.show_result_type_rg);
        crop_image_rg = (RadioGroup) findViewById(R.id.crop_image_rg);

        crop_x_et = (EditText) findViewById(R.id.crop_x_et);
        crop_y_et = (EditText) findViewById(R.id.crop_y_et);
        crop_width_et = (EditText) findViewById(R.id.crop_width_et);
        crop_height_et = (EditText) findViewById(R.id.crop_height_et);

        hsv_lower_h_et = (EditText) findViewById(R.id.hsv_lower_h_et);
        hsv_lower_s_et = (EditText) findViewById(R.id.hsv_lower_s_et);
        hsv_lower_v_et = (EditText) findViewById(R.id.hsv_lower_v_et);
        hsv_higher_h_et = (EditText) findViewById(R.id.hsv_higher_h_et);
        hsv_higher_s_et = (EditText) findViewById(R.id.hsv_higher_s_et);
        hsv_higher_v_et = (EditText) findViewById(R.id.hsv_higher_v_et);

        begin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long starttime = System.nanoTime();
                test();
                long consumingTime = System.nanoTime() - starttime;
                function_tv.setText("函数执行时间：" + String.valueOf(consumingTime / 1000000) + "ms");
            }
        });

        out = null;

        // test();
    }

    public void test() {
        int in_sample_size = Integer.valueOf(in_sample_size_et.getText().toString());
        int canny_threshold = Integer.valueOf(canny_threshold_et.getText().toString());
        int accumelator_threshold = Integer.valueOf(accumelator_threshold_et.getText().toString());
        int accumelator_reso = Integer.valueOf(accumelator_reso_et.getText().toString());
        int radius_lower = Integer.valueOf(radius_lower_et.getText().toString());
        int radius_higher = Integer.valueOf(radius_higher_et.getText().toString());

        int show_image_type_rb_id = show_image_type_rg.getCheckedRadioButtonId();
        View show_image_type_rb = show_image_type_rg.findViewById(show_image_type_rb_id);
        int show_image_type = show_image_type_rg.indexOfChild(show_image_type_rb);

        int show_result_type_rb_id = show_result_type_rg.getCheckedRadioButtonId();
        View show_result_type_rb = show_result_type_rg.findViewById(show_result_type_rb_id);
        int show_result_type = show_result_type_rg.indexOfChild(show_result_type_rb);

        int crop_image_rb_id = crop_image_rg.getCheckedRadioButtonId();
        View crop_image_rb = crop_image_rg.findViewById(crop_image_rb_id);
        int crop_image = crop_image_rg.indexOfChild(crop_image_rb);

        int hsv_lower_h = Integer.valueOf(hsv_lower_h_et.getText().toString());
        int hsv_lower_s = Integer.valueOf(hsv_lower_s_et.getText().toString());
        int hsv_lower_v = Integer.valueOf(hsv_lower_v_et.getText().toString());

        int hsv_higher_h = Integer.valueOf(hsv_higher_h_et.getText().toString());
        int hsv_higher_s = Integer.valueOf(hsv_higher_s_et.getText().toString());
        int hsv_higher_v = Integer.valueOf(hsv_higher_v_et.getText().toString());

        // read the image and show in the screen
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = in_sample_size;
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.bak_test_jpg, options);

        if (crop_image == 0) {
            int crop_x = Integer.valueOf(crop_x_et.getText().toString());
            int crop_y = Integer.valueOf(crop_y_et.getText().toString());
            int crop_width = Integer.valueOf(crop_width_et.getText().toString());
            int crop_height = Integer.valueOf(crop_height_et.getText().toString());
            bm = Bitmap.createBitmap(bm, crop_x, crop_y, crop_width, crop_height);
        }
        origin_iv.setImageBitmap(bm);

        // convert the image into a Mat variable
        Mat imageMat = new Mat(bm.getHeight(), bm.getWidth(), CvType.CV_8U, new Scalar(4));
        Utils.bitmapToMat(bm, imageMat);
        image_reso_tv.setText("图像分辨率：" + imageMat.size());

        // get the hsv image Mat
        Mat hsvMat = new Mat();
        Mat threshold1Mat = new Mat();
        Mat threshold2Mat = new Mat();
        Mat thresholdMat = new Mat();
        Mat resultMat = new Mat();
        Imgproc.cvtColor(imageMat, hsvMat, Imgproc.COLOR_RGB2HSV);
        // Core.inRange(hsvMat, new Scalar(160, 80, 30), new Scalar(180, 255, 255), thresholdMat);
        long tt1 = System.nanoTime();
        Core.inRange(hsvMat, new Scalar(0, 80, 100), new Scalar(15, 255, 255), threshold1Mat);
        Core.inRange(hsvMat, new Scalar(165, 80, 100), new Scalar(180, 255, 255), threshold2Mat);
        Core.bitwise_or(threshold1Mat, threshold2Mat, thresholdMat);
        long tt2 = System.nanoTime() - tt1;
        // get the grey scale image mat
        Mat greyMat = new Mat();
        Imgproc.cvtColor(imageMat, greyMat, Imgproc.COLOR_BGR2GRAY);
        // Imgproc.GaussianBlur(greyMat, greyMat, new Size(9, 9), 2, 2);

        // get the edge image mat
        // Mat edgeMat = new Mat();
        // Imgproc.Canny(greyMat, edgeMat, canny_threshold / 2, canny_threshold);

        // convert the Mat variable back to image
        Bitmap new_bm = Bitmap.createBitmap(imageMat.cols(), imageMat.rows(), Bitmap.Config.ARGB_8888);


        if (show_image_type == 0) {
            Utils.matToBitmap(imageMat, new_bm);
        } else {
            Utils.matToBitmap(thresholdMat, new_bm);
        }

        // run hough circles to find the ball
        Canvas canvas = new Canvas(new_bm);
        canvas.drawBitmap(new_bm, new Matrix(), null);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);
        Mat circleMat = new Mat();
        long startTime = System.nanoTime();
        Imgproc.HoughCircles(thresholdMat, circleMat, Imgproc.HOUGH_GRADIENT, accumelator_reso, 100, canny_threshold, accumelator_threshold, radius_lower, radius_higher);
        long consumingTime = System.nanoTime() - startTime + tt2;
        hough_tv.setText("Hough圆检测时间：" + String.valueOf(Math.round(consumingTime * 1.0 / 1000000 * 100) * 1.0 / 100) + "ms");
        circle_number_tv.setText("检测出圆的个数：" + String.valueOf(circleMat.cols()));
        double circleData[];
        float x, y, r;
        if (circleMat.cols() > 0) {
            circleData = circleMat.get(0, 0);
            x = (float) circleData[0];
            y = (float) circleData[1];
            r = (float) circleData[2];
            circle_result_tv.setText("检测出的圆坐标及半径：(" + String.valueOf(x) + ", " + String.valueOf(y) + ", " + String.valueOf(r) + ")");
            if (crop_image == 1) {
                crop_x_et.setText(String.valueOf(Math.round(x - r * 1.5)));
                crop_y_et.setText(String.valueOf(Math.round(y - r * 1.5)));
                crop_width_et.setText(String.valueOf(Math.round(r * 3)));
                crop_height_et.setText(String.valueOf(Math.round(r * 3)));
            }
        }
        for (int i = 0; i < circleMat.cols(); i++) {
            circleData = circleMat.get(0, i);
            x = (float) circleData[0];
            y = (float) circleData[1];
            r = (float) circleData[2];
            if (show_result_type == 2) {
                break;
            }
            canvas.drawArc(new RectF(x - r, y - r, x + r, y + r), 0, 360, false, paint);
            if (show_result_type == 0) {
                break;
            }
        }

        try {
            out = new FileOutputStream(folder + "output.png");
            new_bm.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        result_iv.setImageBitmap(new_bm);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test_open_cv, menu);
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
