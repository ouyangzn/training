package com.ouyangzn.module.testFlexboxLayout;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import com.google.android.flexbox.FlexboxLayout;
import com.ouyangzn.R;
import com.ouyangzn.base.BaseActivity;
import com.ouyangzn.lib.utils.ScreenUtils;
import java.util.Random;

public class FlexboxLayoutActivity extends BaseActivity implements View.OnClickListener {

  private FlexboxLayout mFlexboxLayout;
  private ScrollView mScrollView;
  private Random mRandom;

  @Override protected int getContentResId() {
    return R.layout.activity_flexbox_layout;
  }

  @Override protected void initData() {
    mRandom = new Random(100);

  }

  @Override protected void initView(Bundle savedInstanceState) {
    mFlexboxLayout = (FlexboxLayout) findViewById(R.id.layout_container);
    mScrollView = (ScrollView) findViewById(R.id.scrollView);
    findViewById(R.id.textview1).setOnClickListener(this);
    findViewById(R.id.textview2).setOnClickListener(this);
    findViewById(R.id.textview3).setOnClickListener(this);
    findViewById(R.id.btn_add).setOnClickListener(this);
    findViewById(R.id.btn_get_selected_tag).setOnClickListener(this);

  }

  @Override public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_add:
        mFlexboxLayout.addView(getTextView("add标签" + mRandom.nextInt()));
        mScrollView.post(new Runnable() {
          @Override public void run() {
            //mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            mScrollView.smoothScrollTo(mScrollView.getRight(), mScrollView.getBottom());
          }
        });
        break;
      case R.id.btn_get_selected_tag:

        String content = null;
        int count = mFlexboxLayout.getChildCount();
        if (count > 0) {
          StringBuilder sb = new StringBuilder("选中了：");
          TextView textView;
          boolean isSelected = false;
          for (int i = 0; i < count; i++) {
            textView = (TextView) mFlexboxLayout.getChildAt(i);
            if (textView.isSelected()) {
              sb.append(textView.getText()).append(", ");
              isSelected = true;
            }
          }
          if (isSelected) {
            content = sb.substring(0, sb.length() - 2);
          } else {
            content = "一个都没选";
          }
        }
        toast(content);
        break;
      case R.id.textview1:
      case R.id.textview2:
      case R.id.textview3:
      default:
        v.setSelected(!v.isSelected());
        break;
    }
  }

  private TextView getTextView(String text) {
    TextView textView = new TextView(getApplicationContext());
    FlexboxLayout.LayoutParams params =
        new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT,
            FlexboxLayout.LayoutParams.WRAP_CONTENT);
    params.rightMargin = ScreenUtils.dp2px(getApplicationContext(), 5);
    params.leftMargin = ScreenUtils.dp2px(getApplicationContext(), 5);
    params.topMargin = ScreenUtils.dp2px(getApplicationContext(), 5);
    params.bottomMargin = ScreenUtils.dp2px(getApplicationContext(), 5);
    textView.setGravity(Gravity.CENTER);
    textView.setText(text);
    textView.setBackgroundResource(R.drawable.shape_bg_tag);
    textView.setTextColor(getResources().getColorStateList(R.color.color_tag));
    textView.setLayoutParams(params);
    textView.setOnClickListener(this);
    return textView;
  }
}
