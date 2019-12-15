package com.gameguildstudios.markyourself;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;


public class ActivityCourse extends AppCompatActivity {
    ImageButton addAssignment;
    Button calcBtn;
    SwipeMenuListView mListView;
    AssignmentListAdapter mAdapter;
    ArrayList<Assignment> assignmentList;
    EditText goalGrade;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        mListView = (SwipeMenuListView) findViewById((R.id.swipeListView));
        addAssignment = (ImageButton)findViewById(R.id.addAWBtn);
        calcBtn = (Button)findViewById(R.id.calcBtn);

        assignmentList = new ArrayList<>();
        mAdapter = new AssignmentListAdapter(ActivityCourse.this,
                R.layout.layout_assignments, assignmentList);

        mListView.setAdapter(mAdapter);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            getSupportActionBar().setTitle(bundle.getString("CourseName"));
        }

        //Add button functions
        onBtnClick();

        //Swipe Menu
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "edit" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0,174,255)));
                // set item width
                openItem.setWidth(170);
                // set item title
                openItem.setTitle("Edit");
                // set item title font size
                openItem.setTitleSize(13);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                deleteItem.setTitle("Del");
                // set item title font size
                deleteItem.setTitleSize(13);
                // set item title font color
                deleteItem.setTitleColor(Color.WHITE);
                // set item width
                deleteItem.setWidth(170);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        mListView.setMenuCreator(creator);

        //SetMenuCreator Listener
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0: //Edit
                        showInputBox(assignmentList.get(position), position);
                        break;
                    case 1: //Delete
                        removeItem(position);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }

    //Buttons onClick Functions
    public void onBtnClick(){
        addAssignment.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                showInputAssignmentBox();
            }
        });
        calcBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){int w = 0;
                //calculate expected grade
                goalGrade = (EditText)findViewById(R.id.inputGradeGoal);
                if(!goalGrade.getText().toString().trim().equals("") && !assignmentList.isEmpty()){
                    double finalGoal = Integer.parseInt(goalGrade.getText().toString().trim());
                    double c = 0; //current grade
                    double wa = 0; //weight accounted for

                    int g = 0;

                    for(int i = 0; i< assignmentList.size();i++){  //calculate final values or wr, wa, c
                        w = assignmentList.get(i).getWeight();
                        g = assignmentList.get(i).getGrade();

                        if (g > 0){
                            wa+=w;
                            c+=w*g;
                        }
                        else if(g==0){ // real grade has not exist //if first elem (c = 0)
                            if(i!=0){
                                c = c/wa;
                            }
                            double gradeNeeded = ((finalGoal-c*(wa/100))/((100-wa)/100));
                            Assignment assignment = assignmentList.get(i);
                            assignment.target = "Goal: "+ Math.ceil(gradeNeeded);
                            mAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                    Toast.makeText(getApplicationContext(),"Next goal calculated.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    //Remove list item
    public void removeItem(int position){
        Assignment item = assignmentList.get(position);
        assignmentList.remove(item);
        mAdapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(),"Removed assignment.", Toast.LENGTH_SHORT).show();
    }

    //Edit List Item
    public void showInputBox(final Assignment assignment, final int index){
        final Dialog dialog=new Dialog(ActivityCourse.this);
        dialog.setTitle("Edit Assignment");
        dialog.setContentView(R.layout.edit_assignment);
        TextView txtMessage=(TextView)dialog.findViewById(R.id.txtmessage);
        txtMessage.setText("Edit assignment");
        txtMessage.setTextColor(Color.parseColor("#00aeff"));

        //Calls up the calender picker if user clicks on date edit text
        final TextView editDate = (TextView)dialog.findViewById(R.id.inputDate);
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        ActivityCourse.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String d = year + "/" + month + "/" + String.format("%02d",day);
                editDate.setText(d);
            }
        };

        //User clicks update button
        Button bt=(Button)dialog.findViewById(R.id.btdone);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editAsg = (EditText)dialog.findViewById(R.id.inputAssignment);
                final EditText editWeight=(EditText)dialog.findViewById(R.id.inputWeight);
                final EditText editGrade=(EditText)dialog.findViewById(R.id.inputGradeGoal);

                if(!editAsg.getText().toString().trim().equals("")) {
                    String a = editAsg.getText().toString().trim();
                    assignment.name = a;
                }
                if(!editGrade.getText().toString().trim().equals("")) {
                    Integer g = Integer.parseInt(editGrade.getText().toString().trim());
                    goalGrade = (EditText)findViewById(R.id.inputGradeGoal);
                    if(g > 100){
                        editGrade.setText("");
                        Toast.makeText(getApplicationContext(),"Grade cannot be over 100.", Toast.LENGTH_SHORT).show();
                    }else{
                        assignment.grade = g;
                        assignment.target = "";
                    }
                }
                if(!editDate.getText().toString().trim().equals("")) {
                    String d = editDate.getText().toString().trim();
                    assignment.date = "Due on: "+ d;
                }
                if(!editWeight.getText().toString().trim().equals("")) {
                    Integer w = Integer.parseInt(editWeight.getText().toString().trim());
                    if(w>100){
                        editWeight.setText("");
                        Toast.makeText(getApplicationContext(),"Weight cannot be over 100%.", Toast.LENGTH_SHORT).show();
                    }else{
                        assignment.weight = w;
                    }
                }
                assignmentList.set(index, assignment);
                Collections.sort(assignmentList); //Sort assignments by date
                mAdapter.notifyDataSetChanged();
                dialog.dismiss();
                Toast.makeText(getApplicationContext(),"Assignment edited.", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    //Add assignment
    public void showInputAssignmentBox(){
        final Dialog dialog=new Dialog(ActivityCourse.this);
        dialog.setTitle("Edit Assignment");
        dialog.setContentView(R.layout.input_assignment_box);
        TextView txtMessage=(TextView)dialog.findViewById(R.id.txtmessage);
        txtMessage.setText("Add new assignment");
        txtMessage.setTextColor(Color.parseColor("#00aeff"));
        final TextView inputD = (TextView) dialog.findViewById(R.id.inputD);

        //Calls up the calender picker if user clicks on date edit text
        inputD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        ActivityCourse.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String d = year + "/" + month + "/" + String.format("%02d",day);
                inputD.setText(d);
            }
        };

        //When user clicks the Add button
        Button bt=(Button)dialog.findViewById(R.id.btdone);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText inputA = (EditText)dialog.findViewById(R.id.inputA);
                final EditText inputW = (EditText)dialog.findViewById(R.id.inputW);

                if(!inputD.getText().toString().trim().equals("") && !inputA.getText().toString().trim().equals("") && !inputW.getText().toString().trim().equals("")){
                    String a = inputA.getText().toString().trim();
                    Integer w = Integer.parseInt(inputW.getText().toString().trim());
                    if(w>100 || w <=0) {
                        inputW.setText("");
                        Toast.makeText(getApplicationContext(), "Weight value is invalid.", Toast.LENGTH_SHORT).show();
                    }else {
                        String d = inputD.getText().toString().trim();
                        Assignment newA = new Assignment(a, w, 0, "Due on: " + d, "Goal: ");
                        assignmentList.add(newA);
                        Collections.sort(assignmentList); //Sort assignments by date
                        mAdapter.notifyDataSetChanged();
                        inputA.setText("");
                        inputW.setText("");
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "New assignment added.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Missing values.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }
}
