package com.example.planpro;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.planpro.project.AddProject;
import com.example.planpro.project.Project;
import com.example.planpro.project.ProjectsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ViewProjects";

    private RecyclerView rvProjects;
    private RecyclerView.LayoutManager Projects_LayoutManager;
    private ProjectsAdapter Projects_adapter;
    private List<Project> Projects = new ArrayList<>();

    private dbSetUp DB = new dbSetUp();
    private SimpleDateFormat DateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    private  Timestamp FDTS, LDTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // getSupportActionBar().hide();

        // Add Button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                startActivity(new Intent(getApplicationContext(), AddProject.class));
            }
        });

        // Projects RV setup
        rvProjects = findViewById(R.id.ProjectRV);

        Projects_LayoutManager = new LinearLayoutManager(this);
        rvProjects.setLayoutManager ( Projects_LayoutManager );
        Projects_adapter = new ProjectsAdapter(this, Projects);

        rvProjects.setAdapter(Projects_adapter);

        // Retrieve Projects Method
        getProjects();
    }

    private void getProjects(){

        CollectionReference clubRef = dbSetUp.db.collection("Projects");
        clubRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            Projects.clear();


                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final Project project = new Project();

                                String project_id = document.get("ProjectID").toString();
                                String project_name = document.get("Name").toString();
                               // String project_manager = document.get("ProjectManager").toString();
                                String projects_description = document.get("Description").toString();
                                String start_date = document.get("StartDate").toString();
                                String end_date = document.get("EndDate").toString();
                                String total_cost = document.get("TotalCost").toString();



                                try {
                                    Date StartD = DateFormat.parse(start_date);
                                    FDTS = new Timestamp(StartD);
                                    Date FinishD = DateFormat.parse(end_date);
                                    LDTS = new Timestamp(FinishD);
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                project.setId(project_id);
                                project.setName(project_name);
                               // project.setProjectManager(project_manager);
                                project.setDescription(projects_description);
                                project.setStartDate(FDTS);
                                project.setEndDate(LDTS);
                                project.setTotalCost(Double.parseDouble(total_cost));


                                // Add to list
                                Projects.add(project);
                                Projects_adapter.notifyDataSetChanged();
                            }

                        } else Log.w(TAG, "Error getting documents.", task.getException());

                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Projects.clear();
        getProjects();
    }
}
