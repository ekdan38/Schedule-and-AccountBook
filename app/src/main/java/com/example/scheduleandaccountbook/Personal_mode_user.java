package com.example.scheduleandaccountbook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Personal_mode_user#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Personal_mode_user extends Fragment {

    private FirebaseAuth auth;
    private DatabaseReference uDatabaseRef;//User Account DB
    private DatabaseReference puDatabaseRef;

//
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Personal_mode_user() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment user.
     */
    // TODO: Rename and change types and number of parameters
    public static Personal_mode_user newInstance(String param1, String param2) {
        Personal_mode_user fragment = new Personal_mode_user();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Personal_mode_user newInstance() {
        return new Personal_mode_user();
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
        View view = inflater.inflate(R.layout.fragment_personal_mode_user,container,false);
        //로그아웃 처리 시작
        auth = FirebaseAuth.getInstance();
        Button btn_logout = view.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent = new Intent(getActivity(), login.class);
                startActivity(intent);
                getActivity().finish();
                Toast.makeText(getActivity(), "로그아웃", Toast.LENGTH_SHORT).show();//로그아웃
            }
        });
        //로그아웃 처리 끝
        //회원 탈퇴 시작
        Button btn_withdrawal = view.findViewById(R.id.btn_withdrawal);
        btn_withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                auth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            uDatabaseRef = FirebaseDatabase.getInstance().getReference("AccountBook");
                            uDatabaseRef.child("UserAccount").child(user.getUid()).removeValue();
                            Toast.makeText(getActivity(), "계정 삭제 완료", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), login.class);
                            startActivity(intent);
                            getActivity().finish();


                        }
                    }
                });

            }
        });
        Button btn_mkGroup = view.findViewById(R.id.btn_mkGroup);
        btn_mkGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_mkGroup:{
                        ((Personal_mode_menu)getActivity()).replaceFragment(Personal_mode_makegroup.newInstance());

                    }
//                        ((Personal_mode_menu)getActivity()).replaceFragment(Personal_mode_makegroup.newInstance());

                }
                    // 다른 버튼 핸들링
                }

        });


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference("AccountBook").child("Public_User_DB");
        groupRef.orderByChild("creatorUid").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot groupSnapshot : dataSnapshot.getChildren()) {
//                    btn_mkGroup.setVisibility(View.INVISIBLE);
                    btn_mkGroup.setText("초대코드");
                    btn_mkGroup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ((Personal_mode_menu)getActivity()).InviteCodeFragment(Show_InviteCode.newInstance());

                        }
                    });
                    String inviteCode = groupSnapshot.child("inviteCode").getValue(String.class);

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        //회원 탈퇴 끝
        return view;

    }
}