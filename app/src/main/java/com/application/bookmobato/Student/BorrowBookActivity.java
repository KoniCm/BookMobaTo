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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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

    String titleX, nameX;
    TextView title;
    ImageView bookImage;
    AutoCompleteTextView name;
    Button addRequestBook;

    DatabaseReference databaseReference;

    SharedPreferences sharedPreferences;

    String imgURL;
    Uri uri;

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
            Glide.with(this).load(bundle.getString("image")).into(bookImage);
            title.setText(bundle.getString("title"));
        }

        addRequestBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertData();
            }
        });

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                        } else {
                            Toast.makeText(BorrowBookActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        bookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photo = new Intent(Intent.ACTION_PICK);
                photo.setType("image/*");
                activityResultLauncher.launch(photo);
            }
        });
    }

    private void findID() {
        title = findViewById(R.id.title_details);
        bookImage = findViewById(R.id.book_cover_details);
        name = findViewById(R.id.input_name);
        addRequestBook = findViewById(R.id.addRequestBook);

    }

    private void InsertData() {

        AlertDialog.Builder builder = new AlertDialog.Builder(BorrowBookActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        try {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("BookInformationReq")
                    .child(uri.getLastPathSegment());

            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete()) ;
                    Uri urlImage = uriTask.getResult();
                    imgURL = urlImage.toString();
                    dialog.dismiss();
                    uploadData();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BorrowBookActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(BorrowBookActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    private void uploadData() {

        titleX = title.getText().toString();
        nameX = name.getText().toString();

        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        BorrowStudentClasses bookClasses = new BorrowStudentClasses(titleX, nameX, imgURL);

        FirebaseDatabase.getInstance().getReference("BookInformationReq").child(currentDate)
                .setValue(bookClasses)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(BorrowBookActivity.this, "Successfully, Request pending!", Toast.LENGTH_SHORT).show();
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
}