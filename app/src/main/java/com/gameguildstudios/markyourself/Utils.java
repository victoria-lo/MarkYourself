package com.gameguildstudios.markyourself;
import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class Utils implements Serializable {
    private static final String filename = "courseAssignmentData";
    private SystemData data;
    private Context context;

    Utils(Context context) {
        this.context = context;
        data = new SystemData();
        initialize();
    }
    private void initialize() {
        String contents;
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(filename);
        } catch (FileNotFoundException e) {
            try {
                File.createTempFile(filename, null, context.getCacheDir());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (fis != null) {
            InputStreamReader inputStreamReader =
                    new InputStreamReader(fis, StandardCharsets.UTF_8);
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line = reader.readLine();
                while (line != null) {
                    stringBuilder.append(line).append('\n');
                    line = reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                contents = stringBuilder.toString();
            }
            if (contents.equals(""))
                return;
            String[] byCourses = contents.split("\n");
            for (String dataByCourse : byCourses) {
                String[] courseData = dataByCourse.split(","); // first element is the course name
                ArrayList<Assignment> aList = new ArrayList<>();
                if (courseData.length > 1) {
                    for (int j = 1; j < courseData.length; j += 5) {
                        Assignment a = new Assignment(courseData[j], Double.parseDouble(courseData[j + 1]),
                                Double.parseDouble(courseData[j + 2]), courseData[j + 3], courseData[j + 4]);
                        aList.add(a);
                    }
                }
                data.dataMap.put(courseData[0], aList);
            }
        }
    }

    void save() {
        try {
            try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
                fos.write(dataToString().getBytes());
                System.out.println(dataToString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    SystemData getData() {
        return data;
    }

    ArrayList<String> getCourseList() {
        return new ArrayList<>(data.dataMap.keySet());
    }

    ArrayList<Assignment> getAssignments(String courseName) {
        return data.dataMap.get(courseName);
    }

    private String dataToString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (data.dataMap.isEmpty())
            return "";
        for (HashMap.Entry<String, ArrayList<Assignment>> entry : data.dataMap.entrySet()) {
            stringBuilder.append(entry.getKey());
            stringBuilder.append(",");
            for (int i = 0; i < entry.getValue().size(); i++) {
                stringBuilder.append(entry.getValue().get(i).name);
                stringBuilder.append(",");
                stringBuilder.append(entry.getValue().get(i).weight);
                stringBuilder.append(",");
                stringBuilder.append(entry.getValue().get(i).grade);
                stringBuilder.append(",");
                stringBuilder.append(entry.getValue().get(i).date);
                stringBuilder.append(",");
                stringBuilder.append(entry.getValue().get(i).target);
            }
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

    void addCourse(String courseName) {
        data.dataMap.put(courseName, new ArrayList<Assignment>());
    }

    void addAssigment(String courseName, Assignment assignment) {
        data.dataMap.get(courseName).add(assignment);
    }

    class SystemData implements Serializable {
        HashMap<String, ArrayList<Assignment>> dataMap;
        SystemData() {
            dataMap = new HashMap<>();
        }
    }
}
