import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    public static final int DEFAULT_PORT = 25565;
    public static final String LOG_FILE = "./log.txt";
    public static final int LOG_LEVEL = Logger.DEBUG;

    private static void start(int port, Logger log) {
        ServerSocket listener;

        try {
            listener = new ServerSocket(port);
            log.i("Listening for connections on port " + port);
            while (true) {
                new ConnectionThread(listener.accept(), log);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Logger log;
        try {
            log = new Logger(LOG_FILE, LOG_LEVEL);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        if (args.length > 1) {
            System.err.println("Usage server [port?]");
            System.exit(1);
        }

        int port = DEFAULT_PORT;

        if (args.length == 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Usage server [port?]");
            }
        }

        start(port, log);
    }
}
