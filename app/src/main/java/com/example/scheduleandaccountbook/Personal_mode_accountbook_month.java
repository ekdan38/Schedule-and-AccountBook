package com.example.scheduleandaccountbook;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Personal_mode_accountbook_month#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Personal_mode_accountbook_month extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Personal_mode_accountbook_month() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Personal_mode_accountbook_month.
     */
    // TODO: Rename and change types and number of parameters
    public static Personal_mode_accountbook_month newInstance(String param1, String param2) {
        Personal_mode_accountbook_month fragment = new Personal_mode_accountbook_month();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Personal_mode_accountbook_month newInstance() {
        return new Personal_mode_accountbook_month();
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
        View view = inflater.inflate(R.layout.fragment_personal_mode_accountbook_month,container,false);
        Button btn_day = view.findViewById(R.id.btn_pday);
        Button btn_ocr = view.findViewById(R.id.btn_pocr);

        btn_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.personal_mode_menu_frame, new Personal_mode_accountbook());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        btn_ocr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Personal_mode_menu)getActivity()).replaceFragment(Personal_mode_accountbook_ocr.newInstance());

            }
        });
        return view;
    }
}