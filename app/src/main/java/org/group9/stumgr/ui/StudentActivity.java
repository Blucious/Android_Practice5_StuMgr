package org.group9.stumgr.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import org.group9.stumgr.R;
import org.group9.stumgr.bean.Student;
import org.group9.stumgr.databinding.ActivityStudentBinding;
import org.group9.stumgr.service.StudentService;

import java.util.List;
import java.util.stream.Collectors;

public class StudentActivity extends AppCompatActivity {

   private List<Student> students;
   private List<String> studentNames;

   private ActivityStudentBinding bd;

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      bd = DataBindingUtil.setContentView(
         this, R.layout.activity_student);

      initData();

      initView();
   }

   private void initData() {
      students = StudentService.getAllStudents();
      studentNames = students.stream().map(Student::getName)
         .collect(Collectors.toList());
   }

   private void initView() {
      ArrayAdapter<Object> adapter = new ArrayAdapter<>(
         this, android.R.layout.simple_list_item_1,
         studentNames.toArray());

      bd.list.setAdapter(adapter);

      bd.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Student stu = students.get(position);

            StudentFragment fragment = (StudentFragment) getSupportFragmentManager()
               .findFragmentById(R.id.stuinfo);

            fragment.setStudent(stu);
         }
      });

   }
}
