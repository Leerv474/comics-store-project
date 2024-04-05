package EmployeeSystem.Employees;

import java.util.Date;

class Manager extends Employee {

    public Manager(String surname, String name, String patronymic, Date birthday, String gender, EWorkRate workRate) {
        super(surname, name, patronymic, birthday, gender, workRate);
    }

    @Override
    public int calcSalary(int hoursWorked) {
        int hourlyRate = 600;
        return hourlyRate * hoursWorked;
    }
}