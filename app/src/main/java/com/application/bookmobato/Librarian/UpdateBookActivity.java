package com.application.bookmobato.Librarian;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.application.bookmobato.R;
import com.bumptech.glide.Glide;

public class UpdateBookActivity extends AppCompatActivity {

    EditText title_input, author_input,genre_input,publish_input,pages_input,description_input;
    Button update_button,delete_button;
    ImageView images_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book);

        findID();

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
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
}