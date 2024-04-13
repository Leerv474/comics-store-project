package org.store.EmployeeSystem;

import org.store.EmployeeSystem.Employees.*;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class EmployeeDAO {
    private List<String> inputData = new ArrayList<>();
    private HashMap<String, String> data = new HashMap<>();
    private Path path;

    public EmployeeDAO(Path path) {
        this.path = path;
    }
    public int saveFile(String fileName) {
        String[] dataTitles = {"Position", "Surname", "Name", "Patronymic", "Birth Date", "Gender", "Bank Account Number"};
        try (FileWriter writer = new FileWriter(path.toString() + '/' + fileName)){
            writer.write("---Employee---\n");
            for (String title : dataTitles) {
                writer.write(title + ' ' + this.data.get(title) + '\n');
            }
        } catch (IOException e) {
            return 1;
        }
        return 0;
    }
    public int loadFile(String fileName) {
        List<String> lineList;
        Path filePath = Path.of(this.path.toString(), fileName);
        try (Stream<String> lines = Files.lines(filePath)) {
            lineList = lines.toList();
        } catch (IOException e) {
            return 1;
        }
        if (lineList.size() < 8) {
            return 2;
        }
        if (!lineList.getFirst().contains("---Employee---")) {
            return 3;
        }
        String[] dataTitles = {"Position", "Surname", "Name", "Patronymic", "Birth Date", "Gender", "Bank Account Number"};
        for (int i = 0; i < dataTitles.length; i++) {
            if (!lineList.get(i + 1).contains(dataTitles[i])) {
                return 3;
            }
            data.put(dataTitles[i], lineList.get(i + 1).substring(dataTitles.length + 2));
        }
        return 0;
    }

    public void setData(HashMap<String, String> newData) {
        this.data = newData;
    }

    public Employee getEmployee() {
        LocalDate birthDate = LocalDate.parse(data.get("Birth Date"));
        int bankAccountNumber = Integer.parseInt(data.get("Bank Account Number"));
        return switch (data.get("Position")) {
            case "Manager" ->
                    new Manager(data.get("Surname"), data.get("Name"), data.get("Patronymic"), birthDate, data.get("Gender"), bankAccountNumber);
            case "Cashier" ->
                    new Cashier(data.get("Surname"), data.get("Name"), data.get("Patronymic"), birthDate, data.get("Gender"), bankAccountNumber);
            case "Facilities Staff" ->
                    new FacilitiesStaff(data.get("Surname"), data.get("Name"), data.get("Patronymic"), birthDate, data.get("Gender"),
                            bankAccountNumber);
            case "Inventory Manager" ->
                    new InventoryManager(data.get("Surname"), data.get("Name"), data.get("Patronymic"), birthDate, data.get("Gender"),
                            bankAccountNumber);
            default -> null;
        };
    }

    public void deleteFile(String fileName) {
        String filePath = this.path.toString() + '/' + fileName;
        File file = new File(filePath);
        boolean isDeleted = file.delete();
        if (!isDeleted) {
            System.err.println("Failed to delete file");
        }
    }
}
