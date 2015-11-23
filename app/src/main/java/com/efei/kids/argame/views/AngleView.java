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

public class AngleView extends FrameLayout {

    private Context mContext;
    private View mRoot;
    private Button update_btn;
    private EditText angle_yaw_et;
    private EditText angle_pitch_et;
    private EditText angle_roll_et;

    public AngleView(Context context) {
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
        mRoot = inflate.inflate(R.layout.view_angle, null);
        initControllerView(mRoot);
        return mRoot;
    }

    private void initControllerView(View v) {
        update_btn = (Button) v.findViewById(R.id.update_btn);
        angle_yaw_et = (EditText) v.findViewById(R.id.angle_yaw_et);
        angle_pitch_et = (EditText) v.findViewById(R.id.angle_pitch_et);
        angle_roll_et = (EditText) v.findViewById(R.id.angle_roll_et);

        update_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int yaw = Integer.valueOf(angle_yaw_et.getText().toString());
                int pitch = Integer.valueOf(angle_pitch_et.getText().toString());
                int roll = Integer.valueOf(angle_roll_et.getText().toString());

                ((MainActivity)mContext).setEulerAngleData(yaw, pitch, roll);
            }
        });
    }
}
