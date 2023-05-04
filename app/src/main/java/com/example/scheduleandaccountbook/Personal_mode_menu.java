package com.example.scheduleandaccountbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.common.internal.Objects;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Personal_mode_menu extends AppCompatActivity {
    private FrameLayout menu_frame;
    BottomNavigationView bottomNavigationView;
    private DatabaseReference puDatabaseRef;
    private DatabaseReference groupRef;



    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.personal_mode_menu_frame, fragment).commit();
        fragmentTransaction.addToBackStack(null);
    }

    public void InviteCodeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.personal_mode_menu_frame, fragment).commit();
        fragmentTransaction.addToBackStack(null);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        init(); //객체 정의
        SettingListener(); //리스너 등록

        //맨 처음 시작할 탭 설정
        bottomNavigationView.setSelectedItemId(R.id.Personal_mode_tab_accountbook);

//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.Personal_mode_tab_menu, Personal_mode_user.newInstance()).commit();
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.Personal_mode_tab_makegroup, Personal_mode_user.newInstance()).commit();

    }


    private void init() {
        menu_frame = findViewById(R.id.personal_mode_menu_frame);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

    }
    private void SettingListener() {
        //선택 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new Personal_mode_menu.TabSelectedListener());
    }

    class TabSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.Personal_mode_tab_accountbook: {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.personal_mode_menu_frame, new Personal_mode_accountbook())
                            .commit();
                    return true;
                }
                case R.id.Personal_mode_tab_statistics: {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.personal_mode_menu_frame, new Personal_mode_statistics())
                            .commit();
                    return true;
                }

                case R.id.Personal_mode_tab_user: {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.personal_mode_menu_frame, new Personal_mode_user())
                            .commit();
                }
                    return true;
                }


                return false;
            }
        }

}