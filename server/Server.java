import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    public static final int DEFAULT_PORT = 25565;
    public static final String LOG_FILE = "./log.txt";
    public static final int LOG_LEVEL = Logger.DEBUG;

    /**
     * 
     * @param port
     * @param log
     * 
     */
    private static void start(int port, Logger log) {

        // Open ServerSocket and send client sockets to new threads.
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

        // Start logger
        Logger log;
        try {
            log = new Logger(LOG_FILE, LOG_LEVEL);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Arg checking
        if (args.length > 1) {
            System.err.println("Usage server [port?]");
            System.exit(1);
        }

        // Parse port

        int port = DEFAULT_PORT;

        if (args.length == 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Usage server [port?]");
            }
        }

        // Start server

        start(port, log);
    }
}
