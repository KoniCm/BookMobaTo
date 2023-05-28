package com.application.bookmobato.Librarian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.application.bookmobato.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
public class BookListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    SwipeRefreshLayout swipeRefreshLayout;

    RecyclerView recyclerView;
    ArrayList<BookClasses> list;
    FloatingActionButton addBook;

    DatabaseReference dataBook;
    LibrarianCustomAdapter librarianCustomAdapter;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        findID();
        addBookInformation();

        swipeRefreshLayout.setOnRefreshListener(BookListActivity.this);
        searchView.clearFocus();

        dataBook = FirebaseDatabase.getInstance().getReference("BookInformation");

        list = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(BookListActivity.this));
        librarianCustomAdapter = new LibrarianCustomAdapter(this, list);
        recyclerView.setAdapter(librarianCustomAdapter);

        dataBook.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BookClasses bookClasses = dataSnapshot.getValue(BookClasses.class);
                    bookClasses.setKey(dataSnapshot.getKey());
                    list.add(bookClasses);
                }
                librarianCustomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error");
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)  {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return false;
            }
        });
    }

    private void addBookInformation() {
        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookListActivity.this, AddingBookActivity.class);
                startActivity(intent);
            }
        });
    }

    private void findID() {
        addBook = findViewById(R.id.add_btnBook);
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.search);
        swipeRefreshLayout = findViewById(R.id.refreshActivity);

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("BookMobaTo");
        builder.setMessage("Do you want to exit an app");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(BookListActivity.this, "Thank your for using the application", Toast.LENGTH_SHORT).show();
                finishAffinity();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.setCancelable(true);
            }
        });
        builder.create().show();
    }
    public void searchList(String text) {
        ArrayList<BookClasses> search = new ArrayList<>();
        for (BookClasses bookClasses : list) {
            if(bookClasses.getTitle().toLowerCase().contains(text.toLowerCase())) {
                search.add(bookClasses);
            }
        }
        librarianCustomAdapter.searchData(search);
    }

    @Override
    public void onRefresh() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BookListActivity.this, "Refreshed Successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BookListActivity.this, BookListActivity.class);
                startActivity(intent);
                librarianCustomAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
    }
}