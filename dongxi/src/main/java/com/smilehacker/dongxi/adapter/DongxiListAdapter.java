package com.smilehacker.dongxi.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smilehacker.dongxi.R;
import com.smilehacker.dongxi.Utils.CircleTransform;
import com.smilehacker.dongxi.activity.DetailActivity;
import com.smilehacker.dongxi.app.App;
import com.smilehacker.dongxi.app.Constants;
import com.smilehacker.dongxi.model.Dongxi;
import com.smilehacker.dongxi.view.DynamicHeightImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kleist on 13-12-25.
 */
public class DongxiListAdapter extends BaseAdapter {

    private final static String TAG = DongxiListAdapter.class.getName();

    private Context mContext;
    private List<Dongxi> mDongxiList;
    private LayoutInflater mInflater;

    private CircleTransform mCircleTransform;
    private ImageViewSize mImageSize;
    private ImageViewSize mAvatarSize;

    private int mDongxiImageWidth;
    private int mDongxiImageHeight;
    private RelativeLayout.LayoutParams mImageLayoutParams;

    public DongxiListAdapter(Context context, List<Dongxi> dongxiList) {
        mContext = context;
        mDongxiList = dongxiList;
        mInflater = LayoutInflater.from(context);
        mCircleTransform = new CircleTransform();
        mImageSize = new ImageViewSize();
        mAvatarSize = new ImageViewSize();
        computerImageViewSize();
    }

    private void computerImageViewSize() {
        /**
         * padding 1dp margin 15dp
         * width = match_parent - 32dp
         */
        App app = (App) mContext.getApplicationContext();
        mDongxiImageWidth = (int) (app.deviceInfo.screenWidth - 30 * app.deviceInfo.density);
        mDongxiImageHeight = mDongxiImageWidth * 2 / 3;
        mImageLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mDongxiImageHeight);

        Log.i(TAG, "h="+mDongxiImageHeight + " w=" + mDongxiImageWidth + " density="+app.deviceInfo.density);
    }

    @Override
    public int getCount() {
        return mDongxiList.size();
    }

    @Override
    public Object getItem(int i) {
        return mDongxiList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_dongxi_list, parent, false);
            holder = new ViewHolder();
            holder.image = (DynamicHeightImageView) convertView.findViewById(R.id.iv_dongxi);
            holder.price = (TextView) convertView.findViewById(R.id.tv_dongxi_price);
            holder.title = (TextView) convertView.findViewById(R.id.tv_dongxi_title);
            holder.avatar = (ImageView) convertView.findViewById(R.id.iv_author_avatar);
            holder.author = (TextView) convertView.findViewById(R.id.tv_author_name);
            holder.comment = (TextView) convertView.findViewById(R.id.tv_dongxi_comment);
            holder.dongxi = convertView.findViewById(R.id.v_dongxi);
//            holder.image.setLayoutParams(mImageLayoutParams);
            holder.image.setHeightRatio(0.66);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        final Dongxi dongxi = mDongxiList.get(position);
        holder.price.setText(dongxi.price);
        holder.title.setText(dongxi.title);
        holder.author.setText(dongxi.author.name);
        if (TextUtils.isEmpty(dongxi.text)) {
            holder.comment.setVisibility(View.GONE);
        } else {
            holder.comment.setVisibility(View.VISIBLE);
            holder.comment.setText(dongxi.text);
        }

        Picasso.with(mContext).load(dongxi.pictures.get(0).src).fit().centerCrop()
                .error(R.drawable.img_default).into(holder.image);

        Picasso.with(mContext).load(dongxi.author.largeAvatar).transform(mCircleTransform).fit().centerCrop().into(holder.avatar);

        holder.dongxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Constants.KEY_DONGXI, dongxi);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        public DynamicHeightImageView image;
        public ImageView avatar;
        public TextView price;
        public TextView title;
        public TextView author;
        public TextView comment;
        public View dongxi;
    }

    private static class ImageViewSize {
        public Boolean isGetSize = false;
        public int height;
        public int width;
    }
}
