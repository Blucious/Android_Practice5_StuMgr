package org.group9.stumgr.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.group9.stumgr.R;
import org.group9.stumgr.bean.StudentCriteria;
import org.group9.stumgr.bean.Student;
import org.group9.stumgr.databinding.SimpleListItemBinding;
import org.group9.stumgr.service.StudentService;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StudentListAdapter
   extends RecyclerView.Adapter<StudentListAdapter.StudentViewHolder> {

   private final Activity activity;
   private final LayoutInflater layoutInflater;

   /**
    * {@code students}：外部传入的完整的学生列表
    * {@code filteredStudents}：根据{@code studentCriteria}过滤、
    * 根据{@code sortingTypeIndex}排序后的学生列表
    */
   private List<Student> students = Collections.emptyList();
   private List<Student> filteredStudents = Collections.emptyList();
   private StudentCriteria studentCriteria = new StudentCriteria();
   private int sortingTypeIndex = 0;

   /**
    * 保存外部传入的监听器
    */
   private ViewItemOnClickListener viewItemOnClickListener;
   private DataUpdatingFinishedListener dataUpdatingFinishedListener;

   /**
    * 创建一个只有一个线程的线程池，来执行过滤和排序等操作。
    * 以避免在UI线程进行重计算操作。
    */
   private final ExecutorService executorService =
      Executors.newFixedThreadPool(1);

   public StudentListAdapter(Activity activity, List<Student> students,
                             ViewItemOnClickListener viewItemOnClickListener,
                             DataUpdatingFinishedListener dataUpdatingFinishedListener) {
      this.activity = activity;
      layoutInflater = activity.getLayoutInflater();
      this.viewItemOnClickListener = viewItemOnClickListener;
      this.dataUpdatingFinishedListener = dataUpdatingFinishedListener;
      setStudents(students);
      notifyDataChanged();
   }

   public int getSortingTypeIndex() {
      return sortingTypeIndex;
   }

   public void setSortingTypeIndex(int sortingTypeIndex) {
      this.sortingTypeIndex = sortingTypeIndex;
   }

   public StudentCriteria getStudentCriteria() {
      return studentCriteria;
   }

   public void setStudentCriteria(StudentCriteria studentCriteria) {
      if (studentCriteria == null) {
         studentCriteria = new StudentCriteria();
      }
      this.studentCriteria = studentCriteria;
   }

   public void setStudents(List<Student> students) {
      if (students == null) {
         students = Collections.emptyList();
      }
      this.students = students;
      // 清除之前学生名拼音的缓存
      studentCriteria.clearPinyinCache();
   }

   /**
    * 通知该适配器数据发生变化（学生列表、排序方式和过滤条件中其一或其中多个发生变化）。
    * 该方法会异步执行数据更新。
    */
   public void notifyDataChanged() {
      executorService.submit(() -> {

         doFilterAndSort();

         activity.runOnUiThread(() -> {
            notifyDataSetChanged();
            if (dataUpdatingFinishedListener != null) {
               dataUpdatingFinishedListener.onFinished();
            }
         });
      });
   }

   private void doFilterAndSort() {
      filteredStudents = StudentService.filter(students, studentCriteria);
      StudentService.sort(filteredStudents, sortingTypeIndex);
   }

   @NonNull
   @Override
   public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      SimpleListItemBinding bd = DataBindingUtil.inflate(layoutInflater,
         R.layout.simple_list_item, parent, false);
      return new StudentViewHolder(bd);
   }

   @Override
   public int getItemCount() {
      return filteredStudents.size();
   }

   public Student getFilteredStudent(int position) {
      int size = filteredStudents.size();
      if (position < 0 || position >= size) {
         return null;
      }
      return filteredStudents.get(position);
   }

   @Override
   public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
      final Student stu = filteredStudents.get(position);

      holder.bd.textView.setText(stu.getName());
      holder.bd.getRoot().setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            if (viewItemOnClickListener != null) {
               viewItemOnClickListener.onClick(stu, position);
            }
         }
      });
   }

   public static class StudentViewHolder extends RecyclerView.ViewHolder {
      private final SimpleListItemBinding bd;

      public StudentViewHolder(SimpleListItemBinding bd) {
         super(bd.getRoot());
         this.bd = bd;
      }
   }

   public interface ViewItemOnClickListener {
      void onClick(Student student, int position);
   }

   public interface DataUpdatingFinishedListener {
      void onFinished();
   }

}
