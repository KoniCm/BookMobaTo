package com.application.bookmobato.Librarian;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.application.bookmobato.R;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class LibrarianCustomAdapter extends RecyclerView.Adapter<LibrarianCustomAdapter.MyViewHolder> {

    Context context;
    ArrayList<BookClasses> list;

    public LibrarianCustomAdapter(Context context, ArrayList<BookClasses> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public LibrarianCustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibrarianCustomAdapter.MyViewHolder holder, final int position) {
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
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.details_edit, popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();

                        if(id == R.id.edit_menu) {
                            Intent intent = new Intent(context, UpdateBookActivity.class);
                            intent.putExtra("image", list.get(holder.getAdapterPosition()).getImage());
                            intent.putExtra("title", list.get(holder.getAdapterPosition()).getTitle());
                            intent.putExtra("author", list.get(holder.getAdapterPosition()).getAuthor());
                            intent.putExtra("genre", list.get(holder.getAdapterPosition()).getGenre());
                            intent.putExtra("publishdate", list.get(holder.getAdapterPosition()).getPublishdate());
                            intent.putExtra("numpages", list.get(holder.getAdapterPosition()).getNumpages());
                            intent.putExtra("description", list.get(holder.getAdapterPosition()).getDescription());
                            intent.putExtra("Key", list.get(holder.getAdapterPosition()).getKey());
                            context.startActivity(intent);
                        } else if(id == R.id.view_menu) {
                            Intent intent = new Intent(context, BookDetails.class);
                            intent.putExtra("image", list.get(holder.getAdapterPosition()).getImage());
                            intent.putExtra("title", list.get(holder.getAdapterPosition()).getTitle());
                            intent.putExtra("author", list.get(holder.getAdapterPosition()).getAuthor());
                            intent.putExtra("genre", list.get(holder.getAdapterPosition()).getGenre());
                            intent.putExtra("publishdate", list.get(holder.getAdapterPosition()).getPublishdate());
                            intent.putExtra("numpages", list.get(holder.getAdapterPosition()).getNumpages());
                            intent.putExtra("description", list.get(holder.getAdapterPosition()).getDescription());
                            intent.putExtra("Key", list.get(holder.getAdapterPosition()).getKey());
                            context.startActivity(intent);
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

        TextView title, author, genre, publishdate, numpages, description;

        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.book_title_txt);
            author = itemView.findViewById(R.id.book_author_txt);
            genre = itemView.findViewById(R.id.book_genre_txt);
            publishdate = itemView.findViewById(R.id.book_publish_txt);
            numpages = itemView.findViewById(R.id.book_pages_txt);
            description = itemView.findViewById(R.id.book_description_txt);
            bookImage = itemView.findViewById(R.id.place_holder_display);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}