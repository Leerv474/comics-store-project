package EmployeeSystem.Employees;
import java.time.LocalDate;
public class InventoryManager extends Employee {
    public InventoryManager(String surname, String name, String patronymic, LocalDate birthday, String gender, int bankAccountNumber) {
        super(surname, name, patronymic, birthday, gender, bankAccountNumber);
    }

    @Override
    public int calcSalary(int hoursWorked) {
        int hourlyRate = 500;
        return hourlyRate * hoursWorked;
    }
}
