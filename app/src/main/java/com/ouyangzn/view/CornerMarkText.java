/*
 * Copyright (c) 2016.  ouyangzn   <ouyangzn@163.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ouyangzn.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import com.ouyangzn.R;
import com.ouyangzn.lib.utils.ScreenUtils;
import com.ouyangzn.utils.Log;

/**
 * Created by ouyangzn on 2016/9/14.<br/>
 * Description：角标textView
 * eg:
 * **********************
 * *            *  n    *
 * *              *  e  *
 * *                *  w*
 * *                  * *
 * **********************
 */
public class CornerMarkText extends View {

  private final static String TAG = CornerMarkText.class.getSimpleName();

  private int mMinSize;

  private Paint mTextPaint;
  private Paint mTextBgPaint;
  private String mText;
  private float mTextSize;
  private int mTextColor;
  private int mBgColor;

  public CornerMarkText(Context context) {
    this(context, null);
  }

  public CornerMarkText(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CornerMarkText(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initDefaultValue(context);
    init(context, attrs);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public CornerMarkText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    initDefaultValue(context);
    init(context, attrs);
  }

  private void initDefaultValue(Context context) {
    mMinSize = ScreenUtils.dp2px(context, 20);
  }

  public void setText(String text) {
    mText = text;
    invalidate();
  }

  public void setMinSize(int minSize) {
    this.mMinSize = minSize;
  }

  private void init(Context context, AttributeSet attrs) {
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CornerMarkText);
    mBgColor = ta.getColor(R.styleable.CornerMarkText_bg_color, 0x8000ff00);
    mText = ta.getString(R.styleable.CornerMarkText_text);
    mTextSize = ta.getDimensionPixelSize(R.styleable.CornerMarkText_text_size, ScreenUtils.sp2px(context, 10));
    mTextColor = ta.getColor(R.styleable.CornerMarkText_text_color, getResources().getColor(R.color.colorAccent));
    ta.recycle();
    int padding = ScreenUtils.dp2px(context, 2);
    setPadding(padding, padding, padding, padding);
    initTextPaint();
    initTextBgPaint();
  }

  private void initTextBgPaint() {
    mTextBgPaint = new Paint();
    mTextBgPaint.setColor(mBgColor);
    mTextBgPaint.setStyle(Paint.Style.FILL);
    mTextBgPaint.setStrokeWidth(mTextSize);
    // 抗锯齿
    mTextBgPaint.setAntiAlias(true);
    Log.d(TAG, "----------ascent = " + mTextPaint.ascent() + " ,descent = " + mTextPaint.descent());
    Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
    Log.d(TAG, "----------bottom = "
        + fontMetrics.bottom
        + " ,top = "
        + fontMetrics.top
        + " ,descent = "
        + fontMetrics.descent
        + " ,ascent = "
        + fontMetrics.ascent);
  }

  private void initTextPaint() {
    mTextPaint = new Paint();
    mTextPaint.setColor(mTextColor);
    mTextPaint.setTextSize(mTextSize);
    // 抗锯齿
    mTextPaint.setAntiAlias(true);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    if (TextUtils.isEmpty(mText)) {
      setMeasuredDimension(0, 0);
      return;
    }

    int size = measureWidthHeight(widthMeasureSpec, heightMeasureSpec);
    //int size = measureSize(widthMeasureSpec, heightMeasureSpec);
    if (size < mMinSize) size = mMinSize;
    setMeasuredDimension(size, size);
  }

  private int measureSize(int widthMeasureSpec, int heightMeasureSpec) {
    int width = measureWidth(widthMeasureSpec);
    int height = measureHeight(heightMeasureSpec);
    return Math.max(width, height);
  }

