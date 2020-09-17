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

import com.rztechtunes.chatapp.R;
import com.rztechtunes.chatapp.SendMessageFragment;
import com.rztechtunes.chatapp.pojo.AlluserContractPojo;
import com.rztechtunes.chatapp.pojo.AuthPojo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AllContractListAdaper extends RecyclerView.Adapter<AllContractListAdaper.ContractViewHolder> {
    List<AlluserContractPojo> list;
    Context context;

    public AllContractListAdaper(List<AlluserContractPojo> list, Context context) {
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

        Picasso.get().load(list.get(position).getImage()).into(holder.imageView);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessageFragment.reciverID = list.get(position).getU_ID();
                SendMessageFragment.reciverImage = list.get(position).getImage();
                SendMessageFragment.reciverName = list.get(position).getName();
                Navigation.findNavController(holder.itemView).navigate(R.id.sendMessageFragment);
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
