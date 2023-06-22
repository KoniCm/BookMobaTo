package com.application.bookmobato.MainLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.application.bookmobato.Dashboard.DashboardLibrarian;
import com.application.bookmobato.Librarian.BookListActivity;
import com.application.bookmobato.R;
import com.google.android.material.textfield.TextInputEditText;

public class LibrarianLoginActivity extends AppCompatActivity {

    TextInputEditText input_usernameLibrarian,input_passwordLibrarian;
    Button btn_loginLibrarian;

    String user = "69";
    String pass =  "tryme69";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_librarian_login);

        findID();

        btn_loginLibrarian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userX = input_usernameLibrarian.getText().toString();
                String passX = input_passwordLibrarian.getText().toString();

                if(userX.isEmpty() || passX.isEmpty()){
                    Toast.makeText(LibrarianLoginActivity.this, "Please fill the empty field", Toast.LENGTH_SHORT).show();
                } else if(!userX.equals(user) || !passX.equals(pass)) {
                    Toast.makeText(LibrarianLoginActivity.this, "Wrong username and password!", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(LibrarianLoginActivity.this, "Successfully login as a librarian", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LibrarianLoginActivity.this, DashboardLibrarian.class);
                    startActivity(intent);
                }
            }
        });
    }
    private void findID() {
        input_usernameLibrarian = findViewById(R.id.input_usernameLibrarian);
        input_passwordLibrarian = findViewById(R.id.input_passwordLibrarian);
        btn_loginLibrarian = findViewById(R.id.btn_loginLibrarian);
    }
}