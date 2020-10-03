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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.rztechtunes.chatapp.pojo.CallingPojo;
import com.rztechtunes.chatapp.pojo.UserInformationPojo;
import com.rztechtunes.chatapp.viewmodel.MessageViewModel;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetActivityDelegate;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.URL;
import java.util.List;

import static android.content.ContentValues.TAG;

public class VideoCallingFrag extends Fragment {

    ImageView cancelBtn;
    MessageViewModel messageViewModel;
    private MediaPlayer mp;
    public static String reciverID;
    private Context mContext;
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



        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
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

                    JitsiMeetConferenceOptions options1 = new JitsiMeetConferenceOptions.Builder()
                            .setRoom(userinfo.getRoom_name())
                            .setWelcomePageEnabled(false)
                            .build();
                    JitsiMeetActivity.launch(mContext,options1);


                    Navigation.findNavController(getActivity(),R.id.nav_host_fragment).navigate(R.id.homeFragment);
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