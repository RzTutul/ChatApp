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
import com.rztechtunes.chatapp.R;
import com.rztechtunes.chatapp.UserProfileFrag;
import com.rztechtunes.chatapp.pojo.UserInformationPojo;
import com.rztechtunes.chatapp.viewmodel.FirendViewModel;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MyFriendListAdaper extends RecyclerView.Adapter<MyFriendListAdaper.ContractViewHolder> {
    List<UserInformationPojo> list;
    Context context;
    FirendViewModel firendViewModel = new FirendViewModel();

    public MyFriendListAdaper(List<UserInformationPojo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ContractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.my_friend_list_row,parent,false);
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


            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Unfriend?")
                            .setContentText("Want to unfriend this person!")
                            .setConfirmText("Yes, Unfriend!")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    firendViewModel.unFirend(list.get(position).getU_ID());
                                    notifyDataSetChanged();
                                    sDialog
                                            .setTitleText("Unfriend!")
                                            .setContentText("Unfriend has been Successful!")
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                                }
                            })
                            .show();



                    return false;
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

        public ContractViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.nameTV);
            aboutTV = itemView.findViewById(R.id.aboutTV);
            imageView = itemView.findViewById(R.id.profile_image);
        }
    }
}
