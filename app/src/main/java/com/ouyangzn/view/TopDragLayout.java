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
import android.view.ViewGroup;
import android.widget.FrameLayout;
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

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public TopDragLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    mDragHelper = ViewDragHelper.create(this, new ViewDragCallback());
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TopDragLayout);
    mRemainHeight = ta.getDimensionPixelOffset(R.styleable.TopDragLayout_remain_height, 0);
    mThresholdHeight = ta.getDimensionPixelOffset(R.styleable.TopDragLayout_threshold_height, 0);
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
      Log.d(TAG, "----------left = " + left
          + " ,top = " + top
          + " ,dx = " + dx
          + " ,dy = " + dy
          + " ,changedView = " + changedView);
      super.onViewPositionChanged(changedView, left, top, dx, dy);
      // 临界值没设置的，默认一半高为临界值
      if (mThresholdHeight == 0) {
        mSwitchVisible = (Math.abs(top - mDragViewTop) / (float) changedView.getHeight()) > 0.5;
      } else {
        mSwitchVisible = Math.abs(top - mDragViewTop) > mThresholdHeight;
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
