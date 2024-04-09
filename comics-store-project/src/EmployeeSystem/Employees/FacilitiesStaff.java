package EmployeeSystem.Employees;

import java.time.LocalDate;

public class FacilitiesStaff extends Employee {
    public FacilitiesStaff(String surname, String name, String patronymic, LocalDate birthday, String gender, int bankAccountNumber) {
        super(surname, name, patronymic, birthday, gender, bankAccountNumber);
    }

    @Override
    public int calcSalary(int hoursWorked) {
        int hourlyRate = 300;
        return hourlyRate * hoursWorked;
    }
}