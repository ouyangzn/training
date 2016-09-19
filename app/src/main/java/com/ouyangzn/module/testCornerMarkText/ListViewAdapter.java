package com.ouyangzn.module.testCornerMarkText;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ouyangzn.R;
import com.ouyangzn.base.BaseListViewAdapter;
import com.ouyangzn.view.CornerMarkText;
import java.util.List;

/**
 * Created by ouyangzn on 2016/9/19.<br/>
 * Description：
 */
public class ListViewAdapter extends BaseListViewAdapter<String> {

  private LayoutInflater mInflater;

  public ListViewAdapter(Context context, List<String> list) {
    super(list);
    mInflater = LayoutInflater.from(context);
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder holder;
    if (convertView == null) {
      holder = new ViewHolder();
      convertView = mInflater.inflate(R.layout.item_corner_mark_text, parent, false);
      holder.mCornerMarkText = (CornerMarkText) convertView.findViewById(R.id.language);
      convertView.setTag(holder);
    } else {
      holder = (ViewHolder) convertView.getTag();
    }
    holder.mCornerMarkText.setText(mData.get(position));
    return convertView;
  }

  public class ViewHolder {
    public CornerMarkText mCornerMarkText;
  }
}
