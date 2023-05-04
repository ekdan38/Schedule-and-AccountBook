package com.example.scheduleandaccountbook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Public_mode_user#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Public_mode_user extends Fragment {
    private DatabaseReference uDatabaseRef;//User Account DB

    private FirebaseAuth auth;
    private DatabaseReference mDatabaseRef;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Public_mode_user() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Public_mode_user.
     */
    // TODO: Rename and change types and number of parameters
    public static Public_mode_user newInstance(String param1, String param2) {
        Public_mode_user fragment = new Public_mode_user();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_public_mode_user,container,false);
        Button btn_logout = view.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("로그아웃");
                builder.setMessage("정말 로그아웃 하시겠습니까?");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        auth.signOut();
                        Intent intent = new Intent(getActivity(), login.class);
                        startActivity(intent);
                        getActivity().finish();
                        Toast.makeText(getContext(), "로그아웃", Toast.LENGTH_SHORT).show(); //로그아웃
                    }
                });
                builder.setNegativeButton("아니요", null);
                builder.show();
            }


        });


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
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("계정 삭제");
                            builder.setMessage("정말 계정을 삭제 하시겠습니까?");
                            builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    uDatabaseRef.child("UserAccount").child(user.getUid()).removeValue();
                                    Toast.makeText(getActivity(), "계정 삭제 완료", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), login.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }
                            });
                            builder.setNegativeButton("아니요", null);
                            builder.show();



                        }
                    }
                });

            }
        });
        Button btn_invitecode = view.findViewById(R.id.btn_invitecodde);
        btn_invitecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Public_mode_menu)getActivity()).replaceFragment(Show_InviteCode.newInstance());

            }
        });

        //회원 탈퇴 끝
        return view;

    }}