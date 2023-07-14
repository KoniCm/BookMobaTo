package com.application.bookmobato.Librarian;

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

import com.application.bookmobato.Dashboard.DashboardLibrarian;
import com.application.bookmobato.MainLogin.LibrarianLoginActivity;
import com.application.bookmobato.MainLogin.MainLoginActivity;
import com.application.bookmobato.R;
import com.application.bookmobato.Student.BorrowStudentClasses;
import com.application.bookmobato.Student.StudentClasses;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentBookReq extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    ArrayList<BorrowStudentClasses> list;
    RecyclerView recyclerView;

    DatabaseReference studentBookReq;
    SearchView searchView;

    SwipeRefreshLayout swipeRefreshLayout;
    StudentBookReqCustomAdapter studentBookReqCustomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_book_req);

        findID();
        swipeRefreshLayout.setOnRefreshListener(StudentBookReq.this);
        searchView.clearFocus();

        studentBookReq = FirebaseDatabase.getInstance().getReference("BookInformationReq");

        list = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(StudentBookReq.this));
        studentBookReqCustomAdapter = new StudentBookReqCustomAdapter(this, list);
        recyclerView.setAdapter(studentBookReqCustomAdapter);

        studentBookReq.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BorrowStudentClasses bookReq = dataSnapshot.getValue(BorrowStudentClasses.class);
                    bookReq.setKey(dataSnapshot.getKey());
                    list.add(bookReq);
                }
                studentBookReqCustomAdapter.notifyDataSetChanged();
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

    public void searchList(String text) {
        ArrayList<BorrowStudentClasses> search = new ArrayList<>();
        for (BorrowStudentClasses bookReq : list) {
            if(bookReq.getName().toLowerCase().contains(text.toLowerCase())) {
                search.add(bookReq);
            }
        }
        studentBookReqCustomAdapter.searchData(search);
    }
    public void findID() {
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.search);
        swipeRefreshLayout = findViewById(R.id.refreshActivity);
    }

    @Override
    public void onRefresh() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(StudentBookReq.this, "Refreshed Successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StudentBookReq.this, StudentBookReq.class);
                startActivity(intent);
                studentBookReqCustomAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(StudentBookReq.this, DashboardLibrarian.class);
        startActivity(intent);
        super.onBackPressed();
    }
}