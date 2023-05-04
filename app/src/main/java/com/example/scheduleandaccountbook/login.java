package com.example.scheduleandaccountbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private DatabaseReference uDatabaseRef; // user account 실시간 db
    private EditText Id,Pwd; // 회원가입 입력 필드

    private SignInButton btn_google;// 구글 로그인 버튼
    private FirebaseAuth auth; //파이어베이스 인증 객체
    private GoogleApiClient googleApiClient; //구글 API 클라이언트 객체
    private static final int REQ_SIGN_GOOGLE = 100; //구글 로그인 결과 코드

//    private DatabaseReference mDatabaseRef; // 실시간데이터 베이스
//    private EditText Id, Pwd;//회원가입 아이디 패스워드 입력 필드
//    private Button Sign_up; //회원가입 버튼
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        auth = FirebaseAuth.getInstance();
        uDatabaseRef = FirebaseDatabase.getInstance().getReference("AccountBook");

        Id = findViewById(R.id.Id);
        Pwd = findViewById(R.id.Pwd);


        Button btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //로그인 요청
                String strId = Id.getText().toString();
                String strPwd = Pwd.getText().toString();
                auth.signInWithEmailAndPassword(strId,strPwd).addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //로그인 성공
                            Toast.makeText(login.this,"로그인성공",Toast.LENGTH_SHORT).show();//로그인이 성공했으면
                            Intent intent = new Intent(getApplicationContext(), Select.class);
                            startActivity(intent);
                            finish(); //로그인 액티비티 파괴하고 넘어가자!
                        }else {//로그인 실패
                            Toast.makeText(login.this,"로그인 실패",Toast.LENGTH_SHORT).show();//로그인이 성공했으면

                        }
                    }
                });
            }
        });

        Button btn_sign_up = findViewById(R.id.btn_signup);
        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원가입 버튼 눌렀을때 행동
                Intent intent = new Intent(login.this, sign_up.class);
                startActivity(intent);
            }
        });




        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        auth = FirebaseAuth.getInstance();
        btn_google = findViewById(R.id.btn_google_login);//구글 로그인버튼 누르면
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, REQ_SIGN_GOOGLE);
            }
        });
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//구글 로그인 인증 요청 했을 때 결과값 돌려받는곳
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_SIGN_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {//인증 결과가 성공적이면
                GoogleSignInAccount account = result.getSignInAccount(); //account라는 데이터는 구글 로그인 정보를 갖고 있다(닉네임,프로필,등등)
                resultLogin(account); //로그인 결과값 출력하라는 메소드
            }
        }
    }
    private void resultLogin(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(login.this,"구글 로그인성공",Toast.LENGTH_SHORT).show();//로그인이 성공했으면
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("AccountBook").child("UserAccount").child(uid);
                            if(userRef == null)
                            {
                                userRef.child(uid).setValue(uid);
                                Toast.makeText(login.this,"null인디",Toast.LENGTH_SHORT).show();//로그인이 성공했으면

                            }
                            Intent intent = new Intent(getApplicationContext(), Personal_mode_menu.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(login.this,"로그인 실패",Toast.LENGTH_SHORT).show();//로그인이 성공했으면

                        }


                    }
                });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}


