package com.application.bookmobato.Librarian;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.application.bookmobato.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UpdateBookActivity extends AppCompatActivity {

    String title, author, genre, publishdate, numpages, description;

    EditText updateTitle, updateAuthor, updateGenre ,updatePublishdate ,updatePages , updateDescription;
    Button update_button;
    ImageView images_update;

    String imageUrl;
    String key, oldImageURL;
    Uri uri;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    String imgURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book);

        findID();
        datePickerDialogListener();

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            images_update.setImageURI(uri);
                        } else {
                            Toast.makeText(UpdateBookActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            Glide.with(this).load(bundle.getString("image")).into(images_update);
            updateTitle.setText(bundle.getString("title"));
            updateAuthor.setText(bundle.getString("author"));
            updateGenre.setText(bundle.getString("genre"));
            updatePublishdate.setText(bundle.getString("publishdate"));
            updatePages.setText(bundle.getString("numpages"));
            updateDescription.setText(bundle.getString("description"));

            key = bundle.getString("Key");
            oldImageURL = bundle.getString("image");

            // set the imgUrl (String)
            imgURL = bundle.getString("image");
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("BookInformation").child(key);

//        images_update.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Intent photo = new Intent(Intent.ACTION_PICK);
////                photo.setType("image/*");
////                activityResultLauncher.launch(photo);
////            }
////        });

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
    }

    private void datePickerDialogListener() {
        updatePublishdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePicker = new DatePickerDialog(UpdateBookActivity.this);
                datePicker.show();
                datePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        updatePublishdate.setText(year +"-"+ month +"-"+ day);

                    }
                });
            }
        });
    }

    private void findID() {
        updateTitle = findViewById(R.id.inputTitle2);
        updateAuthor = findViewById(R.id.inputAuthor2);
        updateGenre = findViewById(R.id.inputGenre2);
        updatePublishdate = findViewById(R.id.inputPublishdate2);
        updatePages = findViewById(R.id.inputNumpages2);
        updateDescription = findViewById(R.id.inputDescription2);
        update_button = findViewById(R.id.updateBtn);
        images_update = findViewById(R.id.book_cover2);
    }
    public void saveImage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateBookActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        try {

            storageReference = FirebaseStorage.getInstance().getReference().child("BookInformation").child(uri.getLastPathSegment());

            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete());
                    Uri urlImage = uriTask.getResult();
                    imageUrl = urlImage.toString();
                    updateData();
                    dialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
            String ExceptionMessage = "Please select new book cover to update the all details";
            Toast.makeText(this, ExceptionMessage, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    public void updateData() {
        title = updateTitle.getText().toString().trim();
        author = updateAuthor.getText().toString().trim();
        genre = updateGenre.getText().toString().trim();
        publishdate = updatePublishdate.getText().toString().trim();
        numpages = updatePages.getText().toString().trim();
        description = updateDescription.getText().toString().trim();

        BookClasses bookClasses = new BookClasses(title, author, genre, publishdate, numpages, description, imgURL);

        databaseReference.setValue(bookClasses).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL);
                reference.delete();
                Toast.makeText(UpdateBookActivity.this, "Successfully, Book Updated!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateBookActivity.this, BookListActivity.class);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateBookActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}