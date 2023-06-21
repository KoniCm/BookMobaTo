package com.application.bookmobato.Librarian;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.application.bookmobato.R;
import com.application.bookmobato.Student.BorrowStudentClasses;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class StudentBookReqCustomAdapter extends RecyclerView.Adapter<StudentBookReqCustomAdapter.MyViewHolder> {

    Context context;
    ArrayList<BorrowStudentClasses> list;

    public StudentBookReqCustomAdapter(Context context, ArrayList<BorrowStudentClasses> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public StudentBookReqCustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.myrow_studentbookreq, parent, false);
        return new StudentBookReqCustomAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentBookReqCustomAdapter.MyViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImage()).into(holder.bookImage);
        BorrowStudentClasses studentBookReq = list.get(position);
        holder.title.setText(studentBookReq.getTitle());
        holder.name.setText(studentBookReq.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void searchData(ArrayList<BorrowStudentClasses> searchlist) {
        list = searchlist;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, name;
        ImageView bookImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.studentReq_title);
            name = itemView.findViewById(R.id.studentReq_name);
            bookImage = itemView.findViewById(R.id.studentReq_image);
        }
    }
}