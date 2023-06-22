package com.application.bookmobato.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.application.bookmobato.Librarian.AddingBookActivity;
import com.application.bookmobato.Librarian.BookClasses;
import com.application.bookmobato.Librarian.BookListActivity;
import com.application.bookmobato.Librarian.LibrarianCustomAdapter;
import com.application.bookmobato.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentBookListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    SearchView searchView;
    ArrayList<BookClasses> list;
    DatabaseReference dataBook;
    StudentBookCustomAdapter studentBookCustomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_book_list);

        findID();

        swipeRefreshLayout.setOnRefreshListener(StudentBookListActivity.this);
        searchView.clearFocus();

        dataBook = FirebaseDatabase.getInstance().getReference("BookInformation");

        list = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(StudentBookListActivity.this));
        studentBookCustomAdapter = new StudentBookCustomAdapter(this, list);
        recyclerView.setAdapter(studentBookCustomAdapter);

        dataBook.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BookClasses bookClasses = dataSnapshot.getValue(BookClasses.class);
                    bookClasses.setKey(dataSnapshot.getKey());
                    list.add(bookClasses);
                }
                studentBookCustomAdapter.notifyDataSetChanged();
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
        studentBookCustomAdapter.searchData(search);
    }

    @Override
    public void onRefresh() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(StudentBookListActivity.this, "Refreshed Successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StudentBookListActivity.this, StudentBookListActivity.class);
                startActivity(intent);
                studentBookCustomAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.student_info, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.student_information) {
            Intent accountInfo = new Intent(StudentBookListActivity.this, StudentAccountInformationX.class);
            startActivity(accountInfo);
        }
        return super.onOptionsItemSelected(item);
    }
}