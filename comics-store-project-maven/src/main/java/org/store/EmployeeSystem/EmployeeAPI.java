package org.store.EmployeeSystem;

import org.store.EmployeeSystem.Employees.*;
import org.store.TextDocument.Document;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.time.LocalDate;
import java.nio.file.Files;
import java.util.stream.Stream;

public class EmployeeAPI {
    private static final HashMap<Integer, Employee> employeeMap = new HashMap<>();
    private static final Scanner scanner = new Scanner(System.in);

    private static final Path directoryPath = Path.of("src/main/java/org/store/EmployeeData");
    private static final EmployeeDAO dao = new EmployeeDAO(directoryPath);

    public static void startEmployeeAPI() {
        EmployeeAPI api = new EmployeeAPI();
        try {
            Files.createDirectory(directoryPath);
        } catch (IOException ignored) {}

        api.loadData();
        char inputOption;
        while (true) {
            System.out.println("...Employee System...");
            System.out.println("Choose action:\n1 - List employees\n2 - Hire new employee\n3 - Fire employee\n4 - Calculate salaries\n0 - Exit\n");
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
            if (inputOption == '4') {
                api.calcSalaries();
            }
            System.out.println();
        }
    }

    private void loadData() {
        List<String> fileNames = new ArrayList<>();
        try (Stream<Path> files = Files.list(directoryPath)) {
            files.filter(Files::isRegularFile)
                    .forEach(file -> fileNames.add(file.getFileName().toString()));
        } catch (IOException e) {
            System.err.println("Failed to load data");
        }
        for (String fileName : fileNames) {
            int hashId = Integer.parseInt(fileName.substring(0, fileName.length() - 4));
            dao.loadFile(fileName);
            employeeMap.put(hashId, dao.getEmployee());
        }
    }

    public void listEmployees() {
        System.out.println("---List of Employees---");
        TreeMap<String, Integer> list = new TreeMap<>();
        employeeMap.forEach((key, value) -> list.put(String.format("%s %s %s", value.getSurname(),
                value.getName(), value.getPatronymic()), key));
        list.forEach((key, value) -> System.out.printf("%d: %s, %s\n", value, key, employeeMap.get(value).getPositionName()));
        System.out.println();
    }

    public void hireEmployee() {
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
            newHash = random.nextInt(maxId - minId + 1) + minId;
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

    public void calcSalaries() {
        HashMap<Integer, Integer> salariesById = new HashMap<>();
        employeeMap.forEach((key, value) -> salariesById.put(key, value.calcSalary(40)));
        System.out.println("The list of employees with salaries:");
        var employees = employeeMap.entrySet();
        List<String> reportContents = new ArrayList<>();
        for (Map.Entry<Integer, Employee> employee : employees) {
            String dataString = String.format("%d: %s %s %s, %s - %d\n", employee.getKey(), employee.getValue().getSurname(),
                    employee.getValue().getName(), employee.getValue().getPatronymic(), employee.getValue().getPositionName(),
                    salariesById.get(employee.getKey()));
            System.out.print(dataString);
            reportContents.add(dataString);
        }
        System.out.print("Do you want to save this as a document? (y/n): ");
        char option;
        do {
            option = scanner.next().toLowerCase().charAt(0);
            if (option == 'n') {
                return;
            }
        } while (option != 'y');
        String inputPath = "src/main/java/org/store/DocumentData";
        inputPath += String.format("/report_%s.txt", String.valueOf(LocalDate.now()));
        Document salaryReport = new Document();
        salaryReport.createFile(inputPath, "Salary report", "Comics Store");
        salaryReport.setFileContents(reportContents);
        int result = salaryReport.saveDocument();
        if (result == 4) {
            System.out.println("Report on this date was already created");
            return;
        }
        System.out.println("\nData saved as a document");
    }
}