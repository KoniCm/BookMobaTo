package com.application.bookmobato.Student;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.application.bookmobato.R;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

public class BorrowBookActivity extends AppCompatActivity {

    TextView title;
    ImageView bookImage;
    TextInputEditText name,comment;
    Button addRequestBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_book);

        findID();

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            Glide.with(this).load(bundle.getString("image")).into(bookImage);
            title.setText(bundle.getString("title"));
            name.setText(bundle.getString("name"));
        }

        addRequestBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(BorrowBookActivity.this, "Your request in now pending...", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void findID() {
        title = findViewById(R.id.title_details);
        bookImage = findViewById(R.id.book_cover_details);
        name = findViewById(R.id.input_name);
        comment = findViewById(R.id.input_Comment);
        addRequestBook = findViewById(R.id.addRequestBook);

    }
}