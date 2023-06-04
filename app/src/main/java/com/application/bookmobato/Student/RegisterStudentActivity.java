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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.application.bookmobato.MainLogin.StudentLoginActivity;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RegisterStudentActivity extends AppCompatActivity {

    /***
     * Adding Strand list
     */

    String id,name,section,strand,gradeLevel,pass,confirmPass;
    AutoCompleteTextView strand_input,section_input,level_input;
    TextInputEditText id_input, name_input, pass_input,input_Confirmpassword;
    Button addStudent_btn;

    String[] strandList = {"IT-MAWD", "ABM", "CULINARY", "TOPER", "HUMSS"};
    String[] sectionList = {"ICTE101A", "IT301A", "ACT101", "ABMT101A", "HUMSS101A", "TEM301"};

    String[] gradeLevelList = {"11","12"};
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;
    ArrayAdapter<String> adapter3;

    ImageView userImage;
    String imgURL;
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student);

        findID();

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK) {
                            Intent xData = result.getData();
                            uri = xData.getData();
                            userImage.setImageURI(uri);
                        } else {
                            Toast.makeText(RegisterStudentActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photo = new Intent(Intent.ACTION_PICK);
                photo.setType("image/*");
                activityResultLauncher.launch(photo);
            }
        });

        adapter = new ArrayAdapter<String>(this, R.layout.list_dropdown_strand, strandList);
        strand_input.setAdapter(adapter);
        //
        adapter2 = new ArrayAdapter<String>(this, R.layout.list_dropdown_section, sectionList);
        section_input.setAdapter(adapter2);

        adapter3 = new ArrayAdapter<String>(this, R.layout.list_dropdown_gradelevel, gradeLevelList);
        level_input.setAdapter(adapter3);


        strand_input.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String item = adapterView.getItemAtPosition(position).toString();
            }
        });

        section_input.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView2, View view, int position, long id) {
                String item2 = adapterView2.getItemAtPosition(position).toString();
            }
        });
        level_input.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView3, View view, int position, long id) {
                String items3 = adapterView3.getItemAtPosition(position).toString();
            }
        });

        addStudent_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //-- List Of Strand
                List<String> listStrand = new ArrayList<>();

                listStrand.add("it-mawd");
                listStrand.add("abm");
                listStrand.add("culinary");
                listStrand.add("toper");
                listStrand.add("humss");

                //-- List of Section
                List<String> listSection = new ArrayList<>();

                listSection.add("icte101a");
                listSection.add("it301a");
                listSection.add("act101");
                listSection.add("abmt101a");
                listSection.add("humss101a");
                listSection.add("tem301");

                // -- List of GradeLevel
                List<String> listGradelevel = new ArrayList<>();

                listGradelevel.add("11");
                listGradelevel.add("12");

                declaration();

                if(isInputEmpty(id,name,section,strand,gradeLevel,pass)) {
                    Toast.makeText(RegisterStudentActivity.this, "Fill the empty field", Toast.LENGTH_SHORT).show();
                } else if (name.length() < 10){
                    name_input.setError("Ex: Dela Cruz, Juan");
                    name_input.requestFocus();
                }else if(id.length() != 11) {
                    Toast.makeText(RegisterStudentActivity.this, "Enter a valid Student ID", Toast.LENGTH_SHORT).show();
                } else if (!gradeLevel.equals("11") && !gradeLevel.equals("12")) {
                    Toast.makeText(RegisterStudentActivity.this, "Enter a valid grade level", Toast.LENGTH_SHORT).show();
                } else if(!listStrand.contains(strand.toLowerCase())) {
                    Toast.makeText(RegisterStudentActivity.this, "Enter a valid strand", Toast.LENGTH_SHORT).show();
                } else if(!listSection.contains(section.toLowerCase())){
                    Toast.makeText(RegisterStudentActivity.this, "Enter a valid section", Toast.LENGTH_SHORT).show();
                } else if(!listGradelevel.contains(gradeLevel.toLowerCase())){
                    Toast.makeText(RegisterStudentActivity.this, "Enter a valid grade level", Toast.LENGTH_SHORT).show();
                }else if(pass.length() < 8) {
                    Toast.makeText(RegisterStudentActivity.this,"Password must be longer than 8 characters!",Toast.LENGTH_SHORT).show();
                } else if(!pass.equals(confirmPass)){
                    Toast.makeText(RegisterStudentActivity.this,"Password does not matched!",Toast.LENGTH_SHORT).show();
                }else {
                    insertData();
                }
            }
        });
    }
    private void findID() {
        userImage = findViewById(R.id.userImage);
        id_input = findViewById(R.id.id_input);
        name_input = findViewById(R.id.name_input);
        level_input = findViewById(R.id.level_input);
        section_input = findViewById(R.id.section_input);
        strand_input = findViewById(R.id.strand_input);
        addStudent_btn = findViewById(R.id.btn_loginStudent);
        pass_input = findViewById(R.id.input_password);
        input_Confirmpassword = findViewById(R.id.input_Confirmpassword);
    }

    private boolean isInputEmpty(String... inputs) {
        for (String input : inputs) {
            if (input.isEmpty()) { return true; }
        }
        return false;
    }

    public void insertData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterStudentActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        try {

            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("UserInformationImage")
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
                    Toast.makeText(RegisterStudentActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Please select a profile picture", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    private void uploadData() {

        declaration();

        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        StudentClasses studentClasses = new StudentClasses(id, name, section, strand, gradeLevel, pass, imgURL);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("UserInformation");

        DatabaseReference childRef = ref.push();

        childRef.setValue(studentClasses)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            final Toast toast = new Toast(getApplicationContext());
                            toast.setDuration(Toast.LENGTH_LONG);
                            View custom = getLayoutInflater().inflate(R.layout.toast_message_student, null);
                            toast.setView(custom);
                            toast.show();
                            clearTextField();
                            Intent intent = new Intent(RegisterStudentActivity.this, StudentLoginActivity.class);
                            startActivity(intent);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterStudentActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    public void declaration() {
         id = id_input.getText().toString();
         name = name_input.getText().toString();
         section = section_input.getText().toString();
         strand = strand_input.getText().toString();
         gradeLevel = level_input.getText().toString();
         pass = pass_input.getText().toString();
         confirmPass = input_Confirmpassword.getText().toString();
    }
    public void clearTextField() {
        id_input.getText().clear();
        name_input.getText().clear();
        section_input.getText().clear();
        strand_input.getText().clear();
        level_input.getText().clear();
        pass_input.getText().clear();
        input_Confirmpassword.getText().clear();
        userImage.setImageResource(R.drawable.book_cover);
    }
}