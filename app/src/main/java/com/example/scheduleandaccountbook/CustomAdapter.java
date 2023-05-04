package com.example.scheduleandaccountbook;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<User> arrayList;
    private Context context;

    public void updateData(ArrayList<User> newarrayList){
        arrayList = newarrayList;
        notifyDataSetChanged();
    }


    public CustomAdapter(ArrayList<User> list, Context context) {
        this.arrayList = list;
        this.context = context;
        Collections.sort(arrayList, new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date date1 = format.parse(user1.getDate());
                    Date date2 = format.parse(user2.getDate());
                    return date2.compareTo(date1); // 내림차순으로 정렬
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent,false);
        context = parent.getContext(); // sdf
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        holder.tv_state.setText(arrayList.get(position).getState());
        holder.tv_amount.setText(String.valueOf(arrayList.get(position).getAmount()));
        holder.tv_category.setText(arrayList.get(position).getCategory());
        holder.tv_memo.setText(arrayList.get(position).getMemo());


        User user = arrayList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                EditTransactionDialog editTransactionDialog = new EditTransactionDialog();
                Bundle args = new Bundle();
                args.putInt("amount", user.getAmount());
                args.putString("note", user.getMemo());
                args.putString("category", user.getCategory());
                args.putString("category", user.getCategory());
                args.putString("state", user.getState());
                args.putString("date", user.getDate());
                editTransactionDialog.setArguments(args);
                editTransactionDialog.show(fragmentManager, "EditTransactionDialog");
            }
        });
    }
//                Toast.makeText(context, "Clicked item at position: " + holder.getAdapterPosition(),Toast.LENGTH_LONG).show();
//                Toast.makeText(context, "상태: " + user.getState(),Toast.LENGTH_LONG).show();
//                Toast.makeText(context, "가격: " + user.getAmount(),Toast.LENGTH_LONG).show();
//                Toast.makeText(context, "카테고리: " + user.getCategory(),Toast.LENGTH_LONG).show();
//                Toast.makeText(context, "메모: " + user.getMemo(),Toast.LENGTH_LONG).show();
//                  Toast.makeText(context, "메모: " + user.getDate(),Toast.LENGTH_LONG).show();

//                User user = arrayList.get(holder.getAdapterPosition());
//                EditTransactionDialog dialog = new EditTransactionDialog(context, user);
//                dialog.setOnTransactionEditListener(new EditTransactionDialog.OnTransactionEditListener() {
//                    @Override
//                    public void onTransactionEdit(User editedUser) {
//                        // 데이터 수정이 완료된 경우 호출되는 콜백 메서드
//                        // 여기서 데이터를 업데이트하거나 저장할 수 있습니다.
//                        updateTransaction(editedUser); // 데이터 업데이트 메서드 호출
//                    }
//
//                    @Override
//                    public void onTransactionDelete() {
//                        // 데이터 삭제 버튼이 클릭된 경우 호출되는 콜백 메서드
//                        deleteTransaction(user); // 데이터 삭제 메서드 호출
//                    }
//                });
//            }
//        });
//    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tv_state;
        TextView tv_amount;
        TextView tv_category;
        TextView tv_memo;

        public CustomViewHolder(View itemView) {
            super(itemView);
            this.tv_state = itemView.findViewById(R.id.tv_pay);
            this.tv_amount = itemView.findViewById(R.id.tv_amount);
            this.tv_category = itemView.findViewById(R.id.tv_category);
            this.tv_memo = itemView.findViewById(R.id.tv_memo);


        }
    }

}
