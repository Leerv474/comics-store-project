package org.store;

import org.store.TextDocument.DocumentAPI;
import org.store.EmployeeSystem.EmployeeAPI;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        EmployeeAPI.start();
    }
}

