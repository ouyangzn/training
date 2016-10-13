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

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import com.ouyangzn.R;
import com.ouyangzn.utils.Log;

/**
 * Created by ouyangzn on 2016/10/12.<br/>
 * Description：拖动子view隐藏/显示的layout
 */
public class DragLayout extends FrameLayout {

  private final static String TAG = DragLayout.class.getSimpleName();

  private ViewDragHelper mDragHelper;
  /** 隐藏时遗留的高或宽 */
  private int mRemainDistance;
  /** 拖动隐藏显示的拖动距离临界值 */
  private int mThresholdDistance;

  public DragLayout(Context context) {
    this(context, null);
  }

  public DragLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public DragLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    mDragHelper = ViewDragHelper.create(this, new ViewDragCallback());
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TopDragLayout);
    mRemainDistance = ta.getDimensionPixelOffset(R.styleable.TopDragLayout_remain_height, 0);
    mThresholdDistance = ta.getDimensionPixelOffset(R.styleable.TopDragLayout_threshold_height, 0);
    ta.recycle();
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    Log.d(TAG, "---------onDraw---------");
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    Log.d(TAG, "---------onMeasure---------");
  }

  @Override protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    Log.d(TAG, "---------onLayout---------");
    // 高度改为包裹
    //ViewGroup.LayoutParams params = getLayoutParams();
    //if (params.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
    //  params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
    //  setLayoutParams(params);
    //}
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    Log.d(TAG, "---------onFinishInflate---------");
    // 只能有一个子view
    if (getChildCount() != 1) {
      throw new UnsupportedOperationException("more than 1 child is not allowed");
    }
  }

  @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
    boolean helpResult = mDragHelper.shouldInterceptTouchEvent(ev);
    boolean intercept = false;
    switch (MotionEventCompat.getActionMasked(ev)) {
      case MotionEvent.ACTION_MOVE:
        // 判断是否该拦截此事件自己消费--->模仿support-v4中的DrawerLayout处理方式
        intercept = mDragHelper.checkTouchSlop(ViewDragHelper.DIRECTION_VERTICAL);
        // 此时如果需要拦截事件，需要手动捕获拖动的view
        if (intercept) mDragHelper.captureChildView(this.getChildAt(0), ev.getPointerId(0));
        break;
    }
    return helpResult | intercept;
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    mDragHelper.processTouchEvent(event);
    return true;
  }

  @Override public void computeScroll() {
    if (mDragHelper.continueSettling(true)) {
      ViewCompat.postInvalidateOnAnimation(this);
    }
  }

  class ViewDragCallback extends ViewDragHelper.Callback {

    private boolean mSwitchVisible;
    private int mDragViewLeft;
    private int mDragViewTop;
    private int mDragViewBottom;

    @Override public boolean tryCaptureView(View child, int pointerId) {
      Log.d(TAG, "-----------tryCaptureView----------------");
      Log.d(TAG, "----------pointerId = " + pointerId + " ,child = " + child);
      return true;
    }

    @Override
    public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
      Log.d(TAG, "-----------onViewPositionChanged----------------");
      Log.d(TAG, "----------left = "
          + left
          + " ,top = "
          + top
          + " ,dx = "
          + dx
          + " ,dy = "
          + dy
          + " ,changedView = "
          + changedView);
      super.onViewPositionChanged(changedView, left, top, dx, dy);
      // 临界值没设置的，默认一半高为临界值
      if (mThresholdDistance == 0) {
        mSwitchVisible = (Math.abs(top - mDragViewTop) / (float) changedView.getHeight()) > 0.5;
      } else {
        mSwitchVisible = Math.abs(top - mDragViewTop) > mThresholdDistance;
      }
    }

    @Override public void onViewCaptured(View capturedChild, int activePointerId) {
      super.onViewCaptured(capturedChild, activePointerId);
      // 获取被拖动view的初始位置
      mDragViewLeft = capturedChild.getLeft();
      mDragViewTop = capturedChild.getTop();
      mDragViewBottom = capturedChild.getBottom();
      Log.d(TAG, "-----------onViewCaptured.mDragViewLeft = "
          + mDragViewLeft
          + " ,mDragViewTop = "
          + mDragViewTop
          + " ,mDragViewBottom = "
          + mDragViewBottom);
    }

    @Override public void onViewDragStateChanged(int state) {
      Log.d(TAG, "-----------onViewDragStateChanged----------------");
      super.onViewDragStateChanged(state);
    }

    @Override public void onViewReleased(View releasedChild, float xvel, float yvel) {
      Log.d(TAG, "-----------onViewReleased----------------");
      Log.d(TAG,
          "----------xvel = " + xvel + " ,yvel = " + yvel + " ,releasedChild = " + releasedChild);
      if (mSwitchVisible) {
        Log.d(TAG, "----------拖动有效---------");
        if (mDragViewTop < 0) {
          // 原来是隐藏的
          mDragHelper.smoothSlideViewTo(releasedChild, mDragViewLeft, 0);
        } else {
          mDragHelper.smoothSlideViewTo(releasedChild, mDragViewLeft,
              mRemainDistance - releasedChild.getHeight());
        }
        //相当于Scroller的startScroll方法
        ViewCompat.postInvalidateOnAnimation(DragLayout.this);
      } else {
        // 归原位
        Log.d(TAG, "----------拖动无效---------");
        if (mDragViewTop < 0) {
          // 原来是隐藏的
          mDragHelper.smoothSlideViewTo(releasedChild, mDragViewLeft,
              mRemainDistance - releasedChild.getHeight());
        } else {
          mDragHelper.smoothSlideViewTo(releasedChild, mDragViewLeft, 0);
        }
        ViewCompat.postInvalidateOnAnimation(DragLayout.this);
      }
    }

    @Override public void onEdgeTouched(int edgeFlags, int pointerId) {
      Log.d(TAG, "-----------onEdgeTouched----------------");
      super.onEdgeTouched(edgeFlags, pointerId);
    }

    @Override public int clampViewPositionHorizontal(View child, int left, int dx) {
      Log.d(TAG, "---------clampViewPositionHorizontal.left = "
          + left
          + " ,dx = "
          + dx
          + " ,child = "
          + child);
      // 不允许横向拖动
      return mDragViewLeft;
    }

    @Override public int clampViewPositionVertical(View child, int top, int dy) {
      Log.d(TAG, "---------clampViewPositionVertical.top = "
          + top
          + " ,dy = "
          + dy
          + " ,child = "
          + child);
      // 不允许往下拖动
      if (top > 0) {
        top = 0;
      }
      // 拖动的view已经被隐藏了，不能继续往上拖
      if (mDragViewTop < 0 && top < mDragViewTop) {
        top = mDragViewTop;
      }
      return top;
    }
  }
}
