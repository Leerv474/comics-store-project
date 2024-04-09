package EmployeeSystem.Employees;
import java.time.LocalDate;

public abstract class Employee {

    private int id;
    private final String surname;
    private final String name;
    private final String patronymic;
    private final LocalDate birthday;
    private final String gender;
    private final int bankAccountNumber;
    private LocalDate employmentDate;

    public Employee(String surname, String name, String patronymic, LocalDate birthday, String gender, int bankAccountNumber) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.birthday = birthday;
        this.gender = gender;
        this.bankAccountNumber = bankAccountNumber;
    }

    public abstract int calcSalary(int hoursWorked);

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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