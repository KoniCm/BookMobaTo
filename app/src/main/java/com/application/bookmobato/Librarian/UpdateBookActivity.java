package com.application.bookmobato.Librarian;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.application.bookmobato.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class UpdateBookActivity extends AppCompatActivity {

    EditText title_input, author_input,genre_input,publish_input,pages_input,description_input;
    Button update_button,delete_button;
    ImageView images_update;
    String key = "";
    String imageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book);

        findID();


        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOneRow();
            }
        });

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            key = bundle.getString("Key");
            imageUrl = bundle.getString("image");
            //
            Glide.with(this).load(bundle.getString("image")).into(images_update);
            title_input.setText(bundle.getString("title"));
            author_input.setText(bundle.getString("author"));
            genre_input.setText(bundle.getString("genre"));
            publish_input.setText(bundle.getString("publishdate"));
            pages_input.setText(bundle.getString("numpages"));
            description_input.setText(bundle.getString("description"));
        }
    }

    private void findID() {
        title_input = findViewById(R.id.inputTitle2);
        author_input = findViewById(R.id.inputAuthor2);
        genre_input = findViewById(R.id.inputGenre2);
        publish_input = findViewById(R.id.inputPublishdate2);
        pages_input = findViewById(R.id.inputNumpages2);
        description_input = findViewById(R.id.inputDescription2);
        update_button = findViewById(R.id.updateBtn);
        delete_button = findViewById(R.id.deleteBtn);
        images_update = findViewById(R.id.book_cover2);
    }
    private void deleteOneRow() {

        String title = title_input.getText().toString();

        //Simple Dialog box
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + title);
        builder.setCancelable(false);
        builder.setMessage("Are you sure you want to delete?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("BookInformation");
                FirebaseStorage storage = FirebaseStorage.getInstance();

                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        databaseReference.child(key).removeValue();
                        Toast.makeText(UpdateBookActivity.this, "Successfully, Book Deleted", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateBookActivity.this, BookListActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.setCancelable(true);
            }
        });
        builder.create().show();
    }
}