package org.group9.stumgr.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.group9.stumgr.R;
import org.group9.stumgr.bean.Student;
import org.group9.stumgr.databinding.ActivityStudentManagerBinding;
import org.group9.stumgr.util.FileUtils;
import org.group9.stumgr.service.PermissionManager;
import org.group9.stumgr.service.StudentService;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;

public class StudentManagerActivity extends AppCompatActivity {
   private static final String TAG = StudentManagerActivity.class.getSimpleName();


   private List<Student> students;

   private ActivityStudentManagerBinding bd;
   private RecyclerView studentsRecyclerView;
   private StudentListAdapter studentListAdapter;


   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      bd = DataBindingUtil.setContentView(
         this, R.layout.activity_student_manager);

      initData();

      initView();
   }

   private void initData() {
      setStudentsAndSyncToAdapter(
         StudentService.getRandomStudentsAsList(25)
      );

   }

   private void initView() {

      // 搜索框相关
      bd.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
         @Override
         public boolean onQueryTextSubmit(String query) {
            return false;
         }

         @Override
         public boolean onQueryTextChange(String newText) {
            studentListAdapter.getStudentCriteria().setNameFragment(newText);
            studentListAdapter.notifyDataChanged();
            return true;
         }
      });

      // 学生列表相关
      {
         // DataBinding找不到这个id，原因不明。故手动findViewById
         studentsRecyclerView = bd.getRoot().findViewById(R.id.studentRecyclerView);

         // 设置布局管理器
         studentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
         // 添加分割线装饰
         DividerItemDecoration did = new DividerItemDecoration(
            studentsRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
         studentsRecyclerView.addItemDecoration(did);

         // 适配器
         studentListAdapter = new StudentListAdapter(this, students,
            new StudentListAdapter.ViewItemOnClickListener() {
               @Override
               public void onClick(Student student, int position) {
                  displayStudent(student);
               }
            },
            new StudentListAdapter.DataUpdatingFinishedListener() {
               @Override
               public void onFinished() {
                  // 将列表滚动到顶部，从头开始展示更新后的内容
                  studentsRecyclerView.scrollToPosition(0);

                  // 显示第一个学生的信息
                  displayFirstStudent();
               }
            });
         studentsRecyclerView.setAdapter(studentListAdapter);

      }

   }

   /**
    * 设置学生列表，不会进行关联数据的更新
    */
   private void setStudentsAndSyncToAdapter(@Nullable List<Student> students) {
      if (students == null) {
         students = Collections.emptyList();
      }
      this.students = students;
      if (studentListAdapter != null) {
         studentListAdapter.setStudents(students);
      }
   }

   /**
    * 展示{@code studentListAdapter}中过滤后的学生列表中的第一个学生
    */
   private void displayFirstStudent() {
      Student student = studentListAdapter.getFilteredStudent(0);
      displayStudent(student);
   }

   /**
    * 在{@link StudentInfoFragment}中展示指定学生
    */
   private void displayStudent(@Nullable Student stu) {

      StudentInfoFragment fragment = (StudentInfoFragment) getSupportFragmentManager()
         .findFragmentById(R.id.stuInfoFragment);

      fragment.setStudent(stu);
   }

   /* ---------------- Activity返回值处理相关 开始 ---------------- */
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {

      if (requestCode == UIConstants.REQ_CODE_GET_CONTENT) {
         // 获取文件选择器返回值 将json转化为java对象
         if (resultCode == Activity.RESULT_OK) { //是否选择，没选择就不会继续
            Uri data1 = data.getData();
            File file = new File(FileUtils.getFilePathByUri(StudentManagerActivity.this, data1));
//          File file = UriToFile.trans(StudentActivity.this,data1);
            List<Student> newStudents = StudentService.importStuInfoByJson(file);
            // 导入后刷新列表
            setStudentsAndSyncToAdapter(newStudents);
            studentListAdapter.notifyDataChanged();

            Toast.makeText(StudentManagerActivity.this,
               "成功导入" + newStudents.size() + "条数据", Toast.LENGTH_LONG).show();
         }
      } else {
         super.onActivityResult(requestCode, resultCode, data);
      }
   }
   /* ---------------- Activity返回值处理相关 结束 ---------------- */


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

      } else if (id == R.id.exportM) {
         onExportMOptionSelected();

      } else if (id == R.id.importM) {
         onImportMOptionSelected();

      } else {
         return super.onOptionsItemSelected(item);
      }

      return true;
   }

   public void onSortingOptionSelected(@NonNull MenuItem item) {

      final int prevSortingTypeIndex = studentListAdapter.getSortingTypeIndex();

      AlertDialog alertDialog = new AlertDialog.Builder(this)
         .setIcon(R.drawable.ic_baseline_sort_30_dark)
         .setTitle("排序方式")
         .setSingleChoiceItems(StudentService.SORTING_METHOD_NAMES, prevSortingTypeIndex,
            (dialog, which) -> studentListAdapter.setSortingTypeIndex(which))
         .setPositiveButton("关闭", (dialog, which) -> dialog.dismiss())
         .setOnDismissListener(dialog -> {
            if (prevSortingTypeIndex != studentListAdapter.getSortingTypeIndex()) {
               studentListAdapter.notifyDataChanged();
            }
         })
         .create();

      alertDialog.show();
   }

   public void onSearchingOptionSelected(@NonNull MenuItem item) {

      if (bd.searchViewWrapper.getVisibility() == View.GONE) {
         // 显示搜索视图时，调用onActionViewExpanded，以展开SearchView
         bd.searchView.onActionViewExpanded();

         bd.searchViewWrapper.setVisibility(View.VISIBLE);
         item.setIcon(R.drawable.ic_baseline_search_off_30);

      } else {
         // 隐藏搜索视图时，调用onActionViewCollapsed，以清空SearchView内的文字
         bd.searchView.onActionViewCollapsed();

         bd.searchViewWrapper.setVisibility(View.GONE);
         item.setIcon(R.drawable.ic_baseline_search_30);
      }
   }

   public void onExportMOptionSelected() {
      try {
         boolean PermissionResult = new PermissionManager().build().RequestPermission(StudentManagerActivity.this, this);

         String path = StudentService.exportStuInfoByJson(StudentManagerActivity.this, students);
         Toast.makeText(StudentManagerActivity.this, "成功导出至" + path, Toast.LENGTH_LONG).show();
         Log.d(TAG, "onOptionsItemSelected: " + path);


      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }
   }

   public void onImportMOptionSelected() {
      boolean PermissionResult = new PermissionManager().build().RequestPermission(StudentManagerActivity.this, this);

      Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
      intent.setType("*/*");
      intent.addCategory(Intent.CATEGORY_OPENABLE);
      startActivityForResult(intent, UIConstants.REQ_CODE_GET_CONTENT);

//      RequestPermissionRImpl.RequestPermissionAndroidR(StudentActivity.this);

   }

   /* ---------------- 菜单相关 结束 ---------------- */

}
