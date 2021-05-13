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
import org.group9.stumgr.util.android.FileUtils;
import org.group9.stumgr.util.android.PermissionManager;
import org.group9.stumgr.service.StudentService;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;

public class StudentActivity extends AppCompatActivity {
   private static final String TAG = StudentActivity.class.getSimpleName();


   private List<Student> students;

   private ActivityStudentManagerBinding bd;
   private RecyclerView studentsRecyclerView;
   private StudentsAdapter studentsAdapter;

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      bd = DataBindingUtil.setContentView(
         this, R.layout.activity_student_manager);

      initData();

      initView();
   }

   private void initData() {
      setStudentsSafely(
         StudentService.getRandomStudentsAsList(getResources(), 10)
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
            String nameFragment = newText != null ? newText : "";
            studentsAdapter.getStudentCriteria().setNameFragment(nameFragment);
            notifyConditionChanged();
            return true;
         }
      });

      // 学生列表相关
      {
         studentsAdapter = new StudentsAdapter(this, students, new StudentsAdapter.ViewOnClickListener() {
            @Override
            public void onClick(Student student, int position) {
               displayStudent(student);
            }
         });

         // DataBinding找不到这个id，原因不明。故手动findViewById
         studentsRecyclerView = bd.getRoot().findViewById(R.id.studentRecyclerView);

         // 设置布局管理器
         studentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
         // 添加分割线装饰
         DividerItemDecoration did = new DividerItemDecoration(
            studentsRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
         studentsRecyclerView.addItemDecoration(did);

         studentsRecyclerView.setAdapter(studentsAdapter);

         notifyConditionChanged();
      }

   }

   private void setStudentsSafely(List<Student> students) {
      if (students == null) {
         students = Collections.emptyList();
      }
      this.students = students;
      if (studentsAdapter != null) {
         studentsAdapter.setStudents(students);
      }
   }

   /**
    * 通知学生过滤条件或排序方式其一或多者发生变化。
    */
   private void notifyConditionChanged() {

      studentsAdapter.notifyDataChanged(new StudentsAdapter.OperationDoneListener() {
         @Override
         public void onDone() {
            // 将列表滚动到顶部，从头开始展示更新后的内容
            studentsRecyclerView.scrollToPosition(0);

            // 显示第一个学生的信息
            Student student = studentsAdapter.getFilteredStudent(0);
            displayStudent(student);
         }
      });

   }

   /**
    * 在{@link StudentFragment}中展示指定学生
    */
   private void displayStudent(Student stu) {
      if (stu == null) {
         return;
      }

      StudentFragment fragment = (StudentFragment) getSupportFragmentManager()
         .findFragmentById(R.id.stuInfoFragment);

      fragment.setStudent(stu);
   }

   /*
    * 获取文件选择器返回值 将json转化为java对象
    **/
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {

      if (requestCode == UIConstants.RC_GET_CONTENT) {
         if (resultCode == Activity.RESULT_OK) { //是否选择，没选择就不会继续
            Uri data1 = data.getData();
            File file = new File(FileUtils.getFilePathByUri(StudentActivity.this, data1));
//          File file = UriToFile.trans(StudentActivity.this,data1);
            List<Student> newStudents = StudentService.importStuInfoByJson(file);
            // 导入后重新刷新列表
            System.out.println(newStudents);
            setStudentsSafely(newStudents);
            notifyConditionChanged();

            Toast.makeText(StudentActivity.this,
               "成功导入" + newStudents.size() + "条数据", Toast.LENGTH_LONG).show();
         }
      } else {
         super.onActivityResult(requestCode, resultCode, data);
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

      final int prevSortingTypeIndex = studentsAdapter.getSortingTypeIndex();

      AlertDialog alertDialog = new AlertDialog.Builder(this)
         .setIcon(R.drawable.ic_baseline_sort_30_dark)
         .setTitle("排序方式")
         .setSingleChoiceItems(StudentService.SORTING_METHOD_NAMES, prevSortingTypeIndex,
            (dialog, which) -> studentsAdapter.setSortingTypeIndex(which))
         .setPositiveButton("关闭", (dialog, which) -> dialog.dismiss())
         .setOnDismissListener(dialog -> {
            if (prevSortingTypeIndex != studentsAdapter.getSortingTypeIndex()) {
               notifyConditionChanged();
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
         boolean PermissionResult = new PermissionManager().build().RequestPermission(StudentActivity.this, this);

         String path = StudentService.exportStuInfoByJson(StudentActivity.this, students);
         Toast.makeText(StudentActivity.this, "成功导出至" + path, Toast.LENGTH_LONG).show();
         Log.d(TAG, "onOptionsItemSelected: " + path);


      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }
   }

   public void onImportMOptionSelected() {
      boolean PermissionResult = new PermissionManager().build().RequestPermission(StudentActivity.this, this);

      Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
      intent.setType("*/*");
      intent.addCategory(Intent.CATEGORY_OPENABLE);
      startActivityForResult(intent, UIConstants.RC_GET_CONTENT);

//      RequestPermissionRImpl.RequestPermissionAndroidR(StudentActivity.this);

   }

   /* ---------------- 菜单相关 结束 ---------------- */

}
