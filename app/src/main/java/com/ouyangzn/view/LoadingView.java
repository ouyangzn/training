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
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import com.ouyangzn.lib.utils.ScreenUtils;

/**
 * Created by ouyangzn on 2016/10/12.<br/>
 * Description：
 */
public class LoadingView extends View {

  private static final int MIN_WIDTH = 150;
  private static final int MIN_HEIGHT = 150;

  private Paint mArcPaint;

  public LoadingView(Context context) {
    this(context, null);
  }

  public LoadingView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public LoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init();
  }

  private void init() {
    mArcPaint = new Paint();
    mArcPaint.setColor(0xffffffff);
    mArcPaint.setAntiAlias(true);
    mArcPaint.setStyle(Paint.Style.STROKE);
    mArcPaint.setStrokeWidth(ScreenUtils.dp2px(getContext(), 13));
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int width = measureWidth(widthMeasureSpec);
    int height = measureHeight(heightMeasureSpec);
    setMeasuredDimension(width, height);
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

  @Override protected void onDraw(Canvas canvas) {
    drawArc(canvas);
  }

  private void drawArc(Canvas canvas) {
    //float x = (getWidth() - getHeight() / 2) / 2;
    float x = getWidth() / 4;
    float y = getHeight() / 4;

    RectF oval = new RectF(x, y, getWidth() - x, getHeight() - y);
    mArcPaint.setColor(Color.BLUE);
    canvas.drawArc(oval, 180, 140, false, mArcPaint);
    mArcPaint.setColor(Color.YELLOW);
    canvas.drawArc(oval, 0, 140, false, mArcPaint);
    mArcPaint.setColor(0x88000000);
    canvas.drawArc(oval, -90, 120, false, mArcPaint);
    mArcPaint.setColor(0x88ff0000);
    canvas.drawArc(oval, 90, 120, false, mArcPaint);

    //RectF rectF = new RectF();
    //mArcPaint.setColor(0xff888888);
    //rectF.left = getWidth() / 2;
    //rectF.top = 30;
    //rectF.right = rectF.left + 100;
    //rectF.bottom = rectF.top + ScreenUtils.dp2px(getContext(), 94);
    //canvas.drawArc(rectF, 200f,200f, false, mArcPaint);
    //mArcPaint.setColor(0xffffffff);
    //rectF.left = getWidth() / 3;
    //rectF.top = 0;
    //rectF.right = rectF.left+100;
    //rectF.bottom = rectF.top + ScreenUtils.dp2px(getContext(), 94);
    //canvas.drawArc(rectF, 50f,200f, false, mArcPaint);
  }
}
