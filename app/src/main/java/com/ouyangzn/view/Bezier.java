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
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ouyangzn on 2016/10/12.<br/>
 * Description：
 */
public class Bezier extends View {
  private Paint mPaint;
  private int centerX, centerY;

  private PointF start, end, control1, control2;

  private boolean mode = true;

  public Bezier(Context context) {
    this(context, null);
  }

  public Bezier(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public Bezier(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public Bezier(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init();
  }

  public void setMode(boolean mode) {
    this.mode = mode;
  }

  private void init() {
    mPaint = new Paint();
    mPaint.setColor(Color.BLACK);
    mPaint.setStrokeWidth(8);
    mPaint.setStyle(Paint.Style.STROKE);
    mPaint.setTextSize(60);

    start = new PointF(0, 0);
    end = new PointF(0, 0);
    control1 = new PointF(0, 0);
    control2 = new PointF(0, 0);
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    centerX = w / 2;
    centerY = h / 2;

    // 初始化数据点和控制点的位置
    start.x = centerX;
    start.y = centerY;
    end.x = centerX;
    end.y = centerY - 20;
    control1.x = centerX;
    control1.y = centerY - 100;
    control2.x = centerX;
    control2.y = centerY - 100;
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    // 根据触摸位置更新控制点，并提示重绘
    if (mode) {
      control1.x = event.getX();
      control1.y = event.getY();
    } else {
      control2.x = event.getX();
      control2.y = event.getY();
    }
    invalidate();
    return true;
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    // 绘制数据点和控制点
    mPaint.setColor(Color.GRAY);
    mPaint.setStrokeWidth(20);
    canvas.drawPoint(start.x, start.y, mPaint);
    canvas.drawPoint(end.x, end.y, mPaint);
    canvas.drawPoint(control1.x, control1.y, mPaint);
    canvas.drawPoint(control2.x, control2.y, mPaint);

    // 绘制辅助线
    mPaint.setStrokeWidth(4);
    canvas.drawLine(start.x, start.y, control1.x, control1.y, mPaint);
    canvas.drawLine(control1.x, control1.y, control2.x, control2.y, mPaint);
    canvas.drawLine(control2.x, control2.y, end.x, end.y, mPaint);

    // 绘制贝塞尔曲线
    mPaint.setColor(Color.RED);
    mPaint.setStrokeWidth(8);

    Path path = new Path();

    path.moveTo(start.x, start.y);
    path.cubicTo(control1.x, control1.y, control2.x, control2.y, end.x, end.y);

    canvas.drawPath(path, mPaint);
  }
}
