package com.ouyangzn.base;

import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ouyangzn on 2016/9/19.<br/>
 * Description：
 */
public abstract class BaseListViewAdapter<T> extends BaseAdapter {

  protected List<T> mData;

  public BaseListViewAdapter(List<T> list) {
    this.mData = list == null ? new ArrayList<T>(0) : list;
  }

  public List<T> getData() {
    return mData;
  }

  public void add(T data) {
    mData.add(data);
    notifyDataSetChanged();
  }

  public void add(List<T> data) {
    if (data != null && data.size() != 0) {
      mData.addAll(data);
      notifyDataSetChanged();
    }
  }

  public void remove(int index) {
    mData.remove(index);
    notifyDataSetChanged();
  }

  @Override public int getCount() {
    return mData.size();
  }

  @Override public Object getItem(int position) {
    return mData.get(position);
  }

  @Override public long getItemId(int position) {
    return position;
  }
}
