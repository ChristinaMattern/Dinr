package com.example.dinr;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class Home_Screen_Adapter extends RecyclerView.Adapter<Home_Screen_Adapter.myViewHolder> {

    Context mContext;
    List<item> mData;

    public Home_Screen_Adapter(Context mContext, List<item> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.home_screen_card_layout, parent, false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int i) {
        holder.background.setImageResource(mData.get(i).getBackground());
        holder.title.setText(mData.get(i).getTitle());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        ImageView background;
        TextView title;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            background = itemView.findViewById(R.id.card_background);
            title = itemView.findViewById(R.id.titleTV);
        }
    }

}
