package com.rztechtunes.chatapp.adapter;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
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
import com.rztechtunes.chatapp.FullScreenImageView;
import com.rztechtunes.chatapp.R;
import com.rztechtunes.chatapp.SendMessageFragment;
import com.rztechtunes.chatapp.pojo.SendGroupMsgPojo;
import com.rztechtunes.chatapp.pojo.SenderReciverPojo;
import com.rztechtunes.chatapp.viewmodel.GroupViewModel;
import com.rztechtunes.chatapp.viewmodel.MessageViewModel;

import java.util.List;

public class GroupMessageAdaper extends RecyclerView.Adapter<GroupMessageAdaper.ContractViewHolder> {
    List<SendGroupMsgPojo> list;
    Context context;
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    String firebaseUser;

    public GroupMessageAdaper(List<SendGroupMsgPojo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ContractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MSG_TYPE_RIGHT) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.chat_item_right, parent, false);
            return new ContractViewHolder(view);
        } else {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.chat_item_left, parent, false);
            return new ContractViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final ContractViewHolder holder, final int position) {


        if ((list.get(position).getImage()).equals("")) {
            holder.msgTV.setVisibility(View.VISIBLE);
            holder.imageView.setVisibility(View.GONE);
            holder.msgTV.setText(list.get(position).getMsg());
            holder.timeTV.setText(list.get(position).getDateTime());
            // Picasso.get().load(list.get(position).getSenderImage()).into(holder.profile_image);
            Glide.with(context)
                    .load(list.get(position).getSenderImage())
                    .centerCrop()
                    .placeholder(R.drawable.ic_perm_)
                    .into(holder.profile_image);
        } else {
            holder.imageView.setVisibility(View.VISIBLE);
            holder.msgTV.setVisibility(View.GONE);
            //  Picasso.get().load(list.get(position).getSenderImage()).into(holder.profile_image);

            Glide.with(context)
                    .load(list.get(position).getSenderImage())
                    .centerCrop()
                    .placeholder(R.drawable.ic_perm_)
                    .into(holder.profile_image);

            holder.timeTV.setText(list.get(position).getDateTime());
            //Picasso.get().load(list.get(position).getImage()).into(holder.imageView);


            Glide.with(context)
                    .load(list.get(position).getImage())
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .into(holder.imageView);
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenImageView.image = list.get(position).getImage();
                FullScreenImageView.senderName = list.get(position).getSenderName();
                FullScreenImageView.sendTime = list.get(position).getDateTime();
                //Set position so that backpress go to the recylerview item postion
                SendMessageFragment.position = position;
                Navigation.findNavController(holder.itemView).navigate(R.id.fullScreenImageView);
            }
        });


        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Image");
                builder.setIcon(R.drawable.ic_image_black_24dp);
                CharSequence[] dialogIte = {"Remove"};

                builder.setItems(dialogIte, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {


                            case 0:
                                if ((FirebaseAuth.getInstance().getUid()).equals(list.get(position).getSenderID())) {
                                    GroupViewModel groupViewModel = new GroupViewModel();

                                    groupViewModel.removeMessage(list.get(position).getId(), list.get(position).getGroupID());
                                    notifyDataSetChanged();

                                } else {
                                    Toast.makeText(context, "You can't remove this message!", Toast.LENGTH_SHORT).show();
                                }

                                break;


                        }

                    }
                });
                builder.create().show();


                return false;
            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Text");
                builder.setIcon(R.drawable.ic_baseline_message_24);
                CharSequence[] dialogIte = {"Copy", "Remove"};

                builder.setItems(dialogIte, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("", list.get(position).getMsg());
                                clipboard.setPrimaryClip(clip);
                                Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show();

                                break;

                            case 1:
                                if ((FirebaseAuth.getInstance().getUid()).equals(list.get(position).getSenderID())) {
                                    GroupViewModel groupViewModel = new GroupViewModel();

                                    groupViewModel.removeMessage(list.get(position).getId(), list.get(position).getGroupID());
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Message Removed!", Toast.LENGTH_SHORT).show();


                                } else {
                                    Toast.makeText(context, "You can't remove this message!", Toast.LENGTH_SHORT).show();
                                }

                                break;


                        }

                    }
                });
                builder.create().show();

                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ContractViewHolder extends RecyclerView.ViewHolder {

        ImageView profile_image;
        ImageView imageView;
        TextView msgTV, timeTV;

        public ContractViewHolder(@NonNull View itemView) {
            super(itemView);

            msgTV = itemView.findViewById(R.id.showmsg);
            profile_image = itemView.findViewById(R.id.profile_image);
            imageView = itemView.findViewById(R.id.imageView);
            timeTV = itemView.findViewById(R.id.timeTV);


        }
    }

    @Override
    public int getItemViewType(int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (firebaseUser.equals(list.get(position).getSenderID())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }

    }
}
