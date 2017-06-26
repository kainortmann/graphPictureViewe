package main;

import com.sun.javafx.geom.Point2D;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.io.File;
import java.util.Optional;

public class Controller {

    File lastFolderInFileChooser;
    boolean setBoundariesModeEnabled = false;
    int timesDoubleKlicked = 0;
    double xminScene = 0;
    double xmaxScene = 0;
    double yminScene = 0;
    double ymaxScene = 0;
    double xminHuman = 0;
    double xmaxHuman = 0;
    double yminHuman = 0;
    double ymaxHuman = 0;
    javafx.scene.shape.Circle markedCoordinate = new Circle(0, 0, 5);

    @FXML
    private Label labelXValue;

    @FXML
    private Label labelYValue;

    @FXML
    private AnchorPane anchorRootPane;

    @FXML
    private MenuItem menuEnableEditingBoundaries;

    @FXML
    private ImageView imageView;


    @FXML
    public void initialize() {

        markedCoordinate.setVisible(false);
        markedCoordinate.setFill( new Color(0, 0, 0, 0));
        markedCoordinate.setStroke(new Color(0, 0, 0, 1));
        anchorRootPane.getChildren().add(markedCoordinate);

        imageView.imageProperty().addListener((observable, oldValue, newValue) -> {
            imageView.setFitWidth(newValue.getWidth());
            imageView.setFitHeight(newValue.getHeight());
        });



    }

    @FXML
    void onMouseCkicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            if (setBoundariesModeEnabled) {
                if (mouseEvent.getClickCount() == 2) {
                    String chosenPointName = setPointWithUserDecision(mouseEvent);
                    if (!chosenPointName.equals("")) {
                        askForCoordinateValue(chosenPointName);
                    }
                }
            } else if (mouseEvent.getClickCount() == 1) {
                this.markedCoordinate.setCenterX(mouseEvent.getSceneX());
                this.markedCoordinate.setCenterY(mouseEvent.getSceneY());
                this.markedCoordinate.setVisible(true);

                //System.out.println("Mouse on P(" + mouseEvent.getX() +" | " + mouseEvent.getY()  +")");



                labelXValue.setText(String.valueOf(xminHuman + ((xmaxHuman-xminHuman)/(xmaxScene-xminScene)) * (mouseEvent.getX() - xminScene)));
                labelYValue.setText(String.valueOf(yminHuman + ((ymaxHuman-yminHuman)/(ymaxScene-yminScene)) * (mouseEvent.getY() - yminScene)));

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
            xminScene = mouseEvent.getX();
            chosenPointName = "x_min";
        } else if (result.get() == buttonTypeXmax) {
            xmaxScene = mouseEvent.getX();
            chosenPointName = "x_max";
        } else if (result.get() == buttonTypeYmin) {
            yminScene = mouseEvent.getY();
            chosenPointName = "y_min";
        } else if (result.get() == buttonTypeYmax) {
            ymaxScene = mouseEvent.getY();
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
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("IMAGE (*.jpg,*.jpeg,*.gif,*.bmp,*.png", "*.jpg","*.jpeg","*.gif","*.bmp","*.png");

        fileChooser.getExtensionFilters().addAll(imageFilter);
        fileChooser.setInitialDirectory(lastFolderInFileChooser);
        //Show open file dialog
        File file = fileChooser.showOpenDialog(null);
        if (file==null || file.isDirectory()){
            return;
        }

        lastFolderInFileChooser = file.getParentFile();
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
