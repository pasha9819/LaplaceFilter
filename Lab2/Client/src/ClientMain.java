import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

public class ClientMain extends Application {
    static String url;
    volatile byte[] destImage;
    private volatile Stage mainStage;

    @FXML
    TextField ipTextField;
    @FXML
    volatile ImageView imageView;

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = primaryStage;
        AnchorPane pane = FXMLLoader.load(getClass().getResource("client.fxml"));
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    public void openClick(){
        if (ipTextField.getText() == null || ipTextField.getText().isEmpty()){
            ErrorWindow.show("Enter server ip!");
            return;
        }
        url = "rmi://" + ipTextField.getText() + ":1099/";
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("..."));
        File file = fileChooser.showOpenDialog(mainStage);

        if (file != null){
            ClientTaskThread t = new ClientTaskThread(file, this);
            t.start();
        }
    }

    public void saveClick(){
        if (destImage == null && imageView.getImage() == null){
            ErrorWindow.show("No image to save");
            return;
        }
        File file = new File("result.png");

        try(OutputStream os = new FileOutputStream(file)){
            os.write(destImage);
        }catch (IOException e){
            ErrorWindow.show("Failed to write image to file");
        }
    }

    public static void main(String[] args) {
        String policyPath = "client.policy";
        URL policyURL = ClientMain.class.getResource(policyPath);
        System.setProperty("java.security.policy", policyURL.toString());
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        launch();
    }
}