  private int measureWidthHeight(int widthMeasureSpec, int heightMeasureSpec) {
    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
    int heightSize = MeasureSpec.getSize(heightMeasureSpec);

    int width;
    int height;
    if (widthMode == MeasureSpec.EXACTLY) {
      // Parent has told us how big to be. So be it.
      width = widthSize;
      Log.d(TAG, "----------onMeasure.widthMode = EXACTLY , mText = " + mText + ", width = " + width);
    } else {
      width = (int) mTextPaint.measureText(mText) + getPaddingRight() + getPaddingLeft();
      Log.d(TAG, "----------onMeasure.widthMode ≠ EXACTLY , mText = " + mText + " ,measureTextWidth = " + (width - 12));
      if (widthMode == MeasureSpec.AT_MOST) {
        width = Math.min(widthSize, width);
      }
    }
    if (heightMode == MeasureSpec.EXACTLY) {
      // Parent has told us how big to be. So be it.
      height = heightSize;
    } else {
      //height = (int) mTextPaint.descent() + getPaddingTop() + getPaddingBottom();
      height = (int) mTextSize + getPaddingTop() + getPaddingBottom();
      //Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
      //height = (int) (fontMetrics.bottom - fontMetrics.top + getPaddingTop() + getPaddingBottom());
      if (heightMode == MeasureSpec.AT_MOST) {
        height = Math.min(height, heightSize);
      }
    }
    Log.d(TAG, "-----------onMeasure.text = " + mText + " ,width = " + width + " ,height = " + height);
    return Math.max(width, height);
  }

  private int measureWidth(int measureSpec) {
    int result = 0;
    int specMode = MeasureSpec.getMode(measureSpec);
    int specSize = MeasureSpec.getSize(measureSpec);

    if (specMode == MeasureSpec.EXACTLY) {
      // We were told how big to be
      result = specSize;
    } else {
      // Measure the text
      result = (int) mTextPaint.measureText(mText) + getPaddingLeft()
          + getPaddingRight();
      if (specMode == MeasureSpec.AT_MOST) {
        // Respect AT_MOST value if that was what is called for by measureSpec
        result = Math.min(result, specSize);
      }
    }

    return result;
  }

  private int measureHeight(int measureSpec) {
    int result = 0;
    int specMode = MeasureSpec.getMode(measureSpec);
    int specSize = MeasureSpec.getSize(measureSpec);

    int ascent = (int) mTextPaint.ascent();
    if (specMode == MeasureSpec.EXACTLY) {
      // We were told how big to be
      result = specSize;
    } else {
      // Measure the text (beware: ascent is a negative number)
      result = (int) (-ascent + mTextPaint.descent()) + getPaddingTop()
          + getPaddingBottom();
      if (specMode == MeasureSpec.AT_MOST) {
        // Respect AT_MOST value if that was what is called for by measureSpec
        result = Math.min(result, specSize);
      }
    }
    return result;
  }

  @Override protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
  }

  @Override protected void onDraw(Canvas canvas) {
    if (TextUtils.isEmpty(mText)) return;

    drawTextBg(canvas);
    drawText(canvas);
  }

  private void drawTextBg(Canvas canvas) {
    canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, mTextBgPaint);
    //canvas.drawLine(0, 0, getWidth(), getHeight(), mTextBgPaint);
    // x坐标 从正中间往右上移动mTextSize的距离 == 往右移动 mTextSize / 1.5 的距离
    // y坐标
    //canvas.drawLine(0 + mTextSize * 2 / 3, 0, getWidth() + mTextSize * 2 / 3, getHeight(), mTextBgPaint);
  }

  private void drawText(Canvas canvas) {
    canvas.save();
    // 从正中间旋转45°
    //canvas.rotate(45, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
    //canvas.rotate(45, getMeasuredWidth() / 2 + mTextSize / 2, getMeasuredHeight() / 2 + mTextSize / 2);
    // 文字画在正中间
    int width = getMeasuredWidth();
    width -= mTextPaint.measureText(mText);
    width = width / 2;
    float height = (getMeasuredHeight() - getPaddingBottom() - getPaddingTop()) / 2 + mTextSize / 2;
    canvas.drawText(mText, width, height, mTextPaint);
    canvas.restore();
  }

}
