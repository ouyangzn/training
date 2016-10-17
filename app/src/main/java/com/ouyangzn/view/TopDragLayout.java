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
import android.content.res.TypedArray;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import com.ouyangzn.R;
import com.ouyangzn.utils.Log;

/**
 * Created by ouyangzn on 2016/9/12.<br/>
 * Description：拖动子view隐藏/显示的layout
 */
public class TopDragLayout extends FrameLayout {

  private final static String TAG = TopDragLayout.class.getSimpleName();

  private ViewDragHelper mDragHelper;
  /** 隐藏时底部遗留的高度 */
  private int mRemainHeight;
  /** 拖动隐藏显示的临界值高度 */
  private int mThresholdHeight;
  /** 要被拖动的子view */
  private int mDraggedViewId;
  private View mDraggedView;
  private float downY;

  public TopDragLayout(Context context) {
    this(context, null);
  }

  public TopDragLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public TopDragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    mDragHelper = ViewDragHelper.create(this, new ViewDragCallback());
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TopDragLayout);
    mRemainHeight = ta.getDimensionPixelOffset(R.styleable.TopDragLayout_remain_height, 0);
    mThresholdHeight = ta.getDimensionPixelOffset(R.styleable.TopDragLayout_threshold_height, 0);
    mDraggedViewId = ta.getResourceId(R.styleable.TopDragLayout_dragged_view_id, View.NO_ID);
    ta.recycle();
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    if (mDraggedViewId != View.NO_ID) {
      mDraggedView = findViewById(mDraggedViewId);
    } else {
      mDraggedView = getChildAt(0);
    }
  }

  @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
    boolean helpResult = mDragHelper.shouldInterceptTouchEvent(ev);
    if (isHorizontalScrollView(mDraggedView)) {
      boolean intercept = false;
      switch (MotionEventCompat.getActionMasked(ev)) {
        case MotionEvent.ACTION_MOVE:
          // 判断是否该拦截此事件自己消费--->模仿support-v4中的DrawerLayout处理方式
          intercept = mDragHelper.checkTouchSlop(ViewDragHelper.DIRECTION_VERTICAL);
          // 此时如果需要拦截事件，需要手动捕获拖动的view
          if (intercept) mDragHelper.captureChildView(mDraggedView, ev.getPointerId(0));
          break;
      }
      return helpResult || intercept;
    }
    if (isVerticalScrollView(mDraggedView)) {
      // 如果子View被隐藏了，执行拖动出来
      if (mDraggedView.getBottom() == mRemainHeight) {
        mDragHelper.captureChildView(mDraggedView, ev.getPointerId(0));
        return true;
      }
      boolean intercept = false;
      switch (MotionEventCompat.getActionMasked(ev)) {
        case MotionEvent.ACTION_DOWN:
          downY = ev.getY();
          break;
        case MotionEvent.ACTION_MOVE:
          // 手指往下滑动
          if (ev.getY() - downY > 0) {
            intercept = !ViewCompat.canScrollVertically(mDraggedView, -1);
            Log.d(TAG, "----------手指下滑，intercept = " + intercept);
          } else {
            intercept = !ViewCompat.canScrollVertically(mDraggedView, 1);
            Log.d(TAG, "----------手指上滑，intercept = " + intercept);
          }
          break;
      }
      Log.d(TAG, "----------return = " + (helpResult || intercept));
      // 此时如果需要拦截事件，需要手动捕获拖动的view
      if (intercept) mDragHelper.captureChildView(mDraggedView, ev.getPointerId(0));
      return helpResult || intercept;
    }
    return helpResult;
  }

  private boolean isHorizontalScrollView(View view) {
    if (view instanceof RecyclerView) {
      RecyclerView recyclerView = (RecyclerView) view;
      RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
      if (layoutManager instanceof LinearLayoutManager) {
        LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
        if (manager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
          return true;
        }
      }
    }
    return view instanceof HorizontalScrollView
        || ViewCompat.canScrollHorizontally(view, -1)
        || ViewCompat.canScrollHorizontally(view, 1);
  }

  private boolean isVerticalScrollView(View view) {
    if (view instanceof RecyclerView) {
      RecyclerView recyclerView = (RecyclerView) view;
      RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
      if (layoutManager instanceof LinearLayoutManager) {
        LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
        if (manager.getOrientation() == LinearLayoutManager.VERTICAL) {
          return true;
        }
      }
    }
    return view instanceof ScrollView
        || ViewCompat.canScrollVertically(view, -1)
        || ViewCompat.canScrollVertically(view, 1);
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

    /** 拖动是否有效的开关，null表示保持原样不变 */
    private Boolean mIsDragValid;
    private int mDragViewLeft;
    private int mDragViewTop;
    private int mDragViewBottom;

    @Override public boolean tryCaptureView(View child, int pointerId) {
      Log.d(TAG, "-----------tryCaptureView----------------");
      Log.d(TAG, "----------pointerId = " + pointerId + " ,child = " + child);
      return child == mDraggedView;
    }

    @Override
    public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
      if (changedView != mDraggedView) {
        mIsDragValid = null;
        return;
      }
      Log.d(TAG, "-----------onViewPositionChanged----------------");
      Log.d(TAG, "----------left = " + left
          + " ,top = " + top
          + " ,dx = " + dx
          + " ,dy = " + dy
          + " ,changedView = " + changedView);
      // 临界值没设置的，默认一半高为临界值
      if (mThresholdHeight == 0) {
        mIsDragValid = (Math.abs(top - mDragViewTop) / (float) changedView.getHeight()) > 0.5;
      } else {
        mIsDragValid = Math.abs(top - mDragViewTop) > mThresholdHeight;
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
      //if (releasedChild != mDraggedView) return;
      if (mIsDragValid == null) {
        return;
      }
      Log.d(TAG, "-----------onViewReleased----------------");
      Log.d(TAG,
          "----------xvel = " + xvel + " ,yvel = " + yvel + " ,releasedChild = " + releasedChild);
      if (mIsDragValid) {
        Log.d(TAG, "----------拖动有效---------");
        if (mDragViewTop < 0) {
          // 原来是隐藏的
          mDragHelper.smoothSlideViewTo(releasedChild, mDragViewLeft, 0);
        } else {
          mDragHelper.smoothSlideViewTo(releasedChild, mDragViewLeft,
              mRemainHeight - releasedChild.getHeight());
        }
        //相当于Scroller的startScroll方法
        ViewCompat.postInvalidateOnAnimation(TopDragLayout.this);
      } else {
        // 归原位
        Log.d(TAG, "----------拖动无效---------");
        if (mDragViewTop < 0) {
          // 原来是隐藏的
          mDragHelper.smoothSlideViewTo(releasedChild, mDragViewLeft,
              mRemainHeight - releasedChild.getHeight());
        } else {
          mDragHelper.smoothSlideViewTo(releasedChild, mDragViewLeft, 0);
        }
        ViewCompat.postInvalidateOnAnimation(TopDragLayout.this);
      }
    }

    @Override public void onEdgeTouched(int edgeFlags, int pointerId) {
      Log.d(TAG, "-----------onEdgeTouched----------------");
      super.onEdgeTouched(edgeFlags, pointerId);
    }

    @Override public int clampViewPositionHorizontal(View child, int left, int dx) {
      Log.d(TAG, "---------clampViewPositionHorizontal.left = " + left
          + " ,dx = " + dx
          + " ,child = " + child);
      // 不允许横向拖动
      return mDragViewLeft;
    }

    @Override public int clampViewPositionVertical(View child, int top, int dy) {
      if (child != mDraggedView) return child.getTop();
      Log.d(TAG, "---------clampViewPositionVertical.top = " + top
          + " ,dy = " + dy
          + " ,child = " + child);
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
