package com.rztechtunes.chatapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.rztechtunes.chatapp.FullScreenImageView;
import com.rztechtunes.chatapp.R;
import com.rztechtunes.chatapp.SendMessageFragment;
import com.rztechtunes.chatapp.pojo.SenderReciverPojo;

import java.util.List;

public class FriendMediaImageAdaper extends RecyclerView.Adapter<FriendMediaImageAdaper.ContractViewHolder> {
    List<SenderReciverPojo> list;
    Context context;
    public FriendMediaImageAdaper(List<SenderReciverPojo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ContractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.friend_media_image_row,parent,false);
            return new ContractViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull final ContractViewHolder holder, final int position) {

            Glide.with(context)
                    .load(list.get(position).getImage())
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenImageView.image = list.get(position).getImage();
                FullScreenImageView.senderName = list.get(position).getSenderName();
                FullScreenImageView.sendTime = list.get(position).getStatus();
                //Set position so that backpress go to the recylerview item postion
                Navigation.findNavController(holder.itemView).navigate(R.id.fullScreenImageView);

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ContractViewHolder extends RecyclerView.ViewHolder{


        ImageView imageView;


        public ContractViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);

        }
    }


}
