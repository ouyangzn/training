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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import com.ouyangzn.lib.utils.ScreenUtils;

/**
 * Created by ouyangzn on 2016/10/12.<br/>
 * Description：
 */
public class LoadingView3 extends View {

  private static final int MIN_WIDTH = 65;
  private static final int MIN_HEIGHT = 65;

  private Paint mBgPaint;
  private Paint mProgressPaint;
  private float mRotateDegree = 0;

  public LoadingView3(Context context) {
    this(context, null);
  }

  public LoadingView3(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public LoadingView3(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    mBgPaint = new Paint();
    mBgPaint.setColor(Color.WHITE);
    mBgPaint.setAntiAlias(true);
    mBgPaint.setDither(true);
    mBgPaint.setStyle(Paint.Style.STROKE);
    mBgPaint.setStrokeWidth(ScreenUtils.dp2px(getContext(), 4));

    mProgressPaint = new Paint();
    mProgressPaint.setColor(Color.RED);
    mProgressPaint.setAntiAlias(true);
    mProgressPaint.setDither(true);
    mProgressPaint.setStyle(Paint.Style.STROKE);
    mProgressPaint.setStrokeWidth(ScreenUtils.dp2px(getContext(), 4));
    // 设置边角为圆角
    mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int width = measureWidth(widthMeasureSpec);
    int height = measureHeight(heightMeasureSpec);
    int size = Math.max(width, height);
    setMeasuredDimension(size, size);
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    int paddingLeft = getPaddingLeft();
    int paddingRight = getPaddingRight();
    int paddingTop = getPaddingTop();
    int paddingBottom = getPaddingBottom();
    // 使用渐变色，setShader与setColor不能共存
    mProgressPaint.setColor(-1);
    mProgressPaint.setShader(
        new LinearGradient(paddingLeft, paddingTop, w - paddingRight, h - paddingBottom,
            Color.TRANSPARENT, Color.RED, Shader.TileMode.CLAMP));
  }

  @Override protected void onDraw(Canvas canvas) {
    drawCircle(canvas);
  }

  private void drawCircle(Canvas canvas) {
    int paddingTop = getPaddingTop();
    int paddingBottom = getPaddingBottom();
    int paddingLeft = getPaddingLeft();
    int paddingRight = getPaddingRight();
    // 圆弧
    RectF oval =
        new RectF(paddingLeft, paddingTop, getWidth() - paddingRight, getHeight() - paddingBottom);
    // drawArc时画笔会顺时针旋转来绘画
    // oval：定义可以画线的上下左右位置
    // startAngle：起始角度，x坐标轴的右边为0°
    // sweepAngle：画笔扫过的角度，以startAngle计算出的点为起点
    // userCenter：true时会连接圆心，画出来扇形，false则不会连接圆心，画出来圆弧
    //canvas.drawArc(oval, 135, 270, false, mBgPaint);
    //canvas.drawArc(oval, 135, 90, false, mBgPaint);
    //canvas.drawArc(oval, 90, 270, false, mBgPaint);
    // -----------以下4条圆弧可组成圆
    canvas.drawArc(oval, 0, 90, false, mBgPaint);
    canvas.drawArc(oval, 90, 90, false, mBgPaint);
    canvas.drawArc(oval, 180, 90, false, mBgPaint);
    canvas.drawArc(oval, 270, 90, false, mBgPaint);
    // 直接画一个圆
    //canvas.drawArc(oval, 0, 360, false, mBgPaint);
    // 画圆的上半部分，圆形进度条50%的进度所需画的角度 = 50 / 100 * 360°
    // 不能与其他的公用paint，会导致其他已绘制图形出现问题
    //canvas.save();
    canvas.rotate(mRotateDegree * 10, getWidth() / 2, getHeight() / 2);
    canvas.drawArc(oval, 180, 180, false, mProgressPaint);
    //canvas.restore();
    postDelayed(new Runnable() {
      @Override public void run() {
        if (mRotateDegree > 360) mRotateDegree = 0;
        mRotateDegree++;
        invalidate();
      }
    }, 10);
  }

  private int measureHeight(int heightMeasureSpec) {
    int mode = MeasureSpec.getMode(heightMeasureSpec);
    int size = MeasureSpec.getSize(heightMeasureSpec);
    int minHeight = ScreenUtils.dp2px(getContext(), MIN_HEIGHT);
    int height;
    if (mode == MeasureSpec.EXACTLY) {
      height = size;
    } else {
      height = minHeight;
      if (mode == MeasureSpec.AT_MOST) {
        height = Math.min(height, size);
      }
    }
    if (height < minHeight) height = minHeight;
    return height;
  }

  private int measureWidth(int widthMeasureSpec) {
    int mode = MeasureSpec.getMode(widthMeasureSpec);
    int size = MeasureSpec.getSize(widthMeasureSpec);
    int minWidth = ScreenUtils.dp2px(getContext(), MIN_WIDTH);
    int width;
    if (mode == MeasureSpec.EXACTLY) {
      width = size;
    } else {
      width = minWidth;
      if (mode == MeasureSpec.AT_MOST) {
        width = Math.min(size, width);
      }
    }
    if (width < minWidth) width = minWidth;
    return width;
  }
}
