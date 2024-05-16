package main.java.gui;

import java.io.IOException;
import java.util.Collection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import main.java.data.Author;

/**
 * This class controls the Summary view by loading the corresponding fxml file
 * and initialising the table's columns with the authors' properties.
 *
 * @author Sebastian Quek
 *
 */
public class SummaryController extends StackPane {

    @FXML
    private TableView<AuthorBean> summaryTable;
    @FXML
    private TableColumn<AuthorBean, String> authorNameColumn;
    @FXML
    private TableColumn<AuthorBean, Integer> linesOfCodeColumn;
    @FXML
    private TableColumn<AuthorBean, Double> proportionColumn;

    private static final String OVERVIEW_LAYOUT_FXML = "/layouts/Summary.fxml";

    public SummaryController(MainApp mainApp, Collection<Author> inputSummaryData) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(OVERVIEW_LAYOUT_FXML));
        loader.setController(this);
        loader.setRoot(this);

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ObservableList<AuthorBean> summaryData = FXCollections.observableArrayList();
        for (Author author : inputSummaryData) {
            summaryData.add(new AuthorBean(author));
        }

        summaryTable.setItems(summaryData);
        summaryTable.getSelectionModel().selectedItemProperty()
                                        .addListener((observableValue, oldValue, newValue) -> {
                                                         mainApp.handleMouseClick(newValue);
                                                     });

        authorNameColumn.setCellValueFactory(new PropertyValueFactory<AuthorBean, String>("name"));
        linesOfCodeColumn.setCellValueFactory(new PropertyValueFactory<AuthorBean, Integer>("linesOfCode"));
        proportionColumn.setCellValueFactory(new PropertyValueFactory<AuthorBean, Double>("proportion"));
    }
}
