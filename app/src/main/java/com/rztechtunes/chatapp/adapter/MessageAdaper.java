package com.rztechtunes.chatapp.adapter;

import android.app.AlertDialog;
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
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.auth.FirebaseAuth;
import com.rztechtunes.chatapp.FullScreenImageView;
import com.rztechtunes.chatapp.ImageDownloadManager.DirectoryHelper;
import com.rztechtunes.chatapp.ImageDownloadManager.DownloadImageService;
import com.rztechtunes.chatapp.R;
import com.rztechtunes.chatapp.SendMessageFragment;
import com.rztechtunes.chatapp.pojo.SenderReciverPojo;


import java.util.List;

public class MessageAdaper extends RecyclerView.Adapter<MessageAdaper.ContractViewHolder> {
    List<SenderReciverPojo> list;
    Context context;
    public  static  final int MSG_TYPE_LEFT = 0;
    public  static  final int MSG_TYPE_RIGHT = 1;
    boolean isImageFitToScreen=false;
    String firebaseUser;
    public MessageAdaper(List<SenderReciverPojo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ContractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==MSG_TYPE_RIGHT)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.chat_item_right,parent,false);
            return new ContractViewHolder(view);
        }
        else
        {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.chat_item_left,parent,false);
            return new ContractViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final ContractViewHolder holder, final int position) {



        if ((list.get(position).getImage())==null)
        {
            holder.msgTV.setVisibility(View.VISIBLE);
            holder.imageView.setVisibility(View.GONE);
            holder.msgTV.setText(list.get(position).getMsg());
            holder.timeTV.setText(list.get(position).getStatus());
           // Picasso.get().load(list.get(position).getSenderImage()).into(holder.profile_image);
            Glide.with(context)
                    .load(list.get(position).getSenderImage())
                    .centerCrop()
                    .placeholder(R.drawable.ic_perm_)
                    .into(holder.profile_image);
        }
        else
        {
            holder.imageView.setVisibility(View.VISIBLE);
            holder.msgTV.setVisibility(View.GONE);
          //  Picasso.get().load(list.get(position).getSenderImage()).into(holder.profile_image);

            Glide.with(context)
                    .load(list.get(position).getSenderImage())
                    .centerCrop()
                    .placeholder(R.drawable.ic_perm_)
                    .into(holder.profile_image);

            holder.timeTV.setText(list.get(position).getStatus());
             //Picasso.get().load(list.get(position).getImage()).into(holder.imageView);


            Glide.with(context)
                    .load(list.get(position).getImage())
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .into(holder.imageView);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenImageView.image = list.get(position).getImage();
                FullScreenImageView.senderName = list.get(position).getSenderName();
                FullScreenImageView.sendTime = list.get(position).getStatus();
                //Set position so that backpress go to the recylerview item postion
                SendMessageFragment.position = position;
                Navigation.findNavController(holder.itemView).navigate(R.id.action_sendMessageFragment_to_fullScreenImageView);


            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ContractViewHolder extends RecyclerView.ViewHolder{

        ImageView profile_image;
        ImageView imageView;
        TextView msgTV,timeTV;

        public ContractViewHolder(@NonNull View itemView) {
            super(itemView);

            msgTV = itemView.findViewById(R.id.showmsg);
            profile_image = itemView.findViewById(R.id.profile_image);
            imageView = itemView.findViewById(R.id.imageView);
            timeTV = itemView.findViewById(R.id.timeTV);


        }
    }

    @Override
    public int getItemViewType(int position) {

         firebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

         if (firebaseUser.equals(list.get(position).getSenderID()))
         {
             return MSG_TYPE_RIGHT;
         }
         else
         {
             return MSG_TYPE_LEFT;
         }

    }
}
