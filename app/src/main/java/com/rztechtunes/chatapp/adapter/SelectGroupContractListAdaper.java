package com.rztechtunes.chatapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rztechtunes.chatapp.R;
import com.rztechtunes.chatapp.pojo.UserInformationPojo;

import java.util.ArrayList;
import java.util.List;

public class SelectGroupContractListAdaper extends RecyclerView.Adapter<SelectGroupContractListAdaper.ContractViewHolder> {
    List<UserInformationPojo> list;
    List<UserInformationPojo> selectContractList  = new ArrayList<>();
    Context context;

    public SelectGroupContractListAdaper() {
    }

    public SelectGroupContractListAdaper(List<UserInformationPojo> list, Context context) {
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
        holder.aboutTV.setText(list.get(position).getStatus());

        Glide.with(context)
                .load(list.get(position).getprofileImage())
                .placeholder(R.drawable.ic_perm_)
                .into(holder.imageView);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.get(position).setSelected(!list.get(position).isSelected());
                selectContractList.add( list.get(position) );
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

    public List<UserInformationPojo> getSelectedContract()
    {
        List<UserInformationPojo> finalSelect= new ArrayList<>();
        for (UserInformationPojo contractPojo: selectContractList)
        {
            if (contractPojo.isSelected())
            {
                finalSelect.add(contractPojo);
            }
        }

        return finalSelect;
    }

}
