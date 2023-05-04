package com.example.scheduleandaccountbook;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Personal_mode_makegroup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Personal_mode_makegroup extends Fragment {

    private DatabaseReference puDatabaseRef;
    private DatabaseReference peDatabaseRef;
    private FirebaseAuth auth; //파이어베이스 인증 객체

    private void createNewGroup(String groupName) {
//        DatabaseReference groupsRef = FirebaseDatabase.getInstance().getReference("groups");

        FirebaseAuth Auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = Auth.getCurrentUser();
        if (currentUser == null) {
            // 현재 접속중인 사용자가 없는 경우 처리
            return;
        }
        String creatorUid = currentUser.getUid();
        puDatabaseRef = FirebaseDatabase.getInstance().getReference("AccountBook").child("Public_User_DB");

        peDatabaseRef = FirebaseDatabase.getInstance().getReference("AccountBook");
        // Generate a new key for the group
        String groupId = puDatabaseRef.push().getKey();

        // Generate a new invite code for the group
        String inviteCode = generateInviteCode();

        // Create a new group object
        puDatabaseRef.child(groupId).child("creatorUid").setValue(creatorUid);
        puDatabaseRef.child(groupId).child("groupName").setValue(groupName);
        puDatabaseRef.child(groupId).child("inviteCode").setValue(inviteCode);
        puDatabaseRef.child(groupId).child("members").setValue(creatorUid);

        peDatabaseRef.child("UserAccount").child(currentUser.getUid()).child("GroupId").setValue(groupId);

//        Public_User_DB group = new Public_User_DB();
//        group.setGroupName(groupName);
//        group.setCreatorUid(creatorUid);
//        group.setInviteCode(inviteCode);
//        group.setMembers(creatorUid);
//        puDatabaseRef.child(groupId).setValue(group);

        //////////////////////////////////////////////////////////////////////
//        Public_User_DB group = new Public_User_DB(groupName, creatorUid, inviteCode);
        // Write the group object to the Realtime Database
//
//
//        puDatabaseRef.child(groupId).setValue(group);
//
//        DatabaseReference membersRef = puDatabaseRef.child(groupId).child("members");
//        membersRef.child(creatorUid).setValue(true);
        //////////////////////////////////////////////////////////////////////


//        Public_mode_DB_members groupmemeber= new Public_mode_DB_members(creatorUid);
//        DatabaseReference membersRef = puDatabaseRef.child(groupId);
//        membersRef.setValue(groupmemeber);

//        DatabaseReference membersRef = puDatabaseRef.child(groupId).child("members");
//        membersRef.child(creatorUid).setValue();
//
//        Personal_User_DB PeGroup = new Personal_User_DB(groupId);
//        FirebaseUser firebaseUser = auth.getCurrentUser(); //getCurrentUser => 회원가입 이 된 유저를 가져온다.
//        peDatabaseRef.child("Personal_User_DB").child(firebaseUser.getUid()).setValue(PeGroup);

    }

    private String generateInviteCode() {
        // Generate a random alphanumeric string of length 6
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int length = 6;
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(allowedChars.length());
            sb.append(allowedChars.charAt(index));
        }
        return sb.toString();
    }
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Personal_mode_makegroup() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Personal_mode_makegroup.
     */
    // TODO: Rename and change types and number of parameters
    public static Personal_mode_makegroup newInstance(String param1, String param2) {
        Personal_mode_makegroup fragment = new Personal_mode_makegroup();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Personal_mode_makegroup newInstance() {
        return new Personal_mode_makegroup();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal_mode_makegroup, container, false);
        // 4번째 Fragment 화면 구성
        Button btn_GroupName = view.findViewById(R.id.btn_GroupName);
        EditText edt_GroupName = view.findViewById(R.id.edt_GroupName);


        btn_GroupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TextView textView1 = view.findViewById(R.id.textView1);
//                TextView textView2 = view.findViewById(R.id.Show_Invite_code);
//                textView1.setVisibility(View.INVISIBLE);
//                edt_GroupName.setVisibility(View.INVISIBLE);
//                btn_GroupName.setVisibility(View.INVISIBLE);
//
//
//
////                textView2.setText();
//                textView2.setVisibility(View.VISIBLE);
//
                String groupName = edt_GroupName.getText().toString();
                createNewGroup(groupName);
                ((Personal_mode_menu)getActivity()).InviteCodeFragment(Show_InviteCode.newInstance());
                Toast.makeText(getActivity(), "모임을 만들었습니다!", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}