package com.example.scheduleandaccountbook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

    public class Public_mode_menu extends AppCompatActivity {
    private FrameLayout menu_frame;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_mode_menu);

        init(); //객체 정의
        SettingListener(); //리스너 등록

        //맨 처음 시작할 탭 설정
        bottomNavigationView.setSelectedItemId(R.id.public_mode_tab_accountbook);
    }
        private void init() {
            menu_frame = findViewById(R.id.public_mode_menu_frame);
            bottomNavigationView = findViewById(R.id.bottomNavigationView);
        }
        private void SettingListener() {
            //선택 리스너 등록
            bottomNavigationView.setOnNavigationItemSelectedListener(new Public_mode_menu.TabSelectedListener());
        }

        class TabSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.public_mode_tab_accountbook: {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.public_mode_menu_frame, new Public_mode_accountbook())
                                .commit();
                        return true;
                    }

                    case R.id.public_mode_tab_statistics: {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.public_mode_menu_frame, new Public_mode_statistics())
                                .commit();
                        return true;
                    }

                    case R.id.public_mode_tab_user: {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.public_mode_menu_frame, new Public_mode_user())
                                .commit();
                        return true;
                    }
                }

                return false;
            }
        }

    }
