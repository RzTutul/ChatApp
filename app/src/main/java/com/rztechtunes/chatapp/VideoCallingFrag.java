package com.rztechtunes.chatapp;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rztechtunes.chatapp.pojo.CallingPojo;
import com.rztechtunes.chatapp.pojo.UserInformationPojo;
import com.rztechtunes.chatapp.viewmodel.MessageViewModel;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetActivityDelegate;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class VideoCallingFrag extends Fragment {

    ImageView cancelBtn,profile_image;
    TextView nameTV;
    MessageViewModel messageViewModel;
    private MediaPlayer mp;
    public static String reciverID;
    public static String name;
    public static String image;
    private Context mContext;
    CountDownTimer count;
    public VideoCallingFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        messageViewModel = ViewModelProviders.of(this).get(MessageViewModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_calling, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cancelBtn = view.findViewById(R.id.cancelBtn);
        profile_image = view.findViewById(R.id.profile_image);
        nameTV = view.findViewById(R.id.nameTV);

        nameTV.setText(name);
        //Picasso.get().load(reciverImage).into(prfileImage);
        Glide.with(getActivity())
                .load(image)
                .placeholder(R.drawable.ic_perm_)
                .into(profile_image);

        mp = MediaPlayer.create(getActivity(), R.raw.callingtone);
        mp.setLooping(true);
        mp.start();
        URL serverUrl;
        try {

            serverUrl = new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(serverUrl)
                    .setWelcomePageEnabled(false)
                    .build();
            JitsiMeet.setDefaultConferenceOptions(options);

        }
        catch (Exception e) {

        }

        //Calling 60 seconds if it not receivice
        try {
         count =    new CountDownTimer(60000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                    // btnTimer.setText(seconds + " Secound Wait");
                }

                @Override
                public void onFinish() {
                    mp.stop();
                    messageViewModel.CallCancel(reciverID);
                    Toast.makeText(mContext, "No Response!", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.homeFragment);

                }
            };

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        count.start();






        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                count.cancel();
                messageViewModel.CallCancel(reciverID);
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).popBackStack();
            }
        });

        messageViewModel.getRecivingStatus().observe(getActivity(), new Observer<List<CallingPojo>>() {
            @Override
            public void onChanged(List<CallingPojo> userInformationPojos) {
                mp.stop();
                if (userInformationPojos.size()>0)
                {
                    CallingPojo userinfo = userInformationPojos.get(userInformationPojos.size()-1);
                    if (userinfo.getCall_type().equals("Video"))
                    {


                        JitsiMeetConferenceOptions options1 = new JitsiMeetConferenceOptions.Builder()
                                .setRoom(userinfo.getRoom_name())
                                .setWelcomePageEnabled(false)
                                .build();
                        JitsiMeetActivity.launch(mContext,options1);

                    }
                    else
                    {
                        JitsiMeetConferenceOptions options1 = new JitsiMeetConferenceOptions.Builder()
                                .setRoom(userinfo.getRoom_name())
                                .setWelcomePageEnabled(false)
                                .setAudioOnly(true)
                                .build();
                        JitsiMeetActivity.launch(mContext,options1);
                    }


                    Navigation.findNavController(getActivity(),R.id.nav_host_fragment).popBackStack();
                }


            }
        });



    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
}