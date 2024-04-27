package org.store.Store.Comics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

public class ComicsAPI {
    private static final HashMap<Integer, Comic> comicsMap = new HashMap<>();
    private static final Scanner scanner = new Scanner(System.in);

    private static final Path directoryPath = Path.of("src/main/java/org/store/ComicsData");
    private static final ComicsDAO dao = new ComicsDAO(directoryPath);

    public static void startComicsAPI() {
        ComicsAPI api = new ComicsAPI();

        try {
            Files.createDirectory(directoryPath);
        } catch (IOException ignored) {}

        api.loadData();
        char inputOption;
        while (true) {
            System.out.println("...Comics system...");
            System.out.println("""
                    Choose action:
                    0 - close API
                    1 - list comics
                    2 - add new comic
                    3 - sell comic
                    4 - restock comic
                    5 - write off\
                     comic""");
            inputOption = scanner.next().charAt(0);
            switch (inputOption) {
                case '0':
                    return;
                case '1':
                    api.listComics();
                    break;
                case '2':
                    api.addComic();
                    break;
                case '3':
                    api.registerSale();
                    break;
                case '4':
                    api.restockComic();
                    break;
                case '5':
                    api.removeComic();
                    break;
            }
            System.out.println();
        }
    }

    public void loadData() {
        List<String> fileNames = new ArrayList<>();
        try (Stream<Path> files = Files.list(directoryPath)) {
            files.filter(Files::isRegularFile).forEach(file -> fileNames.add(file.getFileName().toString()));
        } catch (IOException e) {
            System.err.println("Failed to load data");
        }

        for (String fileName : fileNames) {
            int hashId = Integer.parseInt(fileName.substring(0, fileName.length() -  4));
            dao.loadFile(fileName);
            comicsMap.put(hashId, dao.getComic());
        }
    }

    public void listComics() {
        while (true) {
            System.out.println("0 - go back\n1 - by title\n2 - by author\n3 - by amount in stock\n4 - by price");
            byte option;
            try {
                option = scanner.nextByte();
            } catch (Exception e) {
                continue;
            }
            switch (option) {
                case 0:
                    return;
                case 1:
                    listByTitle();
                    return;
                case 2:
                    listByAuthor();
                    return;
                case 3:
                    listByAmount();
                    return;
                case 4:
                    listByPrice();
                    return;
            }
        }
    }

    private void listByTitle() {
        System.out.println("---List of comics---");
        TreeMap<String, Integer> list = new TreeMap<>();
        comicsMap.forEach((key, value) -> list.put(value.getTitle(), key));
        list.forEach((key, value) -> System.out.printf("%d: %s, %s, amount: %d, price: %d\n", value, key,
                comicsMap.get(value).getAuthor(), comicsMap.get(value).getAmountInStock(), comicsMap.get(value).getPrice()));
        System.out.println();
    }

    private void listByAuthor() {
        System.out.println("---List of comics---");
        TreeMap<String, Integer> list = new TreeMap<>();
        comicsMap.forEach((key, value) -> list.put(value.getAuthor(), key));
        list.forEach((key, value) -> System.out.printf("%d: %s, %s, amount: %d, price: %d\n", value, comicsMap.get(value).getTitle(),
                key, comicsMap.get(value).getAmountInStock(), comicsMap.get(value).getPrice()));
        System.out.println();
    }

    private void listByAmount() {
        System.out.println("---List of comics---");
        TreeMap<Integer, Integer> list = new TreeMap<>();
        comicsMap.forEach((key, value) -> list.put(value.getAmountInStock(), key));
        list.forEach((key, value) -> System.out.printf("%d: %s, %s, amount: %d, price: %d\n", value, comicsMap.get(value).getTitle(),
                comicsMap.get(value).getAuthor(),
                key, comicsMap.get(value).getPrice()));
        System.out.println();
    }

    private void listByPrice() {
        System.out.println("---List of comics---");
        TreeMap<Integer, Integer> list = new TreeMap<>();
        comicsMap.forEach((key, value) -> list.put(value.getPrice(), key));
        list.forEach((key, value) -> System.out.printf("%d: %s, %s, amount: %d, price: %d\n", value, comicsMap.get(value).getTitle(),
                comicsMap.get(value).getAuthor(),
                comicsMap.get(value).getAmountInStock(), key));
        System.out.println();
    }

