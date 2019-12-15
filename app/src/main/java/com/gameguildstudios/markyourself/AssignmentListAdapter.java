package com.gameguildstudios.markyourself;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class AssignmentListAdapter extends ArrayAdapter<Assignment> {

    private static final String TAG = "PersonListAdapter";

    private Context mContext;
    private int mResource;

    private static class ViewHolder {
        TextView name;
        TextView weight;
        TextView grade;
        TextView date;
        TextView target;
    }

    public AssignmentListAdapter(Context context, int resource, ArrayList<Assignment> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the persons information
        String name = getItem(position).getName();
        Integer weight = getItem(position).getWeight();
        Integer grade = getItem(position).getGrade();
        String date = getItem(position).getDate();
        String target = getItem(position).getTarget();

        //Create assignment
        Assignment assignment = new Assignment(name,weight,grade,date, target);

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        ViewHolder holder;


        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.assignmentView);
            holder.weight = (TextView) convertView.findViewById(R.id.weightView);
            holder.grade = (TextView) convertView.findViewById(R.id.gradeView);
            holder.date = (TextView) convertView.findViewById(R.id.dateView);
            holder.target = (TextView) convertView.findViewById(R.id.targetView);

            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        holder.name.setText(assignment.getName());
        holder.weight.setText(assignment.getWeight().toString());
        holder.grade.setText(assignment.getGrade().toString());
        holder.date.setText(assignment.getDate());
        holder.target.setText(assignment.getTarget());


        return convertView;
    }
}