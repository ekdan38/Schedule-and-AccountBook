package com.example.scheduleandaccountbook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class sign_up extends AppCompatActivity {

    private FirebaseAuth auth; //파이어베이스 인증 객체
    private DatabaseReference uDatabaseRef; // 실시간 데이터베이스
    private DatabaseReference peDatabaseRef;
    private DatabaseReference checkDatabaseRef;
    private EditText Id,Pwd; // 회원가입 입력 필드
    private Button btn_sign_up; // 회원가입 버튼

    private boolean checkDuplicate(List<UserAccount> userList, String id, String pwd) {
        for (UserAccount user : userList) {
            if (user.getId().equals(id)) {
                // 이미 등록된 아이디가 있으면 회원가입을 중단하고 false 반환
                Toast.makeText(sign_up.this, "이미 등록된 아이디입니다.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        // 등록된 아이디가 없으면 회원가입을 진행하고 true 반환
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        uDatabaseRef = FirebaseDatabase.getInstance().getReference("AccountBook");
        peDatabaseRef = FirebaseDatabase.getInstance().getReference("AccountBook");
        Id = findViewById(R.id.Id);
        Pwd = findViewById(R.id.Pwd);
        btn_sign_up = findViewById(R.id.btn_signup);

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원가입 버튼 누르면 처리될 항목들
                String strId = Id.getText().toString();
                String strPwd = Pwd.getText().toString();

                uDatabaseRef.child("UserAccount").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        List<UserAccount> userList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            UserAccount user = dataSnapshot.getValue(UserAccount.class);
                            userList.add(user);
                        }

                        if (checkDuplicate(userList, strId, strPwd)) {
                            // 중복된 아이디가 없으면 회원가입을 진행
                            auth.createUserWithEmailAndPassword(strId, strPwd).addOnCompleteListener(sign_up.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(Task<AuthResult> task) {
                                    if (task.isSuccessful()) { //로그인이 됐다면 아래 수행
                                        FirebaseUser firebaseUser = auth.getCurrentUser(); //getCurrentUser => 회원가입 이 된 유저를 가져온다.
                                        UserAccount account = new UserAccount();
                                        account.setUid(firebaseUser.getUid());//사용자 고유 값
                                        account.setId(firebaseUser.getEmail());
                                        account.setPwd(strPwd);

                                        UserAccount personal = new UserAccount();
                                        personal.setUid(firebaseUser.getUid());

                                        //setValue=> database 에 insert(삽입) 행위
                                        peDatabaseRef.child("Personal_User_DB").child(firebaseUser.getUid()).setValue(personal);
                                        //perseonal에 ID토큰
                                        uDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);
                                        //useraccount에 토큰,아이디,비번
                                        Toast.makeText(sign_up.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
//                                        Toast.makeText(sign_up.this, "회원가입에 실패", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // do nothing
                    }
                });
            }
        });

    }
}