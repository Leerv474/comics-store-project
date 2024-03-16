package TextDocument;

import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
import java.io.FileWriter;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Stream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Document {
    private String title;
    private String author;
    private String path;
    private long fileSize; // fileSize determined by File in bytes
    private LocalDate creationDate;
    private List<String> fileContents;
    private boolean fileExists;

    // I need this class to be able to create new one if it doesn't exist, open existing, write

    private void parseData() {
        List<String> lineList;
        try (Stream<String> lines = Files.lines(Paths.get(path))) {
            lineList = lines.toList();
        } catch (IOException e) {
            this.fileExists = false;
            return;
        }
        this.fileExists = true;

        boolean hasTitle = lineList.get(0).contains("Title:");
        boolean hasAuthor = lineList.get(1).contains("Author:");
        boolean hasCreationDate = lineList.get(2).contains("Creation date:");
        if (!hasTitle || !hasAuthor || !hasCreationDate) {
            System.err.println("File doesn't follow document formatting");
            return;
        }
        title = lineList.get(0).substring(7);
        author = lineList.get(1).substring(8);
        String datePattern = "yyyy-MM-dd";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        creationDate = LocalDate.parse(lineList.get(2).substring(15).trim(), formatter);
        lineList.remove(0);
        lineList.remove(1);
        lineList.remove(2);
        lineList.remove(3);
        fileContents = lineList;
    }
    public Document(String path) {
        this.path = path;
        parseData();
    }

    public void setTitle(String title) {
        if (title.isEmpty()) {
            System.err.println("Title wasn't given");
            return;
        }
        this.title = title;
    }
    public void setAuthor(String author) {
        if (author.length() < 2) {
            System.err.println("Author name if too short");
            return;
        }
        this.author = author;
    }
    public void setFileContents(List<String> fileContents) {
        this.fileContents = fileContents;
    }
    public void saveDocument() {
        try (FileWriter writer = new FileWriter(path);){
            writer.write("Title: " + this.title + '\n');
            writer.write("Author: " + this.author + '\n');
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = dateFormat.format(this.creationDate);
            writer.write("Creation Date: " + dateString + "\n\n");
            for (String line : fileContents) {
                writer.write(line + '\n');
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
