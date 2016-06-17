package com.owl.crazynote;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder>{
    private Context context;
    private List<Task> taskItems;
    public TaskAdapter(List<Task> taskItems, Context context) {
        this.taskItems = taskItems;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.task_list_row, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {
        final Task task = taskItems.get(position);
        viewHolder.noteTitle.setText(task.getTitle());
        if(!(task.getDate()=="no")){
            try {
                viewHolder.date.setText(dateFormatter(task.getDate()));
            } catch (ParseException e) {
//                e.printStackTrace(); shhhhhhhhhhhhhh

            }
        }else{
//            viewHolder.noteTitle.setText(" "); TODO: 07.06.16
//            viewHolder.date.setVisibility(TextView.INVISIBLE);
        }
        viewHolder.iconCircle.setImageDrawable(generatorIcon(task.getTitle(), task.getColorCircleIcon()));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,TaskActivity.class);
                intent.putExtra("title", task.getTitle());
                intent.putExtra("description", task.getDescription());
                intent.putExtra("date", task.getDate());
                intent.putExtra("id", task.getId());
                context.startActivity(intent);
            }
        });
    }
    private Drawable generatorIcon(String giverSign, int color){
        if(giverSign.equals(" ")){
            return TextDrawable.builder().buildRound("T",color);
        }else{
            return TextDrawable.builder().buildRound((giverSign.substring(0,1)).toUpperCase(),color);

        }
    }
    private String dateFormatter(String date) throws ParseException {
        DateFormat format1 = new SimpleDateFormat("dd.MM.yyyy",Locale.UK);
        DateFormat format2 = new SimpleDateFormat("d, MMMM",Locale.UK);
        Date date2 = format1.parse(date);
        return format2.format(date2);
    }

    @Override
    public int getItemCount() {
        return taskItems.size();
    }


    public final static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView noteTitle;
        public TextView date;
        public ImageView iconCircle;

        public MyViewHolder(View view) {
            super(view);
            noteTitle = (TextView) view.findViewById(R.id.title_text_view);
            date = (TextView) view.findViewById(R.id.date);
            iconCircle = (ImageView) view.findViewById(R.id.circle_sing_icon);

        }
    }
}