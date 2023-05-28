package com.application.bookmobato.Librarian;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.application.bookmobato.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class BookDetails extends AppCompatActivity {

    TextView title_input, author_input,genre_input,publish_input,pages_input,description_input;
    ImageView coverDetails;
    String key = "";
    String imageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        findID();



        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            key = bundle.getString("Key");
            imageUrl = bundle.getString("image");
            //
            Glide.with(this).load(bundle.getString("image")).into(coverDetails);
            title_input.setText(bundle.getString("title"));
            author_input.setText(bundle.getString("author"));
            genre_input.setText(bundle.getString("genre"));
            publish_input.setText(bundle.getString("publishdate"));
            pages_input.setText(bundle.getString("numpages"));
            description_input.setText(bundle.getString("description"));
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
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_onerow, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.delete_row) {
            deleteOneRow();
        }
        return super.onOptionsItemSelected(item);
    }
    private void deleteOneRow() {

        String title = title_input.getText().toString();

        //Simple Dialog box
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Book Title: " + title);
        builder.setCancelable(false);
        builder.setMessage("Do you want to delete this book?");
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
                        Toast.makeText(BookDetails.this, "Successfully, Book Deleted!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(BookDetails.this, BookListActivity.class);
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