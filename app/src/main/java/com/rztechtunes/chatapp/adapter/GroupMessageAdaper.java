package com.rztechtunes.chatapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.rztechtunes.chatapp.R;
import com.rztechtunes.chatapp.pojo.SendGroupMsgPojo;
import com.rztechtunes.chatapp.pojo.SenderReciverPojo;

import java.util.List;

public class GroupMessageAdaper extends RecyclerView.Adapter<GroupMessageAdaper.ContractViewHolder> {
    List<SendGroupMsgPojo> list;
    Context context;
    public  static  final int MSG_TYPE_LEFT = 0;
    public  static  final int MSG_TYPE_RIGHT = 1;

    String firebaseUser;
    public GroupMessageAdaper(List<SendGroupMsgPojo> list, Context context) {
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



        if ((list.get(position).getImage()).equals(""))
        {
            holder.msgTV.setVisibility(View.VISIBLE);
            holder.imageView.setVisibility(View.GONE);
            holder.msgTV.setText(list.get(position).getMsg());
            holder.timeTV.setText(list.get(position).getDateTime());
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

            holder.timeTV.setText(list.get(position).getDateTime());
             //Picasso.get().load(list.get(position).getImage()).into(holder.imageView);


            Glide.with(context)
                    .load(list.get(position).getImage())
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .into(holder.imageView);
        }


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
