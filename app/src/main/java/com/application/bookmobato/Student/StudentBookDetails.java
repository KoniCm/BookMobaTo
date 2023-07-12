package com.application.bookmobato.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.application.bookmobato.Librarian.AddingBookActivity;
import com.application.bookmobato.Librarian.BookClasses;
import com.application.bookmobato.Librarian.BookListActivity;
import com.application.bookmobato.MainLogin.MainLoginActivity;
import com.application.bookmobato.MainLogin.StudentLoginActivity;
import com.application.bookmobato.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class StudentBookDetails extends AppCompatActivity {

    String title, author, genre, publishdate, numpages, description, studentID;

    TextView title_input, author_input, genre_input, publish_input, pages_input, description_input, id_student;
    ImageView coverDetails;

    String imgURL;
    Uri uri;

    DatabaseReference databaseReference;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_book_details);

        findID();

        databaseReference = FirebaseDatabase.getInstance().getReference("UserInformation");

        sharedPreferences = getSharedPreferences("MyCache", Context.MODE_PRIVATE);
        String value = sharedPreferences.getString("keyID", "default_value");
        Query query = databaseReference.orderByChild("id").equalTo(value);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        id_student.setText((ds.child("id").getValue().toString()));
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

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            Glide.with(this).load(bundle.getString("image")).into(coverDetails);
            title_input.setText(bundle.getString("title"));
            author_input.setText(bundle.getString("author"));
            genre_input.setText(bundle.getString("genre"));
            publish_input.setText(bundle.getString("publishdate"));
            pages_input.setText(bundle.getString("numpages"));
            description_input.setText(bundle.getString("description"));

            // set the imgUrl (String)
            imgURL = bundle.getString("image");
        }

        String title = title_input.getText().toString();

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(title);
        }
    }

    private void findID() {
        title_input = findViewById(R.id.title_details);
        author_input = findViewById(R.id.author_details);
        genre_input = findViewById(R.id.genre_details);
        publish_input = findViewById(R.id.publish_details);
        pages_input = findViewById(R.id.pages_details);
        description_input = findViewById(R.id.description_details);
        coverDetails = findViewById(R.id.book_cover_details);
        id_student = findViewById(R.id.id_fecth);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.favorite_book, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.favourite) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Do you want to add this book to your favorites?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    uploadData();
                    builder.setCancelable(true);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    builder.setCancelable(true);
                }
            });
            builder.create().show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void declarationOfInfo() {
        title = title_input.getText().toString();
        author = author_input.getText().toString();
        genre = publish_input.getText().toString();
        publishdate = pages_input.getText().toString();
        numpages = pages_input.getText().toString();
        description = description_input.getText().toString();
        studentID = id_student.getText().toString();
    }

    private void uploadData() {

        declarationOfInfo();

        BookClasses bookClasses = new BookClasses(studentID ,title, author, genre, publishdate, numpages, description,imgURL);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("BookInformationFavorite");

        DatabaseReference childRef = ref.push();

        childRef.setValue(bookClasses)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(StudentBookDetails.this, "Successfully, Book Added to Favorites!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(StudentBookDetails.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(StudentBookDetails.this, StudentBookListActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}