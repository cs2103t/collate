package main.java.gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * This class handles the Folder Chooser GUI.
 * 
 * @@author Riwu
 *
 */
public class FolderChooserController extends VBox {

    @FXML
    private Button inputFolderButton;

    @FXML
    private Button outputFolderButton;

    @FXML
    private ToggleButton omitSubFolderToggle;

    @FXML
    private TextField fileTypes;

    private static final String FOLDER_CHOOSER_FXML = "/main/resources/layouts/FolderChooser.fxml";
    private static final String WINDOW_TITLE = "Collate - Folder chooser";
    private static final String CLICK_TO_OMIT_SUB_FOLDER = "Click to omit sub folders";
    private static final String CLICK_TO_INCLUDE_SUB_FOLDER = "Click to include sub folders";

    private static final String FILE_TYPES_DELIMITER = "[,\\s]+";
    private static final String STRING_EMPTY = "";

    private File inputFolder;
    private File outputFolder;

    private boolean omitSubFolder = false;

    private Stage folderChooserStage;
    private MainApp mainApp;

    public FolderChooserController(MainApp mainApp) {
        this.mainApp = mainApp;

        FXMLLoader loader = new FXMLLoader(getClass().getResource(FOLDER_CHOOSER_FXML));
        loader.setController(this);
        loader.setRoot(this);

        folderChooserStage = new Stage();
        folderChooserStage.setTitle(WINDOW_TITLE);
        try {
            folderChooserStage.setScene(new Scene(loader.load()));
            folderChooserStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        omitSubFolderToggle.setText(CLICK_TO_OMIT_SUB_FOLDER);
    }

    private File getSelectedFolder(Button button) {
        File file = new DirectoryChooser().showDialog(folderChooserStage);
        if (file != null) {
            button.setText(file.toString());
        }
        return file;
    }

    @FXML
    private void onInputFolderClick() {
        File file = getSelectedFolder(inputFolderButton);
        if (file != null) {
            inputFolder = file;
        }
    }

    @FXML
    private void onOutputFolderClick() {
        File file = getSelectedFolder(outputFolderButton);
        if (file != null) {
            outputFolder = file;
        }
    }

    @FXML
    private void onOmitSubFolderToggle() {
        omitSubFolder = !omitSubFolder;
        omitSubFolderToggle.setText(omitSubFolder ? CLICK_TO_INCLUDE_SUB_FOLDER : CLICK_TO_OMIT_SUB_FOLDER);
    }

    @FXML
    private void onCollateButtonClick() {
        if (inputFolder == null) {
            return;
        }
        folderChooserStage.close();

        String saveDirectory = (outputFolder == null) ? STRING_EMPTY : outputFolder.toString();

        mainApp.handleCollateButtonClick(inputFolder, new File(saveDirectory).getAbsolutePath(),
                                         omitSubFolder, getFileTypes());
    }

    private ArrayList<String> getFileTypes() {
        String fileTypesText = fileTypes.getText();
        return (fileTypesText.equals(STRING_EMPTY))
             ? new ArrayList<String>()
             : new ArrayList<String>(Arrays.asList(fileTypesText.split(FILE_TYPES_DELIMITER)));
    }
}
