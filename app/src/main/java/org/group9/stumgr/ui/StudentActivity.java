package org.group9.stumgr.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import org.group9.stumgr.R;
import org.group9.stumgr.bean.Student;
import org.group9.stumgr.databinding.ActivityStudentManagerBinding;
import org.group9.stumgr.service.FileUtils;
import org.group9.stumgr.service.PermissionManager;
import org.group9.stumgr.service.StudentService;

import java.io.File;
import java.io.FileNotFoundException;
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
//导入后重新刷新列表
   private void initDataByImport(List<Student> students) {
      this.students = students;
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

/*
* 获取文件选择器返回值 将json转化为java对象
* */
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      if (resultCode == Activity.RESULT_OK) {//是否选择，没选择就不会继续
         Uri data1 = data.getData();
         File file = new File(FileUtils.getFilePathByUri(StudentActivity.this,data1));
//         File file = UriToFile.trans(StudentActivity.this,data1);
         List<Student> students = StudentService.importStuInfoByJson(file);
         initDataByImport(students);
         StudentFragment fragment = (StudentFragment) getSupportFragmentManager()
                 .findFragmentById(R.id.stuInfoFragment);
         fragment.setStudent(students.get(0));
         initView();
         Toast.makeText(StudentActivity.this,"成功导入"+students.size()+"条数据",Toast.LENGTH_LONG).show();



      }
   }




   /* ---------------- 菜单相关 开始 ---------------- */
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater()
         .inflate(R.menu.activity_student_manager, menu);

      return super.onCreateOptionsMenu(menu);
   }

   @SuppressLint("ShowToast")
   @Override
   public boolean onOptionsItemSelected(@NonNull MenuItem item) {
      int id = item.getItemId();

      if (id == R.id.sortingMenuItem) {
         onSortingOptionSelected(item);

      } else if (id == R.id.searchingMenuItem) {
         onSearchingOptionSelected(item);

      }else if (id == R.id.exportM){
         onExportMOptionSelected();
      }else if (id == R.id.importM){
         onImportMOptionSelected();

      }else {
         return super.onOptionsItemSelected(item);
      }

      return true;
   }

   public void onSortingOptionSelected(@NonNull MenuItem item) {

   }

   public void onSearchingOptionSelected(@NonNull MenuItem item) {

   }

   public void onExportMOptionSelected(){
      try {
         boolean PermissionResult = new PermissionManager().build().RequestPermission(StudentActivity.this, this);

            String path = StudentService.exportStuInfoByJson(StudentActivity.this, students);
            Toast.makeText(StudentActivity.this,"成功导出至"+path,Toast.LENGTH_LONG).show();
            Log.d(TAG, "onOptionsItemSelected: "+path);


      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }
   }

   public void onImportMOptionSelected(){
      boolean PermissionResult = new PermissionManager().build().RequestPermission(StudentActivity.this, this);

         Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
         intent.setType("*/*");
         intent.addCategory(Intent.CATEGORY_OPENABLE);
         startActivityForResult(intent, 1);

//      RequestPermissionRImpl.RequestPermissionAndroidR(StudentActivity.this);

   }


      /* ---------------- 菜单相关 结束 ---------------- */
}
