package com.efei.kids.argame.views;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.efei.kids.argame.MainActivity;
import com.efei.kids.argame.R;

public class ControlView extends FrameLayout {

    private Context mContext;
    private View mRoot;
    private Button update_btn;
    private EditText control_canny_threshold_et;
    private EditText control_accumelator_reso_et;
    private EditText control_accumelator_threshold_et;

    public ControlView(Context context) {
        super(context);
        mContext = context;
    }

    public void setAnchorView() {

        LayoutParams frameParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        frameParams.gravity = Gravity.RIGHT;

        View v = makeControllerView();
        ((Activity)mContext).addContentView(v, frameParams);
    }

    protected View makeControllerView() {
        LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRoot = inflate.inflate(R.layout.view_control, null);
        initControllerView(mRoot);
        return mRoot;
    }

    private void initControllerView(View v) {
        update_btn = (Button) v.findViewById(R.id.update_btn);
        control_canny_threshold_et = (EditText) v.findViewById(R.id.control_canny_threshold_et);
        control_accumelator_reso_et = (EditText) v.findViewById(R.id.control_accumelator_reso_et);
        control_accumelator_threshold_et = (EditText) v.findViewById(R.id.control_accumelator_threshold_et);

        update_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int canny_threshold = Integer.valueOf(control_canny_threshold_et.getText().toString());
                int accumelator_reso = Integer.valueOf(control_accumelator_reso_et.getText().toString());
                int accumelator_threshold = Integer.valueOf(control_accumelator_threshold_et.getText().toString());
                ((MainActivity)mContext).setHoughCondition(canny_threshold, accumelator_reso, accumelator_threshold);
            }
        });
    }
}
