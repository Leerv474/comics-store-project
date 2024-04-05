package EmployeeSystem.Employees;
import java.util.Date;

class InventoryManager extends Employee {

    public InventoryManager(String surname, String name, String patronymic, Date birthday, String gender, EWorkRate workRate) {
        super(surname, name, patronymic, birthday, gender, workRate);
    }

    @Override
    public int calcSalary(int hoursWorked) {
        int hourlyRate = 500;
        return hourlyRate * hoursWorked;
    }
}
