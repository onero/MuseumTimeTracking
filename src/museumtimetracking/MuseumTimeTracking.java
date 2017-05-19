/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import museumtimetracking.be.enums.EAppLanguage;
import static museumtimetracking.be.enums.EAppLanguage.*;
import museumtimetracking.exception.DALException;

public class MuseumTimeTracking extends Application {

    public static final String RESOURCE_LOCATION = "museumtimetracking.gui.language.UIResources";
    public static final String ICON = "museumtimetracking/asset/img/icon.png";
    private static final String VIKING_STARTUP_SOUND = "asset/mp3/Viking.mp3";
    private final MediaPlayer mediaPlayer;

    public MuseumTimeTracking() {

        URL sound = this.getClass().getResource(VIKING_STARTUP_SOUND);
        Media media = new Media(sound.toString());
        mediaPlayer = new MediaPlayer(media);

    }

    private static final Stage MAIN_STAGE = new Stage();

    public static ResourceBundle bundle;
    public static StringProperty LOCALE = new SimpleStringProperty(DANISH.toString());

    @Override
    public void start(Stage stage) throws Exception {
        bundle = ResourceBundle.getBundle(RESOURCE_LOCATION, new Locale(LOCALE.get()));

        instantiateLanguageListener();

        Parent startRoot = createLoadingView(stage);

        createMainView();

        FadeTransition fadeIn = createFadeIn(startRoot, stage);

        setOnFadeInFinished(fadeIn, startRoot, MAIN_STAGE, stage);

    }

    /**
     * Create a changelistener for the locale language
     */
    private void instantiateLanguageListener() {
        LOCALE.addListener((observable, oldValue, newValue) -> {

            bundle = ResourceBundle.getBundle(RESOURCE_LOCATION, new Locale(LOCALE.get()));

            try {
                createMainView();
            } catch (IOException ex) {
                Logger.getLogger(MuseumTimeTracking.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    /**
     * Change the language of the application
     *
     * @param language
     */
    public static void changeLanguage(EAppLanguage language) {
        LOCALE.set(language.toString());
    }

    /**
     * Create the start view
     *
     * @param stage
     * @return
     * @throws IOException
     */
    private Parent createLoadingView(Stage stage) throws IOException {
        Parent startRoot = FXMLLoader.load(getClass().getResource("gui/views/startScreen/StartView.fxml"), bundle);
        Scene startScene = new Scene(startRoot);
        stage.getIcons().add(new Image(ICON));
        stage.setScene(startScene);
        stage.initStyle(StageStyle.UNDECORATED);
        return startRoot;
    }

    /**
     * Create the mainview
     *
     * @return
     * @throws IOException
     */
    private Stage createMainView() throws IOException {
        //Start loading main view
        URL location = getClass().getResource("/museumtimetracking/gui/views/root/MTTMainView.fxml");
        FXMLLoader loader = new FXMLLoader(location, bundle);
        Parent root = loader.load();
        MAIN_STAGE.getIcons().add(new Image(ICON));
        Scene scene = new Scene(root);
        MAIN_STAGE.setScene(scene);
        return MAIN_STAGE;
    }

    /**
     * Create the fadein effect
     *
     * @param startRoot
     * @param stage
     * @return
     * @throws IOException
     * @throws DALException
     */
    private FadeTransition createFadeIn(Parent startRoot, Stage stage) throws IOException, DALException {
        //Start fade in of start view
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(3), startRoot);
        fadeIn.setFromValue(0.2);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);
        fadeIn.play();

        // Plays the intro music.
        mediaPlayer.play();

        stage.show();
        return fadeIn;
    }

    /**
     * Set on fadein finished functionality
     *
     * @param fadeIn
     * @param startRoot
     * @param mainStage
     * @param stage
     */
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
                //Start timeline to fadeout music
                Timeline timeline = new Timeline(
                        //KeyFrame is to define the duration the fadeout will take
                        new KeyFrame(Duration.seconds(3),
                                //KeyValue is to define the start and end value
                                new KeyValue(mediaPlayer.volumeProperty(), 0)));
                timeline.play();

                mainStage.show();
                stage.close();

                //Set on finish task
                timeline.setOnFinished((event) -> {
                    // Stop intro music.
                    mediaPlayer.stop();
                });

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
