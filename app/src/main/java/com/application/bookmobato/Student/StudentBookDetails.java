package com.application.bookmobato.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.application.bookmobato.R;
import com.bumptech.glide.Glide;

public class StudentBookDetails extends AppCompatActivity {

    TextView title_input, author_input,genre_input,publish_input,pages_input,description_input;
    ImageView coverDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_book_details);

        findID();

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
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
        inflater.inflate(R.menu.favorite_book, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id  = item.getItemId();

        if(id == R.id.favourite) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Do you want to add this book to your favorites?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(StudentBookDetails.this, "Added to your favorite", Toast.LENGTH_SHORT).show();
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
}