# WheelDialogFragment 从窗口下方滑入的对话框，可用于时间选择，城市选择等等
###### 效果图：
![image](https://github.com/GitPhoenix/WheelDialogFragment/blob/master/screen/wheelDialog.gif)

##### 具体应用：在Activity或者Fragment中
```
Bundle bundle = new Bundle();
bundle.putBoolean(WheelDialogFragment.DIALOG_BACK, false);
bundle.putBoolean(WheelDialogFragment.DIALOG_CANCELABLE, false);
bundle.putBoolean(WheelDialogFragment.DIALOG_CANCELABLE_TOUCH_OUT_SIDE, false);
bundle.putString(WheelDialogFragment.DIALOG_LEFT, "取消");
bundle.putString(WheelDialogFragment.DIALOG_RIGHT, "确定");
bundle.putStringArray(WheelDialogFragment.DIALOG_WHEEL, ResUtil.getStringArray(R.array.main_home_menu));

WheelDialogFragment dialogFragment = WheelDialogFragment.newInstance(WheelDialogFragment.class, bundle);
dialogFragment.setWheelDialogListener(new WheelDialogFragment.OnWheelDialogListener() {
    @Override
    public void onClickLeft(DialogFragment dialog, String value) {
        dialog.dismiss();
    }

    @Override
    public void onClickRight(DialogFragment dialog, String value) {
        dialog.dismiss();
        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onValueChanged(DialogFragment dialog, String value) {
        Log.i("", "current value: " + value);
    }
});

dialogFragment.show(getSupportFragmentManager(), "");
```
