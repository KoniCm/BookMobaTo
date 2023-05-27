package com.application.bookmobato.Librarian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.application.bookmobato.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddingBookActivity extends AppCompatActivity {

    TextInputEditText inputTitle, inputAuthor, inputGenre, inputPublishdate, inputNumpages, inputDescription;

    DatabaseReference databaseBook;

    Button addBookInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_book);

        findID();
        insertData();
        datePickerDialogListener();
    }
    private void findID() {
        inputTitle = findViewById(R.id.inputTitle);
        inputAuthor = findViewById(R.id.inputAuthor);
        inputGenre = findViewById(R.id.inputGenre);
        inputPublishdate = findViewById(R.id.inputPublishdate);
        inputNumpages = findViewById(R.id.inputNumpages);
        inputDescription = findViewById(R.id.inputDescription);
        addBookInfo = findViewById(R.id.addBookBtn);
    }

    private void insertData() {

        addBookInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = inputTitle.getText().toString();
                String author = inputAuthor.getText().toString();
                String genre = inputGenre.getText().toString();
                String publishdate = inputPublishdate.getText().toString();
                String numpages = inputNumpages.getText().toString();
                String description = inputDescription.getText().toString();

                if(isInputEmpty(title,author,genre,publishdate,numpages,description)) {
                    Toast.makeText(AddingBookActivity.this, "Fill the blank, Thank you", Toast.LENGTH_SHORT).show();
                } else {
                    databaseBook = FirebaseDatabase.getInstance().getReference();
                    BookClasses bookClasses = new BookClasses(title,author,genre,publishdate,numpages,description);
                    databaseBook.child("BookInformation").child(title).setValue(bookClasses)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(AddingBookActivity.this, "Successfully, New Book Added!", Toast.LENGTH_SHORT).show();
                                        clearTextField();
                                        Intent intent = new Intent(AddingBookActivity.this,BookListActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(AddingBookActivity.this, "Failed to add book!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
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
    private boolean isInputEmpty(String... inputs) {
        for (String input : inputs) {
            if (input.isEmpty()) { return true; }
        }
        return false;
    }
    private void clearTextField() {
        inputTitle.getText().clear();
        inputAuthor.getText().clear();
        inputGenre.getText().clear();
        inputPublishdate.getText().clear();
        inputNumpages.getText().clear();
        inputDescription.getText().clear();
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
                    Toast.makeText(AddingBookActivity.this, "Clear text field success!",Toast.LENGTH_SHORT).show();
                    clearTextField();
                }
            });
            builder.setNegativeButton("No", null);
            builder.create().show();
        }
        return super.onOptionsItemSelected(item);
    }
}