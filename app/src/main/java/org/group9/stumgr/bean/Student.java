package org.group9.stumgr.bean;

public class Student {
   // 个人信息
   private String name;
   private String phoneNumber;
   private String address;

   // 平时成绩
   //   平时成绩不保存，而是通过计算得出
   //   nm=Normal
   private Integer nmAttendanceScore;
   private Integer nmSpeakingScore;
   private Integer nmDemonstrationScore;
   private Integer nmAnswerScore;

   // 期末成绩
   //   期末成绩、个人得分不保存，而是通过计算得出
   //   et=End of Term
   private Integer etProjectScore;
   private Integer etDemonstrationScore;
   private Integer etAnswerScore;

   public Student() {
   }

   public Student(String name, String phoneNumber) {
      this.name = name;
      this.phoneNumber = phoneNumber;
   }

   // ---------------- 计算型Getter 开始 ----------------
   public Double getNmScore() {
      return 0.;
   }

   public Double getEtIndividualScore() {
      return 0.;
   }

   public Double getEtScore() {
      return 0.;
   }
   // ---------------- 计算型Getter 结束 ----------------


   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getPhoneNumber() {
      return phoneNumber;
   }

   public void setPhoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
   }

   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   public Integer getNmAttendanceScore() {
      return nmAttendanceScore;
   }

   public void setNmAttendanceScore(Integer nmAttendanceScore) {
      this.nmAttendanceScore = nmAttendanceScore;
   }

   public Integer getNmSpeakingScore() {
      return nmSpeakingScore;
   }

   public void setNmSpeakingScore(Integer nmSpeakingScore) {
      this.nmSpeakingScore = nmSpeakingScore;
   }

   public Integer getNmDemonstrationScore() {
      return nmDemonstrationScore;
   }

   public void setNmDemonstrationScore(Integer nmDemonstrationScore) {
      this.nmDemonstrationScore = nmDemonstrationScore;
   }

   public Integer getNmAnswerScore() {
      return nmAnswerScore;
   }

   public void setNmAnswerScore(Integer nmAnswerScore) {
      this.nmAnswerScore = nmAnswerScore;
   }

   public Integer getEtProjectScore() {
      return etProjectScore;
   }

   public void setEtProjectScore(Integer etProjectScore) {
      this.etProjectScore = etProjectScore;
   }

   public Integer getEtDemonstrationScore() {
      return etDemonstrationScore;
   }

   public void setEtDemonstrationScore(Integer etDemonstrationScore) {
      this.etDemonstrationScore = etDemonstrationScore;
   }

   public Integer getEtAnswerScore() {
      return etAnswerScore;
   }

   public void setEtAnswerScore(Integer etAnswerScore) {
      this.etAnswerScore = etAnswerScore;
   }

   @Override
   public String toString() {
      return "Student{" +
         "name='" + name + '\'' +
         ", phoneNumber='" + phoneNumber + '\'' +
         ", nmAttendanceScore=" + nmAttendanceScore +
         ", nmSpeakingScore=" + nmSpeakingScore +
         ", nmDemonstrationScore=" + nmDemonstrationScore +
         ", nmAnswerScore=" + nmAnswerScore +
         ", etProjectScore=" + etProjectScore +
         ", etDemonstrationScore=" + etDemonstrationScore +
         ", etAnswerScore=" + etAnswerScore +
         '}';
   }
}
