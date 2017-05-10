/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking;

import java.io.IOException;
import java.net.URL;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class MuseumTimeTracking extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent startRoot = createLoadingView(stage);

        Stage mainStage = createMainView();

        FadeTransition fadeIn = createFadeIn(startRoot, stage);

        setOnFadeInFinished(fadeIn, startRoot, mainStage, stage);

    }

    private Parent createLoadingView(Stage stage) throws IOException {
        //Start out loading start view
        Parent startRoot = FXMLLoader.load(getClass().getResource("gui/views/startScreen/StartView.fxml"));
        Scene startScene = new Scene(startRoot);
        stage.getIcons().add(new Image("museumtimetracking/asset/img/icon.png"));
        stage.setScene(startScene);
        stage.initStyle(StageStyle.UNDECORATED);
        return startRoot;
    }

    private Stage createMainView() throws IOException {
        //Start loading main view
        Stage mainStage = new Stage();
        URL location = getClass().getResource("/museumtimetracking/gui/views/root/MTTMainView.fxml");
        FXMLLoader loader = new FXMLLoader(location);
        Parent root = loader.load();
        mainStage.getIcons().add(new Image("museumtimetracking/asset/img/icon.png"));
        Scene scene = new Scene(root);
        mainStage.setScene(scene);
        return mainStage;
    }

    private FadeTransition createFadeIn(Parent startRoot, Stage stage) {
        //Start fade in of start view
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(3), startRoot);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);
        fadeIn.play();
        stage.show();
        return fadeIn;
    }

    private void setOnFadeInFinished(FadeTransition fadeIn, Parent startRoot, Stage mainStage, Stage stage) {
        fadeIn.setOnFinished((e) -> {
            //Start fade out of start view
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), startRoot);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setCycleCount(1);
            fadeOut.play();

            //Display main and close start view
            fadeOut.setOnFinished((finishedEvent) -> {
                mainStage.show();
                stage.close();
            });
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
