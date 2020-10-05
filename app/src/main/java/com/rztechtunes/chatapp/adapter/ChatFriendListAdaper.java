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
import com.rztechtunes.chatapp.pojo.SenderReciverPojo;
import com.rztechtunes.chatapp.utils.HelperUtils;
import com.rztechtunes.chatapp.viewmodel.MessageViewModel;


import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import timber.log.Timber;

import static android.content.ContentValues.TAG;

public class ChatFriendListAdaper extends RecyclerView.Adapter<ChatFriendListAdaper.ContractViewHolder> {
    List<SenderReciverPojo> list;
    MessageViewModel messageViewModel = new MessageViewModel();
    Context context;
    String friendID;

    public ChatFriendListAdaper(List<SenderReciverPojo> list, Context context) {
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


        String currentdate = HelperUtils.getDateWithTime();
        long different = HelperUtils.getDefferentBetweenTwoDate(currentdate, list.get(position).getStatus());

        if (different == 0) {
            String[] time = (list.get(position).getStatus()).split("\\s+");
            holder.dateTV.setText("Today " + time[2] + time[3]);
        } else if (different == 1) {
            String[] time = (list.get(position).getStatus()).split("\\s+");
            holder.dateTV.setText("Yesterday " + time[2] + time[3]);
        } else {
            holder.dateTV.setText(list.get(position).getStatus());

        }

        holder.msgTV.setText(list.get(position).getMsg());


        if ((FirebaseAuth.getInstance().getCurrentUser().getUid()).equals(list.get(position).getSenderID())) {
            holder.nameTV.setText(list.get(position).getReciverName());
            //  Picasso.get().load(list.get(position).getReciverImage()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.imageView);
            Glide.with(context)
                    .load(list.get(position).getReciverImage())
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .into(holder.imageView);
        } else if ((FirebaseAuth.getInstance().getCurrentUser().getUid()).equals(list.get(position).getReciverID())) {
            holder.nameTV.setText(list.get(position).getSenderName());
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
                    SendMessageFragment.position = -1;
                    Navigation.findNavController(holder.itemView).navigate(R.id.sendMessageFragment);


                } else if (((FirebaseAuth.getInstance().getCurrentUser().getUid()).equals(list.get(position).getSenderID()))) {
                    SendMessageFragment.reciverID = list.get(position).getReciverID();
                    SendMessageFragment.reciverImage = list.get(position).getReciverImage();
                    SendMessageFragment.reciverName = list.get(position).getReciverName();
                    Navigation.findNavController(holder.itemView).navigate(R.id.sendMessageFragment);
                    SendMessageFragment.position = -1;
                }

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if ((FirebaseAuth.getInstance().getCurrentUser().getUid()).equals(list.get(position).getReciverID())) {
                    friendID = list.get(position).getSenderID();
                } else if (((FirebaseAuth.getInstance().getCurrentUser().getUid()).equals(list.get(position).getSenderID()))) {
                    friendID = list.get(position).getReciverID();
                }


                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Won't be able to recover message!")
                        .setConfirmText("Yes,delete it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                messageViewModel.deleteMessage(friendID);
                                notifyDataSetChanged();
                                sDialog
                                        .setTitleText("Deleted!")
                                        .setContentText("Message has been deleted!")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                            }
                        })
                        .show();

                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ContractViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nameTV, msgTV, dateTV;

        public ContractViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.nameTV);
            msgTV = itemView.findViewById(R.id.msgTV);
            dateTV = itemView.findViewById(R.id.dateTV);
            imageView = itemView.findViewById(R.id.profile_image);
        }
    }
}
