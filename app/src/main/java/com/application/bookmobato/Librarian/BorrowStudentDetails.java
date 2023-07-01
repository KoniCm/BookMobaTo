package com.application.bookmobato.Librarian;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.bookmobato.MainLogin.LibrarianLoginActivity;
import com.application.bookmobato.MainLogin.MainLoginActivity;
import com.application.bookmobato.R;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

public class BorrowStudentDetails extends AppCompatActivity {

    TextView bookTitle;
    ImageView bookCover;

    AutoCompleteTextView nameStudent, borrowedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_studen_deatials);

        findID();

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            Glide.with(this).load(bundle.getString("image")).into(bookCover);
            bookTitle.setText(bundle.getString("title"));
            nameStudent.setText(bundle.getString("name"));
            borrowedDate.setText(bundle.getString("borrowedDate"));
        }
    }

    public void findID() {
        bookTitle = findViewById(R.id.title_details);
        bookCover = findViewById(R.id.book_cover_details);
        nameStudent = findViewById(R.id.input_name);
        borrowedDate = findViewById(R.id.dateBorrow);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BorrowStudentDetails.this, StudentBookReq.class);
        startActivity(intent);
        super.onBackPressed();
    }
}