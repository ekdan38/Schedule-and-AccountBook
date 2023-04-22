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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class sign_up extends AppCompatActivity {

    private FirebaseAuth auth; //파이어베이스 인증 객체
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스
    private DatabaseReference pDatabaseRef;
    private EditText Id,Pwd; // 회원가입 입력 필드
    private Button btn_sign_up; // 회원가입 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("SAA");
        pDatabaseRef = FirebaseDatabase.getInstance().getReference("SAA");
        Id = findViewById(R.id.Id);
        Pwd = findViewById(R.id.Pwd);
        btn_sign_up = findViewById(R.id.btn_signup);

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원가입 버튼 누르면 처리될 항목들
                String strId = Id.getText().toString();
                String strPwd = Pwd.getText().toString();
                //firebase auth  진행
                auth.createUserWithEmailAndPassword(strId, strPwd).addOnCompleteListener(sign_up.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()){ //로그인이 됐다면 아래 수행
                            FirebaseUser firebaseUser = auth.getCurrentUser(); //getCurrentUser => 회원가입 이 된 유저를 가져온다.
                            UserAccount account = new UserAccount();
                            account.setIdToken(firebaseUser.getUid());//사용자 고유 값
                            account.setId(firebaseUser.getEmail());
                            account.setPwd(strPwd);

                            UserAccount personal = new UserAccount();
                            personal.setIdToken(firebaseUser.getUid());


                            //setValue=> database 에 insert(삽입) 행위
                            pDatabaseRef.child("Personal_User_DB").child(firebaseUser.getUid()).setValue(personal);
                            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);
                            Toast.makeText(sign_up.this,"회원가입에 성공",Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(sign_up.this,"회원가입에 실패",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }
}