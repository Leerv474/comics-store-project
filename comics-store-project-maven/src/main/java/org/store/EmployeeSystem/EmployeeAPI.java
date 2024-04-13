package org.store.EmployeeSystem;


import org.store.EmployeeSystem.Employees.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.time.LocalDate;
import java.nio.file.Files;

public class EmployeeAPI {
    private static HashMap<Integer, Employee> employeeMap = new HashMap<>();
    private static final Scanner scanner = new Scanner(System.in);

    private static final Path directoryPath = Path.of("src/main/java/org/store/EmployeeData");
    private static EmployeeDAO dao = new EmployeeDAO(directoryPath);

    public static void start() {
        EmployeeAPI api = new EmployeeAPI();
        try {
            Files.createDirectory(directoryPath);
        } catch (IOException e) {
            System.err.println("Failed to create directory");
        }
        api.loadData();
        char inputOption = 0;
        while (true) {
            System.out.println("...Employee System...");
            System.out.println("Choose action:\n1 - List employees\n2 - Hire new employee\n3 - Fire employee\n0 - Exit\n");
            inputOption = scanner.next().charAt(0);
            if (inputOption == '0') {
                return;
            }
            if (inputOption == '1') {
                api.listEmployees();
            }
            if (inputOption == '2') {
                api.hireEmployee();
            }
            if (inputOption == '3') {
                api.fireEmployee();
            }
            System.out.println();
        }
    }

    private void loadData() {
        List<String> fileNames = new ArrayList<>();
        try {
            Files.list(directoryPath)
                    .filter(Files::isRegularFile)
                    .forEach(file -> fileNames.add(file.getFileName().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int trimIndex;
        for (String fileName : fileNames) {
            int hashId = Integer.parseInt(fileName.substring(0, fileName.length() - 4));
            //TODO: get this done
            dao.loadFile(fileName);
            employeeMap.put(hashId, dao.getEmployee());
        }
    }

    private void listEmployees() {
        System.out.println("The list of employees:");
        var employees = employeeMap.entrySet();
        employees.forEach(employee -> System.out.printf("%d: %s %s %s, %s", employee.getKey(), employee.getValue().getSurname(),
                employee.getValue().getName(), employee.getValue().getPatronymic(), employee.getValue().getPositionName()));
        System.out.println();
    }

    private void hireEmployee() {
        char optionInput;
        final String employeeOptions = "1234";
        System.out.println("Adding employee...\nChoose employee type:\n1 - Manager\n2 - Cashier\n3 - Inventory Manager\n4 - Facilities Staff");
        do {
            optionInput = scanner.next().charAt(0);
        } while (!employeeOptions.contains(String.valueOf(optionInput)));

        String[] titles = {"Surname", "Name", "Patronymic", "Birth Date", "Gender", "Bank Account Number"};
        HashMap<String, String> employeeData = new HashMap<>();

        System.out.println("Enter employee data:");
        for (int i = 0; i < 6; i++) {
            System.out.printf("%s: ", titles[i]);
            do {
                employeeData.put(titles[i], scanner.next());
            } while (employeeData.get(titles[i]).isEmpty());
        }

        // Converting needed strings
        LocalDate birthDate;
        try {
            birthDate = LocalDate.parse(employeeData.get("Birth Date"));
        } catch (Exception e) {
            System.err.println("Failed to convert date");
            return;
        }

        int bankAccountNumber;
        try {
            bankAccountNumber = Integer.parseInt(employeeData.get("Bank Account Number"));
        } catch (Exception e) {
            System.err.println("Failed to convert Bank account number");
            return;
        }

        var random = new Random();
        int minId = 100000;
        int maxId = 999999;
        int newHash= random.nextInt(maxId - minId + 1) + minId;
        while (employeeMap.containsKey(newHash)) {
            newHash = random.nextInt();
        }
        if (optionInput == '1') {
            Manager newManager = new Manager(employeeData.get("Surname"), employeeData.get("Name"), employeeData.get("Patronymic"),
                    birthDate, employeeData.get("Gender"), bankAccountNumber);
            employeeMap.put(newHash, newManager);
            employeeData.put("Position", "Manager");
        }
        if (optionInput == '2') {
            Cashier newCashier = new Cashier(employeeData.get("Surname"), employeeData.get("Name"), employeeData.get("Patronymic"),
                    birthDate, employeeData.get("Gender"), bankAccountNumber);
            employeeMap.put(newHash, newCashier);
            employeeData.put("Position", "Cashier");
        }
        if (optionInput == '3') {
            InventoryManager newInventoryManager = new InventoryManager(employeeData.get("Surname"), employeeData.get("Name"), employeeData.get("Patronymic"),
                    birthDate, employeeData.get("Gender"), bankAccountNumber);
            employeeMap.put(newHash, newInventoryManager);
            employeeData.put("Position", "Inventory Manager");
        }
        if (optionInput == '4') {
            FacilitiesStaff newFacilitiesStaff = new FacilitiesStaff(employeeData.get("Surname"), employeeData.get("Name"), employeeData.get("Patronymic"),
                    birthDate, employeeData.get("Gender"), bankAccountNumber);
            employeeMap.put(newHash, newFacilitiesStaff);
            employeeData.put("Position", "Facilities Staff");
        }
        dao.setData(employeeData);
        dao.saveFile(String.format("%d.txt", newHash));
    }

    public void fireEmployee() {
        System.out.println("Firing employee...");
        System.out.print("Enter employee id: ");

        int employeeId = scanner.nextInt();
        if (!employeeMap.containsKey(employeeId)) {
            System.out.println("Employee not found");
            return;
        }
        employeeMap.remove(employeeId);
        String fileName = String.format("%d.txt", employeeId);
        dao.deleteFile(fileName);
    }
}