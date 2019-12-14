package com.ee542.PNSS;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.URI;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Image> mImages;

    public RecyclerViewAdapter(List<Image> items) {
        mImages = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public ImageView imageView;
        public TextView capture_time;
        public TextView comment;
        public TextView camera_id;
        public TextView ml_result;
        public ViewHolder(View view) {
            super(view);
            cardView = view.findViewById(R.id.photo_item);
            imageView = view.findViewById(R.id.photo);
            capture_time = view.findViewById(R.id.capture_time);
            comment = view.findViewById(R.id.comment);
            camera_id = view.findViewById(R.id.camera_id);
            ml_result = view.findViewById(R.id.ml_result);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.e("url", mImages.get(position).getImageURL());
        Glide.with(holder.imageView.getContext()).load(mImages.get(position).getImageURL()).into(holder.imageView);
//        holder.imageView.setImageURI(new URI(mImages.get(position).getImageURL()));
        holder.comment.setText(mImages.get(position).getComment());
        holder.capture_time.setText(mImages.get(position).getCaptureTime());
        holder.camera_id.setText(mImages.get(position).getCameraId());
        holder.ml_result.setText(mImages.get(position).getMachineLearningResult());
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }
}
