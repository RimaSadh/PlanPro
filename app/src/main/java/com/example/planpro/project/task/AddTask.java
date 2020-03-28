package com.example.planpro.project.task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.planpro.R;
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


public class AddTask extends AppCompatActivity {

    private EditText TName;
    private Button SDate;
    private Button EDate;
    private LinearLayout Resources;
    private EditText RName;
    private EditText RCost;
    private EditText TCost;
    private Button AddMoreRes;
    private Button DeleteLastRes;
    private Button Create;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat DateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    // var's used for check day and time
    private Date StartD;
    private Date FinishD;
    private Timestamp FDTS;
    private Timestamp LDTS;
    //var's used for resources info
    private String Rname;
    private String Rcost;
    private ArrayList<Resource> ResourceList;
    private boolean Resadded = false;
    //for sotre info in DB
    private String ProjectID = "gQSP57vqXzCZ5vLqtNiI";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //for from day
    private final DatePickerDialog.OnDateSetListener FromDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            calendar.set(Calendar.YEAR,year);
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
            // TODO Auto-generated method stub
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            EDate.setText(DateFormat.format(calendar.getTime()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        //set for edit texts and buttons
        Setting ();

        //the start day when click
        SDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialogStartDate = new DatePickerDialog(AddTask.this, FromDate, calendar
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
                DatePickerDialog datePickerDialogFinishDate = new DatePickerDialog(AddTask.this, TillDate, calendar
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
                //add the created linear layout to the parent layout that contains resources info
                Resources.addView(linearLayout);

                //create resource name edit text
                EditText rName = new EditText(getApplicationContext());
                rName.setHint("Resource Name");
                rName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                //add the created edit text to linear layout
                linearLayout.addView(rName);

                //create resource cost edit text
                EditText rCost = new EditText(getApplicationContext());
                rCost.setHint("Resource Cost");
                rCost.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                //add the created edit text to linear layout
                linearLayout.addView(rCost);
            }
        });
        //when click delete res
        DeleteLastRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check first the res has more than one cause at lest one is required
                if (Resources.getChildCount() > 1){
                    //remove last one
                    Resources.removeViewAt(Resources.getChildCount()-1);
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
                for (int i =0 ; i < Resources.getChildCount(); i++){
                    LinearLayout linearLayout = (LinearLayout) Resources.getChildAt(i);
                    if ( checkResources (linearLayout) ) {
                        //the resources info not empty
                        //added to the list
                        ResourceList.add(new Resource (Rname, Integer.parseInt(Rcost)) );
                    }
                }
                //check all fileds not empty && also check when Resources.getChildCount() == ResourceList.count()
                if ( checkFields (TName.getText().toString(),TCost.getText().toString()
                        ,SDate.getText().toString(), EDate.getText().toString())
                        && ResourceList.size() == Resources.getChildCount()){
                    //check the end date after or equal to start date
                    if ( checkDays (SDate.getText().toString(),EDate.getText().toString()) ){
                        //save resources in firebase
                        for (int i=0; i<ResourceList.size(); i++) {
                            Resource resources1 = ResourceList.get(i);
                            SaveResource (resources1);
                        }
                        //save the task in firebase
                        SaveTask (TName.getText().toString(), TCost.getText().toString()
                                ,SDate.getText().toString(), EDate.getText().toString(),
                                ResourceList);
                    }
                }
            }
        });


        getSupportActionBar().hide();
    }

    private void Setting () {
        TName = findViewById(R.id.TaskName);
        SDate = findViewById(R.id.StartButton);
        EDate = findViewById(R.id.EndButton);
        Resources = findViewById(R.id.Rlinear);
        RName = findViewById(R.id.ResourceName);
        RCost = findViewById(R.id.ResourceCost);
        TCost = findViewById(R.id.TaskCost);
        AddMoreRes = findViewById(R.id.AddMoreRe);
        DeleteLastRes = findViewById(R.id.DeleteLR);
        Create = findViewById(R.id.Create);
    }

    private boolean checkResources (LinearLayout linearLayout) {
        Rname = ((EditText) linearLayout.getChildAt(0)).getText().toString();
        Rcost = ((EditText) linearLayout.getChildAt(1)).getText().toString();
        if (!(TextUtils.isEmpty(Rname)) && !(TextUtils.isEmpty(Rcost)) ) {
            return true;
        }
        Toast.makeText(AddTask.this, "All Resources Fields Required", Toast.LENGTH_LONG).show();
        return false;

    }

    private boolean checkFields (String Tname,String Tcost, String Sdate, String Edate ) {
        if (!(TextUtils.isEmpty(Tname)) && !(TextUtils.isEmpty(Tcost))
        && !(Sdate.equals("Set Date")) && !(Edate.equals("Set Date"))){
            return true;
        }
        Toast.makeText(AddTask.this, "All Fields Required", Toast.LENGTH_LONG).show();
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // check if end day before start day
        if (FinishD.before(StartD) || (FinishD.compareTo(StartD) == -1)) {
            Toast.makeText(AddTask.this,"The End Day must be after or same as Start Day", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean SaveResource (Resource resources) {
            //store the resource obj in firestore
            String ResID = db.collection("Resource").document().getId();
            resources.setID(ResID);
            Map<String, Object> Res = new HashMap<>();
            Res.put("name", resources.getName());
            Res.put("cost", resources.getCost()+"");
            db.collection("Resource").document(ResID)
                    .set(Res)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("Added","Success");
                                Resadded = task.isSuccessful();
                            } else {
                                Log.d("Added","error");
                            }
                        }
                    });
        return Resadded;
    }

    private void SaveTask (String Tname,String Tcost,String SDate, String EDate, ArrayList<Resource> resources) {
        try {
            StartD = DateFormat.parse(SDate);
            FDTS = new Timestamp(StartD);
            FinishD = DateFormat.parse(EDate);
            LDTS = new Timestamp(FinishD);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        final Task task = new Task(Tname, Integer.parseInt(Tcost), FDTS, LDTS, resources);
        //store the task obj in firestore
        String TaskID = db.collection("Task").document().getId();
        task.setID(TaskID);
        final Map<String, Object> Task = new HashMap<>();
        Task.put("Name", task.getName());
        Task.put("TaskCost", task.getCost()+"");
        Task.put("EarlyStartDate", task.getStart());
        Task.put("EarlyFinishDate", task.getEnd());
        Task.put("ProjectID", ProjectID);
        for (int i=0; i<resources.size(); i++)
            Task.put("ResourceID"+i, resources.get(i).getID());

        db.collection("Task").document(TaskID)
                .set(Task)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddTask.this, "The Task Added", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(AddTask.this, "The Task Not Added Try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}
