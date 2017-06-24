package sample;


import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.io.File;
import java.util.Optional;

public class Controller {

    File lastFolderInFileChooser;
    boolean setBoundariesModeEnabled = false;
    int timesDoubleKlicked = 0;
    double xminScene, xmaxScene, yminScene, ymaxScene = 0;
    double xminHuman, xmaxHuman, yminHuman, ymaxHuman = 0;

    @FXML
    private AnchorPane anchorRootPane;

    @FXML
    private MenuItem menuEnableEditingBoundaries;

    @FXML
    private ImageView imageView;

    @FXML
    public void initialize() {
        imageView.fitHeightProperty().bind(anchorRootPane.heightProperty());
        imageView.fitWidthProperty().bind(anchorRootPane.widthProperty());
    }

    @FXML
    void onMouseCkicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            if (mouseEvent.getClickCount() == 2) {
                if (setBoundariesModeEnabled) {
                    String chosenPointName = setPointWithUserDecision(mouseEvent);
                    if (!chosenPointName.equals("")) {
                        askForCoordinateValue(chosenPointName);
                    }
                }
            }
        }

    }


    private String setPointWithUserDecision(MouseEvent mouseEvent) {
        String chosenPointName = ""; //just for the next message in order to diplay the choice as text again
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Set Point");
        alert.setHeaderText("Choose, which point you want to set!");

        ButtonType buttonTypeXmin = new ButtonType("x_min");
        ButtonType buttonTypeXmax = new ButtonType("x_max");
        ButtonType buttonTypeYmin = new ButtonType("y_min");
        ButtonType buttonTypeYmax = new ButtonType("y_max");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeXmin, buttonTypeXmax, buttonTypeYmin, buttonTypeYmax, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == buttonTypeXmin) {
            xminScene = mouseEvent.getSceneX();
            chosenPointName = "x_min";
        } else if (result.get() == buttonTypeXmax) {
            xmaxScene = mouseEvent.getSceneX();
            chosenPointName = "x_max";
        } else if (result.get() == buttonTypeYmin) {
            yminScene = mouseEvent.getSceneX();
            chosenPointName = "y_min";
        } else if (result.get() == buttonTypeYmax) {
            xmaxScene = mouseEvent.getSceneX();
            chosenPointName = "y_max";
        }

        return chosenPointName;
    }

    private void askForCoordinateValue(String coordinateName) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Set the value of " + coordinateName);
        dialog.setHeaderText("Type in the value of " + coordinateName);

// Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            switch (coordinateName) {
                case "x_min":
                    xminHuman = Double.valueOf(result.get());
                    break;
                case "x_max":
                    xmaxHuman = Double.valueOf(result.get());
                    break;
                case "y_min":
                    yminHuman = Double.valueOf(result.get());
                    break;
                case "y_max":
                    ymaxHuman = Double.valueOf(result.get());
                    break;
            }
        }
    }


    @FXML
    void menuFileOpen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
        fileChooser.setInitialDirectory(lastFolderInFileChooser);
        //Show open file dialog
        File file = fileChooser.showOpenDialog(null);

        Image image = new Image(file.toURI().toString());
        imageView.setImage(image);

    }


    @FXML
    void onMenuSetBoundaries(ActionEvent event) {
        System.out.println("bla");
        setBoundariesModeEnabled = !setBoundariesModeEnabled;
        if (setBoundariesModeEnabled) {
            menuEnableEditingBoundaries.setText("Disable boundarie editing");
        } else {
            menuEnableEditingBoundaries.setText("Enable boundarie editing");
        }
    }

}
