package com.rztechtunes.chatapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rztechtunes.chatapp.R;
import com.rztechtunes.chatapp.SendMessageFragment;
import com.rztechtunes.chatapp.pojo.AlluserContractPojo;
import com.rztechtunes.chatapp.pojo.SenderReciverPojo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdaper extends RecyclerView.Adapter<MessageAdaper.ContractViewHolder> {
    List<SenderReciverPojo> list;
    Context context;
    public  static  final int MSG_TYPE_LEFT = 0;
    public  static  final int MSG_TYPE_RIGHT = 1;
    boolean isImageFitToScreen=false;
    String firebaseUser;
    public MessageAdaper(List<SenderReciverPojo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ContractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType==MSG_TYPE_RIGHT)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.chat_item_right,parent,false);
            return new ContractViewHolder(view);
        }
        else
        {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.chat_item_left,parent,false);
            return new ContractViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final ContractViewHolder holder, final int position) {



        if ((list.get(position).getImage())==null)
        {
            holder.msgTV.setVisibility(View.VISIBLE);
            holder.msgTV.setText(list.get(position).getMsg());
            holder.timeTV.setText(list.get(position).getStatus());
            Picasso.get().load(list.get(position).getSenderImage()).into(holder.profile_image);
        }
        else
        {
            holder.imageView.setVisibility(View.VISIBLE);
            holder.msgTV.setVisibility(View.GONE);
            Picasso.get().load(list.get(position).getSenderImage()).into(holder.profile_image);
            holder.timeTV.setText(list.get(position).getStatus());
            Picasso.get().load(list.get(position).getImage()).into(holder.imageView);


        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  if(isImageFitToScreen) {
                    Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show();
                    isImageFitToScreen=false;
                    //holder.imageView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    //holder.imageView.setAdjustViewBounds(true);
                    holder.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                }
                else{
                    isImageFitToScreen=true;
                    holder.imageView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                }*/
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ContractViewHolder extends RecyclerView.ViewHolder{

        ImageView profile_image,imageView;
        TextView msgTV,timeTV;

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

         if (firebaseUser.equals(list.get(position).getSenderID()))
         {
             return MSG_TYPE_RIGHT;
         }
         else
         {
             return MSG_TYPE_LEFT;
         }

    }
}
