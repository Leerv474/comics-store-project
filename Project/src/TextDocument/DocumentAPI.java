package TextDocument;

import java.util.Scanner;
import java.io.Console;
import java.io.File;
import java.util.List;

public class DocumentAPI {

    private final Document document = new Document();
    private final Scanner scanner = new Scanner(System.in);
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
            title = scanner.next();
        } while (title.isEmpty());
        document.setTitle(title);
        String author;
        do {
            author = scanner.next();
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
            System.out.printf("\nTitle: %s\n", document.getTitle());
            System.out.printf("Author: %s\n", document.getAuthor());
            System.out.printf("Creation date: %s\n\n", document.getCreationDate());

            currentPageLine = (currentPage - 1) * numberOfPageLines;
            nextPageLine = currentPageLine + numberOfPageLines;
            for (int currentLine = currentPageLine; currentLine < nextPageLine; currentLine++) {
                if (currentLine == contents.size() - 1) {
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