package org.store.Store.Comics;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class ComicsAPI {
    private static final HashMap<Integer, Comic> comicsMap = new HashMap<>();
    private static final Scanner scanner = new Scanner(System.in);

    private static final Path directoryPath = Path.of("src/main/java/org/store/ComicsData");
    private static final ComicsDAO dao = new ComicsDAO(directoryPath);

    public static void start() {
        ComicsAPI api = new ComicsAPI();

        try {
            Files.createDirectory(directoryPath);
        } catch (IOException ignored) {}

    }

    private void loadData() {
        List<String> fileNames = new ArrayList<>();
        try (Stream<Path> files = Files.list(directoryPath)) {
            files.filter(Files::isRegularFile).forEach(file -> fileNames.add(file.getFileName().toString()));
        } catch (IOException e) {
            System.err.println("Failed to load data");
        }

        for (String fileName : fileNames) {
            int hashId = Integer.parseInt(fileName.substring(0, fileName.length() -  4));
            dao.loadFile(fileName);
            comicsMap.put(hashId, dao.getEmployee());
        }
    }

    private void listEmployees() {
        System.out.println("The list of employees:");
        var comics = comicsMap.entrySet();
        comics.forEach(comic -> System.out.printf("%d: %s, %s\n", comic.getKey(), comic.getValue().getTitle(), comic.getValue().getAuthor()));
        System.out.println();
    }

    private void addComic() {

    }

    private void removeComic() {

    }

    private void restockComic() {

    }
}
