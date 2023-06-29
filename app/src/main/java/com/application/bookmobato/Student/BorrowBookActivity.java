package com.application.bookmobato.Student;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.application.bookmobato.Librarian.AddingBookActivity;
import com.application.bookmobato.Librarian.BookClasses;
import com.application.bookmobato.Librarian.BookListActivity;
import com.application.bookmobato.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BorrowBookActivity extends AppCompatActivity {

    String titleX, nameX, setDateBorrowed;
    TextView title;
    ImageView bookImage;
    AutoCompleteTextView name;

    TextInputEditText setDateBorrowing;
    Button addRequestBook;

    DatabaseReference databaseReference;

    SharedPreferences sharedPreferences;

    String imgURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_book);

        databaseReference = FirebaseDatabase.getInstance().getReference("UserInformation");

        sharedPreferences = getSharedPreferences("MyCache", Context.MODE_PRIVATE);
        String value = sharedPreferences.getString("keyID", "default_value");
        Query query = databaseReference.orderByChild("id").equalTo(value);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        name.setText((ds.child("name").getValue().toString()));
                    }
                } else {
                    Log.d("TAG", "Data does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", "Error: " + databaseError.getMessage());
            }
        });

        findID();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            // this is for displaying the image to ImageView
            Glide.with(this).load(bundle.getString("image")).into(bookImage);
            title.setText(bundle.getString("title"));

            // set the imgUrl (String)
            imgURL = bundle.getString("image");
        }

        datePickerDialogListener();

        addRequestBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });
    }

    private void findID() {
        title = findViewById(R.id.title_details);
        bookImage = findViewById(R.id.book_cover_details);
        name = findViewById(R.id.input_name);
        setDateBorrowing = findViewById(R.id.dateBorrow);
        addRequestBook = findViewById(R.id.addRequestBook);
    }

    private void uploadData() {

        titleX = title.getText().toString();
        nameX = name.getText().toString();
        setDateBorrowed = setDateBorrowing.getText().toString();

        BorrowStudentClasses bookClasses = new BorrowStudentClasses(titleX, nameX, imgURL, setDateBorrowed);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("BookInformationReq");

        DatabaseReference childRef = ref.push();

        childRef.setValue(bookClasses)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(BorrowBookActivity.this, "Successfully, Book borrowed!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(BorrowBookActivity.this, StudentBookListActivity.class);
                            startActivity(intent);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BorrowBookActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void datePickerDialogListener() {
        setDateBorrowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePicker = new DatePickerDialog(BorrowBookActivity.this);
                datePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        setDateBorrowing.setText(year +"-"+ month +"-"+ day);

                    }
                });

                datePicker.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
                datePicker.show();

            }
        });
    }
}