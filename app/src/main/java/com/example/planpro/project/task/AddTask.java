package com.example.planpro.project.task;

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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.planpro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class AddTask extends AppCompatActivity {

    private EditText TName;
    private Button SDate;
    private Button EDate;
    private LinearLayout Resources;
    private ImageView back;
    private EditText RName;
    private EditText RCost;
    private EditText TCost;
    private Button AddMoreRes;
    private Button DeleteLastRes;
    private Button Create;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat DateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    private SimpleDateFormat DateFormat1 = new SimpleDateFormat("E,dd MMM yyyy");
    // var's used for check day and time
    private Date StartD;
    private Date FinishD;
    private Date StartPrj;
    private Date EndPrj;
    private Date LateEndPrjOld;
    private Timestamp FDTS;
    private Timestamp LDTS;
    private Timestamp LateEndPRj;
    //var's used for resources info
    private String Rname;
    private String Rcost;
    private String TaskID;
    private ArrayList<Resource> ResourceList;
    private boolean Resadded = false;
    //for sotre info in DB
    private String ProjectID;
    private String ProjStart;
    private String ProjEnd;
    private String ProjLateEnd;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean updatePrjDate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        TaskID = db.collection("Tasks").document().getId();
        ProjectID = getIntent().getStringExtra("project_id");
        ProjStart = getIntent().getStringExtra("start_Date");
        ProjEnd = getIntent().getStringExtra("end_Date");
        ProjLateEnd = getIntent().getStringExtra("late_end");

        //for test can't send the id
       if (ProjectID == null) {
            ProjectID = "KEMUHdFjORgcUxujCT64";
        }
        if (ProjStart == null) {
            ProjStart = "Sun,2 Aug 2020";
        }
        if (ProjEnd == null) {
            ProjEnd = "Fri,2 Oct 2020";
        }
        if (ProjLateEnd == null) {
            ProjLateEnd = "Fri,2 Oct 2020";
        }
        //set for edit texts and buttons
        Setting();

        back = findViewById(R.id.backButton);

        //the start day when click
        SDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialogStartDate = new DatePickerDialog(AddTask.this, R.style.DialogTheme, FromDate, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialogStartDate.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialogStartDate.show();
            }
        });
        //the finish day when click
        EDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialogFinishDate = new DatePickerDialog(AddTask.this, R.style.DialogTheme, TillDate, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialogFinishDate.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialogFinishDate.show();
            }
        });

        //when user click add more resources
        AddMoreRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add linear layout consists of  tew edit texts for name and cost
                LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                //linearLayout.layout(5, 100, 5,5);
                linearLayout.layout(5, 100, 5, 5);
                //add the created linear layout to the parent layout that contains resources info
                Resources.addView(linearLayout);

                //create resource name edit text
                EditText rName = new EditText(getApplicationContext());
                rName.setHint("Resource Name" + Resources.getChildCount());
                rName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                rName.setBackground(RName.getBackground());
                rName.setWidth(RName.getWidth());
                rName.setHeight(RName.getHeight());
                rName.setBottom(10);
                rName.setPadding(5, 5, 5, 5);
                rName.setId(Resources.getChildCount());
                Log.d("ID", rName.getId() + "");
                //add the created edit text to linear layout
                linearLayout.addView(rName);

                //create resource cost edit text
                EditText rCost = new EditText(getApplicationContext());
                rCost.setHint("Resource Cost" + Resources.getChildCount());
                rCost.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                rCost.setBackground(RCost.getBackground());
                rCost.setWidth(RCost.getWidth());
                rCost.setHeight(RCost.getHeight());
                rCost.setPadding(5, 5, 5, 5);
                rCost.setTop(10);
                rCost.setInputType(RCost.getInputType());
                rCost.setId(Resources.getChildCount());
                Log.d("ID", rCost.getId() + "");
                //add the created edit text to linear layout
                linearLayout.addView(rCost);
            }
        });
        //when click delete res
        DeleteLastRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check first the res has more than one cause at lest one is required
                if (Resources.getChildCount() > 1) {
                    //remove last one
                    Resources.removeViewAt(Resources.getChildCount() - 1);
                }
                //when the count equal to one can't removed
                else {
                    Toast.makeText(AddTask.this, "You should enter at least one resource", Toast.LENGTH_LONG).show();
                }
            }
        });


        //click the create button
        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check the resources all not empty
                ResourceList = new ArrayList<Resource>();
                for (int i = 0; i < Resources.getChildCount(); i++) {
                    LinearLayout linearLayout = (LinearLayout) Resources.getChildAt(i);
                    if (checkResources(linearLayout)) {
                        //the resources info not empty
                        //added to the list
                        ResourceList.add(new Resource(Rname, Integer.parseInt(Rcost), TaskID));
                    }
                }
                //check all fileds not empty && also check when Resources.getChildCount() == ResourceList.count()
                if (checkFields(TName.getText().toString(), TCost.getText().toString()
                        , SDate.getText().toString(), EDate.getText().toString())
                        && ResourceList.size() == Resources.getChildCount()) {
                    //check the end date after or equal to start date
                    if (checkDays(SDate.getText().toString(), EDate.getText().toString())) {
                        //save resources in firebase
                        for (int i = 0; i < ResourceList.size(); i++) {
                            Resource resources1 = ResourceList.get(i);
                            SaveResource(resources1);
                        }
                        //save the task in firebase
                        SaveTask(TName.getText().toString(), TCost.getText().toString()
                                , SDate.getText().toString(), EDate.getText().toString(),
                                ResourceList);
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getSupportActionBar().hide();
    }


    //for from day
    private final DatePickerDialog.OnDateSetListener FromDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SDate.setText(DateFormat.format(calendar.getTime()));
        }
    };
    //for end day
    private final DatePickerDialog.OnDateSetListener TillDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            EDate.setText(DateFormat.format(calendar.getTime()));
        }
    };

    private void Setting() {
        TName = findViewById(R.id.TaskName);
        SDate = findViewById(R.id.StartButton);
        EDate = findViewById(R.id.EndButton);
        Resources = findViewById(R.id.Rlinear);
        RName = findViewById(R.id.Resources);
        RCost = findViewById(R.id.ResourceCost);
        TCost = findViewById(R.id.TaskCost);
        AddMoreRes = findViewById(R.id.AddMoreRe);
        DeleteLastRes = findViewById(R.id.DeleteLR);
        Create = findViewById(R.id.Create);
    }

    private boolean checkResources(LinearLayout linearLayout) {
        Rname = ((EditText) linearLayout.getChildAt(0)).getText().toString();
        Rcost = ((EditText) linearLayout.getChildAt(1)).getText().toString();
        if (!(TextUtils.isEmpty(Rname)) && !(TextUtils.isEmpty(Rcost))) {
            return true;
        }
        Toast.makeText(AddTask.this, "All Resources Fields Required", Toast.LENGTH_LONG).show();
        return false;

    }

    private boolean checkFields(String Tname, String Tcost, String Sdate, String Edate) {
        if (!(TextUtils.isEmpty(Tname)) && !(TextUtils.isEmpty(Tcost))
                && !(Sdate.equals("Set Date")) && !(Edate.equals("Set Date"))) {
            return true;
        }
        Toast.makeText(AddTask.this, "All Fields Required", Toast.LENGTH_LONG).show();
        return false;
    }

    private boolean checkDays(String SDate, String EDate) {
        Date CurrentDate = new Date();

        //convert string to date to used in compare
        try {
            StartD = DateFormat.parse(SDate);
            FinishD = DateFormat.parse(EDate);
            StartPrj = DateFormat1.parse(ProjStart);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //check if the start task date before project start date
        if ((StartD.before(StartPrj) || (StartD.compareTo(StartPrj) == -1))) {
            Toast.makeText(AddTask.this, "The Chosen Start Date Must Not Before Project Date", Toast.LENGTH_LONG).show();
            return false;
        }
        // check if end day before start day
        if (FinishD.before(StartD) || (FinishD.compareTo(StartD) == -1)) {
            Toast.makeText(AddTask.this, "The End Day must be after or same as Start Day", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean SaveResource(Resource resources) {
        //store the resource obj in firestore
        String ResID = db.collection("Resource").document().getId();
        resources.setID(ResID);
        Map<String, Object> Res = new HashMap<>();
        Res.put("name", resources.getName());
        Res.put("cost", resources.getCost() + "");
        Res.put("taskID", TaskID);

        db.collection("Resource").document(ResID)
                .set(Res)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Added", "Success");
                            Resadded = task.isSuccessful();
                        } else {
                            Log.d("Added", "Error");
                        }
                    }
                });
        return Resadded;
    }

    private void SaveTask(String Tname, String Tcost, String SDate, String EDate, ArrayList<Resource> resources) {
        try {
            StartD = DateFormat.parse(SDate);
            FDTS = new Timestamp(StartD);
            FinishD = DateFormat.parse(EDate);
            LDTS = new Timestamp(FinishD);
            EndPrj = DateFormat1.parse(ProjEnd);
            LateEndPrjOld = DateFormat1.parse(ProjLateEnd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //check when finish task date after prj
        //check if the finish task date after project date end
        if ((FinishD.after(EndPrj) || (FinishD.compareTo(EndPrj) == 1))) {
            //update the project end to be the start finish
            //check if the late end == end date

            //if late == end or before the end task update
            if (LateEndPrjOld.compareTo(EndPrj) == 0 || LateEndPrjOld.before(FinishD)) {
                try {
                    LateEndPRj = new Timestamp(DateFormat.parse(EDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                db.collection("Projects")
                        .document(ProjectID).update("LateEnd", LateEndPRj)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                                if (task.isSuccessful()) {
                                    updatePrjDate = true;
                                    Log.d("Lateend", "Updated" + LateEndPRj);
                                }
                            }
                        });
            }
        }
            //first update proj

            final Task task = new Task(TaskID, Tname, Double.parseDouble(Tcost), FDTS, LDTS, resources);
            //store the task obj in firestore

            final Map<String, Object> Task = new HashMap<>();
            Task.put("Name", task.getName());
            Task.put("TaskCost", task.getCost() + "");
            Task.put("EarlyStartDate", FDTS);
            Task.put("EarlyFinishDate", LDTS);
            Task.put("ProjectID", ProjectID);

            Log.d("Size Array", resources.size() + "");

            for (int i = 0; i < resources.size(); i++)
                Task.put("ResourceID" + i, resources.get(i).getID());

            db.collection("Tasks").document(TaskID)
                    .set(Task)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddTask.this, "Task Added Successfully", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(AddTask.this, "Fail To Add Task, Please Try Again", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }

    }

