package EmployeeSystem.Employees;

import java.util.Date;

class FacilitiesStaff extends Employee {

    public FacilitiesStaff(String surname, String name, String patronymic, Date birthday, String gender, EWorkRate workRate) {
        super(surname, name, patronymic, birthday, gender, workRate);
    }

    @Override
    public int calcSalary(int hoursWorked) {
        int hourlyRate = 300;
        return hourlyRate * hoursWorked;
    }
}