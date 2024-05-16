package main.java.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.java.backend.Logic;

/**
 * MainApp is the entry point for JavaFX applications.
 *
 * @author Sebastian Quek
 *
 */
public class MainApp extends Application {

    private static final String ROOT_LAYOUT_FXML = "/layouts/RootLayout.fxml";
    private static final String WINDOW_TITLE = "Collate";

    private static final String FEEDBACK_COLLATE_SUCCESSFUL = "Collate successful!";
    private static final String FEEDBACK_EMPTY = "";
    private static final String FEEDBACK_INVALID_COMMAND = "Invalid command.";

    private Stage primaryStage;
    private BorderPane rootLayout;

    private Logic logic;
    private CommandBarController commandBarController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        initRootLayout();
        initPrimaryStage(primaryStage);

        initLogic();

        // Add components to RootLayout
        addCommandBar(this);
    }

    /**
     * Initialises the RootLayout that will contain all other JavaFX components.
     */
    private void initRootLayout() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(ROOT_LAYOUT_FXML));
        try {
            rootLayout = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialises the main JavaFX Stage with RootLayout being the main Scene.
     *
     * @param primaryStage
     */
    private void initPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(WINDOW_TITLE);
        this.primaryStage.setScene(new Scene(rootLayout));
        this.primaryStage.show();
    }

    private void initLogic() {
        logic = new Logic();
    }

    private void addCommandBar(MainApp mainApp) {
        commandBarController = new CommandBarController(mainApp);
        rootLayout.setBottom(commandBarController);
    }

    private void addSummary(MainApp mainApp) {
        rootLayout.setCenter(new SummaryController(mainApp, logic.getAuthors()));
    }

    private void addFileStats(String authorName) {
        rootLayout.setCenter(new FileStatsController(authorName,
                                                     logic.getTargetAuthorStatistics()));
    }


    // ================================================================
    // Methods which refer to Logic directly
    // ================================================================

    public void handleKeyPress(KeyCode key, String userInput) {
        if (key == KeyCode.ENTER) {
            handleEnterPress(userInput);
        } else if (key == KeyCode.ESCAPE) {
            addSummary(this);
        }
    }

    private void handleEnterPress(String userInput) {
        switch (logic.executeCommand(userInput)) {

            case COLLATE :
                commandBarController.setFeedback(FEEDBACK_COLLATE_SUCCESSFUL);
            case SUMMARY :
                addSummary(this);
                break;

            case EXIT :
                primaryStage.hide();
                break;

            case VIEW :
                String authorName = logic.getTargetAuthorName();
                if (authorName != null) {
                    commandBarController.setFeedback(FEEDBACK_EMPTY);
                    addFileStats(authorName);
                    break;
                }
            case INVALID :
            default :
                commandBarController.setFeedback(FEEDBACK_INVALID_COMMAND);
                break;
        }
        commandBarController.clear();
    }

    public void handleMouseClick(AuthorBean selectedAuthor) {
        String authorName = selectedAuthor.nameProperty().get();
        logic.setTargetAuthorIfAuthorExists(authorName);
        commandBarController.setFeedback(FEEDBACK_EMPTY);
        addFileStats(authorName);
    }
}