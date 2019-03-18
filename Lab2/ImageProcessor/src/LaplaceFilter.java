import java.io.IOException;
import java.rmi.Remote;

public interface LaplaceFilter extends Remote {
    byte[] transform(byte[] image) throws IOException;
}
