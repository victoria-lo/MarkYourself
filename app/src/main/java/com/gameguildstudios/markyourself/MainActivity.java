package com.gameguildstudios.markyourself;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.io.Serializable;
import java.util.ArrayList;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

public class MainActivity extends AppCompatActivity {

    SwipeMenuListView listView;
    ArrayAdapter<String> mAdapter;
    ImageButton addCourse;
    EditText newCourse;
    ArrayList<String> courses;
    Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils = new Utils(getApplicationContext());
        setContentView(R.layout.activity_main);

        listView = (SwipeMenuListView) findViewById(R.id.swipeListView);

        addCourse = (ImageButton)findViewById(R.id.addBtn);
        newCourse = (EditText)findViewById(R.id.enterCourse);
        courses = utils.getCourseList();
        mAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, courses);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, ActivityCourse.class);
                intent.putExtra("CourseName", listView.getItemAtPosition(i).toString());
                startActivity(intent);
            }
        });
        listView.setAdapter(mAdapter);

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
                // set item title fontsize
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
                // set item title fontsize
                deleteItem.setTitleSize(13);
                // set item title font color
                deleteItem.setTitleColor(Color.WHITE);
                // set item width
                deleteItem.setWidth(170);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        listView.setMenuCreator(creator);

        //SetMenuCreator Listener
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0: //Edit
                        showInputBox(utils.getCourseList().get(position), position);
                        break;
                    case 1: //Delete
                        removeItem(position);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        //Add Course
        onBtnClick();
    }

    public void onBtnClick(){
        addCourse.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String courseName = newCourse.getText().toString().trim();
                if(!courseName.equals((""))){
                    courses.add(courseName);
                    mAdapter.notifyDataSetChanged();
                    newCourse.setText("");
                    utils.addCourse(courseName);
                    utils.save();
                }else{
                    Toast.makeText(getApplicationContext(),"Enter a course.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void removeItem(int position){
        String item = utils.getCourseList().get(position);
        utils.getCourseList().remove(item);
        mAdapter.notifyDataSetChanged();
        Toast.makeText(getApplicationContext(),"Removed course.", Toast.LENGTH_SHORT).show();
    }

    public void showInputBox(String oldItem, final int index){
        final Dialog dialog=new Dialog(MainActivity.this);
        dialog.setTitle("Edit Course Name");
        dialog.setContentView(R.layout.edit_course_box);
        TextView txtMessage=(TextView)dialog.findViewById(R.id.txtmessage);
        txtMessage.setText("Edit course");
        txtMessage.setTextColor(Color.parseColor("#00aeff"));
        final EditText editText=(EditText)dialog.findViewById(R.id.inputA);
        editText.setText(oldItem);
        Button bt=(Button)dialog.findViewById(R.id.btdone);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.getCourseList().set(index,editText.getText().toString());
                mAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
