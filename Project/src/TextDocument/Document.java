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
    private final String path;
    private LocalDate creationDate;
    private List<String> fileContents;


    private void parseData() {
        List<String> lineList;
        try (Stream<String> lines = Files.lines(Paths.get(path))) {
            lineList = lines.toList();
        } catch (IOException e) {
            System.err.println("Failed to read file");
            return;
        }
        if (lineList.size() < 3){
            return;
        }

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
        lineList.removeFirst();
        lineList.removeFirst();
        lineList.removeFirst();
        lineList.removeFirst();
        fileContents = lineList;
    }
    public Document(String path) {
        this.path = path;
        parseData();
    }

    /**
     * @return An integer value indicating the result:
     *      <ul>
     *          <li> 0 - success</li>
     *          <li> 1 - failure </li>
     *      </ul>
     */
    public int setTitle(String title) {
        if (title.isEmpty()) {
            return 1;
        }
        this.title = title;
        return 0;
    }
    /**
     * @return An integer value indicating the result:
     *      <ul>
     *          <li> 0 - success</li>
     *          <li> 1 - failure </li>
     *      </ul>
     */
    public int setAuthor(String author) {
        if (author.length() < 2) {
            return 1;
        }
        this.author = author;
        return 0;
    }

    /**
     * @return An integer value indicating the result:
     *      <ul>
     *          <li> 0 - success</li>
     *          <li> 1 - failure </li>
     *      </ul>
     */
    public int setFileContents(List<String> fileContents) {
        if (fileContents.isEmpty()) {
            return 1;
        }
        this.fileContents = fileContents;
        return 0;
    }

    public String getTitle() {
        return this.title;
    }
    public String getAuthor() {
        return this.author;
    }
    public List<String> getFileContents() {
        return this.fileContents;
    }
    public long getFileSize() {
        File file = new File(path);
        return file.length();
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
        File file = new File(path);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    return 1;
                }
            } catch (IOException e) {
                return 1;
            }
        }
        if (this.title.isEmpty()) {
            return 2;
        }
        if (this.author.isEmpty()) {
            return 3;
        }
        try (FileWriter writer = new FileWriter(path)){
            writer.write("Title: " + this.title + '\n');
            writer.write("Author: " + this.author + '\n');
            this.creationDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dateString = this.creationDate.format(formatter);
            writer.write("Creation Date: " + dateString + "\n\n");
            for (String line : fileContents) {
                writer.write(line + '\n');
            }
        } catch (IOException e) {
            return 4;
        }
        return 0;
    }
}
