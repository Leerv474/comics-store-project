package org.store.Store.Comics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class ComicsDAO {
    private final HashMap<String, String> data = new HashMap<>();
    private final Path path;
    private final String[] dataTitles = {"Title", "Author", "Genre", "Release Date", "Price", "Amount in Stock"};

    public ComicsDAO(Path path) {
        this.path = path;
    }

    public void saveFile(String fileName) {
        try (FileWriter writer = new FileWriter(path.toString() + '/' + fileName)){
            writer.write("---Comic---\n");
            for (String title : this.dataTitles) {
                writer.write(title + ": " + this.data.get(title) + '\n');
            }
            writer.flush();
        } catch (IOException e) {
            System.err.println("Failed to write file");
        }
    }

    public void loadFile(String fileName) {
        List<String> lineList;
        Path filePath = Path.of(this.path.toString(), fileName);
        try (Stream<String> lines = Files.lines(filePath)) {
            lineList = lines.toList();
        } catch (IOException e) {
            System.err.println("Failed to read file");
            return;
        }
        if (lineList.size() < 6) {
            System.err.println("File doesn't follow formatting");
            return;
        }
        if (!lineList.getFirst().contains("---Comic---")) {
            System.err.println("File doesn't follow formatting");
            return;
        }
        for (int i = 0; i < this.dataTitles.length; i++) {
            if (!lineList.get(i + 1).contains(this.dataTitles[i])) {
                System.err.println("File doesn't follow formatting");
                return;
            }
            data.put(this.dataTitles[i], lineList.get(i + 1).substring(this.dataTitles[i].length() + 2));
        }
    }

    public void setData(Comic comic) {
        this.data.put("Title", comic.getTitle());
        this.data.put("Author", comic.getAuthor());
        this.data.put("Genre", comic.getGenre().name());
        this.data.put("Release Date", comic.getReleaseDate().toString());
        this.data.put("Price", String.valueOf(comic.getPrice()));
        this.data.put("Amount in Stock", String.valueOf(comic.getAmountInStock()));
    }

    public Comic getComic() {
        LocalDate releaseDate = LocalDate.parse(data.get("Release Date"));
        int amountInStock = Integer.parseInt(data.get("Amount in Stock"));
        int price = Integer.parseInt(data.get("Price"));
        Genre genre = Genre.valueOf(data.get("Genre"));

        return new Comic(this.data.get("Title"), this.data.get("Author"), genre, releaseDate, price, amountInStock);
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