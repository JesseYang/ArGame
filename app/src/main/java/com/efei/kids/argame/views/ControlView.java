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
import android.widget.RadioGroup;

import com.efei.kids.argame.MainActivity;
import com.efei.kids.argame.R;

public class ControlView extends FrameLayout {

    private Context mContext;
    private View mRoot;
    private Button update_btn;
    private EditText control_canny_threshold_et;
    private EditText control_accumelator_reso_et;
    private EditText control_accumelator_threshold_et;
    RadioGroup control_show_image_type_rg;
    RadioGroup control_image_range_rg;

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
        control_show_image_type_rg = (RadioGroup) v.findViewById(R.id.control_show_image_type_rg);
        control_image_range_rg = (RadioGroup) v.findViewById(R.id.control_image_range_rg);

        update_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int canny_threshold = Integer.valueOf(control_canny_threshold_et.getText().toString());
                int accumelator_reso = Integer.valueOf(control_accumelator_reso_et.getText().toString());
                int accumelator_threshold = Integer.valueOf(control_accumelator_threshold_et.getText().toString());

                int control_show_image_type_rb_id = control_show_image_type_rg.getCheckedRadioButtonId();
                View control_show_image_type_rb = control_show_image_type_rg.findViewById(control_show_image_type_rb_id);
                int control_show_image_type = control_show_image_type_rg.indexOfChild(control_show_image_type_rb);

                int control_image_range_rb_id = control_image_range_rg.getCheckedRadioButtonId();
                View control_image_range_rb = control_image_range_rg.findViewById(control_image_range_rb_id);
                int control_image_range = control_image_range_rg.indexOfChild(control_image_range_rb);

                ((MainActivity)mContext).setHoughCondition(canny_threshold, accumelator_reso, accumelator_threshold, control_show_image_type, control_image_range);
            }
        });
    }
}
