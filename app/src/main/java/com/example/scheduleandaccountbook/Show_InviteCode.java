package com.example.scheduleandaccountbook;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Show_InviteCode#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Show_InviteCode extends Fragment {

    private DatabaseReference puDatabaseRef;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Show_InviteCode() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Show_InviteCode.
     */
    // TODO: Rename and change types and number of parameters
    public static Show_InviteCode newInstance(String param1, String param2) {
        Show_InviteCode fragment = new Show_InviteCode();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Show_InviteCode newInstance() {
        return new Show_InviteCode();
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
        View view = inflater.inflate(R.layout.fragment_show__invite_code, container, false);

        TextView text2 = view.findViewById(R.id.textView2);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("AccountBook").child("UserAccount").child(uid);
        DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference("AccountBook").child("Public_User_DB");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String groupId = dataSnapshot.child("GroupId").getValue(String.class);
                groupRef.child(groupId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String members = dataSnapshot.child("members").getValue(String.class);
                            if ((members.contains("," + uid + ",")) || (members.contains(uid + ","))
                                    || (members.contains("," + uid)) || (members.contains(uid)) ) {
                                String inviteCode = dataSnapshot.child("inviteCode").getValue(String.class);
                                text2.setText(inviteCode);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // 에러 처리
            }
        });


//        DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference("AccountBook").child("Public_User_DB");
//        groupRef.orderByChild("creatorUid").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot groupSnapshot : dataSnapshot.getChildren()) {
//                    String inviteCode = groupSnapshot.child("inviteCode").getValue(String.class);
//                    text2.setText(inviteCode);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//
//            }
//        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    // handle back button press here
                    getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().getSupportFragmentManager().popBackStack();

                    return true;
                }
                return false;
            }
        });
        return view;
    }
}