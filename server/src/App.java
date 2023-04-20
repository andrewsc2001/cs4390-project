import java.io.IOException;
import java.net.ServerSocket;

public class App {
    public static final int DEFAULT_PORT = 25565;
    public static void main(String[] args) {
        if(args.length > 2) {
            System.err.println("Usage " + args[0] + " [port?]");
            System.exit(1);
        }

        int port = DEFAULT_PORT;

        if(args.length == 2) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (Exception e) {
                System.err.println("Usage " + args[0] + " [port?]");
            }
        }

        ServerSocket listener;

        try {
            listener = new ServerSocket(port);
            while(true) {
                new ConnectionThread(listener.accept());
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
