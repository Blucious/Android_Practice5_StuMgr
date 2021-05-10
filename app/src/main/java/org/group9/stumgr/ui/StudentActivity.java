package org.group9.stumgr.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.group9.stumgr.R;
import org.group9.stumgr.bean.Student;
import org.group9.stumgr.databinding.ActivityStudentManagerBinding;
import org.group9.stumgr.service.StudentService;

import java.util.List;
import java.util.stream.Collectors;

public class StudentActivity extends AppCompatActivity {
   private static final String TAG = StudentActivity.class.getSimpleName();


   private List<Student> students;
   private List<String> studentNames;

   private ActivityStudentManagerBinding bd;

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      bd = DataBindingUtil.setContentView(
         this, R.layout.activity_student_manager);

      initData();

      initView();
   }

   private void initData() {
      students = StudentService.getRandomStudentsAsList(getResources(), 25);
      studentNames = students.stream().map(Student::getName)
         .collect(Collectors.toList());
   }

   private void initView() {
      ArrayAdapter<Object> adapter = new ArrayAdapter<>(
         this, android.R.layout.simple_list_item_1,
         studentNames.toArray());

      bd.stuList.setAdapter(adapter);

      bd.stuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Student stu = students.get(position);

            StudentFragment fragment = (StudentFragment) getSupportFragmentManager()
               .findFragmentById(R.id.stuInfoFragment);

            fragment.setStudent(stu);
         }
      });

   }


   /* ---------------- 菜单相关 开始 ---------------- */
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater()
         .inflate(R.menu.activity_student_manager, menu);

      return super.onCreateOptionsMenu(menu);
   }

   @Override
   public boolean onOptionsItemSelected(@NonNull MenuItem item) {
      int id = item.getItemId();

      if (id == R.id.sortingMenuItem) {
         onSortingOptionSelected(item);

      } else if (id == R.id.searchingMenuItem) {
         onSearchingOptionSelected(item);

      } else {
         return super.onOptionsItemSelected(item);
      }

      return true;
   }

   public void onSortingOptionSelected(@NonNull MenuItem item) {

   }

   public void onSearchingOptionSelected(@NonNull MenuItem item) {

   }

   /* ---------------- 菜单相关 结束 ---------------- */
}
