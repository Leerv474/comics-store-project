import TextDocument.Document;
import TextDocument.DocumentAPI;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        DocumentAPI test = new DocumentAPI();

        test.openDocument();
        test.printDocument();

    }
}