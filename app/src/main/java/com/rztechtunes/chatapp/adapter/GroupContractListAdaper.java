package com.rztechtunes.chatapp.adapter;

import android.content.Context;
import android.graphics.Color;
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
import com.rztechtunes.chatapp.R;
import com.rztechtunes.chatapp.SendMessageFragment;
import com.rztechtunes.chatapp.pojo.AlluserContractPojo;

import java.util.List;

import static android.content.ContentValues.TAG;

public class GroupContractListAdaper extends RecyclerView.Adapter<GroupContractListAdaper.ContractViewHolder> {
    List<AlluserContractPojo> list;
    Context context;

    public GroupContractListAdaper(List<AlluserContractPojo> list, Context context) {
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
        holder.aboutTV.setText(list.get(position).getAbout());

        Glide.with(context)
                .load(list.get(position).getImage())
                .placeholder(R.drawable.ic_perm_)
                .into(holder.imageView);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.get(position).setSelected(!list.get(position).isSelected());
                holder.itemView.setBackgroundColor(list.get(position).isSelected() ? Color.GRAY : Color.WHITE);

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

    public List<AlluserContractPojo> getSelectedContract()
    {
        return list;
    }

}
