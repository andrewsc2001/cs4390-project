import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    /**
     * 
     * @param name The users name
     * @param addr The InetAddress to connect to
     * @param port The port to connect on
     */
    private static void start(String name, InetAddress addr, int port) {

        // Attempt connection, exit if failed
        Socket socket;
        try {
            socket = new Socket(addr, port);
        } catch (IOException e) {
            System.err.println("Connection Failed");
            return;
        }

        try {
            // Initialize streams
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            String req = "", res = "";

            // Send name to server
            out.writeBytes(name + "\n");
            out.flush();

            // Recieve echoed name and compare. If mismatched, close and exit.
            res = in.readLine();
            if (!name.equals(res)) {
                System.err.println("Server did not recieve correct name, returned \"" + res + "\" instead, exiting");
                out.writeBytes("CLOSE\n");
                out.flush();
                System.exit(0);
            }

            System.out.println("Connected to server");

            // Loop over user input
            while (true) {
                req = userInput.readLine();

                // Write user input to server
                out.writeBytes(req + "\n");
                out.flush();

                // If server closes connection, exit
                if (req.equals("CLOSE")) {
                    System.out.println("Server closed connection.");
                    break;
                }

                // Print response
                res = in.readLine();
                System.out.println(res);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Failed to close socket.");
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        // Arg checking
        if (args.length != 3) {
            System.err.println("Usage client [name] [hostname] [port]");
            System.exit(1);
        }
        // Parse name
        String name = args[0];

        // Parse hostname and convert to InetAddress
        InetAddress addr = null;
        try {
            addr = InetAddress.getByName(args[1]);
        } catch (UnknownHostException e) {
            System.err.println("Could not determine IP address of " + args[1]);
            e.printStackTrace();
            System.exit(1);
        }

        int port = 0;

        // Parse port
        try {
            port = Integer.parseInt(args[2]);
            if (port < 1 || port > 35565) {
                System.err.println(port + " is an invalid port number");
                System.exit(1);
            }
        } catch (NumberFormatException e) {
            System.err.println(args[2] + " is not a number");
            System.exit(1);
        }

        // Start client
        start(name, addr, port);
    }
}
