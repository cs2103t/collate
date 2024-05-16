package main.java.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import main.java.data.SourceFile;

/**
 * This class controls the view that shows an author's individual contributions
 * to the source files.
 *
 * @author Sebastian Quek
 *
 */
public class FileStatsController extends BorderPane {

    @FXML
    private ListView<FileStatsItem> fileStats;

    @FXML
    private Label title;

    private static final String FILE_STATS_FXML = "/layouts/FileStats.fxml";

    private ArrayList<FileStatsItem> items;

    public FileStatsController(String authorName,
                               HashMap<SourceFile, Integer> statistics) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FILE_STATS_FXML));
        loader.setController(this);
        loader.setRoot(this);

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        title.setText(authorName);
        initStats(statistics);
    }

    private void initStats(HashMap<SourceFile, Integer> statistics) {
        items = new ArrayList<FileStatsItem>();

        for (SourceFile sourceFile : statistics.keySet()) {
            addFileStatsItem(sourceFile, statistics.get(sourceFile));
        }

        Collections.sort(items);
        fileStats.setItems(FXCollections.observableList(items));
    }

    /**
     * Each FileStatsItem corresponds to a source file and is displayed as a row
     * in this custom view.
     *
     * @param currentFile
     * @param currentNumLines
     */
    private void addFileStatsItem(SourceFile currentFile, int currentNumLines) {
        items.add(new FileStatsItem(currentFile.getRelativeFilePath(),
                                    currentNumLines,
                                    (double) currentNumLines /
                                            currentFile.getNumLines() * 100));
    }
}
