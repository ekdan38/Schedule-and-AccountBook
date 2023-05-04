package com.example.scheduleandaccountbook;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddTransactionDialog extends DialogFragment {

    private EditText etAmount, etNote, etcategory;
    private Button btnIncome, btnExpense, btnAdd, btnCancel;
    private CalendarView calendarView;
    private int year,month,dayOFMonth;

    private boolean isIncomeSelected = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        View view = inflater.inflate(R.layout.dialog_add_transaction, container, false);
        etAmount = view.findViewById(R.id.et_amount);
        etAmount.setInputType(InputType.TYPE_CLASS_NUMBER);
        etNote = view.findViewById(R.id.et_memo);
        etcategory = view.findViewById(R.id.et_category);
        btnIncome = view.findViewById(R.id.btn_income);
        btnExpense = view.findViewById(R.id.btn_expense);
        btnAdd = view.findViewById(R.id.btn_submit);
        btnCancel = view.findViewById(R.id.btn_cancel);
        calendarView = view.findViewById(R.id.calendar_view);
        calendarView.setDate(Calendar.getInstance().getTimeInMillis(), false, true);
        etAmount.setInputType(InputType.TYPE_CLASS_NUMBER);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int dayOFMonth) {
                AddTransactionDialog.this.year = year;
                AddTransactionDialog.this.month = month;
                AddTransactionDialog.this.dayOFMonth = dayOFMonth;
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

                    String cleanString = s.toString().replaceAll("[^\\d]", ""); // 숫자 이외의 문자 모두 제거
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
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = etAmount.getText().toString().trim();
                String note = etNote.getText().toString().trim();
                String category = etcategory.getText().toString().trim();

                // 날짜 가져오기
                Calendar calendar = Calendar.getInstance();
//                calendar.set(year, month, dayOFMonth);
                if(year ==0 &&month==0&&dayOFMonth==0){
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH);
                    dayOFMonth = calendar.get(Calendar.DAY_OF_MONTH);
                }
                calendar.set(year, month, dayOFMonth);
                String date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(calendar.getTime());
                if (isIncomeSelected){
                    isIncomeSelected = true;
                    //수입 저장
                    String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference incomeRef = FirebaseDatabase.getInstance()
                            .getReference("AccountBook/Personal_User_DB/"+Uid)
                            .child("income");
//                            .child(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));

                    int amountInt = Integer.parseInt(amount.replace(",", "").replace("원", ""));
                    HashMap<String, Object> incomeData = new HashMap<>();
                    incomeData.put("state", "수입");
                    incomeData.put("amount", amountInt);
                    incomeData.put("category", category);
                    incomeData.put("memo", note);
                    incomeData.put("date", date);
                    incomeRef.push().setValue(incomeData);
                }else{
                    String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference expenseRef = FirebaseDatabase.getInstance()
                            .getReference("AccountBook/Personal_User_DB/"+Uid)
                            .child("expense");
//                            .child(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
                    HashMap<String, Object> expenseData = new HashMap<>();
                    int amountInt = Integer.parseInt(amount.replace(",", "").replace("원", ""));

                    expenseData.put("state", "지출");
                    expenseData.put("amount", amountInt);
                    expenseData.put("category", category);
                    expenseData.put("memo", note);
                    expenseData.put("date" ,date);
                    expenseRef.push().setValue(expenseData);
                }
                dismiss();
                Intent intent = new Intent(getActivity(), Personal_mode_menu.class);//일단은 메뉴로 넘어가게
                startActivity(intent);
                getActivity().overridePendingTransition(0,0);
                getActivity().finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

            }
        });
        return view;
    }}
