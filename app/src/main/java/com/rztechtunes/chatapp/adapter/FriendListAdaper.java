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
import com.google.firebase.auth.FirebaseAuth;
import com.rztechtunes.chatapp.R;
import com.rztechtunes.chatapp.SendMessageFragment;
import com.rztechtunes.chatapp.pojo.AlluserContractPojo;
import com.rztechtunes.chatapp.pojo.SenderReciverPojo;


import java.util.List;

import static android.content.ContentValues.TAG;

public class FriendListAdaper extends RecyclerView.Adapter<FriendListAdaper.ContractViewHolder> {
    List<SenderReciverPojo> list;
    Context context;

    public FriendListAdaper(List<SenderReciverPojo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ContractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.friend_contract_row, parent, false);
        return new ContractViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ContractViewHolder holder, final int position) {


        holder.msgTV.setText(list.get(position).getMsg());
        holder.dateTV.setText(list.get(position).getStatus());



        if ((FirebaseAuth.getInstance().getCurrentUser().getUid()).equals(list.get(position).getSenderID())) {
            holder.nameTV.setText(list.get(position).getReciverName());
          //  Picasso.get().load(list.get(position).getReciverImage()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.imageView);
            Glide.with(context)
                    .load(list.get(position).getReciverImage())
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .into(holder.imageView);
        }
        else if ((FirebaseAuth.getInstance().getCurrentUser().getUid()).equals(list.get(position).getReciverID())) {
            holder.nameTV.setText(list.get(position).getSenderName());
           // Picasso.get().load(list.get(position).getSenderImage()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.imageView);
            Glide.with(context)
                    .load(list.get(position).getSenderImage())
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .into(holder.imageView);

        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((FirebaseAuth.getInstance().getCurrentUser().getUid()).equals(list.get(position).getReciverID())) {
                    SendMessageFragment.reciverID = list.get(position).getSenderID();
                    SendMessageFragment.reciverImage = list.get(position).getSenderImage();
                    SendMessageFragment.reciverName = list.get(position).getSenderName();
                    Navigation.findNavController(holder.itemView).navigate(R.id.sendMessageFragment);

                }

                else if(((FirebaseAuth.getInstance().getCurrentUser().getUid()).equals(list.get(position).getSenderID()))) {
                    SendMessageFragment.reciverID = list.get(position).getReciverID();
                    SendMessageFragment.reciverImage = list.get(position).getReciverImage();
                    SendMessageFragment.reciverName = list.get(position).getReciverName();
                    Navigation.findNavController(holder.itemView).navigate(R.id.sendMessageFragment);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ContractViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nameTV, msgTV,dateTV;

        public ContractViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.nameTV);
            msgTV = itemView.findViewById(R.id.msgTV);
            dateTV = itemView.findViewById(R.id.dateTV);
            imageView = itemView.findViewById(R.id.profile_image);
        }
    }
}
