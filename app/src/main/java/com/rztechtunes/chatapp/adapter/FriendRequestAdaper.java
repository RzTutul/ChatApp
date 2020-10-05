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
import com.google.android.material.button.MaterialButton;
import com.rztechtunes.chatapp.R;
import com.rztechtunes.chatapp.UserProfileFrag;
import com.rztechtunes.chatapp.pojo.UserInformationPojo;
import com.rztechtunes.chatapp.viewmodel.FirendViewModel;

import java.util.List;

public class FriendRequestAdaper extends RecyclerView.Adapter<FriendRequestAdaper.ContractViewHolder> {
    List<UserInformationPojo> list;
    Context context;
    FirendViewModel firendViewModel= new FirendViewModel();
    public FriendRequestAdaper(List<UserInformationPojo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ContractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.friend_request_row,parent,false);
        return new ContractViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ContractViewHolder holder, final int position) {



            holder.nameTV.setText(list.get(position).getName());
            holder.aboutTV.setText(list.get(position).getStatus());
            Glide.with(context)
                    .load(list.get(position).getprofileImage())
                    .placeholder(R.drawable.ic_perm_)
                    .into(holder.imageView);


            holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserInformationPojo friendRequestPojo = list.get(position);
                    firendViewModel.acceptedRequest(friendRequestPojo);
                }
            });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileFrag.userID = list.get(position).getU_ID();
                Navigation.findNavController(holder.itemView).navigate(R.id.userProfileFrag);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    class ContractViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView nameTV,aboutTV;
        MaterialButton acceptBtn,rejectBtn;

        public ContractViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.nameTV);
            aboutTV = itemView.findViewById(R.id.aboutTV);
            imageView = itemView.findViewById(R.id.profile_image);
            acceptBtn = itemView.findViewById(R.id.acceptButton);
            rejectBtn = itemView.findViewById(R.id.rejectBtn);
        }
    }
}
