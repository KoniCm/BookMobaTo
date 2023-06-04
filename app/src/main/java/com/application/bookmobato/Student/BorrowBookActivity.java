package com.application.bookmobato.Student;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.bookmobato.Librarian.BookClasses;
import com.application.bookmobato.R;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class BorrowBookActivity extends AppCompatActivity {

    TextView title;
    ImageView bookImage;
    TextInputEditText name,comment;
    Button addRequestBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_book);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserInformation");

        findID();

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            Glide.with(this).load(bundle.getString("image")).into(bookImage);
            title.setText(bundle.getString("title"));
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