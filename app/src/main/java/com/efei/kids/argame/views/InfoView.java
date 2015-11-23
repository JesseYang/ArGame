package com.efei.kids.argame.views;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.efei.kids.argame.data.HoughResultData;
import com.efei.kids.argame.R;

public class InfoView extends FrameLayout {

    private Context mContext;
    private View mRoot;

    private TextView internal_frame_time_tv;
    private TextView hough_number_tv;
    private TextView x_tv;
    private TextView y_tv;
    private TextView r_tv;
    private TextView width_tv;
    private TextView height_tv;
    private TextView info_hough_time_tv;
    private TextView info_function_time_tv;
    private LinearLayout info_view_wrapper;

    public InfoView(Context context) {
        super(context);
        mContext = context;
    }

    public void setAnchorView() {

        LayoutParams frameParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        // frameParams.gravity = Gravity.;

        View v = makeControllerView();
        ((Activity)mContext).addContentView(v, frameParams);
    }

    protected View makeControllerView() {
        LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRoot = inflate.inflate(R.layout.view_info, null);
        initControllerView(mRoot);
        return mRoot;
    }

    private void initControllerView(View v) {
        internal_frame_time_tv = (TextView) v.findViewById(R.id.internal_frame_time_tv);
        hough_number_tv = (TextView) v.findViewById(R.id.hough_number_tv);
        x_tv = (TextView) v.findViewById(R.id.x_tv);
        y_tv = (TextView) v.findViewById(R.id.y_tv);
        r_tv = (TextView) v.findViewById(R.id.r_tv);
        width_tv = (TextView) v.findViewById(R.id.width_tv);
        height_tv = (TextView) v.findViewById(R.id.height_tv);
        info_hough_time_tv = (TextView) v.findViewById(R.id.info_hough_time_tv);
        info_function_time_tv = (TextView) v.findViewById(R.id.info_function_time_tv);
        info_view_wrapper = (LinearLayout) v.findViewById(R.id.info_view_wrapper);
    }

    public void setInternalTime(long internalTime) {
        internal_frame_time_tv.setText(mContext.getString(R.string.internal_frame_time_tv_prefix) + String.valueOf(internalTime / 1000000L));
    }

    public void setHoughResultData(HoughResultData data) {
        hough_number_tv.setText(mContext.getString(R.string.hough_number_tv_prefix) + String.valueOf(data.circleNumber));
        x_tv.setText(mContext.getString(R.string.x_tv_prefix) + String.valueOf(data.x));
        y_tv.setText(mContext.getString(R.string.y_tv_prefix) + String.valueOf(data.y));
        r_tv.setText(mContext.getString(R.string.r_tv_prefix) + String.valueOf(data.r));
        width_tv.setText(mContext.getString(R.string.width_tv_prefix) + String.valueOf(data.width));
        height_tv.setText(mContext.getString(R.string.height_tv_prefix) + String.valueOf(data.height));
        info_hough_time_tv.setText(mContext.getString(R.string.info_hough_time_tv_prefix) + String.valueOf(data.houghTime));
        info_function_time_tv.setText(mContext.getString(R.string.info_function_time_tv_prefix) + String.valueOf(data.functionTime));
    }
}
