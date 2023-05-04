package com.example.scheduleandaccountbook;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Personal_mode_accountbook#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Personal_mode_accountbook extends Fragment {

    private TextView dateTextView;
    private String currentDate;
//다이어로그 클래스 불러오기 => AddTransactionDialog
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<User> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private TextView tv_income;
    private TextView tv_expense;
    private TextView tv_amount;
    private int totalAmount = 0;
    private String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();



    private void showAddTransactionDialog(){//다이어로그 메소드
        AddTransactionDialog dialog = new AddTransactionDialog();
        dialog.show(getActivity().getSupportFragmentManager(), "AddTransactionDialog");

    }
    private void loatTotal(){
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("AccountBook/Personal_User_DB")
                .child(Uid);
        databaseRef.keepSynced(true);
        Query expenseQuery = databaseRef.child("expense")
                .orderByChild("date")
                .startAt(currentDate)
                .endAt(currentDate + "\uf8ff");
        expenseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int totalExpense = 0;
                for(DataSnapshot expenseSnapshot : dataSnapshot.getChildren()){
                    int amount = expenseSnapshot.child("amount").getValue(Integer.class);
                    totalExpense += amount;
                    totalAmount -=amount;
                }
                String formattedExpense = String.format("%,d원", totalExpense);
                String formattedAmount = String.format("%,d원", totalAmount);

                tv_expense.setText(formattedExpense);
                tv_amount.setText(formattedAmount);

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        Query incomeQuery = databaseRef.child("income")
                .orderByChild("date")
                .startAt(currentDate)
                .endAt(currentDate + "\uf8ff");
        incomeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int totalIncome = 0;
                for(DataSnapshot expenseSnapshot : dataSnapshot.getChildren()){
                    int amount = expenseSnapshot.child("amount").getValue(Integer.class);
                    totalIncome += amount;
                    totalAmount +=amount;

                }
                String formattedIncome = String.format("%,d원", totalIncome);
                String formattedAmount = String.format("%,d원", totalAmount);
                tv_income.setText(formattedIncome);
                tv_amount.setText(formattedAmount);

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


        totalAmount = 0;
    }
    private void loadData(){
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("AccountBook/Personal_User_DB")
                .child(Uid);
        databaseRef.keepSynced(true);

// 불러올 데이터 리스트 초기화
        List<User> expenseList = new ArrayList<>();
        List<User> incomeList = new ArrayList<>();

// 오늘 날짜에 해당하는 expense 데이터를 불러오기
        Query expenseQuery = databaseRef.child("expense")
                .orderByChild("date")
                .startAt(currentDate)
                .endAt(currentDate + "\uf8ff");
        expenseQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    expenseList.add(user);
                }
                // 불러온 데이터를 income 데이터와 합치기
                Query incomeQuery = databaseRef.child("income")
                        .orderByChild("date")
                        .startAt(currentDate)
                        .endAt(currentDate + "\uf8ff");
                incomeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);
                            incomeList.add(user);
                        }
                        // expense와 income 데이터를 번갈아가면서 표시하기 위해 리스트를 병합
                        List<User> mergedList = new ArrayList<>();
                        int expenseSize = expenseList.size();
                        int incomeSize = incomeList.size();
                        int i = 0, j = 0;
                        while (i < expenseSize && j < incomeSize) {
                            if (expenseList.get(i).getDate().compareTo(incomeList.get(j).getDate()) > 0) {
                                mergedList.add(expenseList.get(i));
                                i++;
                            } else {
                                mergedList.add(incomeList.get(j));
                                j++;
                            }
                        }
                        while (i < expenseSize) {
                            mergedList.add(expenseList.get(i));
                            i++;
                        }
                        while (j < incomeSize) {
                            mergedList.add(incomeList.get(j));
                            j++;
                        }
                        // 어댑터에 데이터 설정
                        CustomAdapter adapter = new CustomAdapter(new ArrayList<>(mergedList), getContext());
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
//    private String getTime(){//현재 시간 string으로 저장하는 메소드\
//        Calendar mCalendar = Calendar.getInstance();
//        today = mCalendar.getTime();
//        String mDateFormat = "yyyy-MM-dd";
//        String getTime = new SimpleDateFormat(mDateFormat).format(today);
//
//        return getTime;
//    }
    private String setCurrentDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        currentDate = dateFormat.format(new Date());
        dateTextView.setText(currentDate);
        return currentDate;
    }



    public void previousDate(View view) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = dateFormat.parse(currentDate);
            if (date != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                currentDate = dateFormat.format(calendar.getTime());
                dateTextView.setText(currentDate);
                loadData();
                loatTotal();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void nextDate(View view) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = dateFormat.parse(currentDate);
            if (date != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                currentDate = dateFormat.format(calendar.getTime());
                dateTextView.setText(currentDate);
                loadData();
                loatTotal();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Personal_mode_accountbook() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Personal_mode_accountbook.
     */
    // TODO: Rename and change types and number of parameters
    public static Personal_mode_accountbook newInstance(String param1, String param2) {
        Personal_mode_accountbook fragment = new Personal_mode_accountbook();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public static Personal_mode_accountbook newInstance() {
        return new Personal_mode_accountbook();
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


        View view = inflater.inflate(R.layout.fragment_personal_mode_accountbook,container,false);

        tv_income = view.findViewById(R.id.tv_income);
        tv_expense = view.findViewById(R.id.tv_expense);
        tv_amount = view.findViewById(R.id.tv_total);


        Button btn_pmonth = view.findViewById(R.id.btn_pmonth);
        Button btn_pocr = view.findViewById(R.id.btn_pocr);

        btn_pmonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Personal_mode_menu)getActivity()).replaceFragment(Personal_mode_accountbook_month.newInstance());
            }
        });
        btn_pocr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Personal_mode_menu)getActivity()).replaceFragment(Personal_mode_accountbook_ocr.newInstance());
            }
        });

        //////////////////////////////////////////////////////////
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();//User 객체를 담을 어레이 리스트(_어댑터쪽으로)

        dateTextView = view.findViewById(R.id.getTime);
        String a = setCurrentDate();
        dateTextView.setText(a);
//        currentDate = setCurrentDate();
//        tgettime.setText(getTime()); // 현재 시간으로 TextView 설정

        //////////////////////////////////////////////////////////
        //날짜 버트으로 바꾸기
        ImageButton btn_back = view.findViewById(R.id.back);
        ImageButton btn_next = view.findViewById(R.id.next);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextDate(view);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousDate(view);
            }
        });

//////////////////////////////////////////////////////////////////////////////////////////////////////////////

        FloatingActionButton add_fab = view.findViewById(R.id.add_fab);
        add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTransactionDialog();

            }
        });
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
        loadData();
        loatTotal();
        return view;
    }

}