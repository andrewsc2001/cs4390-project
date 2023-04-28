import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;

public class Logger {
    private static final Boolean writeToConsole = true;
    private String fname;

    /**
     * Restricts the output of the logger.
     * When set to Logger.DEBUG, DEBUG, INFO, and ERROR messages will be logged.
     * When set to Logger.INFO, INFO and ERROR messages will be logged and so on.
     */
    private int logLevel;
    public static final int DEBUG = 0;
    public static final int INFO = 1;
    public static final int ERROR = 2;

    /**
     * 
     * @param fname
     * @param logLevel
     * @throws IOException
     */
    public Logger(
            String fname,
            int logLevel) throws IOException {
        this.fname = fname;
        this.logLevel = logLevel;
    }

    /**
     * 
     * @param line String to log
     *             Opens file, writes formatted log message, closes file
     */
    public void d(String line) {
        try (FileWriter out = new FileWriter(fname, true)) {
            if (logLevel <= DEBUG) {
                try {
                    out.write(ZonedDateTime.now().toString() + "\tDEBUG:\t" + line + "\n");
                    if (writeToConsole)
                        System.out.println(ZonedDateTime.now().toString() + "\tDEBUG:\t" + line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param line String to log
     *             Opens file, writes formatted log message, closes file
     */
    public void i(String line) {
        try (FileWriter out = new FileWriter(fname, true)) {
            if (logLevel <= INFO) {
                try {
                    out.write(ZonedDateTime.now().toString() + "\tINFO:\t" + line + "\n");
                    if (writeToConsole)
                        System.out.println(ZonedDateTime.now().toString() + "\tINFO:\t" + line);
                } catch (IOException e) {
                    System.out.println("EEEEEEEE");
                    e.printStackTrace();
                }
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param line String to log
     *             Opens file, writes formatted log message, closes file
     */
    public void e(String line) {
        try (FileWriter out = new FileWriter(fname, true)) {
            if (logLevel <= ERROR) {
                try {
                    out.write(ZonedDateTime.now().toString() + "\tERROR:\t" + line + "\n");
                    if (writeToConsole)
                        System.out.println(ZonedDateTime.now().toString() + "\tERROR:\t" + line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
