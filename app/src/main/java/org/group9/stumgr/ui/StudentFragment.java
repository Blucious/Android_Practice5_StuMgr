package org.group9.stumgr.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import org.group9.stumgr.R;
import org.group9.stumgr.bean.Student;
import org.group9.stumgr.databinding.FragmentStudentInfoBinding;


public class StudentFragment extends Fragment {

   private FragmentStudentInfoBinding bd;

   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater,
                            @Nullable ViewGroup container,
                            @Nullable Bundle savedInstanceState) {

      bd = DataBindingUtil.inflate(
         inflater, R.layout.fragment_student_info, container, false);

      return bd.getRoot();
   }

   public void setStudent(Student student) {
      bd.setStudent(student);
   }


}
