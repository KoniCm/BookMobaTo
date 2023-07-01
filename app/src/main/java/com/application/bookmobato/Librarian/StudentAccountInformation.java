package com.application.bookmobato.Librarian;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.bookmobato.MainLogin.LibrarianLoginActivity;
import com.application.bookmobato.MainLogin.MainLoginActivity;
import com.application.bookmobato.R;
import com.bumptech.glide.Glide;

public class StudentAccountInformation extends AppCompatActivity {

    ImageView studentImage;
    TextView id, name, level, section, strand, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_account_information);

        findID();

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            Glide.with(this).load(bundle.getString("image")).into(studentImage);
            id.setText(bundle.getString("id"));
            name.setText(bundle.getString("name"));
            level.setText(bundle.getString("level"));
            section.setText(bundle.getString("section"));
            strand.setText(bundle.getString("strand"));
            pass.setText(bundle.getString("pass"));
        }
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
        Intent intent = new Intent(StudentAccountInformation.this, StudentActivityList.class);
        startActivity(intent);
        super.onBackPressed();
    }
}