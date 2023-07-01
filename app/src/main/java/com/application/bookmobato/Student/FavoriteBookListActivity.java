package com.application.bookmobato.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.application.bookmobato.Librarian.BookClasses;
import com.application.bookmobato.Librarian.BookListActivity;
import com.application.bookmobato.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoriteBookListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    ArrayList<BookClasses> list;
    FloatingActionButton addBook;
    DatabaseReference dataBook;
    FavoriteBookListCustomAdapter favoriteBookListCustomAdapter;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_book_list);

        findID();

        swipeRefreshLayout.setOnRefreshListener(FavoriteBookListActivity.this);
        searchView.clearFocus();

        dataBook = FirebaseDatabase.getInstance().getReference("UserInformation").child("BookInformationFavorite");

        list = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(FavoriteBookListActivity.this));
        favoriteBookListCustomAdapter = new FavoriteBookListCustomAdapter(this, list);
        recyclerView.setAdapter(favoriteBookListCustomAdapter);

        dataBook.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BookClasses bookClasses = dataSnapshot.getValue(BookClasses.class);
                    bookClasses.setKey(dataSnapshot.getKey());
                    list.add(bookClasses);
                }
                favoriteBookListCustomAdapter.notifyDataSetChanged();
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

    private void findID() {
        addBook = findViewById(R.id.add_btnBook);
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.search);
        swipeRefreshLayout = findViewById(R.id.refreshActivity);

    }



    public void searchList(String text) {
        ArrayList<BookClasses> search = new ArrayList<>();
        for (BookClasses bookClasses : list) {
            if(bookClasses.getTitle().toLowerCase().contains(text.toLowerCase())) {
                search.add(bookClasses);
            }
        }
        favoriteBookListCustomAdapter.searchData(search);
    }

    @Override
    public void onRefresh() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FavoriteBookListActivity.this, "Refreshed Successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FavoriteBookListActivity.this, FavoriteBookListActivity.class);
                startActivity(intent);
                favoriteBookListCustomAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FavoriteBookListActivity.this, StudentBookListActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}