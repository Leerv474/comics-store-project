package org.store.EmployeeSystem.Employees;
import java.time.LocalDate;

public abstract class Employee {

    private String surname;
    private String name;
    private String patronymic;
    private LocalDate birthday;
    private String gender;
    private int bankAccountNumber;
    private LocalDate employmentDate;

    public Employee() {}
    public Employee(String surname, String name, String patronymic, LocalDate birthday, String gender, int bankAccountNumber) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.birthday = birthday;
        this.gender = gender;
        this.bankAccountNumber = bankAccountNumber;
    }

    public abstract int calcSalary(int hoursWorked);
    public abstract String getPositionName();

    public LocalDate getEmploymentDate() {
        return employmentDate;
    }

    public void setEmploymentDate(LocalDate employmentDate) {
        this.employmentDate = employmentDate;
    }

    public int getBankAccountNumber() {
        return bankAccountNumber;
    }

    public String getGender() {
        return gender;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
}