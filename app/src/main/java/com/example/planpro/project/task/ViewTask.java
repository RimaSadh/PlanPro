package com.example.planpro.project.task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.planpro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

public class ViewTask extends AppCompatActivity {

    private ImageView back;
    private TextView taskNameTV, startDateTV, endDateTV, taskCostTV,resourcesTV;
    private String taskId, taskName, startD, endD, resources="";
    private String taskC;
    private Button delete;
    private boolean update = false;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);

        getSupportActionBar().hide();

        init();

        // get extras
        taskId = getIntent().getStringExtra("task_id");
        taskName = getIntent().getStringExtra("task_name");
        startD = getIntent().getStringExtra("start_date");
        endD = getIntent().getStringExtra("end_date");
        taskC = getIntent().getStringExtra("task_cost");

        //for test can't send the id
        if (taskId == null) {
            taskId ="XAh1gJkSbQWKdnYOgMQE";
        }
        if (taskName == null) {
            taskName = "Controlling";
        }
        if (startD == null) {
            startD = "Tue,11 Aug 2020";
        }
        if (endD == null) {
            endD = "Sun,29 Sep 2020";
        }
        if (taskC == null) {
            taskC = "200.0";
        }


        getResource();

        taskNameTV.setText(taskName);
        startDateTV.setText(startD);
        endDateTV.setText(endD);
        taskCostTV.setText(taskC);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(ViewTask.this);
                dialog.setTitle("Delete Task");
                dialog.setMessage("Are you sure you want to delete this task ? ");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.collection("Tasks")
                                .document(taskId)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull final Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if(document.exists()){
                                                //Delete reso of this task
                                                db.collection("Resource").whereEqualTo("taskID",document.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        for (QueryDocumentSnapshot document : task.getResult()){
                                                            document.getReference().delete();
                                                        }
                                                    }
                                                });

                                                        DocumentReference d= document.getReference();
                                                        d.delete();

                                                //TODO check when the end date of task == late end and so the late end != end of prj
                                                Timestamp End = (Timestamp) document.get("EarlyFinishDate");
                                                Calendar CTaskEnd = Calendar.getInstance();
                                                CTaskEnd.setTimeInMillis(End.getSeconds()*1000);
                                                checkLateEndPrj(document.get("ProjectID").toString(), CTaskEnd);

                                                    }


                                            Toast.makeText(ViewTask.this, "Task deleted",
                                                    Toast.LENGTH_LONG).show();

                                            finish();
                                                }



                                        else {
                                            Toast.makeText(ViewTask.this, task.getException().getMessage(),
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }); //end dialog onComplete
                    }



                });// end dialog onclick

                dialog.setNegativeButton("Cancel",null);

                AlertDialog alertDialog =  dialog.create();
                alertDialog.show();

            }
        });


    }

    private void getResource() {

        db.collection("Resource")
                .whereEqualTo("taskID",taskId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String resourceN = document.get("name").toString();
                                String resourceC = document.get("cost").toString();

                                resources += "- " + resourceN + " | " + resourceC + " $"+" \n";

                            }
                            resourcesTV.setText(resources);
                    }
                });

    }

    private void init() {
        back = findViewById(R.id.backButton);
        delete = findViewById(R.id.DeleteT);
        taskNameTV = findViewById(R.id.textView2);
        startDateTV = findViewById(R.id.Start);
        endDateTV = findViewById(R.id.End);
        resourcesTV = findViewById(R.id.Resources);
        taskCostTV = findViewById(R.id.TaskCost);

    }

    private void checkLateEndPrj (final String prjID, final Calendar CTaskEnd) {
        //TODO take the prj id and search for the late end
        db.collection("Projects").document(prjID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Timestamp lateEnd =(Timestamp) documentSnapshot.get("LateEnd");
                        Calendar CLate = Calendar.getInstance();
                        CLate.setTimeInMillis(lateEnd.getSeconds()*1000);
                        Timestamp endDate =(Timestamp) documentSnapshot.get("EndDate");
                        Calendar CEnd = Calendar.getInstance();
                        CEnd.setTimeInMillis(endDate.getSeconds()*1000);
                        //check if end date == late end if true no update
                        if (CLate.get(Calendar.YEAR)== CEnd.get(Calendar.YEAR)
                                && CLate.get(Calendar.MONTH)+1 == CEnd.get(Calendar.MONTH)+1
                                    && CLate.get(Calendar.DAY_OF_MONTH)== CEnd.get(Calendar.DAY_OF_MONTH)){
                            //no change
                            return;
                        }
                        else {
                            //if it not
                            //check that the late date == task end date
                            if (CLate.get(Calendar.YEAR)== CTaskEnd.get(Calendar.YEAR)
                                    && CLate.get(Calendar.MONTH)+1 == CTaskEnd.get(Calendar.MONTH)+1
                                    && CLate.get(Calendar.DAY_OF_MONTH)== CTaskEnd.get(Calendar.DAY_OF_MONTH)){
                                //here update the late end
                                //search in proj if there was task after the end date
                                //check that if no task found finish after the project then save the old end date
                                if (! updateLateEndPRJ (prjID, CEnd)) {
                                        db.collection("Projects")
                                                .document(prjID).update("LateEnd", endDate)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d("Lateend", "Updated as the end date");
                                                        }
                                                    }
                                                });
                                }
                            }
                        }
                    }
                });
    }

    private boolean updateLateEndPRJ (final String prjID, final Calendar CTPRJEnd) {
        db.collection("Tasks").whereEqualTo("ProjectID",prjID)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (final QueryDocumentSnapshot document : task.getResult() ){
                    //search if the end day > end project
                    Timestamp endDate =(Timestamp) document.get("EarlyFinishDate");
                    Calendar CEnd = Calendar.getInstance();
                    CEnd.setTimeInMillis(endDate.getSeconds()*1000);
                    //check if task year > prj year
                    if ( (CEnd.get(Calendar.YEAR) > CTPRJEnd.get(Calendar.YEAR))
                            || (CEnd.get(Calendar.YEAR) == CTPRJEnd.get(Calendar.YEAR) && (CEnd.get(Calendar.MONTH) > CTPRJEnd.get(Calendar.MONTH)) )
                            || (CEnd.get(Calendar.YEAR) == CTPRJEnd.get(Calendar.YEAR) && (CEnd.get(Calendar.MONTH) == CTPRJEnd.get(Calendar.MONTH)) && (CEnd.get(Calendar.DAY_OF_MONTH) > CTPRJEnd.get(Calendar.DAY_OF_MONTH))  )
                    ) {
                        //here update the late end as the end date
                        db.collection("Projects")
                                .document(prjID).update("LateEnd", endDate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("Lateend", "Updated as the task finish: "+document.getId());
                                        }
                                    }
                                });
                        update = true;
                    }
                    //if no task finish after proj update late as the end

                }
            }
        });
        return update;
    }
}
