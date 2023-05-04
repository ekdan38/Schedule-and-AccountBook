package com.example.scheduleandaccountbook;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Public_mode_accountbook_ocr#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Public_mode_accountbook_ocr extends Fragment {
    private Button btn_day, btn_month, btn_ocr;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Public_mode_accountbook_ocr() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Public_mode_accountbook_ocr.
     */
    // TODO: Rename and change types and number of parameters
    public static Public_mode_accountbook_ocr newInstance(String param1, String param2) {
        Public_mode_accountbook_ocr fragment = new Public_mode_accountbook_ocr();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Public_mode_accountbook_ocr newInstance() {
        return new Public_mode_accountbook_ocr();
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
        View view = inflater.inflate(R.layout.fragment_public_mode_accountbook_ocr,container,false);

        btn_day= view.findViewById(R.id.btn_day);
        btn_month = view.findViewById(R.id.btn_month);
        btn_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.public_mode_menu_frame, new Public_mode_accountbook());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        btn_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Public_mode_menu)getActivity()).replaceFragment(Public_mode_accountbook_month.newInstance());

            }
        });

        return view;
    }
}