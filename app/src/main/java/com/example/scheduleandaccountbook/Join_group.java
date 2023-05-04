package com.example.scheduleandaccountbook;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Join_group#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Join_group extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DatabaseReference puDatabaseRef;
    private DatabaseReference uDatabaseRef;

    private void joinGroup(String inviteCode, String memberUid) {
        puDatabaseRef = FirebaseDatabase.getInstance().getReference("AccountBook").child("Public_User_DB");
        uDatabaseRef = FirebaseDatabase.getInstance().getReference("AccountBook");
        FirebaseAuth Auth = FirebaseAuth.getInstance();
        FirebaseUser Uid = Auth.getCurrentUser();


//        addListenerForSingleValueEvent
        puDatabaseRef.orderByChild("inviteCode").equalTo(inviteCode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) { // 일치하는 데이터가 있다면
                    // A group with the given invite code exists
                    DataSnapshot groupSnapshot = dataSnapshot.getChildren().iterator().next();
                    String groupId = groupSnapshot.getKey();


                    puDatabaseRef.child(groupId).child("members").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) { // 현재 members 값이 존재하는 경우
                                String currentMembers = dataSnapshot.getValue(String.class); // 현재 members 값을 가져옴
                                if (currentMembers.contains(Uid.getUid())) {
                                    // 이미 해당 유저의 Uid 값이 members에 있는 경우, 추가 작업을 수행하지 않음
                                    Toast.makeText(getActivity(), "이미 그룹에 가입한 유저입니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    // 현재 members 값에 새로운 Uid 값을 추가하여 업데이트함
                                    String newMembers = currentMembers + "," + Uid.getUid();
                                    puDatabaseRef.child(groupId).child("members").setValue(newMembers);
                                    uDatabaseRef.child("UserAccount").child(Uid.getUid()).child("GroupId").setValue(groupId);
                                    Toast.makeText(getActivity(), "그룹 가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                }
                            } else { // 현재 members 값이 존재하지 않는 경우, 새로운 값으로 설정함
                                puDatabaseRef.child(groupId).child("members").setValue(Uid.getUid());
                                uDatabaseRef.child("UserAccount").child(Uid.getUid()).child("GroupId").setValue(groupId);
                                Toast.makeText(getActivity(), "그룹 가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            Toast.makeText(getActivity(), "onCancelled 실행됨", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // The invite code is invalid
                    Toast.makeText(getActivity(), "잘못된 초대 코드입니다.", Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });
    }

    public Join_group() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Join_group.
     */
    // TODO: Rename and change types and number of parameters
    public static Join_group newInstance(String param1, String param2) {
        Join_group fragment = new Join_group();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Join_group newInstance() {
        return new Join_group();
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

        View view = inflater.inflate(R.layout.fragment_join_group,container,false);

        Button btn_insertcode = view.findViewById(R.id.btn_insertcode);
        EditText edt_insertcode = view.findViewById(R.id.Edt_insertcode);

        btn_insertcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String Invite_code = edt_insertcode.getText().toString();
            FirebaseAuth Auth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = Auth.getCurrentUser();
            if (currentUser == null) {
                // 현재 접속중인 사용자가 없는 경우 처리
                return;
            }
            String Uid = currentUser.getUid();
            joinGroup(Invite_code, Uid);

            }
        });
        return view;
    }
}