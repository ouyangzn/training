/*
 * Copyright (c) 2016.  ouyangzn   <email : ouyangzn@163.com>
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
 *
 */

package com.ouyangzn.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import com.ouyangzn.R;

/**
 * Created by ouyangzn on 2016/10/12.<br/>
 * Description：
 */
public class LoadingView5 extends View {

  //private static final int MIN_WIDTH = 130;
  //private static final int MIN_HEIGHT = 130;

  private Paint mPaint;

  private Bitmap mBgBitmap;
  private Bitmap mLightingBitmap;
  private int mRotateDegree = 0;

  public LoadingView5(Context context) {
    this(context, null);
  }

  public LoadingView5(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public LoadingView5(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public LoadingView5(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init();
  }

  private void init() {
    mPaint = new Paint();
    mPaint.setColor(Color.WHITE);
    mPaint.setAntiAlias(true);
    mPaint.setStyle(Paint.Style.STROKE);

    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inPreferredConfig = Bitmap.Config.RGB_565;
    mBgBitmap =
        BitmapFactory.decodeResource(getResources(), R.drawable.ic_loading_bg_circle, options);
    mLightingBitmap =
        BitmapFactory.decodeResource(getResources(), R.drawable.ic_lightning, options);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int width = measureWidth(widthMeasureSpec);
    int height = measureHeight(heightMeasureSpec);
    int size = Math.max(width, height);
    setMeasuredDimension(size, size);
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
  }

  private int measureHeight(int heightMeasureSpec) {
    int mode = MeasureSpec.getMode(heightMeasureSpec);
    int size = MeasureSpec.getSize(heightMeasureSpec);
    //int minHeight = ScreenUtils.dp2px(getContext(), MIN_HEIGHT);
    int height;
    if (mode == MeasureSpec.EXACTLY) {
      height = size;
    } else {
      height = mBgBitmap.getHeight() + getPaddingBottom() + getPaddingTop();
      if (mode == MeasureSpec.AT_MOST) {
        height = Math.min(height, size);
      }
    }
    //if (height < minHeight) height = minHeight;
    return height;
  }

  private int measureWidth(int widthMeasureSpec) {
    int mode = MeasureSpec.getMode(widthMeasureSpec);
    int size = MeasureSpec.getSize(widthMeasureSpec);
    //int minWidth = ScreenUtils.dp2px(getContext(), MIN_WIDTH);
    int width;
    if (mode == MeasureSpec.EXACTLY) {
      width = size;
    } else {
      width = mBgBitmap.getWidth() + getPaddingRight() + getPaddingLeft();
      if (mode == MeasureSpec.AT_MOST) {
        width = Math.min(size, width);
      }
    }
    //if (width < minWidth) width = minWidth;
    return width;
  }

  @Override protected void onDraw(Canvas canvas) {
    drawCircle(canvas);
  }

  private void drawCircle(Canvas canvas) {
    float x = (getWidth() - mLightingBitmap.getWidth()) / 2;
    float y = (getHeight() - mLightingBitmap.getHeight()) / 2;
    canvas.drawBitmap(mLightingBitmap, x, y, mPaint);
    // 存为新图层
    int saveLayerCount =
        canvas.saveLayer(0, 0, getWidth(), getHeight(), mPaint, Canvas.ALL_SAVE_FLAG);
    x = (getWidth() - mBgBitmap.getWidth()) / 2;
    y = (getHeight() - mBgBitmap.getHeight()) / 2;
    resetPaint();
    canvas.drawBitmap(mBgBitmap, x, y, mPaint);
    float cx = mBgBitmap.getWidth() / 4 - (mBgBitmap.getHeight() - mBgBitmap.getWidth()) / 2;
    float cy = mBgBitmap.getHeight() / 2;
    resetPaint();
    mPaint.setColor(Color.RED);
    mPaint.setStyle(Paint.Style.FILL);
    //保留相交部分及底层图片
    mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
    canvas.save();
    canvas.rotate(mRotateDegree * 10, mBgBitmap.getWidth() / 2, mBgBitmap.getHeight() / 2);
    float radius = Math.max(mBgBitmap.getWidth(), mBgBitmap.getHeight()) / 3;
    canvas.drawCircle(cx, cy, radius, mPaint);
    // 恢复保存的图层；
    canvas.restoreToCount(saveLayerCount);

    int delay = 10;
    if (mRotateDegree > 180) delay = 150;
    postDelayed(new Runnable() {
      @Override public void run() {
        mRotateDegree++;
        if (mRotateDegree > 36) mRotateDegree = 0;
        invalidate();
      }
    }, delay);
  }

  private void resetPaint() {
    mPaint.reset();
    mPaint.setColor(Color.WHITE);
    mPaint.setAntiAlias(true);
    mPaint.setDither(true);
    mPaint.setStyle(Paint.Style.STROKE);
  }
}
