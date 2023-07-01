package com.application.bookmobato.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.bookmobato.MainLogin.MainLoginActivity;
import com.application.bookmobato.MainLogin.StudentLoginActivity;
import com.application.bookmobato.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class StudentAccountInformationX extends AppCompatActivity {

    ImageView studentImage;
    TextView id, name, level, section, strand, pass;
    DatabaseReference databaseReference;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_account_information2);

        findID();

        databaseReference = FirebaseDatabase.getInstance().getReference("UserInformation");

        sharedPreferences = getSharedPreferences("MyCache", Context.MODE_PRIVATE);
        String value = sharedPreferences.getString("keyID", "default_value");
        Query query = databaseReference.orderByChild("id").equalTo(value);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        id.setText((ds.child("id")).getValue().toString());
                        name.setText((ds.child("name").getValue().toString()));
                        level.setText((ds.child("gradelevel").getValue().toString()));
                        section.setText((ds.child("section").getValue().toString()));
                        strand.setText((ds.child("strand").getValue().toString()));
                        pass.setText((ds.child("pass")).getValue().toString());
                        String imageUrl = ds.child("image").getValue(String.class);
                        if (imageUrl != null) {
                            Picasso.get().load(imageUrl).into(studentImage);
                        }
                    }
                } else {
                    Log.d("TAG", "Data does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", "Error: " + databaseError.getMessage());
            }
        });
    }
    private void findID() {
        id = findViewById(R.id.student_id_txt);
        name = findViewById(R.id.student_name_txt);
        level = findViewById(R.id.student_level_txt);
        section = findViewById(R.id.student_section_txt);
        strand = findViewById(R.id.student_strand_txt);
        pass = findViewById(R.id.student_pass_txt);
        studentImage = findViewById(R.id.imageView);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(StudentAccountInformationX.this, StudentBookListActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}