package Team6.services;

import java.io.IOException;
import java.util.logging.*;

/**
 * Holds details about the logger.
 *
 * @author Team 6
 * @version 2020.04.24
 */
public class AppLogger {
    private static Logger logger;
    private static Handler handler;

    /**
     * Constructor that creates an instance of the AppLogger, initialising the instance.
     */
    private AppLogger(){
    }

    /**
     * Returns the logger.
     * If the file handler can't create a new object, a
     * {@link IOException} will be thrown.
     *
     * @return The logger.
     */
    public static Logger getAppLogger() {
        try{
            if(logger == null){
                logger = Logger.getLogger(AppLogger.class.getName());
                handler = new FileHandler("log.txt", true);
                handler.setFormatter(new SimpleFormatter());
                logger.addHandler(handler);
                logger.setLevel(Level.ALL);
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
        return logger;
    }

    /**
     * Closes the file handler.
     */
    public static void closeHandler(){
        handler.close();
    }
}
