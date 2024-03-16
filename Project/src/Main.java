import TextDocument.Document;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Document test = new Document("data/test.txt");
        test.setAuthor("Lee");
        test.setTitle("Testing");

        List<String> newContents = new ArrayList<>();
        newContents.add("Hello");
        newContents.add("Hello");
        newContents.add("Hello");
        newContents.add("Hello");
        newContents.add("Hello");

        test.setFileContents(newContents);

        test.saveDocument();

    }
}