    public void addComic() {
        System.out.println("...Adding new comic...");
        HashMap<String, String> dataInput = new HashMap<>();
        String[] titles = {"Title", "Author", "Release Date", "Price", "Amount in Stock"};

        for (String title : titles) {
            System.out.printf("%s: ", title);
            do {
                dataInput.put(title, scanner.nextLine());
            } while (dataInput.get(title).isEmpty());
        }
        System.out.println("""
                Choose genre:
                1 - Superhero
                2 - Science fiction
                3 - Fantasy
                4 - Horror
                5 - Noir
                6 - Slice of life
                7 - \
                Historical
                8 - Romance
                9 - Adventure
                10 - Comedy""");
        int option = 0;
        Genre newGenre = null;
        while (newGenre == null) {
            try {
                option = scanner.nextInt();
            } catch (Exception ignore) {}

            newGenre = switch (option) {
                case 1 -> Genre.SUPERHERO;
                case 2 -> Genre.SCIENCE_FICTION;
                case 3 -> Genre.FANTASY;
                case 4 -> Genre.HORROR;
                case 5 -> Genre.NOIR;
                case 6 -> Genre.SLICE_OF_LIFE;
                case 7 -> Genre.HISTORICAL;
                case 8 -> Genre.ROMANCE;
                case 9 -> Genre.ADVENTURE;
                case 10 -> Genre.Comedy;
                default -> null;
            };
        }

        LocalDate releaseDate;
        try {
            releaseDate = LocalDate.parse(dataInput.get("Release Date"));
        } catch (Exception e) {
            System.err.println("Failed to convert date");
            return;
        }
        int price;
        int amountInStock;

        try {
            price = Integer.parseInt(dataInput.get("Price"));
            amountInStock = Integer.parseInt(dataInput.get("Amount in Stock"));
        } catch (Exception e) {
            System.err.println("Failed to convert integer");
            return;
        }

        var random = new Random();
        int minId = 100_000;
        int maxId = 999_999;
        int newHash = random.nextInt(maxId - minId + 1) + minId;
        while (comicsMap.containsKey(newHash)) {
            newHash = random.nextInt(maxId - minId + 1) + minId;
        }
        Comic newComic = new Comic(dataInput.get("Title"), dataInput.get("Author"), newGenre,releaseDate, price,
                amountInStock);
        comicsMap.put(newHash, newComic);
        dao.setData(newComic);
        dao.saveFile(String.format("%d.txt", newHash));
    }

    public void removeComic() {
        System.out.println("...Removing comic...");
        System.out.print("Enter comic id: ");
        
        int comicId = -1;
        do {
            try {
                comicId = scanner.nextInt();
            } catch (Exception ignore) {}
        } while (comicId == -1);

        if (!comicsMap.containsKey(comicId)) {
            System.out.println("Comic not found");
            return;
        }
        comicsMap.remove(comicId);
        String fileName = String.format("%d.txt", comicId);
        dao.deleteFile(fileName);
    }

    public void restockComic() {
        System.out.println("...Restocking...");
        System.out.print("Enter comic id: ");

        int comicId = -1;
        do {
            try {
                comicId = scanner.nextInt();
            } catch (Exception ignore) {}
        } while (comicId > 100000 && comicId < 999999);

        if (!comicsMap.containsKey(comicId)) {
            System.out.println("Comic not found");
            return;
        }
        System.out.print("Enter amount for restock: ");
        int newAmount = 0;
        do {
            try {
                newAmount = scanner.nextInt();
            } catch (Exception ignore) {}
        } while (newAmount > 0);
        Comic comic = comicsMap.get(comicId);
        newAmount += comic.getAmountInStock();
        comic.setAmountInStock(newAmount);
        dao.setData(comic);
        String fileName = String.format("%d.txt", comicId);
        dao.saveFile(fileName);
    }

    public void registerSale() {
        System.out.println("...Registering sale...");
        System.out.print("Enter comic id: ");

        int comicId = -1;
        do {
            try {
                comicId = scanner.nextInt();
            } catch (Exception ignore) {}
        } while (comicId > 100000 && comicId < 999999);

        if (!comicsMap.containsKey(comicId)) {
            System.out.println("Comic not found");
            return;
        }
        System.out.print("Enter amount of sold comics: ");
        int newAmount = 0;
        do {
            try {
                newAmount = scanner.nextInt();
            } catch (Exception ignore) {}
        } while (newAmount > 0);
        Comic comic = comicsMap.get(comicId);
        newAmount -= comic.getAmountInStock();
        comic.setAmountInStock(newAmount);
        dao.setData(comic);
        String fileName = String.format("%d.txt", comicId);
        dao.saveFile(fileName);
    }
}
