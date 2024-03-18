package TextDocument;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocumentAPI {

    public static void startDocumentSession() {
        DocumentAPI documentAPI = new DocumentAPI();
        boolean keepAlive = true;
        while (keepAlive) {
            byte option;
            var scanner = new Scanner(System.in);
            System.out.println("...Document API...\n0 - close API\n1 - open document\n2 - create document\n3 - read document\n4 - edit document\n");

            try {
                option = scanner.nextByte();
            } catch (InputMismatchException e) {
                continue;
            }
            switch (option) {
                case 0:
                    keepAlive = false;
                    break;
                case 1:
                    documentAPI.openDocument();
                    break;
                case 2:
                    documentAPI.createDocument();
                    break;
                case 3:
                    documentAPI.printDocument();
                    break;
                case 4:
                    documentAPI.editDocument();
                    break;
            }
        }
    }
    private final Document document = new Document();
    private final Scanner scanner = new Scanner(System.in);
    private final Scanner lineScanner = new Scanner(System.in);
    private String filePathInput() {
        System.out.print("Enter directory path: ");
        String directory;
        File file;
        do {
            directory = scanner.next();
            file = new File(directory);
        } while (!file.isDirectory());
        System.out.print("Enter file name: ");
        String fileName;
        do {
            fileName = scanner.next();
        } while (fileName.isEmpty());
        return directory + '/' + fileName;
    }

    public void createDocument() {
        String path = filePathInput();
        System.out.print("Enter title of the document: ");
        String title;
        do {
            title = scanner.nextLine();
        } while (title.isEmpty());
        document.setTitle(title);
        System.out.print("Enter author of the document: ");
        String author;
        do {
            author = scanner.nextLine();
        } while (author.isEmpty());
        document.setAuthor(author);

        int creationResult = document.createFile(path, title, author);
        switch (creationResult) {
            case 0:
                System.out.println("File successfully created.");
                break;
            case 1:
                System.out.println("File with this name already exists.");
                break;
            case 2:
                System.out.println("File creation failed.");
                break;
        }
        document.openFile(path);
    }

    public void openDocument() {
        String path = filePathInput();
        int openingResult = document.openFile(path);
        switch (openingResult) {
            case 0:
                System.out.println("Document opened successfully");
                break;
            case 1:
                System.out.println("Failed to read file");
                break;
            case 2, 3:
                System.out.println("File is not a document");
                break;
        }
    }

    // editing: write, appending, substituting line
    private final int linesPerPage = 15;
    private void printDocumentInfo() {
        System.out.printf("\nTitle: %s\n", document.getTitle());
        System.out.printf("Author: %s\n", document.getAuthor());
        System.out.printf("Creation date: %s\n\n", document.getCreationDate());
    }
    private void writeDocument() {
        String input;
        List<String> inputLines = new ArrayList<>();
        int currentPage = 1;
        int numberOfLines;
        boolean continueAppending = true;
        do {
            printDocumentInfo();
            System.out.println("type /end to stop writing\n");
            System.out.printf("page %d\n", currentPage);

            numberOfLines = 0;
            do {
                input = lineScanner.nextLine();
                if (input.equals("/end")) {
                    continueAppending = false;
                    break;
                }
                inputLines.add(input);
                numberOfLines++;
            } while (numberOfLines < this.linesPerPage);
            currentPage++;
        } while (continueAppending);
        document.setFileContents(inputLines);
    }
    private void appendDocument() {
        List<String> inputLines =  new ArrayList<>();
        List<String> contents = new ArrayList<>(document.getFileContents());
        int numberOfPages = contents.size() / this.linesPerPage + 1;
        int lastPageLine = this.linesPerPage * (numberOfPages - 1);
        String input;
        int currentPage = numberOfPages;
        int numberOfLines;
        boolean continueAppending = true;
        do {
            printDocumentInfo();
            System.out.println("type /end to stop writing\n");
            System.out.printf("page %d\n", currentPage);

            while (lastPageLine < contents.size()) {
                System.out.println(contents.get(lastPageLine));
                lastPageLine++;
            }
            numberOfLines = 0;
            do {
                input = lineScanner.nextLine();
                if (input.equals("/end")) {
                    continueAppending = false;
                    break;
                }
                inputLines.add(input);
                numberOfLines++;
            } while (numberOfLines < this.linesPerPage);
            currentPage++;
        } while (continueAppending);
        contents.addAll(inputLines);
        document.setFileContents(contents);
    }
    private void handleLineSubstitute(String input, List<String> contents) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);
        String numberStr;
        String newLine;
        int number;
        if (matcher.find()) {
            numberStr = matcher.group();
            number = Integer.parseInt(numberStr);
            System.out.print("Enter text to substitute with: ");
            newLine = lineScanner.nextLine();
            contents.set(number - 1, newLine);
        } else {
            System.out.println("No such line");
        }
    }
    private void substituteByLine() {
        if (!document.isDocument()) {
            System.out.println("Document file wasn't specified.");
            return;
        }
        List<String> contents = new ArrayList<>(document.getFileContents());

        int numberOfPageLines = 15;
        int numberOfPages = contents.size() / numberOfPageLines + 1;
        int currentPage = 1;
        int currentPageLine;
        int nextPageLine;

        String input;
        do {
            printDocumentInfo();

            currentPageLine = (currentPage - 1) * numberOfPageLines;
            nextPageLine = currentPageLine + numberOfPageLines;
            for (int currentLine = currentPageLine; currentLine < nextPageLine; currentLine++) {
                if (currentLine == contents.size() - 1) {
                    break;
                }
                System.out.printf("%d\t%s\n", currentLine + 1, contents.get(currentLine));
            }

            System.out.printf("\npage %d/%d\n", currentPage, numberOfPages);
            System.out.println("e<number> - edit line by number <number>, q - quit, n - next page, p - previous page");

            input = scanner.next();
            if (input.charAt(0) == 'n' && currentPage < numberOfPages) {
                currentPage++;
            }
            if (input.charAt(0) == 'p' && currentPage > 1) {
                currentPage--;
            }
            if (input.charAt(0) == 'e') {
                handleLineSubstitute(input, contents);
            }

        } while (input.charAt(0) != 'q');
        document.setFileContents(contents);
    }
    public void editDocument() {
        System.out.println("Choose editing method:\n0 - cancel\n1 - write\n2 - append\n3 - substitute lines");
        char enteredOption = scanner.next().charAt(0);
        switch (enteredOption) {
            case '0':
                return;
            case '1':
                writeDocument();
                break;
            case '2':
                appendDocument();
                break;
            case '3':
                substituteByLine();
                break;
        }
        int savingResult = document.saveDocument();
        if (savingResult == 1) {
            System.out.println("There is nothing to write to file");
        }
        if (savingResult == 2) {
            System.out.println("Failed to write to file");
        }
    }
    public void printDocument() {
        if (!document.isDocument()) {
            System.out.println("Document file wasn't specified.");
            return;
        }
        List<String> contents = document.getFileContents();
        int numberOfPageLines = 15;
        int numberOfPages = contents.size() / numberOfPageLines + 1;
        int currentPage = 1;
        int currentPageLine;
        int nextPageLine;

        char input;
        do {
            printDocumentInfo();

            currentPageLine = (currentPage - 1) * numberOfPageLines;
            nextPageLine = currentPageLine + numberOfPageLines;
            for (int currentLine = currentPageLine; currentLine < nextPageLine; currentLine++) {
                if (currentLine == contents.size()) {
                    break;
                }
                System.out.println(contents.get(currentLine));
            }

            System.out.printf("\npage %d/%d\n", currentPage, numberOfPages);
            System.out.println("q - quit, n - next page, p - previous page");

            input = scanner.next().charAt(0);
            if (input == 'n' && currentPage < numberOfPages) {
                currentPage++;
            }
            if (input == 'p' && currentPage > 1) {
                currentPage--;
            }

        } while (input != 'q');
    }
}