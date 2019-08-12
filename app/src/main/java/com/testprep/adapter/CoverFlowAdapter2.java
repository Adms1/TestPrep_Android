//package com.testprep.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//import com.testprep.R;
//import com.testprep.models.PackageData;
//
//import java.util.ArrayList;
//
//public class CoverFlowAdapter2 extends BaseAdapter {
//
//    private ArrayList<PackageData.PackageDataList> mData = new ArrayList<>(0);
//    private ArrayList<PackageData.PackageDataList> mDataa = new ArrayList<>(0);
//    private Context mContext;
//    private String mType;
//
//    public CoverFlowAdapter2(Context context, String type) {
//        mContext = context;
//        mType = type;
//    }
//
//    public void setData(ArrayList<PackageData.PackageDataList> data) {
//        mData = data;
//    }
//
//    public void setDataa(ArrayList<PackageData.PackageDataList> Dataa) {
//        mDataa = Dataa;
//    }
//
//    @Override
//    public int getCount() {
//        if (mType.equalsIgnoreCase("tutor")) {
//            return mDataa.size();
//        } else {
//            return mData.size();
//        }
//    }
//
//    @Override
//    public Object getItem(int pos) {
//        if (mType.equalsIgnoreCase("tutor")) {
//            return mDataa.get(pos);
//        } else {
//            return mData.get(pos);
//        }
//    }
//
//    @Override
//    public long getItemId(int pos) {
//        return pos;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        View rowView = convertView;
//
//        if (rowView == null) {
//            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            rowView = inflater.inflate(R.layout.item_coverflow, null);
//
//            ViewHolder viewHolder = new ViewHolder();
//            viewHolder.text = rowView.findViewById(R.id.label);
//            viewHolder.image = rowView
//                    .findViewById(R.id.image);
//            rowView.setTag(viewHolder);
//        }
//
//        ViewHolder holder = (ViewHolder) rowView.getTag();
//
//        if (mType.equalsIgnoreCase("tutor")) {
//            holder.image.setImageResource(R.drawable.pro_pic1);
//            holder.text.setText(mDataa.get(position).getTutorName());
//        } else {
//            holder.image.setImageResource(R.drawable.pp_1);
//            holder.text.setText(mData.get(position).getTestPackageName());
//        }
//
//        return rowView;
//    }
//
//
//    static class ViewHolder {
//        public TextView text;
//        public ImageView image;
//    }
//}
