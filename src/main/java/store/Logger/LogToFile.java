package store.Logger;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogToFile {


    String location;


    protected static final Logger logger = Logger.getLogger("MYLOG");

    public LogToFile(String location) {
        this.location = location;
    }

    /**
     * log Method
     * enable to log all exceptions to a file and display user message on demand
     *
     * @param ex
     * @param level
     * @param msg
     */
    public void log(Exception ex, String level, String msg) {

        FileHandler fh = null;
        try {
            fh = new FileHandler(location + "/log.txt", true);
            logger.addHandler(fh);
            switch (level) {
                case "severe":
                    logger.log(Level.SEVERE, msg, ex);
                    if (!msg.equals(""))
                        JOptionPane.showMessageDialog(null, msg,
                                "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                case "warning":
                    logger.log(Level.WARNING, msg, ex);
                    if (!msg.equals(""))
                        JOptionPane.showMessageDialog(null, msg,
                                "Warning", JOptionPane.WARNING_MESSAGE);
                    break;
                case "info":
                    logger.log(Level.INFO, msg);

                    //if (!msg.equals(""))
//                        JOptionPane.showMessageDialog(null, msg,
//                                "Info", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case "config":
                    logger.log(Level.CONFIG, msg, ex);
                    break;
                case "fine":
                    logger.log(Level.FINE, msg, ex);
                    break;
                case "finer":
                    logger.log(Level.FINER, msg, ex);
                    break;
                case "finest":
                    logger.log(Level.FINEST, msg, ex);
                    break;
                default:
                    logger.log(Level.CONFIG, msg, ex);
                    break;
            }
        } catch (SecurityException | IOException ex1) {
            logger.log(Level.SEVERE, null, ex1);
        } finally {
            if (fh != null) fh.close();
        }
    }

    public  void writeLog(String info, String message,Exception ex) {
        String filename = "/activity.log";
        String FILENAME = location + filename;
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            fw = new FileWriter(FILENAME, true);
            bw = new BufferedWriter(fw);
            if (info.equals("info")) {
                bw.write("INFO: ");
            } else if (info.equals("error") || ex!=null) {
                bw.write("ERROR: ");
            }
            bw.write(message);
            bw.write("\n");

            if(ex!=null){
                if(ex.getMessage()!=null) {
                    bw.write(ex.getMessage());
                    bw.write("\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();
            } catch (IOException exp) {
                exp.printStackTrace();
            }
        }


    }
}