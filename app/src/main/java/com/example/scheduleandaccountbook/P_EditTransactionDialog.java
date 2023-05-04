package com.example.scheduleandaccountbook;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class P_EditTransactionDialog extends DialogFragment {
    private ArrayList<User> arrayList;
    private EditText etAmount, etNote, etcategory;
    private Button btnIncome, btnExpense, btnUpdate, btnCancel,btnDelete;
    private CalendarView calendarView;
    private int year, month, dayOFMonth;

    private boolean isIncomeSelected = false;
    private String note, category, state, date;
    private String databasePath;
    private int amount;
    private CustomAdapter customAdapter;
    private String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String groupId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        View view = inflater.inflate(R.layout.dialog_edit_transaction, container, false);
        etAmount = view.findViewById(R.id.et_amount);
        etAmount.setInputType(InputType.TYPE_CLASS_NUMBER);
        etNote = view.findViewById(R.id.et_memo);
        etcategory = view.findViewById(R.id.et_category);
        btnIncome = view.findViewById(R.id.btn_income);
        btnExpense = view.findViewById(R.id.btn_expense);
        btnUpdate = view.findViewById(R.id.btn_submit);
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnDelete = view.findViewById(R.id.btn_delete);
        calendarView = view.findViewById(R.id.calendar_view);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("AccountBook").child("UserAccount").child(Uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                groupId = dataSnapshot.child("GroupId").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        // 기존 데이터 가져와서 설정하기
        Bundle args = getArguments();
        if (args != null) {
            amount = args.getInt("amount");
            note = args.getString("note");
            category = args.getString("category");
            state = args.getString("state");
            date = args.getString("date");

            // 값 확인을 위한 로그 출력
            Log.d("EditTransactionDialog", "Amount: " + amount);
            Log.d("EditTransactionDialog", "Note: " + note);
            Log.d("EditTransactionDialog", "Category: " + category);
            Log.d("EditTransactionDialog", "State: " + state);
            Log.d("EditTransactionDialog", "Date: " + date);

            etAmount.setText(String.valueOf(amount));
            etNote.setText(note);
            etcategory.setText(category);

            if (state.equals("수입")) {
                isIncomeSelected = true;
                btnIncome.setBackgroundColor(Color.parseColor("#e74c3c"));
                btnExpense.setBackgroundColor(Color.parseColor("#00ff0000"));
            } else {
                isIncomeSelected = false;
                btnIncome.setBackgroundColor(Color.parseColor("#00ff0000"));
                btnExpense.setBackgroundColor(Color.parseColor("#e74c3c"));
            }
        }

        // 날짜 설정하기
        calendarView.setDate(Calendar.getInstance().getTimeInMillis(), false, true);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int dayOFMonth) {
                P_EditTransactionDialog.this.year = year;
                P_EditTransactionDialog.this.month = month;
                P_EditTransactionDialog.this.dayOFMonth = dayOFMonth;
            }
        });

        etAmount.addTextChangedListener(new TextWatcher() {
            private String lastAmount = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(lastAmount)) {
                    etAmount.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[^\\d]", "");
                    if (cleanString.length() > 0) {
                        double parsed = Double.parseDouble(cleanString);
                        String formatted = decimalFormat.format(parsed); // 천 단위 구분 기호 추가
                        etAmount.setText(formatted + "원"); // 원 추가
                        etAmount.setSelection(formatted.length() + 1); // 원도 포함해서 커서 위치 조정
                    }

                    lastAmount = etAmount.getText().toString();
                    etAmount.addTextChangedListener(this);
                }
            }
        });

        btnIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isIncomeSelected = true;
                btnIncome.setBackgroundColor(Color.parseColor("#e74c3c"));
                btnExpense.setBackgroundColor(Color.parseColor("#00ff0000"));
            }
        });

        btnExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isIncomeSelected = false;
                btnIncome.setBackgroundColor(Color.parseColor("#00ff0000"));
                btnExpense.setBackgroundColor(Color.parseColor("#e74c3c"));
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String amount = etAmount.getText().toString().trim();
                String newnote = etNote.getText().toString().trim();
                String newcategory = etcategory.getText().toString().trim();

                // 날짜 가져오기
                Calendar calendar = Calendar.getInstance();
                if (year == 0 && month == 0 && dayOFMonth == 0) {
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH);
                    dayOFMonth = calendar.get(Calendar.DAY_OF_MONTH);
                }
                calendar.set(year, month, dayOFMonth);
                String ndate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(calendar.getTime());

                if (state.equals("지출")){
                    databasePath = "AccountBook/Public_User_DB/" + groupId + "/expense";
                }
                else{
                    databasePath = "AccountBook/Public_User_DB/" + groupId + "/income";
                }
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference(databasePath);
                databaseRef.orderByChild("date").equalTo(date)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    String oldState = snapshot.child("state").getValue(String.class);
