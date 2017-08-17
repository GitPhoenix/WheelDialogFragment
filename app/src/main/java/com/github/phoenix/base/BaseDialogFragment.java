package com.github.phoenix.base;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.github.phoenix.R;


/**
 * 所有自定义对话框的基类
 *
 * @author Phoenix
 * @date 2016-12-4 9:13
 */
public abstract class BaseDialogFragment extends DialogFragment {
    protected String TAG = getClass().getSimpleName();

    /**
     * 点击返回键是否消失
     */
    public static final String DIALOG_BACK = "dialog_back";
    /**
     * 是否取消
     */
    public static final String DIALOG_CANCELABLE = "dialog_cancelable";
    /**
     * 点击外部是否消失
     */
    public static final String DIALOG_CANCELABLE_TOUCH_OUT_SIDE = "dialog_cancelable_touch_out_side";

    protected Bundle bundle;
    protected Activity activity;

    protected boolean isCancelableTouchOutSide, isCancelable, isBack;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    /**
     * 返回当前DialogFragment的实例，使用Bundle传递参数
     *
     * @param clazz 当前DialogFragment.class
     * @param args  携带参数的Bundle对象
     * @param <T>   返回类型参数控制，必须是BaseDialogFragment的子类
     * @return 当前Fragment的实例
     */
    public static <T extends BaseDialogFragment> T newInstance(Class clazz, Bundle args) {
        T fragment = null;
        try {
            fragment = (T) clazz.newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        if (bundle == null) {
            throw new NullPointerException(TAG + "——>BaseDialogFragment: bundle == null");
        }
        isBack = bundle.getBoolean(DIALOG_BACK, false);
        isCancelable = bundle.getBoolean(DIALOG_CANCELABLE, false);
        isCancelableTouchOutSide = bundle.getBoolean(DIALOG_CANCELABLE_TOUCH_OUT_SIDE, false);

        Log.i(TAG, "isBack =" + isBack + " isCancelable =" + isCancelable + " isCancelableTouchOutSide =" + isCancelableTouchOutSide);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View rootView = inflater.inflate(getLayoutID(), container, false);
        initView(rootView);
        setSubView();
        initEvent();
        onBackPressed();

        return rootView;
    }

    /**
     * @return LayoutId
     */
    protected abstract int getLayoutID();

    /**
     * 初始化对话框视图
     *
     * @param view
     */
    protected abstract void initView(View view);

    /**
     * 设置视图
     */
    protected abstract void setSubView();

    /**
     * 初始化事件监听
     */
    protected abstract void initEvent();

    /**
     * 当对话框消失时的监听事件
     */
    protected abstract void onCancel();

    /**
     * 监听返回键
     */
    private void onBackPressed() {
        //设置窗口以对话框样式显示
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Dialog);
        //设置对话框背景色，否则有虚框
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCancelable(isCancelable);
        getDialog().setCanceledOnTouchOutside(isCancelableTouchOutSide);

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (isBack) {
                        onCancel();
                    }
                    return !isBack;
                }

                return false;
            }
        });
    }
}
