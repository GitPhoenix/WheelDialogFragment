package com.github.phoenix;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.phoenix.utils.ResUtil;
import com.github.phoenix.widget.dialog.WheelDialogFragment;

public class MainActivity extends AppCompatActivity {
    private TextView tvWheel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();
    }

    private void initEvent() {
        tvWheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final WheelDialogFragment wheelViewDialogFragment = WheelDialogFragment
                        .newInstance(ResUtil.getStringArray(R.array.main_home_menu),
                                ResUtil.getString(R.string.app_cancel),
                                ResUtil.getString(R.string.app_sure), true, false, false);
                wheelViewDialogFragment.setWheelDialogListener(new WheelDialogFragment.OnWheelDialogListener() {
                    @Override
                    public void onClickLeft(String value) {
                        wheelViewDialogFragment.dismiss();
                    }

                    @Override
                    public void onClickRight(String value) {
                        wheelViewDialogFragment.dismiss();                        Log.i("", value);
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onValueChanged(String value) {
                        Log.i("", "current value: " + value);
                    }
                });
                wheelViewDialogFragment.show(getSupportFragmentManager(), "");
            }
        });
    }

    private void initView() {
        tvWheel = (TextView) findViewById(R.id.tv_wheel);
    }
}
