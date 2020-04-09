package services;

import java.io.IOException;
import java.util.logging.*;

public class PicLdLogger {
    private final Logger logger = Logger.getLogger(PicLdLogger.class.getName());

    public PicLdLogger() throws IOException {
        Handler handler = new FileHandler("text.txt", true);
        handler.setFormatter(new SimpleFormatter());
        logger.addHandler(handler);
        logger.setLevel(Level.ALL);
    }

    public Logger getLogger() {
        return logger;
    }
}
