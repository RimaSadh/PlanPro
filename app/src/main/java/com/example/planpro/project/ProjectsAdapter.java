package com.example.planpro.project;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.planpro.R;

import java.util.ArrayList;
import java.util.List;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.MyViewHolder>{

    private Context mContext;
    private List<Project> projectsList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView project_name, projects_description, start_date, end_date;
        private CardView card;

        public MyViewHolder(View view) {
            super(view);

            //Id = id;
            project_name =  view.findViewById(R.id.name);
            //projects_description = view.findViewById(R.id.description);
            start_date = view.findViewById(R.id.sdTV);
            end_date = view.findViewById(R.id.edTV);

            card = view.findViewById(R.id.project_card);
            card.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            Intent intent = new Intent(view.getContext(), ViewProject.class);
            intent.putExtra("project_id", projectsList.get(getAdapterPosition()).getId());
            intent.putExtra("project_name", projectsList.get(getAdapterPosition()).getName());
            intent.putExtra("projects_description", projectsList.get(getAdapterPosition()).getDescription());
            intent.putExtra("start_date",projectsList.get(getAdapterPosition()).getStartDate());
            intent.putExtra("end_date", projectsList.get(getAdapterPosition()).getEndDate());
            intent.putExtra("late_end", projectsList.get(getAdapterPosition()).getLateEnd());
            mContext.startActivity(intent);
        }

    }//End MyViewHolder Class


    public ProjectsAdapter(Context mContext, List<Project> l) {
        this.mContext = mContext;
        this.projectsList = l;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.project_card, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Project project = projectsList.get(position);
        holder.project_name.setText(project.getName());

       if(project.getStartDate() != null && project.getEndDate() != null) {

           String start_date = project.getStartDate();
           String end_date = project.getEndDate();

           holder.start_date.setText(start_date);
           holder.end_date.setText(end_date);

        }
    }


    @Override
    public int getItemCount() {
        if (projectsList!=null)
            return projectsList.size();
        else return 0;
    }

    public void updateList(List<Project> list){
        if (list.isEmpty()){
            Toast.makeText(this.mContext, "No Projects found", Toast.LENGTH_SHORT).show();
            projectsList = new ArrayList<Project>();
            notifyDataSetChanged();
        }else{
            projectsList = new ArrayList<Project>();
            projectsList.addAll(list);
            notifyDataSetChanged();
        }
    }


}
