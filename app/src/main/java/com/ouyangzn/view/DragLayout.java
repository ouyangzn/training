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
import com.ouyangzn.lib.utils.ScreenUtils;
import com.ouyangzn.utils.Log;

/**
 * Created by ouyangzn on 2016/10/12.<br/>
 * Description：拖动子view隐藏/显示的layout
 */
public class DragLayout extends FrameLayout {

  public final static int DIRECTION_TOP = 0;
  public final static int DIRECTION_BOTTOM = 1;
  public final static int DIRECTION_LEFE = 2;
  public final static int DIRECTION_RIGHT = 3;
  private final static String TAG = DragLayout.class.getSimpleName();
  private Context mContext;
  private int mScreenWidth;
  private int mScreenHeight;

  private ViewDragHelper mDragHelper;
  /** 隐藏时剩下显示的高度或宽度 */
  private int mRemainDistance;
  /** 拖动隐藏显示的临界值距离 */
  private int mThresholdDistance;
  /** 要被拖动的子view */
  //private int mDraggedViewId;
  private View mDraggedView;
  /** 往哪个方向拖动时隐藏 */
  private int mDirection;
  // 手指按下时的X、Y坐标
  private float mDownX;
  private float mDownY;

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

  private void init(Context context, AttributeSet attrs) {
    mContext = context;
    mScreenWidth = ScreenUtils.getScreenWidth(context);
    mScreenHeight = ScreenUtils.getScreenHeight(context);
    mDragHelper = ViewDragHelper.create(this, new DragLayout.ViewDragCallback());
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DragLayout);
    mRemainDistance = ta.getDimensionPixelOffset(R.styleable.DragLayout_remain_distance, 0);
    mThresholdDistance = ta.getDimensionPixelOffset(R.styleable.DragLayout_threshold_distance, 0);
    //mDraggedViewId = ta.getResourceId(R.styleable.DragLayout_dragged_view_id, View.NO_ID);
    mDirection = ta.getInt(R.styleable.DragLayout_direction, DIRECTION_TOP);
    ta.recycle();
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    //if (mDraggedViewId != View.NO_ID) {
    //  mDraggedView = findViewById(mDraggedViewId);
    //} else {
    //  mDraggedView = getChildAt(0);
    //}
    if (getChildCount() != 1) {
      throw new UnsupportedOperationException("DragLayout should has only one direct child");
    }
    mDraggedView = getChildAt(0);
  }

  @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
    boolean helpResult = mDragHelper.shouldInterceptTouchEvent(ev);
    switch (MotionEventCompat.getActionMasked(ev)) {
      case MotionEvent.ACTION_DOWN:
        mDownX = ev.getX();
        mDownY = ev.getY();
        break;
    }
    if (isHorizontalScrollView(mDraggedView)) {
      boolean intercept = false;
      switch (MotionEventCompat.getActionMasked(ev)) {
        case MotionEvent.ACTION_MOVE:
          if (mDirection == DIRECTION_TOP || mDirection == DIRECTION_BOTTOM) {
            // 判断是否该拦截此事件自己消费--->模仿support-v4中的DrawerLayout处理方式
            intercept =
                mDragHelper.checkTouchSlop(ViewDragHelper.DIRECTION_VERTICAL, ev.getPointerId(0));
          } else {
            // 手指往右滑
            if (ev.getX() - mDownX > 0) {
              // view为左滑隐藏且已隐藏,直接拖动
              if (mDirection == DIRECTION_LEFE && mDraggedView.getRight() == mRemainDistance) {
                intercept = true;
              } else if (mDirection == DIRECTION_RIGHT) {
                // view为右滑隐藏的，根据view自身能否滑动决定是否拦截
                intercept = !ViewCompat.canScrollHorizontally(mDraggedView, -1);
              }
            }
            // 手指往左滑
            else {
              // view为右滑隐藏且已隐藏,直接拖动
              if (mDirection == DIRECTION_RIGHT
                  && (mScreenWidth - mDraggedView.getLeft()) == mRemainDistance) {
                intercept = true;
              } else if (mDirection == DIRECTION_LEFE) {
                // view为左滑隐藏的，根据view自身能否滑动决定是否拦截
                intercept = !ViewCompat.canScrollHorizontally(mDraggedView, 1);
              }
            }
          }
          // 此时如果需要拦截事件，需要手动捕获拖动的view
          if (intercept) mDragHelper.captureChildView(mDraggedView, ev.getPointerId(0));
          break;
      }
      return helpResult || intercept;
    }
    if (isVerticalScrollView(mDraggedView)) {
      boolean intercept = false;
      switch (MotionEventCompat.getActionMasked(ev)) {
        case MotionEvent.ACTION_MOVE:
          if (mDirection == DIRECTION_LEFE || mDirection == DIRECTION_RIGHT) {
            // 判断是否该拦截此事件自己消费--->模仿support-v4中的DrawerLayout处理方式
            intercept =
                mDragHelper.checkTouchSlop(ViewDragHelper.DIRECTION_HORIZONTAL, ev.getPointerId(0));
          } else {
            // 手指往下滑
            if (ev.getY() - mDownY > 0) {
              if (mDirection == DIRECTION_TOP && mDraggedView.getBottom() == mRemainDistance) {
                intercept = true;
              } else if (mDirection == DIRECTION_BOTTOM) {
                intercept = !ViewCompat.canScrollVertically(mDraggedView, -1);
              }
            }
            // 手指往上滑
            else {
              // view为下滑隐藏的，且已被隐藏，直接拖动
              if (mDirection == DIRECTION_BOTTOM
                  && (mScreenHeight - mDraggedView.getBottom()) == mRemainDistance) {
                intercept = true;
              } else if (mDirection == DIRECTION_TOP) {
                // view为上滑隐藏的，根据view自身能否滑动决定是否拦截
                intercept = !ViewCompat.canScrollVertically(mDraggedView, 1);
              }
            }
          }
          break;
      }
      Log.d(TAG, "----------onInterceptTouchEvent.return = " + (helpResult || intercept));
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
    private int mDragViewRight;
    private int mDragViewTop;
    private int mDragViewBottom;
    private int mDragLayoutWidth;
    private int mDragLayoutHeight;

    @Override public boolean tryCaptureView(View child, int pointerId) {
      Log.d(TAG, "-----------tryCaptureView.pointerId = " + pointerId + " ,child = " + child);
      return child == mDraggedView;
    }

    @Override
    public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
      if (changedView != mDraggedView) {
        mIsDragValid = null;
        return;
      }
      //Log.d(TAG, "-----------onViewPositionChanged.left = " + left + " ,top = " + top + " ,dx = " + dx + " ,dy = " + dy);
      // 临界值没设置的，默认一半宽或高为临界值
      if (mThresholdDistance == 0) {
        if (mDirection == DIRECTION_TOP || mDirection == DIRECTION_BOTTOM) {
          mIsDragValid = (Math.abs(top - mDragViewTop) / (float) mDraggedView.getHeight()) > 0.5;
        } else if (mDirection == DIRECTION_LEFE || mDirection == DIRECTION_RIGHT) {
          mIsDragValid = (Math.abs(left - mDragViewLeft) / (float) mDraggedView.getWidth()) > 0.5;
        }
      } else {
        if (mDirection == DIRECTION_TOP || mDirection == DIRECTION_BOTTOM) {
          mIsDragValid = Math.abs(top - mDragViewTop) > mThresholdDistance;
        } else if (mDirection == DIRECTION_LEFE || mDirection == DIRECTION_RIGHT) {
          mIsDragValid = Math.abs(left - mDragViewLeft) > mThresholdDistance;
        }
      }
    }

    @Override public void onViewCaptured(View capturedChild, int activePointerId) {
      super.onViewCaptured(capturedChild, activePointerId);
      // 获取被拖动view的初始位置
      mDragViewLeft = capturedChild.getLeft();
      mDragViewRight = capturedChild.getRight();
      mDragViewTop = capturedChild.getTop();
      mDragViewBottom = capturedChild.getBottom();
      //mDragLayoutWidth = DragLayout.this.getWidth();
      mDragLayoutWidth = DragLayout.this.getWidth();
      mDragLayoutHeight = DragLayout.this.getHeight();
      Log.d(TAG, "-----------onViewCaptured.mDragViewLeft = "
          + mDragViewLeft + " ,mDragViewRight = " + mDragViewRight
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
      Log.d(TAG, "-----------onViewReleased.xvel = " + xvel + " ,yvel = " + yvel);
      // todo 需要判断拖动隐藏的方向
      if (mIsDragValid) {
        Log.d(TAG, "----------拖动有效---------");
        if (mDirection == DIRECTION_TOP) {
          if (mDragViewTop < 0) {
            // 原来是隐藏的
            mDragHelper.smoothSlideViewTo(releasedChild, mDragViewLeft, 0);
          } else {
            mDragHelper.smoothSlideViewTo(releasedChild, mDragViewLeft,
                mRemainDistance - releasedChild.getHeight());
          }
        } else if (mDirection == DIRECTION_BOTTOM) {
          if (mDragViewBottom > mDragLayoutHeight) {
            mDragHelper.smoothSlideViewTo(releasedChild, mDragViewLeft, 0);
          } else {
            mDragHelper.smoothSlideViewTo(releasedChild, mDragViewLeft,
                mDragViewBottom - mRemainDistance);
          }
        } else if (mDirection == DIRECTION_LEFE) {
          if (mDragViewLeft < 0) {
            mDragHelper.smoothSlideViewTo(releasedChild, 0, mDragViewTop);
          } else {
            mDragHelper.smoothSlideViewTo(releasedChild,
                -releasedChild.getWidth() + mRemainDistance, mDragViewTop);
          }
        } else if (mDirection == DIRECTION_RIGHT) {
          if (mDragViewRight > mDragLayoutWidth) {
            mDragHelper.smoothSlideViewTo(releasedChild, 0, mDragViewTop);
          } else {
            mDragHelper.smoothSlideViewTo(releasedChild, releasedChild.getWidth() - mRemainDistance,
                mDragViewTop);
          }
        }
      } else {
        // 归原位
        Log.d(TAG, "----------拖动无效---------");
        if (mDirection == DIRECTION_TOP) {
          if (mDragViewTop < 0) {
            // 原来是隐藏的
            mDragHelper.smoothSlideViewTo(releasedChild, mDragViewLeft,
                mRemainDistance - releasedChild.getHeight());
          } else {
            mDragHelper.smoothSlideViewTo(releasedChild, mDragViewLeft, 0);
          }
        } else if (mDirection == DIRECTION_BOTTOM) {
          if (mDragViewBottom > mDragLayoutHeight) {
            mDragHelper.smoothSlideViewTo(releasedChild, mDragViewLeft, mDragViewTop);
          } else {
            mDragHelper.smoothSlideViewTo(releasedChild, mDragViewLeft, 0);
          }
        } else if (mDirection == DIRECTION_LEFE) {
          if (mDragViewLeft < 0) {
            mDragHelper.smoothSlideViewTo(releasedChild,
                -releasedChild.getWidth() + mRemainDistance, mDragViewTop);
          } else {
            mDragHelper.smoothSlideViewTo(releasedChild, 0, mDragViewTop);
          }
        } else if (mDirection == DIRECTION_RIGHT) {
          if (mDragViewRight > mDragLayoutWidth) {
            mDragHelper.smoothSlideViewTo(releasedChild, releasedChild.getWidth() - mRemainDistance,
                mDragViewTop);
          } else {
            mDragHelper.smoothSlideViewTo(releasedChild, 0, mDragViewTop);
          }
        }
      }
      //相当于Scroller的startScroll方法
      ViewCompat.postInvalidateOnAnimation(DragLayout.this);
    }

    @Override public void onEdgeTouched(int edgeFlags, int pointerId) {
      Log.d(TAG, "-----------onEdgeTouched----------------");
      super.onEdgeTouched(edgeFlags, pointerId);
    }

    @Override public int clampViewPositionHorizontal(View child, int left, int dx) {
      if (child != mDraggedView || mDirection == DIRECTION_TOP || mDirection == DIRECTION_BOTTOM) {
        return mDragViewLeft;
      }
      //Log.d(TAG, "---------clampViewPositionHorizontal.left = " + left + " ,dx = " + dx);

      if (mDirection == DIRECTION_LEFE) {
        if (left > 0) left = 0;
        if (mDragViewLeft < 0 && left < mDragViewLeft) {
          left = mDragViewLeft;
        }
      } else {
        if (left < 0) left = 0;
        if (mDragViewRight > mScreenWidth + mDraggedView.getWidth()) {
          left = mDragViewLeft;
        }
      }
      return left;
    }

    @Override public int clampViewPositionVertical(View child, int top, int dy) {
      if (child != mDraggedView || mDirection == DIRECTION_LEFE || mDirection == DIRECTION_RIGHT) {
        return mDragViewTop;
      }
      //Log.d(TAG, "---------clampViewPositionVertical.top = " + top + " ,dy = " + dy);
      if (mDirection == DIRECTION_TOP) {
        // 不允许往下拖动
        if (top > 0) {
          top = 0;
        }
        // 拖动的view已经被隐藏了，不能继续往上拖
        if (mDragViewTop < 0 && top < mDragViewTop) {
          top = mDragViewTop;
        }
      } else {
        // 往下隐藏的，不允许往上拖动
        //if (mScreenHeight - mDragViewTop == mDraggedView.getHeight()) {
        if (top < 0) {
          top = 0;
        }
        if (mDragViewTop >= mScreenHeight) {
          top = mDragViewTop;
        }
      }
      return top;
    }
  }
}
