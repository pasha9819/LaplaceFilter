import javafx.scene.image.Image;

import java.io.*;
import java.rmi.Naming;



public class ClientTaskThread extends Thread {
    private File imageFile;
    private ClientMain controller;

    ClientTaskThread(File imageFile, ClientMain controller){
        this.imageFile = imageFile;
        this.controller = controller;
    }

    @Override
    public void run() {
        try(InputStream is = new FileInputStream(imageFile)) {
            LaplaceFilter filter = (LaplaceFilter) Naming.lookup(ClientMain.url + "LaplaceFilter");

            byte[] srcImage = new byte[is.available()];
            if (is.read(srcImage) == -1){
                ErrorWindow.show("Failed to read source file");
                return;
            }
            byte[] dest = filter.transform(srcImage);
            controller.imageView.setImage(new Image(new ByteArrayInputStream(dest)));
            controller.destImage = dest;
        } catch (Exception e) {
            ErrorWindow.show("Error occurred while accessing the server\n " + e.getMessage());
        }
    }
}
