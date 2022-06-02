package com.waleed.wallpanther;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Entry> mData;
    private int mSize;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    MyRecyclerViewAdapter(Context context, ArrayList<Entry> d) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = new ArrayList<Entry>(d);
        this.mSize = d.size();
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.entry_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.myTextView1.setText(mData.get(position).getTitle());
        holder.myTextView2.setText(mData.get(position).getAuthor());
        ProgressBar progressBar = holder.pbar;
        progressBar.setVisibility(View.VISIBLE);

        Picasso.with(context).load(mData.get(position).getLink()).fit().centerCrop().into((ImageView) holder.imgView, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError() {

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WallpaperActivity.class);
                intent.putExtra("Details", mData.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSize;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView1;
        TextView myTextView2;
        ImageView imgView;
        ProgressBar pbar;
        ViewHolder(View itemView) {
            super(itemView);
            myTextView1 = itemView.findViewById(R.id.img_info);
            myTextView2 = itemView.findViewById(R.id.author_info);
            imgView = itemView.findViewById(R.id.img_android);
            pbar = itemView.findViewById(R.id.progressBar);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    Entry getItem(int id) {
        return mData.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void updateData(ArrayList<Entry> backgrounds){
        this.mData.clear();
        this.mData.addAll(backgrounds);
        this.mSize = backgrounds.size();
        this.notifyDataSetChanged();
    }

}