package TextDocument;

import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Document {
    private String title;
    private String author;
    private String path;
    private LocalDate creationDate;
    private List<String> fileContents;
    private boolean followsFormatting = false;


    public int openFile(String path) {
        followsFormatting = false;
        this.path = path;
        List<String> lineList;
        try (Stream<String> lines = Files.lines(Paths.get(path))) {
            lineList = lines.toList();
        } catch (IOException e) {
            return 1;
        }
        if (lineList.size() < 4) {
            return 2;
        }
        boolean hasTitle = lineList.get(0).contains("Title:");
        boolean hasAuthor = lineList.get(1).contains("Author:");
        boolean hasCreationDate = lineList.get(2).contains("Creation Date:");
        if (!hasTitle || !hasAuthor || !hasCreationDate) {
            return 3;
        }
        followsFormatting = true;
        title = lineList.get(0).substring(7);
        author = lineList.get(1).substring(8);
        String datePattern = "yyyy-MM-dd";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        creationDate = LocalDate.parse(lineList.get(2).substring(15).trim(), formatter);
        fileContents = lineList.subList(4, lineList.size());
        return 0;
    }
    public int createFile(String path, String title, String author) {
        File file = new File(path);
        if (file.exists()) {
            return 1;
        }
        try {
            if (!file.createNewFile()) {
                return 2;
            }
        } catch (IOException e) {
            return 2;
        }
        this.path = path;
        this.title = title;
        this.author = author;
        this.creationDate = LocalDate.now();
        try (FileWriter writer = new FileWriter(path)){
            writer.write("Title: " + this.title + '\n');
            writer.write("Author: " + this.author + '\n');
            String dateString = getCreationDate();
            writer.write("Creation Date: " + dateString + "\n\n");
            writer.close();
        } catch (IOException e) {
            return 3;
        }
        followsFormatting = true;
        return 0;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public void setFileContents(List<String> fileContents) {
        this.fileContents = fileContents;
    }

    public String getTitle() {
        return this.title;
    }
    public String getAuthor() {
        return this.author;
    }
    public String getCreationDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return this.creationDate.format(formatter);
    }
    public List<String> getFileContents() {
        return this.fileContents;
    }
    public long getFileSize() {
        File file = new File(path);
        return file.length();
    }
    public boolean isDocument() {
        return this.followsFormatting;
    }

    /**
     * This method creates a file and writes data to it.
     *
     * @return An integer value indicating the result:
     *         <ul>
     *         <li>0 - success</li>
     *         <li>1 - failed creating a file</li>
     *         <li>2 - title wasn't specified</li>
     *         <li>3 - author wasn't specified</li>
     *         <li>4 - failed writing to a file</li>
     *         </ul>
     */
    public int saveDocument() {
        if (fileContents.isEmpty()) {
            return 1;
        }
        try (FileWriter writer = new FileWriter(path)){
            for (String line : fileContents) {
                writer.write(line + '\n');
            }
            writer.close();
        } catch (IOException e) {
            return 2;
        }
        return 0;
    }
}
