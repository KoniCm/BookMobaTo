package com.application.bookmobato.Dashboard;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.application.bookmobato.MainLogin.MainLoginActivity;
import com.application.bookmobato.R;
import com.application.bookmobato.Student.StudentBookListActivity;

public class DashboardStudent extends AppCompatActivity {

    CardView booklistforstudent,borrowreturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_student);
        findID();

        booklistforstudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(DashboardStudent.this, StudentBookListActivity.class);
                startActivity(intent);
            }
        });

        borrowreturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(DashboardStudent.this, "Processing", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void findID() {
        booklistforstudent = findViewById(R.id.bookListforStudent);
        borrowreturn = findViewById(R.id.borrowreturn);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder build = new AlertDialog.Builder(this);

        build.setMessage("Do you want to sign out your account");
        build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                SharedPreferences sharedPreferences = getSharedPreferences("MyCache", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Toast.makeText(DashboardStudent.this, "Your account has been sign out", Toast.LENGTH_SHORT).show();
                Intent signOut = new Intent(DashboardStudent.this, MainLoginActivity.class);
                startActivity(signOut);
            }
        });
        build.setNegativeButton("No", null);
        build.create().show();
    }
}