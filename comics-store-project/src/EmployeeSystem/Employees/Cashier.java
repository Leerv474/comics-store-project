package EmployeeSystem.Employees;

import java.util.Date;

class Cashier extends Employee {
    public Cashier(String surname, String name, String patronymic, Date birthday, String gender, EWorkRate workRate) {
        super(surname, name, patronymic, birthday, gender, workRate);
    }

    @Override
    public int calcSalary(int hoursWorked){
        int hourlyRate = 400;
        return hourlyRate * hoursWorked;
    }
}