package com.rztechtunes.chatapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rztechtunes.chatapp.GroupSendMessage;
import com.rztechtunes.chatapp.R;
import com.rztechtunes.chatapp.pojo.AlluserContractPojo;
import com.rztechtunes.chatapp.pojo.GroupPojo;

import java.util.List;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ContractViewHolder> {
    List<GroupPojo> list;
    Context context;

    public GroupListAdapter(List<GroupPojo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ContractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.all_contract_row,parent,false);
        return new ContractViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ContractViewHolder holder, final int position) {

        holder.nameTV.setText(list.get(position).getName());
        holder.aboutTV.setText(list.get(position).getDescription());

        Glide.with(context)
                .load(list.get(position).getImages())
                .placeholder(R.drawable.ic_perm_)
                .into(holder.imageView);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupSendMessage.groupName = list.get(position).getName();
                GroupSendMessage.groupID = list.get(position).getGroupID();
                GroupSendMessage.groupImage = list.get(position).getImages();
                Navigation.findNavController(holder.itemView).navigate(R.id.groupSendMessage);
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

        public ContractViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.nameTV);
            aboutTV = itemView.findViewById(R.id.aboutTV);
            imageView = itemView.findViewById(R.id.profile_image);
        }
    }


}
