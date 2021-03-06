package com.rztechtunes.chatapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.rztechtunes.chatapp.group_chat.GroupSendMessage;
import com.rztechtunes.chatapp.R;
import com.rztechtunes.chatapp.pojo.GroupPojo;
import com.rztechtunes.chatapp.viewmodel.GroupViewModel;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ContractViewHolder> {
    List<GroupPojo> list;
    Context context;
    GroupViewModel groupViewModel = new GroupViewModel();

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

        if ((list.get(position).getGroupID()).equals("Removed"))
        {
            holder.aboutTV.setText("You are removed from this group!");

        }
        else
        {
            holder.aboutTV.setText(list.get(position).getDescription());

        }


        Glide.with(context)
                .load(list.get(position).getImages())
                .placeholder(R.drawable.ic_perm_)
                .into(holder.imageView);


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Want to delete group!")
                        .setConfirmText("Yes,delete it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                groupViewModel.deleteGroup(list.get(position).getGroupID());
                                notifyDataSetChanged();
                                sDialog
                                        .setTitleText("Deleted!")
                                        .setContentText("Group has been deleted!")
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

                if ((list.get(position).getGroupID()).equals("Removed"))
                {
                    Toast.makeText(context, "You are removed from this group!", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    GroupSendMessage.groupName = list.get(position).getName();
                    GroupSendMessage.groupID = list.get(position).getGroupID();
                    GroupSendMessage.groupImage = list.get(position).getImages();
                    Navigation.findNavController(holder.itemView).navigate(R.id.groupSendMessage);
                }

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
