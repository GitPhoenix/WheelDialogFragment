package com.github.phoenix.widget.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.github.phoenix.R;
import com.github.phoenix.utils.ResUtil;
import com.github.phoenix.utils.ScreenUtil;
import com.github.phoenix.widget.WheelView;


/**
 * 滑动选择对话框
 *
 * @author Phoenix
 * @date 2016-10-8 13:54
 */
public class WheelDialogFragment extends DialogFragment implements WheelView.OnValueChangeListener {
    private static final String DIALOG_LEFT = "dialog_left";
    private static final String DIALOG_RIGHT = "dialog_right";
    private static final String DIALOG_WHEEL = "dialog_wheel";
    private static final String DIALOG_BACK = "dialog_back";
    private static final String DIALOG_CANCELABLE = "dialog_cancelable";
    private static final String DIALOG_CANCELABLE_TOUCH_OUT_SIDE = "dialog_cancelable_touch_out_side";

    private Activity activity;
    private View view;
    private WheelView wheelView;
    private TextView tvLeft, tvRight;

    private OnWheelDialogListener onWheelDialogListener;

    private String[] dialogWheel;
    private String dialogLeft, dialogRight;
    private boolean isCancelableTouchOutSide, isCancelable, isBack;

    /**
     * 滑动选择对话框
     *
     * @param wheel 需要滑动选择的内容，数组格式
     * @param left  左边按钮
     * @param right 右边按钮
     * @param isBack 点击返回键是否消失
     * @param isCancelable 是否取消
     * @param isCancelableTouchOutSide  单击外部是否消失
     * @return
     */
    public static WheelDialogFragment newInstance(String[] wheel, String left, String right, boolean isBack, boolean isCancelable, boolean isCancelableTouchOutSide) {
        WheelDialogFragment wheelDialog = new WheelDialogFragment();

        Bundle bundle = new Bundle();
        bundle.putStringArray(DIALOG_WHEEL, wheel);
        bundle.putString(DIALOG_LEFT, left);
        bundle.putString(DIALOG_RIGHT, right);
        bundle.putBoolean(DIALOG_BACK, isBack);
        bundle.putBoolean(DIALOG_CANCELABLE, isCancelable);
        bundle.putBoolean(DIALOG_CANCELABLE_TOUCH_OUT_SIDE, isCancelableTouchOutSide);
        wheelDialog.setArguments(bundle);

        return wheelDialog;
    }

    /**
     * 滑动选择对话框, 默认左边按钮为取消， 右边为确定，点击返回键不消失，触摸外部不消失
     * @param wheel 需要滑动选择的内容，数组格式
     * @return
     */
    public static WheelDialogFragment newInstance(String[] wheel) {
        return newInstance(wheel, "取消", "确定", true, false, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置对话框显示在底部
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        //设置让对话框宽度充满屏幕
        getDialog().getWindow().setLayout(ScreenUtil.getScreenWidth(activity),
                getDialog().getWindow().getAttributes().height);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        dialogWheel = bundle.getStringArray(DIALOG_WHEEL);
        dialogLeft = bundle.getString(DIALOG_LEFT);
        dialogRight = bundle.getString(DIALOG_RIGHT);
        isBack = bundle.getBoolean(DIALOG_BACK, false);
        isCancelable = bundle.getBoolean(DIALOG_CANCELABLE, false);
        isCancelableTouchOutSide = bundle.getBoolean(DIALOG_CANCELABLE_TOUCH_OUT_SIDE, false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.view_dialog_wheel, null);

        initView();

        //设置窗口以对话框样式显示
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog);
        //设置对话框背景色，否则有虚框
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //设置对话框弹出动画，从底部滑入，从底部滑出
        getDialog().getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        getDialog().setCancelable(isCancelable);
        getDialog().setCanceledOnTouchOutside(isCancelableTouchOutSide);

        setSubView();
        initEvent();

        return view;
    }

    private void initEvent() {
        //左边按钮
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onWheelDialogListener != null) {
                    onWheelDialogListener.onClickLeft(getWheelValue());
                }
            }
        });

        //右边按钮
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onWheelDialogListener != null) {
                    onWheelDialogListener.onClickRight(getWheelValue());
                }
            }
        });

        //监听返回键
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
                    return isBack;

                return false;
            }
        });

    }

    private void setSubView() {
        tvLeft.setText(dialogLeft);
        tvRight.setText(dialogRight);

        wheelView.refreshByNewDisplayedValues(dialogWheel);
        //设置是否可以上下无限滑动
        wheelView.setWrapSelectorWheel(false);
        wheelView.setDividerColor(ResUtil.getColor(R.color.colorPrimary));
        wheelView.setSelectedTextColor(ResUtil.getColor(R.color.colorPrimary));
        wheelView.setNormalTextColor(ResUtil.getColor(R.color.default_text_color_normal));
    }

    private void initView() {
        tvLeft = (TextView) view.findViewById(R.id.tv_wheel_dialog_left);
        tvRight = (TextView) view.findViewById(R.id.tv_wheel_dialog_right);
        wheelView = (WheelView) view.findViewById(R.id.WheelView_dialog);
    }

    /**
     * 获取当前值
     * @return
     */
    private String getWheelValue() {
        String[] content = wheelView.getDisplayedValues();
        return content == null ? null : content[wheelView.getValue() - wheelView.getMinValue()];
    }

    @Override
    public void onValueChange(WheelView picker, int oldVal, int newVal) {
        String[] content = wheelView.getDisplayedValues();
        if (content != null && onWheelDialogListener != null) {
            onWheelDialogListener.onValueChanged(content[newVal - wheelView.getMinValue()]);
        }
    }

    public interface OnWheelDialogListener {
        /**
         * 左边按钮单击事件回调
         * @param value
         */
        void onClickLeft(String value);
        /**
         * 右边按钮单击事件回调
         * @param value
         */
        void onClickRight(String value);

        /**
         * 滑动停止时的回调
         * @param value
         */
        void onValueChanged(String value);
    }

    /**
     * 对外开放的方法
     * @param onWheelDialogListener
     */
    public void setWheelDialogListener(OnWheelDialogListener onWheelDialogListener) {
        this.onWheelDialogListener = onWheelDialogListener;
    }
}
