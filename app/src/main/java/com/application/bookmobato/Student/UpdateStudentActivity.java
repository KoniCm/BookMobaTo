package com.application.bookmobato.Student;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.application.bookmobato.Librarian.BookListActivity;
import com.application.bookmobato.Librarian.StudentActivityList;
import com.application.bookmobato.R;
import com.bumptech.glide.Glide;
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

public class UpdateStudentActivity extends AppCompatActivity {

    String id,name,section,strand,gradeLevel,pass,confirmPass;
    TextInputEditText id_input, name_input,strand_input, section_input, level_input, pass_input,input_Confirmpassword;
    Button updateStudent_btn;

    ImageView images_update;

    String imageUrl;
    String key, oldImageURL;
    Uri uri;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_student);

        findID();
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
                            Toast.makeText(UpdateStudentActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            Glide.with(this).load(bundle.getString("image")).into(images_update);
            id_input.setText(bundle.getString("id"));
            name_input.setText(bundle.getString("name"));
            level_input.setText(bundle.getString("level"));
            section_input.setText(bundle.getString("section"));
            strand_input.setText(bundle.getString("strand"));
            pass_input.setText(bundle.getString("pass"));

            //
            key = bundle.getString("Key");
            oldImageURL = bundle.getString("image");
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("UserInformation").child(key);

//        images_update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent photo = new Intent(Intent.ACTION_PICK);
//                photo.setType("image/*");
//                activityResultLauncher.launch(photo);
//            }
//        });

        updateStudent_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
    }
    private void findID() {
        images_update = findViewById(R.id.userImage2);
        id_input = findViewById(R.id.id_input2);
        name_input = findViewById(R.id.name_input2);
        level_input = findViewById(R.id.level_input2);
        section_input = findViewById(R.id.section_input2);
        strand_input = findViewById(R.id.strand_input2);
        pass_input = findViewById(R.id.input_password2);
        input_Confirmpassword = findViewById(R.id.input_Confirmpassword2);
        updateStudent_btn = findViewById(R.id.btn_updateStudent);
    }
    public void saveData(){
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateStudentActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        try {

            storageReference = FirebaseStorage.getInstance().getReference().child("UserInformation").child(uri.getLastPathSegment());
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
            String ExceptionMessage = "Please select new profile picture to update the all details";
            Toast.makeText(this, ExceptionMessage, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }
    public void updateData() {
        id = id_input.getText().toString().trim();
        name = name_input.getText().toString().trim();
        gradeLevel = level_input.getText().toString().trim();
        section = section_input.getText().toString().trim();
        strand = strand_input.getText().toString().trim();
        pass = pass_input.getText().toString().trim();
        confirmPass = input_Confirmpassword.getText().toString();

        if(!confirmPass.equals(pass)) {
            Toast.makeText(this, "Password does not matched", Toast.LENGTH_SHORT).show();
        } else {
            StudentClasses studentClasses = new StudentClasses(id, name, gradeLevel, section, strand, pass, imageUrl);

            databaseReference.setValue(studentClasses).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageURL);
                    reference.delete();
                    Toast.makeText(UpdateStudentActivity.this, "Successfully, Student Updated!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UpdateStudentActivity.this, StudentActivityList.class);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UpdateStudentActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}