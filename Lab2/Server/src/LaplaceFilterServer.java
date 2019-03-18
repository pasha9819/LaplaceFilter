import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class LaplaceFilterServer {
    public static void main(String[] args) {
        try {
            LaplaceFilter filter = new LaplaceFilterImpl();
            LaplaceFilter stub =
                    (LaplaceFilter) UnicastRemoteObject.exportObject(filter, 1099);
            Registry r = LocateRegistry.createRegistry(1099);
            r.rebind("LaplaceFilter", stub);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
