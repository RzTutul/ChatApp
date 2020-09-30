package com.rztechtunes.chatapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rztechtunes.chatapp.FullScreenImageView;
import com.rztechtunes.chatapp.R;
import com.rztechtunes.chatapp.SendMessageFragment;
import com.rztechtunes.chatapp.pojo.StoriesPojo;

import java.util.List;

import static android.content.ContentValues.TAG;

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.StoriesViewHolder> {
    List<StoriesPojo> list;
    Context context;

    public StoriesAdapter(List<StoriesPojo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.stories_row,parent,false);
        return new StoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StoriesViewHolder holder, final int position) {

        Log.i(TAG, "onBindViewHolder: "+list.get(position).getImage());

        holder.nameTV.setText(list.get(position).getName());
        Glide.with(context)
                .load(list.get(position).getProfile())
                .placeholder(R.drawable.ic_perm_)
                .into(holder.profileImage);

        Glide.with(context)
                .load(list.get(position).getImage())
                .placeholder(R.drawable.ic_perm_)
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenImageView.image = list.get(position).getImage();
                FullScreenImageView.senderName = list.get(position).getName();
                FullScreenImageView.sendTime = list.get(position).getTime();
                //Set position so that backpress go to the recylerview item postion
                SendMessageFragment.position = position;
                Navigation.findNavController(holder.itemView).navigate(R.id.fullScreenImageView);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class StoriesViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView,profileImage;
        TextView nameTV;

        public StoriesViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.nameTV);
            imageView = itemView.findViewById(R.id.mydayImage);
            profileImage = itemView.findViewById(R.id.profile_image);
        }
    }
}
