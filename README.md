# WheelDialogFragment 从窗口下方滑入的对话框，可用于时间选择，城市选择等等
######效果图：
![image](https://github.com/GitPhoenix/WheelDialogFragment/blob/master/screen/wheelDialog.gif)

#####具体应用：在Activity或者Fragment中
```
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
               wheelViewDialogFragment.dismiss();                       
               Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onValueChanged(String value) {
               Log.i("", "current value: " + value);
           }
       });
    wheelViewDialogFragment.show(getSupportFragmentManager(), "");
```
