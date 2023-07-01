package com.application.bookmobato.MainLogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.application.bookmobato.Dashboard.DashboardStudent;
import com.application.bookmobato.R;
import com.application.bookmobato.Student.RegisterStudentActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.Objects;

public class StudentLoginActivity extends AppCompatActivity {
    Button btn_loginStudent;
    TextInputEditText input_username,input_password;
    TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        findID();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentLoginActivity.this, RegisterStudentActivity.class);
                startActivity(intent);
            }
        });
        btn_loginStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String id = input_username.getText().toString();
                final String pass = input_password.getText().toString();

                if(id.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(StudentLoginActivity.this, "Please fill the empty field", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserInformation");
                    Query checkUserdatabase = databaseReference.orderByChild("id").equalTo(id);
                    Query checkPassdatabase = databaseReference.orderByChild("pass").equalTo(pass);

                    checkUserdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                input_username.setError(null);
                                checkPassdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            SharedPreferences sharedPreferences = getSharedPreferences("MyCache", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("keyID", id);
                                            editor.apply();

                                            input_password.setError(null);
                                            Toast.makeText(StudentLoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(StudentLoginActivity.this, DashboardStudent.class);
                                            startActivity(intent);
                                            clearField();
                                            /**
                                             * Go to the dashboard for student
                                             * */
                                        } else {
                                            input_password.setError("Wrong password");
                                            input_password.requestFocus();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(StudentLoginActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                input_username.setError("User does not exist");
                                input_username.requestFocus();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(StudentLoginActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void findID() {
        input_username = findViewById(R.id.input_username);
        input_password = findViewById(R.id.input_password);
        btn_loginStudent = findViewById(R.id.btn_loginStudent);
        register = findViewById(R.id.register);
    }
    private void clearField() {
        input_username.getText().clear();
        input_password.getText().clear();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(StudentLoginActivity.this,MainLoginActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}