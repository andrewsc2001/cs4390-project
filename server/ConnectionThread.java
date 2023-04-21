import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.time.Duration;
import java.time.Instant;

public class ConnectionThread extends Thread {

    private Socket socket;
    private Logger log;
    private Instant attached;

    public ConnectionThread(Socket socket, Logger log) {
        this.socket = socket;
        this.log = log;
        this.start();
    }

    @Override
    public void run() {
        try {
            log.d("Connection established");
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            log.d("Waiting for client to give name");
            String name = in.readLine();
            log.d("Client has chosen name " + name);
            out.writeBytes(name + "\n");
            out.flush();
            log.i("[" + name + "] Attached from " + socket.getRemoteSocketAddress());
            attached = Instant.now();

            while (true) {
                String line = in.readLine();
                log.d("[" + name + "] Recieved \"" + line + "\"");

                if (line.equals("CLOSE")) {
                    log.i("[" + name + "] Closed the connection, lifetime "
                            + Duration.between(attached, Instant.now()));
                    socket.close();
                    return;
                }

                String[] args = line.split(" ");

                if (args.length != 3) {
                    log.e("[" + name + "] sent an invalid calculation request \"" + line + "\"");
                    out.writeBytes("ERROR\n");
                    out.flush();
                    continue;
                }

                String verb = args[0];
                float op1, op2;
                try {
                    op1 = Float.parseFloat(args[1]);
                    op2 = Float.parseFloat(args[2]);
                } catch (NumberFormatException e) {
                    log.e("[" + name + "] sent a calculation request with improperly formatted operands \"" + line
                            + "\"");
                    out.writeBytes("ERROR\n");
                    out.flush();
                    continue;
                }

                float result;
                if (verb.equals("ADD")) {
                    result = op1 + op2;
                    log.i("[" + name + "] Calculated " + op1 + " + " + op2 + " = " + result);
                } else if (verb.equals("SUB")) {
                    result = op1 - op2;
                    log.i("[" + name + "] Calculated " + op1 + " - " + op2 + " = " + result);
                } else if (verb.equals("MUL")) {
                    result = op1 * op2;
                    log.i("[" + name + "] Calculated " + op1 + " * " + op2 + " = " + result);
                } else if (verb.equals("DIV")) {
                    if (op2 == 0) {
                        log.e("[" + name + "] Attempted to divide " + op1 + " by 0");
                        out.writeBytes("ERROR\n");
                        out.flush();
                        continue;
                    }
                    result = op1 / op2;
                    log.i("[" + name + "] Calculated " + op1 + " / " + op2 + " = " + result);
                } else {
                    log.e("[" + name + "] sent request with invalud verb \"" + verb + "\"");
                    out.writeBytes("ERROR\n");
                    out.flush();
                    continue;
                }

                out.writeBytes(Float.toString(result) + "\n");
                out.flush();
                log.d("[" + name + "] Wrote result back to client");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
