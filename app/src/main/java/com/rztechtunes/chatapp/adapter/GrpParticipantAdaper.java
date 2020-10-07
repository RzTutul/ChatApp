package com.rztechtunes.chatapp.adapter;

import android.app.AlertDialog;
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
import com.rztechtunes.chatapp.R;
import com.rztechtunes.chatapp.SendMessageFragment;
import com.rztechtunes.chatapp.pojo.UserInformationPojo;
import com.rztechtunes.chatapp.viewmodel.GroupViewModel;

import java.util.List;

public class GrpParticipantAdaper extends RecyclerView.Adapter<GrpParticipantAdaper.ContractViewHolder> {
    List<UserInformationPojo> list;
    Context context;
    String adminID;
    String groupID;
    GroupViewModel groupViewModel = new GroupViewModel();

    public GrpParticipantAdaper(List<UserInformationPojo> list, Context context, String adminID,String groupID) {
        this.list = list;
        this.context = context;
        this.adminID = adminID;
        this.groupID = groupID;
    }

    @NonNull
    @Override
    public ContractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.group_participant_row, parent, false);
        return new ContractViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ContractViewHolder holder, final int position) {

        if (adminID.equals(list.get(position).getU_ID())) {
            holder.adminStatus.setVisibility(View.VISIBLE);
            if ((FirebaseAuth.getInstance().getCurrentUser().getUid()).equals(list.get(position).getU_ID())) {
                holder.nameTV.setText("You");
                holder.StatusTV.setText(list.get(position).getStatus());
                Glide.with(context)
                        .load(list.get(position).getprofileImage())
                        .placeholder(R.drawable.ic_perm_)
                        .into(holder.imageView);
            } else {
                holder.nameTV.setText(list.get(position).getName());
                holder.StatusTV.setText(list.get(position).getStatus());
                Glide.with(context)
                        .load(list.get(position).getprofileImage())
                        .placeholder(R.drawable.ic_perm_)
                        .into(holder.imageView);
            }

        } else {
            holder.adminStatus.setVisibility(View.GONE);
            holder.nameTV.setText(list.get(position).getName());
            holder.StatusTV.setText(list.get(position).getStatus());
            Glide.with(context)
                    .load(list.get(position).getprofileImage())
                    .placeholder(R.drawable.ic_perm_)
                    .into(holder.imageView);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (adminID.equals(list.get(position).getU_ID())) {

                } else {
                    SendMessageFragment.reciverID = list.get(position).getU_ID();
                    SendMessageFragment.reciverImage = list.get(position).getprofileImage();
                    SendMessageFragment.reciverName = list.get(position).getName();
                    Navigation.findNavController(holder.itemView).navigate(R.id.sendMessageFragment);
                }


            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if ((FirebaseAuth.getInstance().getUid()).equals(adminID))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(list.get(position).getName());
                    builder.setIcon(R.drawable.ic_baseline_person_add_24);
                    CharSequence [] dialogIte={"kick out","Make admin","Message"};

                    builder.setItems(dialogIte, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            switch (i)
                            {
                                case 0:
                                    groupViewModel.kickOutFromGroup(list.get(position).getU_ID(),groupID);
                                    notifyDataSetChanged();
                                    break;

                                case 1:

                                    break;
                                case 2:


                            }

                        }
                    });
                    builder.create().show();
                }

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
        TextView nameTV, StatusTV, adminStatus;

        public ContractViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.nameTV);
            adminStatus = itemView.findViewById(R.id.adminStatus);
            StatusTV = itemView.findViewById(R.id.aboutTV);
            imageView = itemView.findViewById(R.id.profile_image);
        }
    }
}
