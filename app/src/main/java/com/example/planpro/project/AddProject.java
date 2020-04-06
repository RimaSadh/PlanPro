package com.example.planpro.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.planpro.R;
import com.example.planpro.project.task.AddTask;
import com.example.planpro.project.task.Resource;
import com.example.planpro.project.task.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddProject extends AppCompatActivity {

    private ImageView back;
    private Button save, start, end;
    private EditText name, desc;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat DateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    //EEE, d MMM yyyy
    private Date StartD;
    private Date FinishD;
    private Timestamp FDTS;
    private Timestamp LDTS;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    private final DatePickerDialog.OnDateSetListener FromDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            start.setText(DateFormat.format(calendar.getTime()));
        }
    };
    //for end day
    private final DatePickerDialog.OnDateSetListener TillDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            end.setText(DateFormat.format(calendar.getTime()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);

        getSupportActionBar().hide();

        back = findViewById(R.id.backButton);
        save = findViewById(R.id.button);
        name = findViewById(R.id.nameET);
        start = findViewById(R.id.start);
        end = findViewById(R.id.end);
        desc = findViewById(R.id.description);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkFields(name.getText().toString(),desc.getText().toString()
                 , start.getText().toString(), end.getText().toString())){

                    if(checkDays(start.getText().toString(), end.getText().toString())){

                        addProject(name.getText().toString(),desc.getText().toString()
                                , start.getText().toString(), end.getText().toString());
                    }
                }
            }
        });

        start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialogStartDate = new DatePickerDialog(AddProject.this, R.style.DialogTheme,FromDate, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialogStartDate.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialogStartDate.show();
            }

        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialogFinishDate = new DatePickerDialog(AddProject.this, R.style.DialogTheme,TillDate, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialogFinishDate.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialogFinishDate.show();
            }
        });

    }


    private boolean checkFields (String name,String desc, String Sdate, String Edate ) {
        if (!(TextUtils.isEmpty(name)) && !(TextUtils.isEmpty(desc)) &&
                !(Sdate.equals("Set Date")) && !(Edate.equals("Set Date"))){
            return true;
        }
        Toast.makeText(AddProject.this, "All Fields Required", Toast.LENGTH_LONG).show();
        return false;
    }


    private boolean checkDays (String SDate, String EDate) {
        Date CurrentDate = new Date();
        //convert string to date to used in compare
        try {
            StartD = DateFormat.parse(SDate);
            FinishD = DateFormat.parse(EDate);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        // check if end day before start day
        if (FinishD.before(StartD) || (FinishD.compareTo(StartD) == -1)) {
            Toast.makeText(AddProject.this,"The End Day must be after or same as Start Day", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    private void addProject (String name,String desc, String SDate, String EDate) {
        try {
            StartD = DateFormat.parse(SDate);
            FDTS = new Timestamp(StartD);
            FinishD = DateFormat.parse(EDate);
            LDTS = new Timestamp(FinishD);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        final Project pro = new Project(name, desc, FDTS, LDTS);

        String proID = db.collection("Projects").document().getId();
        pro.setId(proID);
        final Map<String, Object> Pro = new HashMap<>();
        Pro.put("Name", pro.getName());
        Pro.put("Description", pro.getDescription());
        Pro.put("StartDate", FDTS);
        Pro.put("EndDate", LDTS);
        Pro.put("LateEnd", LDTS);
        Pro.put("ProjectID", proID);
        Pro.put("TotalCost", 0);

        db.collection("Projects").document(proID)
                .set(Pro)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddProject.this, "Project added successfully", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(AddProject.this, "Failed to add project, Try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
