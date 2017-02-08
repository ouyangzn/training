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
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ScrollerCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import com.ouyangzn.R;
import com.ouyangzn.utils.Log;

/**
 * Created by ouyangzn on 2016/12/8.<br/>
 * Description：
 */
public class RefreshLayout extends FrameLayout {

  private static final String TAG = RefreshLayout.class.getSimpleName();
  /**
   * Interpolator defining the animation curve for mScroller
   */
  private static final Interpolator sInterpolator = new Interpolator() {
    @Override public float getInterpolation(float t) {
      t -= 1.0f;
      return t * t * t * t * t + 1.0f;
    }
  };
  private ViewDragHelper mDragHelper;
  private ScrollerCompat mScroller;
  private View mHeaderView;
  private int mHeaderHeight;
  private View mContentView;
  private float mDownY;
  private boolean mRefreshing;
  private SwipeRefreshLayout.OnRefreshListener mListener;

  public RefreshLayout(Context context) {
    this(context, null);
  }

  public RefreshLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void init(Context context) {
    mHeaderView = LayoutInflater.from(context).inflate(R.layout.header_refresh, this, false);
    mDragHelper = ViewDragHelper.create(this, new ViewDragCallback());
    mScroller = ScrollerCompat.create(context, sInterpolator);
    mListener = new SwipeRefreshLayout.OnRefreshListener() {
      @Override public void onRefresh() {
        Log.w(TAG, "OnRefreshListener is null, discard");
      }
    };
  }

  public void setHeaderView(View headerView) {
    removeView(mHeaderView);
    mHeaderView = headerView;
    addHeaderView(headerView);
  }

  public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
    mListener = listener;
  }

  public void setRefreshing(boolean refreshing) {
    // 非刷新变为刷新状态
    if (refreshing && !this.mRefreshing) {
      startRefresh();
    }
    // 刷新变为非刷新
    else if (!refreshing && this.mRefreshing) {
      finishRefresh();
    }
    this.mRefreshing = refreshing;
  }

  private void startRefresh() {
    scrollContentView(mHeaderHeight);
    mListener.onRefresh();
  }

  private void finishRefresh() {
    scrollContentView(0);
  }

  private void scrollContentView(int position) {
    mDragHelper.smoothSlideViewTo(mContentView, mContentView.getLeft(), position);
    //mContentView.scrollTo(0, position);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    int childCount = getChildCount();
    if (childCount > 1) {
      throw new UnsupportedOperationException("more than one direct child is not allowed");
    }
    mContentView = getChildAt(0);
    addHeaderView(mHeaderView);
  }

  private void addHeaderView(final View view) {
    addView(view, 0);
    final ViewTreeObserver observer = view.getViewTreeObserver();
    observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override public void onGlobalLayout() {
        mHeaderHeight = view.getHeight();
      }
    });
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    mHeaderHeight = mHeaderView.getHeight();
  }

  @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
    if (mRefreshing) return false;
    boolean intercept = mDragHelper.shouldInterceptTouchEvent(ev);
    if (intercept) return true;

    switch (ev.getAction()) {
      case MotionEvent.ACTION_DOWN: {
        mDownY = ev.getY();
        break;
      }
      case MotionEvent.ACTION_MOVE: {
        if (isVerticalScrollView(mContentView)) {
          // 手指往下滑动
          if (ev.getY() - mDownY > 0) {
            intercept = !ViewCompat.canScrollVertically(mContentView, -1);
            Log.d(TAG, "----------手指下滑，intercept = " + intercept);
          }
          //else {
          //  intercept = !ViewCompat.canScrollVertically(mContentView, 1);
          //  Log.d(TAG, "----------手指上滑，intercept = " + intercept);
          //}
        } else {
          intercept = mDragHelper.checkTouchSlop(ViewDragHelper.DIRECTION_VERTICAL);
        }
        break;
      }
    }
    if (intercept) mDragHelper.captureChildView(mContentView, ev.getPointerId(0));
    return intercept;
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

  private class ViewDragCallback extends ViewDragHelper.Callback {

    private int mOriginalLeft;
    private int mOriginalTop;
    private boolean isDragValid;

    @Override public boolean tryCaptureView(View child, int pointerId) {
      return child == mContentView;
    }

    @Override public void onViewCaptured(View capturedChild, int activePointerId) {
      super.onViewCaptured(capturedChild, activePointerId);
      mOriginalLeft = capturedChild.getLeft();
      mOriginalTop = capturedChild.getTop();
    }

    @Override
    public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
      super.onViewPositionChanged(changedView, left, top, dx, dy);
      isDragValid = top >= mHeaderHeight / 2;
    }

    //@Override public int clampViewPositionHorizontal(View child, int left, int dx) {
    //  return mOriginalLeft;
    //}

    @Override public int clampViewPositionVertical(View child, int top, int dy) {
      // 把header拖出来后不能继续拖动
      if (top < 0) top = 0;
      if (top > mHeaderHeight) {
        top = mHeaderHeight;
      }
      return top;
    }

    @Override public void onViewReleased(View releasedChild, float xvel, float yvel) {
      super.onViewReleased(releasedChild, xvel, yvel);
      if (isDragValid) {
        mDragHelper.smoothSlideViewTo(releasedChild, mOriginalLeft, mOriginalTop + mHeaderHeight);
        mListener.onRefresh();
      } else {
        mDragHelper.smoothSlideViewTo(releasedChild, mOriginalLeft, 0);
      }
    }
  }
}
