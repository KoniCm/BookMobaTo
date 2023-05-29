package com.application.bookmobato.Librarian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import com.application.bookmobato.R;
import com.application.bookmobato.Student.StudentClasses;
import com.application.bookmobato.Student.StudentCustomAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentActivityList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    ArrayList<StudentClasses> list;
    StudentCustomAdapter studentCustomAdapter;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        findID();

        swipeRefreshLayout.setOnRefreshListener(StudentActivityList.this);
        databaseReference = FirebaseDatabase.getInstance().getReference("UserInformation");

        list = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        studentCustomAdapter = new StudentCustomAdapter(this, list);
        recyclerView.setAdapter(studentCustomAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    StudentClasses studentClasses = dataSnapshot.getValue(StudentClasses.class);
                    studentClasses.setKey(dataSnapshot.getKey());
                    list.add(studentClasses);
                }
                studentCustomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error");
            }
        });

    }

    private void findID() {
        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.refreshActivity);
    }
    @Override
    public void onRefresh() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(StudentActivityList.this, "Refreshed Successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StudentActivityList.this, StudentActivityList.class);
                startActivity(intent);
                studentCustomAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
    }
}