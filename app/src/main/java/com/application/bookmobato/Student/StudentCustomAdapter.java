package com.application.bookmobato.Student;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.bookmobato.Librarian.BookClasses;
import com.application.bookmobato.Librarian.StudentAccountInformation;
import com.application.bookmobato.R;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class StudentCustomAdapter extends RecyclerView.Adapter<StudentCustomAdapter.MyViewHolder> {

    Context context;

    ArrayList<StudentClasses> list;

    public StudentCustomAdapter(Context context, ArrayList<StudentClasses> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public StudentCustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_rowstudent, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentCustomAdapter.MyViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImage()).into(holder.bookImage);
        holder.id.setText(list.get(position).getId());
        holder.name.setText(list.get(position).getName());
        holder.level.setText(list.get(position).getSection());
        holder.section.setText(list.get(position).getStrand());
        holder.strand.setText(list.get(position).getGradelevel());
        holder.pass.setText(list.get(position).getPass());

        holder.studentLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(context, StudentAccountInformation.class);
                intent.putExtra("image", list.get(holder.getAdapterPosition()).getImage());
                intent.putExtra("id", list.get(holder.getAdapterPosition()).getId());
                intent.putExtra("name", list.get(holder.getAdapterPosition()).getName());
                intent.putExtra("level", list.get(holder.getAdapterPosition()).getGradelevel());
                intent.putExtra("section", list.get(holder.getAdapterPosition()).getSection());
                intent.putExtra("strand", list.get(holder.getAdapterPosition()).getStrand());
                intent.putExtra("pass", list.get(holder.getAdapterPosition()).getPass());
                context.startActivity(intent);
                return true;
            }
        });

        holder.studentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateStudentActivity.class);
                intent.putExtra("image", list.get(holder.getAdapterPosition()).getImage());
                intent.putExtra("id", list.get(holder.getAdapterPosition()).getId());
                intent.putExtra("name", list.get(holder.getAdapterPosition()).getName());
                intent.putExtra("level", list.get(holder.getAdapterPosition()).getGradelevel());
                intent.putExtra("section", list.get(holder.getAdapterPosition()).getSection());
                intent.putExtra("strand", list.get(holder.getAdapterPosition()).getStrand());
                intent.putExtra("pass", list.get(holder.getAdapterPosition()).getPass());
                intent.putExtra("Key", list.get(holder.getAdapterPosition()).getKey());
                context.startActivity(intent);
            }
        });
    }
    public void searchData(ArrayList<StudentClasses> searchlist) {
        list = searchlist;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() { return list.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView id, name, level, section, strand, pass;
        ImageView bookImage;

        LinearLayout studentLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.student_id_txt);
            name = itemView.findViewById(R.id.student_name_txt);
            level = itemView.findViewById(R.id.student_level_txt);
            section = itemView.findViewById(R.id.student_section_txt);
            strand = itemView.findViewById(R.id.student_strand_txt);
            pass = itemView.findViewById(R.id.student_pass_txt);
            bookImage = itemView.findViewById(R.id.imageView);

            studentLayout = itemView.findViewById(R.id.studentMainLayout);
        }
    }
}