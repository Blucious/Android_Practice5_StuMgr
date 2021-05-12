package org.group9.stumgr.ui;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.group9.stumgr.R;
import org.group9.stumgr.bean.SimpleStudentCriteria;
import org.group9.stumgr.bean.Student;
import org.group9.stumgr.databinding.SimpleListItemBinding;
import org.group9.stumgr.service.StudentService;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StudentsAdapter
   extends RecyclerView.Adapter<StudentsAdapter.StudentViewHolder> {

   private final Activity activity;
   private final LayoutInflater layoutInflater;

   private List<Student> students = Collections.emptyList();
   private List<Student> filteredStudents = Collections.emptyList();
   private SimpleStudentCriteria studentCriteria = new SimpleStudentCriteria().setDefault();
   private int sortingTypeIndex = 0;

   private ViewOnClickListener listener;

   private final ExecutorService executorService = Executors.newFixedThreadPool(1);

   public StudentsAdapter(Activity activity, List<Student> students,
                          ViewOnClickListener listener) {
      this.activity = activity;
      layoutInflater = activity.getLayoutInflater();
      setStudents(students);
      doFilterAndSort();
      this.listener = listener;
   }

   public int getSortingTypeIndex() {
      return sortingTypeIndex;
   }

   public void setSortingTypeIndex(int sortingTypeIndex) {
      this.sortingTypeIndex = sortingTypeIndex;
   }

   public SimpleStudentCriteria getStudentCriteria() {
      return studentCriteria;
   }

   public void setStudentCriteria(SimpleStudentCriteria studentCriteria) {
      if (studentCriteria == null) {
         studentCriteria = new SimpleStudentCriteria().setDefault();
      }
      this.studentCriteria = studentCriteria;
   }

   public void setStudents(List<Student> students) {
      if (students == null) {
         students = Collections.emptyList();
      }
      this.students = students;
      studentCriteria.clearPinyinCache();
   }

   /**
    * 通知该适配器情况发生变化，即学生列表、排序方式和过滤条件中其一或其中多个发生变化
    */
   public void notifyConditionChanged() {
      doFilterAndSort();
      notifyDataSetChanged();
      //executorService.submit(() -> {
      //   activity.runOnUiThread(() -> {
      //   });
      //});
   }

   private void doFilterAndSort() {
      filteredStudents = StudentService.filter(students, studentCriteria);
      StudentService.sort(filteredStudents, sortingTypeIndex);
   }

   @NonNull
   @Override
   public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      SimpleListItemBinding bd = DataBindingUtil.inflate(
         layoutInflater, R.layout.simple_list_item,
         parent, false);
      return new StudentViewHolder(bd);
   }

   @Override
   public int getItemCount() {
      return filteredStudents.size();
   }

   @Override
   public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
      Student stu = filteredStudents.get(position);

      holder.bd.textView.setText(stu.getName());
      holder.bd.getRoot().setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            if (listener != null) {
               listener.onClick(stu, position);
            }
         }
      });
   }

   public interface ViewOnClickListener {
      void onClick(Student student, int position);
   }

   public static class StudentViewHolder extends RecyclerView.ViewHolder {
      private final SimpleListItemBinding bd;

      public StudentViewHolder(SimpleListItemBinding bd) {
         super(bd.getRoot());
         this.bd = bd;
      }
   }

}
