package org.store.Store;

import org.store.EmployeeSystem.EmployeeAPI;
import org.store.Store.Comics.ComicsAPI;
import org.store.TextDocument.DocumentAPI;

import java.util.Scanner;

public class StoreAPI {
    public static void start() {
        System.out.println("...Welcome to the administrator page...");
        var scanner = new Scanner(System.in);
        char option;
        while (true) {
            System.out.println("Actions:\n0 - close\n1 - Manage comics\n2 - Manage employees\n3 - View documents");
            option = scanner.next().charAt(0);

            if (option == '0') {
                return;
            }
            if (option == '1') {
                ComicsAPI.startComicsAPI();
            }
            if (option == '2') {
                EmployeeAPI.startEmployeeAPI();
            }
            if (option == '3') {
                DocumentAPI.startDocumentApiInDirectory( "src/main/java/org/store/DocumentData");
            }
        }
    }
}