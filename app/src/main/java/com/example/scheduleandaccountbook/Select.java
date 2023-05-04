package com.example.scheduleandaccountbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class Select extends AppCompatActivity {
    private FirebaseAuth auth; //파이어베이스 인증 객체
    private DatabaseReference uDatabaseRef; // 실시간 데이터베이스
    private DatabaseReference peDatabaseRef;
    private DatabaseReference puDatabaseRef;

                ///////////////////////////////////////////////
    public Button btn_personal_mode, btn_public_mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        uDatabaseRef = FirebaseDatabase.getInstance().getReference("AccountBook").child("UserAccount");
        peDatabaseRef = FirebaseDatabase.getInstance().getReference("AccountBook").child("Personal_User_DB");

//        String userIdToken = FirebaseAuth.getInstance().getCurrentUser().getIdToken(0);


        btn_personal_mode = findViewById(R.id.btn_personal_mode);//개인 사용 모드
        btn_personal_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), Personal_mode_menu.class);//일단은 메뉴로 넘어가게
                startActivity(intent);
            }
        });


        btn_public_mode = findViewById(R.id.btn_public_mode);//모임 방 사용 모드
        btn_public_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); //접속중인 사용자의 Uid 받아온다
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("AccountBook").child("UserAccount").child(uid);
                DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference("AccountBook").child("Public_User_DB");

                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String groupId = dataSnapshot.child("GroupId").getValue(String.class);
                        if (groupId != null) {
                            //만약에 사용자의 Uid에서 groupId가 존재한다면 *groupId == null이면 가입되어있는 group이 없다는 뜻이다.
                            //존재한다면 아래 실행
                            groupRef.child(groupId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String members = dataSnapshot.child("members").getValue(String.class);
                                    if ((members.contains("," + uid + ",")) || (members.contains(uid + ","))
                                            || (members.contains("," + uid)) || (members.contains(uid))) {
                                        Intent intent = new Intent(getApplicationContext(), Public_mode_menu.class);//일단은 메뉴로 넘어가게
                                        startActivity(intent);
                                        return;
                                    } else if(members == null) {
                                        members = "개인회원";
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                    Log.e("TAG", "에러 메시지");

                                }
                            });
                        } else {//사용자가 group에 가입되어있지 않다면 아래 실행
                            Toast.makeText(Select.this,"공유 가계부에 가입하거나 만드세요! \n마이페이지 > 공유 가계부 만들기 \n" +
                                    " 공유 가계부 가입",Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // 에러 처리
                        Log.e("TAG", "에러 메시지");

                    }
                });

            }
        });
    }
}
