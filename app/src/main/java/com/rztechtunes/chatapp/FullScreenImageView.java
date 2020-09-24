package com.rztechtunes.chatapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.rztechtunes.chatapp.ImageDownloadManager.DirectoryHelper;
import com.rztechtunes.chatapp.ImageDownloadManager.DownloadImageService;


public class FullScreenImageView extends Fragment {

   public static String image;
   public static String senderName;
   public static String sendTime;

    TextView nameTV,timeTV;
   ImageButton dwonloadBtn;
    PhotoView photoView;
    public FullScreenImageView() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_full_screen_image_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         photoView = view.findViewById(R.id.imageView);
         nameTV = view.findViewById(R.id.nameTV);
         timeTV = view.findViewById(R.id.sendtimeTV);
        dwonloadBtn = view.findViewById(R.id.downloadBtn);

         nameTV.setText(senderName);
         timeTV.setText(sendTime);

         dwonloadBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Toast.makeText(getContext(), "Downloading image...", Toast.LENGTH_SHORT).show();
                 getContext().startService(DownloadImageService.getDownloadService(getActivity(), image, DirectoryHelper.ROOT_DIRECTORY_NAME.concat("/")));

             }
         });

        Glide.with(getActivity())
                .load(image)
                .placeholder(R.drawable.ic_image_black_24dp)
                .into(photoView);

    }
}