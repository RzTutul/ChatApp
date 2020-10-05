package com.rztechtunes.chatapp;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rztechtunes.chatapp.pojo.CallingPojo;
import com.rztechtunes.chatapp.pojo.UserInformationPojo;
import com.rztechtunes.chatapp.viewmodel.MessageViewModel;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.URL;
import java.util.concurrent.TimeUnit;


public class IncomingCallFrag extends Fragment {


    ImageView receiveBtn,profile_image,cancelBtn,videoRecvBtn;
    TextView nameTV;
    private Vibrator vib;
    private MediaPlayer mp;
    public  static CallingPojo callingPojo;

    MessageViewModel messageViewModel;
    public IncomingCallFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        messageViewModel = ViewModelProviders.of(this).get(MessageViewModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_incoming_call, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        receiveBtn = view.findViewById(R.id.receiveBtn);
        videoRecvBtn = view.findViewById(R.id.videoRecvBtn);
        receiveBtn = view.findViewById(R.id.receiveBtn);
        cancelBtn = view.findViewById(R.id.cancelBtn);
        profile_image = view.findViewById(R.id.profile_image);
        nameTV = view.findViewById(R.id.nameTV);

        nameTV.setText(callingPojo.getName());

        Glide.with(getActivity())
                .load(callingPojo.getImage())
                .placeholder(R.drawable.ic_perm_)
                .into(profile_image);



        mp = MediaPlayer.create(getActivity(), R.raw.fbmsngertone);
        mp.setLooping(true);
        vib = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(1000);
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

        if (callingPojo.getCall_type().equals("Video"))
        {
            videoRecvBtn.setVisibility(View.VISIBLE);
            receiveBtn.setVisibility(View.GONE);
        }
        else
        {
            videoRecvBtn.setVisibility(View.GONE);
            receiveBtn.setVisibility(View.VISIBLE);
        }


        //Calling 60 seconds if it not receivice
        try {
            new CountDownTimer(60000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                   // btnTimer.setText(seconds + " Secound Wait");
                }

                @Override
                public void onFinish() {
                    mp.stop();
                    vib.cancel();
                    Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.homeFragment);

                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }


        receiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                vib.cancel();
                messageViewModel.ReceiveCall(callingPojo).observe(getActivity(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (s.equals("1"))
                        {
                            Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.homeFragment);

                            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                                    .setRoom(callingPojo.getRoom_name())
                                    .setWelcomePageEnabled(false)
                                    .setAudioOnly(true)
                                    .build();
                            JitsiMeetActivity.launch(getContext(),options);
                        }

                    }
                });

            }
        });




        videoRecvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                vib.cancel();
                messageViewModel.ReceiveCall(callingPojo).observe(getActivity(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (s.equals("1"))
                        {
                            Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.homeFragment);

                            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                                    .setRoom(callingPojo.getRoom_name())
                                    .setWelcomePageEnabled(false)
                                    .build();
                            JitsiMeetActivity.launch(getContext(),options);

                        }

                    }
                });


            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                vib.cancel();
                Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.homeFragment);
            }
        });

    }
}