package com.example.planpro.project.task;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planpro.R;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder>{

    private Context mContext;
    private List<Task> tasks;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView task_name, taskCost, start_date, end_date;
        private CardView card;

        public MyViewHolder(View view) {
            super(view);

            task_name =  view.findViewById(R.id.Taskname);
            taskCost = view.findViewById(R.id.cost);
            start_date = view.findViewById(R.id.startDate);
            end_date = view.findViewById(R.id.endDate);

            card = view.findViewById(R.id.task_card);
            card.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //go to viewtask and pass task id to display details
            Intent intent = new Intent(view.getContext(), ViewTask.class);
            intent.putExtra("task_id", tasks.get(getAdapterPosition()).getID());
            intent.putExtra("task_name", tasks.get(getAdapterPosition()).getName());
            intent.putExtra("start_date", tasks.get(getAdapterPosition()).getStart());
            intent.putExtra("end_date", tasks.get(getAdapterPosition()).getEnd());
            intent.putExtra("task_cost", tasks.get(getAdapterPosition()).getCost()+"");

            mContext.startActivity(intent);
        }

    }//End MyViewHolder Class


    public TaskAdapter(Context mContext, List<Task> l) {
        this.mContext = mContext;
        this.tasks = l;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_card, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Task task = tasks.get(position);

        holder.task_name.setText(task.getName());
        holder.taskCost.setText(task.getCost()+"");

        String start_date = task.getStart();
        String end_date = task.getEnd();

        holder.start_date.setText(start_date);
        holder.end_date.setText(end_date);

    }


    @Override
    public int getItemCount() {
        if (tasks!=null)
            return tasks.size();
        else return 0;
    }

    public void updateList(List<Task> list){
        if (list.isEmpty()){
            Toast.makeText(this.mContext, "No task found", Toast.LENGTH_SHORT).show();
            tasks = new ArrayList<Task>();
            notifyDataSetChanged();
        }else{
            tasks = new ArrayList<Task>();
            tasks.addAll(list);
            notifyDataSetChanged();
        }
    }


}