//                                        int oldAmount = snapshot.child("amount").getValue(Integer.class);
                                    String oldCategory = snapshot.child("category").getValue(String.class);
                                    String oldMemo = snapshot.child("memo").getValue(String.class);

                                    if (oldState.equals(state) && oldCategory.equals(category) && oldMemo.equals(note)) {
                                        snapshot.getRef().removeValue();
                                        if (isIncomeSelected) {
                                            isIncomeSelected = true;
                                            //수입 저장
                                            String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                            DatabaseReference incomeRef = FirebaseDatabase.getInstance()
                                                    .getReference("AccountBook/Public_User_DB/" + groupId)
                                                    .child("income");
//                            .child(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));

                                            int amountInt = Integer.parseInt(amount.replace(",", "").replace("원", ""));
                                            HashMap<String, Object> incomeData = new HashMap<>();
                                            incomeData.put("state", "수입");
                                            incomeData.put("amount", amountInt);
                                            incomeData.put("category", newcategory);
                                            incomeData.put("memo", newnote);
                                            incomeData.put("date", ndate);
                                            incomeRef.push().setValue(incomeData);
//
                                        }else{
                                            DatabaseReference expenseRef = FirebaseDatabase.getInstance()
                                                    .getReference("AccountBook/Public_User_DB/"+groupId)
                                                    .child("expense");
//                            .child(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
                                            HashMap<String, Object> expenseData = new HashMap<>();
                                            int amountInt = Integer.parseInt(amount.replace(",", "").replace("원", ""));

                                            expenseData.put("state", "지출");
                                            expenseData.put("amount", amountInt);
                                            expenseData.put("category", newcategory);
                                            expenseData.put("memo", newnote);
                                            expenseData.put("date" ,ndate);
                                            expenseRef.push().setValue(expenseData);
                                        }

                                    }
                                    else{

                                        Toast.makeText(getActivity(), "수정 안되다야",Toast.LENGTH_LONG).show();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {

                            }
                        });
                dismiss();
                Intent intent = new Intent(getActivity(), Personal_mode_menu.class);
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
                getActivity().finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = etAmount.getText().toString().trim();
                String note = etNote.getText().toString().trim();
                String category = etcategory.getText().toString().trim();

                Log.d("삭제버튼", "Amount: " + amount);
                Log.d("삭제버튼", "Note: " + note);
                Log.d("삭제버튼", "Category: " + category);
//                    Log.d("삭제버튼", "State: " + state);
//                    Log.d("삭제버튼", "Date: " + date);

                // 날짜 가져오기
                Calendar calendar = Calendar.getInstance();
                if (year == 0 && month == 0 && dayOFMonth == 0) {
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH);
                    dayOFMonth = calendar.get(Calendar.DAY_OF_MONTH);
                }
                calendar.set(year, month, dayOFMonth);
                String ndate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(calendar.getTime());

                String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                if (state.equals("지출")){
                    databasePath = "AccountBook/Public_User_DB/" + groupId + "/expense";
                }
                else{
                    databasePath = "AccountBook/Public_User_DB/" + groupId + "/income";
                }
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference(databasePath);
//                    databaseRef.orderByChild("date").equalTo(date)
//                            .addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
//                                        String oldState = snapshot.child("state").getValue(String.class);
////                                        int oldAmount = snapshot.child("amount").getValue(Integer.class);
//                                        String oldCategory = snapshot.child("category").getValue(String.class);
//                                        String oldMemo = snapshot.child("memo").getValue(String.class);
//
//                                        if (oldState.equals(state) && oldCategory.equals(category) && oldMemo.equals(note)) {
//                                            snapshot.getRef().removeValue();
//
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError error) {
//
//                                }
//                            });
                databaseRef.orderByChild("date").equalTo(date)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String oldState = snapshot.child("state").getValue(String.class);
                                    String oldCategory = snapshot.child("category").getValue(String.class);
                                    String oldMemo = snapshot.child("memo").getValue(String.class);

                                    if (oldState.equals(state) && oldCategory.equals(category) && oldMemo.equals(note)) {
                                        snapshot.getRef().removeValue();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                Toast.makeText(getActivity(), "삭제에 실패했습니다.", Toast.LENGTH_LONG).show();
                            }
                        });

                dismiss();
                Intent intent = new Intent(getActivity(), Public_mode_menu.class);
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
                getActivity().finish();
            }
        });

        return view;
    }
}
