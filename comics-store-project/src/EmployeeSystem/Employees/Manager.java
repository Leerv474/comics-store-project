package EmployeeSystem.Employees;

import java.time.LocalDate;

public class Manager extends Employee {
    public Manager(String surname, String name, String patronymic, LocalDate birthday, String gender, int bankAccountNumber) {
        super(surname, name, patronymic, birthday, gender, bankAccountNumber);
    }

    @Override
    public int calcSalary(int hoursWorked) {
        int hourlyRate = 600;
        return hourlyRate * hoursWorked;
    }
}