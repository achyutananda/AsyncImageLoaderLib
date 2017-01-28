package com.slab.asyncimageloaderlib_demo.adapter;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.slab.asyncimageloaderlib_demo.R;
import com.slab.imageloaderlib.Loader;

import org.json.JSONObject;

import java.util.List;

public class ContentRecyclerAdapter extends RecyclerView.Adapter<ContentRecyclerAdapter.RecyclerViewHolder> {

    private List<JSONObject> contentList;
    private int prevPosition = 0;
    private Context mContext;
    private RecyclerView mRecyclerView;

    public ContentRecyclerAdapter(Context mContext, List<JSONObject> contentList, RecyclerView mRecyclerView) {
        this.mContext = mContext;
        this.contentList = contentList;
        this.mRecyclerView = mRecyclerView;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        JSONObject rootObject = contentList.get(position);

        try {
            JSONObject userObj = rootObject.getJSONObject("user");
            JSONObject profileImgObj = userObj.getJSONObject("profile_image");

            holder.tvLike.setText(String.valueOf(rootObject.getInt("likes")));
            holder.tvName.setText(userObj.getString("name"));
            new Loader(mContext).loadImage(holder.ivProfile, profileImgObj.getString("large"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (position > prevPosition) {
            animate(holder, true);
        } else {
            animate(holder, false);
        }
        prevPosition = position;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.tvName.setTransitionName("trans_text" + position);
            holder.ivProfile.setTransitionName("trans_image" + position);
        }
    }


    @Override
    public int getItemCount() {
        return contentList.size();
    }

//    public void addItem(String s) {
//        contentList.add(s);
//        notifyItemInserted(contentList.size());
////        notifyDataSetChanged();
//    }

    public void removeItem(String s) {
        int pos = contentList.indexOf(s);
        if (pos != -1 && contentList.size() > pos) {
            contentList.remove(pos);
            notifyItemRemoved(pos);
        }
    }

    public void removeItem(int pos) {
        if (pos < contentList.size()) {
            contentList.remove(pos);
            notifyItemRemoved(pos);
        }
    }


    @SuppressLint("NewApi")
    void animate(RecyclerView.ViewHolder holder, boolean goesDown) {

//        YoYo.with(Techniques.RubberBand)
//                .duration(1000)
//                .playOn(holder.itemView);
//        AnimatorSet animatorSet = new AnimatorSet();
//        ObjectAnimator animatorScaleX = ObjectAnimator.ofFloat(holder.itemView, "scaleX" ,0.5F, 0.8F, 1.0F);
//        ObjectAnimator animatorScaleY = ObjectAnimator.ofFloat(holder.itemView, "scaleY", 0.5F, 0.8F, 1.0F);
        ObjectAnimator animatorTranslateY = ObjectAnimator.ofFloat(holder.itemView, "translationY", goesDown == true ? 300 : -300, 0);
//        ObjectAnimator animatorTranslateX = ObjectAnimator.ofFloat(holder.itemView, "translationX", -50, 50, -30, 30, -20, 20, -5, 5, 0);
//        animatorSet.playTogether(animatorTranslateX, animatorTranslateY, animatorScaleX, animatorScaleY);
//        animatorSet.setInterpolator(new AnticipateInterpolator());
//        animatorSet.setDuration(1000);
// animatorSet.start();
        animatorTranslateY.setDuration(1000);
        animatorTranslateY.start();

    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvLike;
        ImageView ivProfile;

        public RecyclerViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvLike = (TextView) view.findViewById(R.id.tvLike);
            ivProfile = (ImageView) view.findViewById(R.id.ivProfile);
        }

    }
}

