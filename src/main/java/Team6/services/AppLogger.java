package Team6.services;

import java.io.IOException;
import java.util.logging.*;

public class AppLogger {
    private static Logger logger;
    private static Handler handler;

    private AppLogger(){

    }

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
    public static void closeHandler(){
        handler.close();
    }
}
