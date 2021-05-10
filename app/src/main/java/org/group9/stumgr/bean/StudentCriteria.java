package org.group9.stumgr.bean;

import java.io.Serializable;
import java.util.function.Predicate;

/**
 * 学生过滤条件
 */
public abstract class StudentCriteria
   implements Serializable, Predicate<Student> {

}
