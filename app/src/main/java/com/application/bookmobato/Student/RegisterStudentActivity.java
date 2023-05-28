package com.application.bookmobato.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.application.bookmobato.MainLogin.StudentLoginActivity;
import com.application.bookmobato.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RegisterStudentActivity extends AppCompatActivity {

    /***
     * Adding Strand list
     */

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://bookmobato-e5e2a-default-rtdb.firebaseio.com/");
    AutoCompleteTextView strand_input,section_input,level_input;
    TextInputEditText id_input, name_input, pass_input,input_Confirmpassword;
    Button addStudent_btn;

    String[] strandList = {"IT-MAWD", "ABM", "CULINARY", "TOPER", "HUMSS"};
    String[] sectionList = {"ICTE101A", "IT301A", "ACT101", "ABMT101A", "HUMSS101A", "TEM301"};

    String[] gradeLevel = {"11","12"};
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;

    ArrayAdapter<String> adapter3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student);

        findID();

        adapter = new ArrayAdapter<String>(this, R.layout.list_dropdown_strand, strandList);
        strand_input.setAdapter(adapter);
        //
        adapter2 = new ArrayAdapter<String>(this, R.layout.list_dropdown_section, sectionList);
        section_input.setAdapter(adapter2);

        adapter3 = new ArrayAdapter<String>(this, R.layout.list_dropdown_gradelevel, gradeLevel);
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

                final String id = id_input.getText().toString();
                final String name = name_input.getText().toString();
                final String section = section_input.getText().toString();
                final String strand = strand_input.getText().toString();
                final String gradeLevel = level_input.getText().toString();
                final String pass = pass_input.getText().toString();
                final String confirmPass = input_Confirmpassword.getText().toString();

                if (isInputEmpty(id,name,section,strand,gradeLevel,pass,confirmPass)) {
                    Toast.makeText(RegisterStudentActivity.this, "Fill the empty field", Toast.LENGTH_SHORT).show();
                } else if (!pass.equals(confirmPass)) {
                    Toast.makeText(RegisterStudentActivity.this, "Password does not matched", Toast.LENGTH_SHORT).show();
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterStudentActivity.this);
                    builder.setView(R.layout.progress_layout);
                    builder.setCancelable(false);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(id)) {
                                Toast.makeText(RegisterStudentActivity.this, "User already registered!", Toast.LENGTH_SHORT).show();
                            } else {

                                databaseReference.child("Users").child(id).child("id").setValue(id);
                                databaseReference.child("Users").child(id).child("fullName").setValue(name);
                                databaseReference.child("Users").child(id).child("section").setValue(section);
                                databaseReference.child("Users").child(id).child("strand").setValue(strand);
                                databaseReference.child("Users").child(id).child("gradeLevel").setValue(gradeLevel);
                                databaseReference.child("Users").child(id).child("pass").setValue(pass);
                                databaseReference.child("Users").child(id).child("confirmPass").setValue(confirmPass);

                                Toast.makeText(RegisterStudentActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterStudentActivity.this, StudentLoginActivity.class);
                                startActivity(intent);
                                clearField();
                                dialog.dismiss();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
    private void findID() {
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
    private void clearField() {
        id_input.getText().clear();
        name_input.getText().clear();
        level_input.getText().clear();
        section_input.getText().clear();
        strand_input.getText().clear();
        pass_input.getText().clear();
    }
}