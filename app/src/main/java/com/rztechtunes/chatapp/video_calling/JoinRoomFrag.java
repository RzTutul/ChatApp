package com.rztechtunes.chatapp.video_calling;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.rztechtunes.chatapp.R;
import com.rztechtunes.chatapp.pojo.SendGroupMsgPojo;
import com.rztechtunes.chatapp.pojo.UserInformationPojo;
import com.rztechtunes.chatapp.utils.HelperUtils;
import com.rztechtunes.chatapp.viewmodel.AuthViewModel;
import com.rztechtunes.chatapp.viewmodel.GroupViewModel;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.URL;

public class JoinRoomFrag extends Fragment {

    public static String groupID;
    ImageView backBtn;
    EditText secretCodeET;
    Button joinBtn,createBtn;
    public  static String groupName;
    GroupViewModel groupViewModel;
    AuthViewModel authViewModel;
    UserInformationPojo currentUserInfo ;
    public JoinRoomFrag() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        groupViewModel = ViewModelProviders.of(this).get(GroupViewModel.class);
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_join_room, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        secretCodeET = view.findViewById(R.id.secretCodeET);
        joinBtn = view.findViewById(R.id.joinBtn);
        createBtn = view.findViewById(R.id.createBtn);
        backBtn = view.findViewById(R.id.backBtn);


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
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });

        authViewModel.getUserInfo().observe(getViewLifecycleOwner(), new Observer<UserInformationPojo>() {
            @Override
            public void onChanged(UserInformationPojo userInformationPojo) {
                currentUserInfo = userInformationPojo;
            }
        });


        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String secretCode = secretCodeET.getText().toString();
                if (secretCode.equals(""))
                {
                    secretCodeET.setError("Enter Secret Code");
                }
                else
                {

                    JitsiMeetConferenceOptions options1 = new JitsiMeetConferenceOptions.Builder()
                            .setRoom(secretCode)
                            .setWelcomePageEnabled(false)
                            .build();
                    JitsiMeetActivity.launch(requireActivity(),options1);
                }

            }
        });


        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String group = groupName.replaceAll("\\s+","");

                String msg = " Create a Room -\n Secret Code: "+(group.toLowerCase())+"\n join the meeting by this code.";
                SendGroupMsgPojo groupMsgPojo;
                if (currentUserInfo!=null)
                {
                     groupMsgPojo = new SendGroupMsgPojo("",groupID,msg,"",currentUserInfo.getU_ID(),currentUserInfo.getName(),currentUserInfo.getprofileImage(), HelperUtils.getDateWithTime());
                    groupViewModel.sendGroupMsg(groupMsgPojo);

                    String secretCode = group.toLowerCase();
                    JitsiMeetConferenceOptions options1 = new JitsiMeetConferenceOptions.Builder()
                            .setRoom(secretCode)
                            .setWelcomePageEnabled(false)
                            .build();
                    JitsiMeetActivity.launch(requireActivity(),options1);

                }


            }
        });




    }
}