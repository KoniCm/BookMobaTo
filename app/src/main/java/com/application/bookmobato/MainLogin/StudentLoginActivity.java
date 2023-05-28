package com.application.bookmobato.MainLogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.application.bookmobato.Librarian.BookListActivity;
import com.application.bookmobato.R;
import com.application.bookmobato.Student.RegisterStudentActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;

public class StudentLoginActivity extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://bookmobato-e5e2a-default-rtdb.firebaseio.com/");

    Button btn_loginStudent;
    ImageView helper;
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
                    String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                    databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(id)) {

                                final String getPassword = snapshot.child(id).child("pass").getValue(String.class);

                                if (getPassword.equals(pass)) {
                                    Toast.makeText(StudentLoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                                    clearField();
                                    Intent intent = new Intent(StudentLoginActivity.this, BookListActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(StudentLoginActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(StudentLoginActivity.this, "User not Found!", Toast.LENGTH_SHORT).show();
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
        helper = findViewById(R.id.help_btn);
        register = findViewById(R.id.register);
    }
    private void clearField() {
        input_username.getText().clear();
        input_password.getText().clear();
    }
}