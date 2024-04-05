package EmployeeSystem.Employees;
import java.util.Date;

abstract class Employee {
     private String surname;
     private String name;
     private String patronymic;
     private Date birthday;
     private String gender;
     private Date employmentDate;
     private EWorkRate workRate;

     public Employee(String surname, String name, String patronymic, Date birthday, String gender, EWorkRate workRate) {
         this.surname = surname;
         this.name = name;
         this.patronymic = patronymic;
         this.birthday = birthday;
         this.gender = gender;
         this.workRate = workRate;
     }

     public abstract int calcSalary(int hoursWorked);
}