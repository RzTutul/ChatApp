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
import com.rztechtunes.chatapp.FullScreenImageView;
import com.rztechtunes.chatapp.R;
import com.rztechtunes.chatapp.SendMessageFragment;
import com.rztechtunes.chatapp.pojo.StoriesPojo;
import com.rztechtunes.chatapp.viewmodel.FriendViewModel;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MyProfileStoriesAdapter extends RecyclerView.Adapter<MyProfileStoriesAdapter.StoriesViewHolder> {
    List<StoriesPojo> list;
    Context context;
    FriendViewModel friendViewModel = new FriendViewModel();

    public MyProfileStoriesAdapter(List<StoriesPojo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.my_stories_row,parent,false);
        return new StoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StoriesViewHolder holder, final int position) {



        Glide.with(context)
                .load(list.get(position).getImage())
                .placeholder(R.drawable.ic_perm_)
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenImageView.image = list.get(position).getImage();
                FullScreenImageView.senderName = list.get(position).getName();
                FullScreenImageView.sendTime = list.get(position).getTime();
                //Set position so that backpress go to the recylerview item postion
                SendMessageFragment.position = position;
                Navigation.findNavController(holder.itemView).navigate(R.id.fullScreenImageView);

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Won't be able to recover message!")
                        .setConfirmText("Yes,delete it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                friendViewModel.deleteMyStories(list.get(position).getId());
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


    class StoriesViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView nameTV;

        public StoriesViewHolder(@NonNull View itemView) {
            super(itemView);


            imageView = itemView.findViewById(R.id.mydayImage);

        }
    }
}
