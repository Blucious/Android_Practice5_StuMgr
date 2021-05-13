package org.group9.stumgr.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

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
import org.group9.stumgr.service.StudentService;

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
      students = StudentService.getRandomStudentsAsList(getResources(), 75);
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
            updateStudentList();
            return true;
         }
      });

      // 学生列表相关
      {
         studentsAdapter = new StudentsAdapter(this, students, new StudentsAdapter.ViewOnClickListener() {
            @Override
            public void onClick(Student student, int position) {
               StudentFragment fragment = (StudentFragment) getSupportFragmentManager()
                  .findFragmentById(R.id.stuInfoFragment);

               fragment.setStudent(student);
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

      }

   }

   private void updateStudentList() {
      studentsAdapter.notifyConditionChanged();
      studentsRecyclerView.scrollToPosition(0);
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

      final int prevSortingTypeIndex = studentsAdapter.getSortingTypeIndex();

      AlertDialog alertDialog = new AlertDialog.Builder(this)
         .setIcon(R.drawable.ic_baseline_sort_30_dark)
         .setTitle("排序方式")
         .setSingleChoiceItems(StudentService.SORTING_METHOD_NAMES, prevSortingTypeIndex,
            (dialog, which) -> studentsAdapter.setSortingTypeIndex(which))
         .setPositiveButton("关闭", (dialog, which) -> dialog.dismiss())
         .setOnDismissListener(dialog -> {
            if (prevSortingTypeIndex != studentsAdapter.getSortingTypeIndex()) {
               updateStudentList();
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

   /* ---------------- 菜单相关 结束 ---------------- */
}
