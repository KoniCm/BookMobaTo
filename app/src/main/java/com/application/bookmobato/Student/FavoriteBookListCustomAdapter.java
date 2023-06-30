package com.application.bookmobato.Student;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.application.bookmobato.Librarian.BookClasses;
import com.application.bookmobato.Librarian.BookDetails;
import com.application.bookmobato.Librarian.BookListActivity;
import com.application.bookmobato.Librarian.UpdateBookActivity;
import com.application.bookmobato.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class FavoriteBookListCustomAdapter extends RecyclerView.Adapter<FavoriteBookListCustomAdapter.MyViewHolder> {

    Context context;
    ArrayList<BookClasses> list;

    public FavoriteBookListCustomAdapter(Context context, ArrayList<BookClasses> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FavoriteBookListCustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteBookListCustomAdapter.MyViewHolder holder, final int position) {
        Glide.with(context).load(list.get(position).getImage()).into(holder.bookImage);
        BookClasses book = list.get(position);
        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthor());
        holder.genre.setText(book.getGenre());
        holder.publishdate.setText(book.getPublishdate());
        holder.numpages.setText(book.getNumpages());
        holder.description.setText(book.getDescription());
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.getMenuInflater().inflate(R.menu.delete_onerow, popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();

                        if(id == R.id.delete_row) {
                            Toast.makeText(context, "Processing delete book", Toast.LENGTH_SHORT).show();
                        }

                        return false;
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void searchData(ArrayList<BookClasses> searchlist) {
        list = searchlist;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView bookImage;

        TextView title, author, genre, publishdate, numpages, description, nameStudent;

        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.book_title_txt);
            author = itemView.findViewById(R.id.book_author_txt);
            genre = itemView.findViewById(R.id.book_genre_txt);
            publishdate = itemView.findViewById(R.id.book_publish_txt);
            numpages = itemView.findViewById(R.id.book_pages_txt);
            description = itemView.findViewById(R.id.book_description_txt);
            nameStudent = itemView.findViewById(R.id.student_name_txt);
            bookImage = itemView.findViewById(R.id.place_holder_display);
            mainLayout = itemView.findViewById(R.id.mainLayout);

        }
    }
}