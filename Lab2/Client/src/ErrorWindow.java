import javafx.application.Platform;
import javafx.scene.control.Alert;

abstract class ErrorWindow {
    static void show(Throwable e){
        show(e.getMessage());
        e.printStackTrace();
    }

    static void show(String message){
        Platform.runLater(() -> {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}