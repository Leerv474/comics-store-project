package EmployeeSystem;

import EmployeeSystem.Employees.*;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;

public class EmployeeAPI {
    private static List<Employee> employeeList = new ArrayList<>();
    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;


    public static void start() {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/comics_store", "roman", "");
        } catch (SQLException e) {
            System.err.println("Failed to connect to data base");
        }
    }

    private void hireEmployee() {
        Scanner scanner = new Scanner(System.in);
        char optionInput = 0;
        final String employeeOptions = "1234";
        System.out.println("Adding employee...\nChoose employee type:\n1 - Cashier\n2 - Facility Staff\n3 - Inventory Manager\n4 - Manager");
        do {
            optionInput = scanner.next().charAt(0);
        } while (!employeeOptions.contains(String.valueOf(optionInput)));

        String inputString;
        String employeePositionToInsert;
        String[] labelStrings = {"Surname: ", "Name: ", "Patronymic: ", "Birth date: ", "Gender: ", "Bank account number: "};
        String[] employeeInputData = new String[6];

        System.out.println("Enter employee data:");
        for (int i = 0; i < 6; i++) {
            System.out.print(labelStrings[i]);
            do {
                employeeInputData[i] = scanner.next();
            } while (employeeInputData[i].isEmpty());
        }

        // Converting needed strings
        LocalDate birthDate;
        try {
            birthDate = LocalDate.parse(employeeInputData[3]);
        } catch (Exception e) {
            System.err.println("Failed to convert date");
            return;
        }

        int bankAccountNumber;
        try {
            bankAccountNumber = Integer.parseInt(employeeInputData[5]);
        } catch (Exception e) {
            System.err.println("Failed to convert Bank account number");
            return;
        }

        if (optionInput == '1') {
            Cashier newCashier = new Cashier(employeeInputData[0], employeeInputData[1], employeeInputData[2], birthDate,
                    employeeInputData[4], bankAccountNumber);
            employeeList.add(newCashier);
        }
        if (optionInput == '2') {
            FacilitiesStaff newFacilitiesStaff = new FacilitiesStaff(employeeInputData[0], employeeInputData[1], employeeInputData[2], birthDate,
                    employeeInputData[4], bankAccountNumber);
            employeeList.add(newFacilitiesStaff);
        }
        if (optionInput == '3') {
            InventoryManager newInventoryManager = new InventoryManager(employeeInputData[0], employeeInputData[1], employeeInputData[2], birthDate,
                    employeeInputData[4], bankAccountNumber);
            employeeList.add(newInventoryManager);
        }
        if (optionInput == '4') {
            Manager newManager = new Manager(employeeInputData[0], employeeInputData[1], employeeInputData[2], birthDate,
                    employeeInputData[4], bankAccountNumber);
            employeeList.add(newManager);
        }
        try {
            statement = connection.createStatement();
            statement.executeQuery("INSERT INTO employees");
            //TODO: get this done
        } catch (SQLException e) {
            System.err.println("Failed to create statement");
        }
    }

    public void fireEmployee() {

    }
}