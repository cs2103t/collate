package main.java.gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

/**
 * This class represents each row in the custom FileStats view.
 * It implements Comparable so that they can be sorted by their percentage
 * values.
 *
 * @author Sebastian Quek
 *
 */
public class FileStatsItem extends BorderPane implements
        Comparable<FileStatsItem> {

    @FXML
    private HBox card;

    @FXML
    private Text filename;

    @FXML
    private Text linesOfCode;

    @FXML
    private Text percentage;

    @FXML
    private Shape circle;

    private static final String FILE_STATS_ITEM_FXML = "/layouts/FileStatsItem.fxml";

    private static final String STRING_TRUNCATED_FORMAT = "...%s";
    private static final String STRING_LINES_OF_CODE_FORMAT = "%s lines";
    private static final String STRING_PERCENTAGE_FORMAT = "%.0f%%";
    private static final String STRING_CIRCLE_FILL_STYLE_FORMAT = "-fx-fill: %s";

    private static final int MAX_FILENAME_LENGTH = 40;

    private static final String BASE_COLOUR_0 = "#FF4D5E";
    private static final String BASE_COLOUR_20 = "#E8803D";
    private static final String BASE_COLOUR_40 = "#FFD251";
    private static final String BASE_COLOUR_60 = "#D7E84A";
    private static final String BASE_COLOUR_80 = "#51FF61";

    private double percentageValue;

    public FileStatsItem(String filename, int linesOfCode, double percentage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FILE_STATS_ITEM_FXML));
        loader.setController(this);
        loader.setRoot(this);

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.percentageValue = percentage;

        this.filename.setText(generateTruncatedName(filename));
        this.linesOfCode.setText(String.format(STRING_LINES_OF_CODE_FORMAT,
                                               linesOfCode));
        this.percentage.setText(String.format(STRING_PERCENTAGE_FORMAT,
                                              percentage));
        this.circle.setStyle(String.format(STRING_CIRCLE_FILL_STYLE_FORMAT,
                                           generateColour(percentage)));
    }

    private String generateColour(double percentage) {
        if (percentage >= 80) {
            return BASE_COLOUR_80;
        } else if (percentage >= 60) {
            return BASE_COLOUR_60;
        } else if (percentage >= 40) {
            return BASE_COLOUR_40;
        } else if (percentage >= 20) {
            return BASE_COLOUR_20;
        } else {
            return BASE_COLOUR_0;
        }
    }

    private String generateTruncatedName(String filename) {
        if (filename.length() >= MAX_FILENAME_LENGTH) {
            return String.format(STRING_TRUNCATED_FORMAT,
                                 filename.substring(filename.length() -
                                                    MAX_FILENAME_LENGTH));
        }
        return filename;
    }

    public double getPercentageValue() {
        return percentageValue;
    }

    @Override
    public int compareTo(FileStatsItem otherItem) {
        return (int) Math.round(otherItem.getPercentageValue() -
                                percentageValue);
    }
}
