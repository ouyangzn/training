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

package com.ouyangzn.module.testRealm.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ouyangzn.recyclerview.MultipleEntity;
import com.ouyangzn.utils.Log;
import io.realm.RealmModel;
import io.realm.RealmResults;
import java.util.List;

/**
 * Created by ouyangzn on 2016/10/9.<br/>
 * Description：
 */
public abstract class PersonBaseAdapter<T extends RealmModel>
    extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  protected static final int VIEW_TYPE_DATA = 0x000001000;
  protected static final int VIEW_TYPE_LOAD_MORE = 0x000001004;
  private static final String TAG = PersonBaseAdapter.class.getSimpleName();
  private static final int PER_PAGE_COUNT = 20;
  protected Context mContext;
  protected LayoutInflater mLayoutInflater;
  protected RealmResults<T> mData;
  /**
   * layout资源id缓存
   */
  private SparseIntArray mLayoutsMap;
  private View mLoadMoreView;
  private boolean mHasMore = false;
  private boolean mIsLoading = false;

  private int mCurrPage = 1;
  private Handler mHandler = new Handler(Looper.getMainLooper());

  /**
   * @param layoutResId item文件的资源id
   * @param data 数据
   */
  public PersonBaseAdapter(int layoutResId, RealmResults<T> data) {
    this.mData = data;
    if (layoutResId != 0) {
      if (mLayoutsMap == null) mLayoutsMap = new SparseIntArray(1);
      mLayoutsMap.put(VIEW_TYPE_DATA, layoutResId);
    }
  }

  /**
   * 如果item为多种类型，使用此构造方法
   *
   * @param layoutMap item文件的map集合，key为itemType，value为layout资源文件id
   * @param data 数据
   */
  public PersonBaseAdapter(SparseIntArray layoutMap, RealmResults<T> data) {
    this.mData = data;
    this.mLayoutsMap = layoutMap;
  }

  public void setLoadMoreView(View loadMoreView) {
    this.mLoadMoreView = loadMoreView;
  }

  public List<T> getData() {
    return mData;
  }

  public void setData(RealmResults<T> data) {
    this.mData = data;
    notifyDataSetChanged();
  }

  public T getItem(int position) {
    if (mData.size() == 0) return null;
    return mData.get(position);
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    BaseViewHolder baseViewHolder;
    if (mContext == null) this.mContext = parent.getContext();
    if (mLayoutInflater == null) this.mLayoutInflater = LayoutInflater.from(mContext);
    switch (viewType) {
      case VIEW_TYPE_LOAD_MORE:
        baseViewHolder = new BaseViewHolder(mLoadMoreView);
        break;
      case VIEW_TYPE_DATA:
      default:
        baseViewHolder = createContentViewHolder(parent, viewType);
        break;
    }
    return baseViewHolder;
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    int viewType = holder.getItemViewType();
    switch (viewType) {
      case VIEW_TYPE_LOAD_MORE:
        if (!mIsLoading && mHasMore) {
          System.gc();
          long total = Runtime.getRuntime().totalMemory(); // byte
          long m1 = Runtime.getRuntime().freeMemory();
          Log.d(TAG, "----------before,内存占用:" + (total - m1));
          mIsLoading = true;
          mCurrPage++;
          mHandler.post(new Runnable() {
            @Override public void run() {
              notifyDataSetChanged();
            }
          });
          mIsLoading = false;
          long total1 = Runtime.getRuntime().totalMemory();
          long m2 = Runtime.getRuntime().freeMemory();
          Log.d(TAG, "----------after,内存占用:" + (total1 - m2));
        }
        break;
      case VIEW_TYPE_DATA:
      default:
        convert((BaseViewHolder) holder, mData.get(holder.getLayoutPosition()));
        break;
    }
  }

  /**
   * 使用holder中的方法设置item的各个数据、view的点击事件等<br/>
   * eg: {@link BaseViewHolder#setText(int, CharSequence)}、<br/>
   * {@link BaseViewHolder#setOnClickListener(int, View.OnClickListener)}、<br/>
   *
   * @param holder 当前项的holder
   * @param item 当前项的数据
   */
  protected abstract void convert(BaseViewHolder holder, T item);

  @Override public int getItemViewType(int position) {
    if (mHasMore && position == mCurrPage * PER_PAGE_COUNT) return VIEW_TYPE_LOAD_MORE;
    return getContentItemViewType(position);
  }

  /**
   * 获取item的类型，如果使用多类型的item布局，重写此方法返回对应的类型
   *
   * @param position 当前item的position
   * @return item类型
   */
  protected int getContentItemViewType(int position) {
    if (mLayoutsMap.size() == 1) {
      return VIEW_TYPE_DATA;
    } else {
      return ((MultipleEntity) mData.get(position)).getItemType();
    }
  }

  @Override public int getItemCount() {
    if (mData == null) return 0;
    int count = 0;
    // 实际需要显示的数据长度
    int currSize = mCurrPage * PER_PAGE_COUNT;
    int totalSize = mData.size();
    if (currSize < totalSize) {
      mHasMore = true;
      count += currSize;
    } else {
      mHasMore = false;
      count += totalSize;
    }
    if (mHasMore && mLoadMoreView != null) {
      count += 1;
    }

    Log.d(TAG, "----------itemCount = " + count);
    return count;
  }

  /**
   * 解决header等view的宽度问题
   */
  @Override public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
    super.onViewAttachedToWindow(holder);
    int type = holder.getItemViewType();
    if (type == VIEW_TYPE_LOAD_MORE) {
      setFullSpan(holder);
    }
  }

  protected void setFullSpan(RecyclerView.ViewHolder holder) {
    if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
      StaggeredGridLayoutManager.LayoutParams params =
          (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
      params.setFullSpan(true);
    }
  }

  /**
   * 解决header等view的宽度问题
   */
  @Override public void onAttachedToRecyclerView(RecyclerView recyclerView) {
    super.onAttachedToRecyclerView(recyclerView);
    RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
    if (manager instanceof GridLayoutManager) {
      final GridLayoutManager gridManager = ((GridLayoutManager) manager);
      gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
        @Override public int getSpanSize(int position) {
          int type = getItemViewType(position);
          return (type == VIEW_TYPE_LOAD_MORE) ? gridManager.getSpanCount() : 1;
        }
      });
    }
  }

  /**
   * 创建recyclerView的viewHolder，如果要支持多类型的item，需要重写此方法
   *
   * @param parent item的父view
   * @param viewType 当前item的viewType
   */
  protected BaseViewHolder createContentViewHolder(ViewGroup parent, int viewType) {
    return createDefContentViewHolder(parent, mLayoutsMap.get(viewType));
  }

  protected BaseViewHolder createDefContentViewHolder(ViewGroup parent, int layoutResId) {
    return new BaseViewHolder(getItemView(layoutResId, parent));
  }

  protected View getItemView(int layoutResId, ViewGroup parent) {
    return mLayoutInflater.inflate(layoutResId, parent, false);
  }
}
