package com.application.bookmobato.Librarian;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
        holder.setBorrowedDate.setText(studentBookReq.getSetBorrowedDate());

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BorrowStudentDetails.class);
                intent.putExtra("image", list.get(holder.getAdapterPosition()).getImage());
                intent.putExtra("title", list.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("name", list.get(holder.getAdapterPosition()).getName());
                intent.putExtra("borrowedDate", list.get(holder.getAdapterPosition()).getSetBorrowedDate());
                context.startActivity(intent);
            }
        });
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

        TextView title, name, setBorrowedDate;
        ImageView bookImage;

        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.studentReq_title);
            name = itemView.findViewById(R.id.studentReq_name);
            setBorrowedDate = itemView.findViewById(R.id.studentReq_dateBorrowed);
            bookImage = itemView.findViewById(R.id.studentReq_image);
            mainLayout = itemView.findViewById(R.id.studentBookReqLayout);
        }
    }

    public void dialogConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage("Do you want to lend this book?");
        builder.setCancelable(false);
        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, "You accept a request from student...", Toast.LENGTH_SHORT).show();
                builder.setCancelable(true);
            }
        });
        builder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                builder.setCancelable(true);
            }
        });

        builder.create().show();
    }
}