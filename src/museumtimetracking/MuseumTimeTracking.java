/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import museumtimetracking.exception.DALException;

public class MuseumTimeTracking extends Application {

    public static final String ICON = "museumtimetracking/asset/img/icon.png";
//    public static final String AUDIO_LOCATION_VIKING = "museumtimetracking/asset/mp3/Viking.mp3";
//    private final Media media;
//    private final MediaPlayer mediaPlayer;

    public MuseumTimeTracking() {
//        File file = new File(AUDIO_LOCATION_VIKING);
//        media = new Media(file.toURI().toString());
//        mediaPlayer = new MediaPlayer(media);
    }
    
//"museumtimetracking/asset/mp3.Viking.mp3"  "file:///Users/mathi/Documents/GitHub/MuseumTimeTracking/src/museumtimetracking/asset/mp3/Viking.mp3"
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
        stage.getIcons().add(new Image(ICON));
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
        mainStage.getIcons().add(new Image(ICON));
        Scene scene = new Scene(root);
        mainStage.setScene(scene);
        return mainStage;
    }

    private FadeTransition createFadeIn(Parent startRoot, Stage stage) throws IOException, DALException {
        //Start fade in of start view
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(3), startRoot);
        fadeIn.setFromValue(0.2);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);
        fadeIn.play();

        // Plays the intro music.
//        mediaPlayer.play();

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
                
                // Stops intro music.
//                mediaPlayer.stop();
                
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
