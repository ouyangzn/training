package com.ouyangzn.view;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import com.ouyangzn.lib.utils.ScreenUtils;
import com.ouyangzn.utils.Log;

/**
 * 上下手势触发事件
 */
public class UpDownSwipeLayout extends FrameLayout {

  private static final String TAG = UpDownSwipeLayout.class.getSimpleName();

  private GestureDetectorCompat mGestureDetector;
  private Listener mListener;
  private float mMaxX;
  private float mMinY;
  private boolean mSwipeEnabled;
  private boolean mDisallowInterceptEnabled;
  private boolean mDisallowIntercept;
  private boolean mConsumedSwipe;

  public UpDownSwipeLayout(final Context context) {
    super(context);
    init(context);
  }

  public UpDownSwipeLayout(final Context context, final AttributeSet set) {
    super(context, set);
    init(context);
  }

  public UpDownSwipeLayout(final Context context, final AttributeSet set, final int n) {
    super(context, set, n);
    init(context);
  }

  void init(final Context context) {
    mSwipeEnabled = true;
    mDisallowInterceptEnabled = true;
    mDisallowIntercept = false;

    final DefaultGestureDetectorListener detectorListener = new DefaultGestureDetectorListener();
    mMaxX = ScreenUtils.dp2px(context, 45f);
    mMinY = ScreenUtils.dp2px(context, 80f);
    mGestureDetector = new GestureDetectorCompat(context.getApplicationContext(), detectorListener);
    mGestureDetector.setIsLongpressEnabled(false);
  }

  /**
   * 设置最小Y轴移动距离
   * @param minY 单位px
   */
  public void setMinY(float minY) {
    mMinY = minY;
  }

  private boolean swipe(MotionEvent e1, MotionEvent e2) {
    if (mListener == null) {
      return false;
    }
    float xDis = e2.getX() - e1.getX();
    float yDis = e2.getY() - e1.getY();
    float absX = Math.abs(xDis);
    float absY = Math.abs(yDis);
    synchronized (this) {
      if (absX <= mMaxX && absY > mMinY && absX < absY && !mConsumedSwipe) {
        mConsumedSwipe = true;
        if (yDis > 0.0f) {
          return mListener.swipeToBottom();
        } else if (yDis < 0.0f) {
          return mListener.swipeToTop();
        }
      }
    }
    return false;
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    Log.d(TAG, "--------------onTouchEvent------------------");
    return super.onTouchEvent(event);
  }

  @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
    Log.d(TAG, "--------------onInterceptTouchEvent------------------");
    return super.onInterceptTouchEvent(ev);
  }

  public boolean dispatchTouchEvent(final MotionEvent ev) {
    Log.d(TAG, "--------------dispatchTouchEvent------------------");
    //if (true) {
    //  return true;
    //}
    if (ev.getAction() == MotionEvent.ACTION_DOWN) {
      mDisallowIntercept = false;
      mConsumedSwipe = false;
    }
    if (mSwipeEnabled
        && !(mDisallowIntercept && mDisallowInterceptEnabled)
        && mGestureDetector.onTouchEvent(ev)) {
      ev.setAction(MotionEvent.ACTION_CANCEL);
    }
    try {
      super.dispatchTouchEvent(ev);
    } catch (Throwable t) {
      Log.d(TAG, "----------dispatchTouchEvent出错：", t);
    }
    return true;
  }

  @Override public void requestDisallowInterceptTouchEvent(final boolean disallowIntercept) {
    super.requestDisallowInterceptTouchEvent(disallowIntercept);
    mDisallowIntercept = disallowIntercept;
  }

  public void setDisallowInterceptEnabled(final boolean enabled) {
    mDisallowInterceptEnabled = enabled;
  }

  public void setOnSwipeListener(final Listener listener) {
    mListener = listener;
  }

  public void setSwipeEnabled(final boolean enabled) {
    mSwipeEnabled = enabled;
  }

  public interface Listener {
    boolean swipeToTop();

    boolean swipeToBottom();
  }

  private class DefaultGestureDetectorListener extends GestureDetector.SimpleOnGestureListener {
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
      return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
      return swipe(e1, e2);
    }
  }
}
