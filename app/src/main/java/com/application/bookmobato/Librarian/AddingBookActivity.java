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
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;
import com.application.bookmobato.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddingBookActivity extends AppCompatActivity {

    /**
     * Adding Book Information to the Firebase Realtime database
     * */

    String title, author, genre, publishdate, numpages, description;
    TextInputEditText inputTitle, inputAuthor, inputGenre, inputPublishdate, inputNumpages, inputDescription;
    Button addBookInfo;
    ImageView upload_cover;
    String imgURL;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_book);

        findID();
        insertData();
        datePickerDialogListener();

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            upload_cover.setImageURI(uri);
                        } else {
                            Toast.makeText(AddingBookActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        upload_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photo = new Intent(Intent.ACTION_PICK);
                photo.setType("image/*");
                activityResultLauncher.launch(photo);
            }
        });
    }

    private void insertData() {
        addBookInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AssigningValue();

                if(isInputEmpty(title,author,genre,publishdate,numpages,description)) {
                    Toast.makeText(AddingBookActivity.this, "Fill the empty field", Toast.LENGTH_SHORT).show();
                } else if(upload_cover == null) {
                    Toast.makeText(AddingBookActivity.this, "Please select a image", Toast.LENGTH_SHORT).show();
                } else {
                    AddingImageAndValue();
                }
            }
        });
    }

    private boolean isInputEmpty(String... inputs) {
        for (String input : inputs) {
            if (input.isEmpty()) { return true; }
        }
        return false;
    }

    private void AssigningValue() {
        title = inputTitle.getText().toString();
        author = inputAuthor.getText().toString();
        genre = inputGenre.getText().toString();
        publishdate = inputPublishdate.getText().toString();
        numpages = inputNumpages.getText().toString();
        description = inputDescription.getText().toString();
    }

    private void AddingImageAndValue() {

        AlertDialog.Builder builder = new AlertDialog.Builder(AddingBookActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        try {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("BookInformation")
                    .child(uri.getLastPathSegment());

            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete());
                    Uri urlImage =  uriTask.getResult();
                    imgURL = urlImage.toString();
                    dialog.dismiss();
                    uploadData();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddingBookActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "Please select a book cover", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }
    private void uploadData() {

        AssigningValue();

        BookClasses bookClasses = new BookClasses(title, author, genre, publishdate, numpages, description,imgURL);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("BookInformation");

        DatabaseReference childRef = ref.push();

        childRef.setValue(bookClasses)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(AddingBookActivity.this, "Successfully, New Book Added!", Toast.LENGTH_SHORT).show();
                            clearTextField();
                            Intent intent = new Intent(AddingBookActivity.this, BookListActivity.class);
                            startActivity(intent);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddingBookActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void datePickerDialogListener() {
        inputPublishdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePicker = new DatePickerDialog(AddingBookActivity.this);
                datePicker.show();
                datePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        inputPublishdate.setText(year +"-"+ month +"-"+ day);

                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.clear_men, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.clear_text) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddingBookActivity.this);

            builder.setMessage("Do you want to clear all text field");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(AddingBookActivity.this, "Successfully, Clear text field!", Toast.LENGTH_SHORT).show();
                    clearTextField();
                }
            });
            builder.setNegativeButton("No", null);
            builder.create().show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearTextField() {
        inputTitle.getText().clear();
        inputAuthor.getText().clear();
        inputGenre.getText().clear();
        inputPublishdate.getText().clear();
        inputNumpages.getText().clear();
        inputDescription.getText().clear();
        upload_cover.setImageResource(R.drawable.book_cover);
    }

    private void findID() {
        inputTitle = findViewById(R.id.inputTitle);
        inputAuthor = findViewById(R.id.inputAuthor);
        inputGenre = findViewById(R.id.inputGenre);
        inputPublishdate = findViewById(R.id.inputPublishdate);
        inputNumpages = findViewById(R.id.inputNumpages);
        inputDescription = findViewById(R.id.inputDescription);
        addBookInfo = findViewById(R.id.addBookBtn);
        upload_cover = findViewById(R.id.upload_cover);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddingBookActivity.this, BookListActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}