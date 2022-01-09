import frontend.Gui;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String [] args) throws IOException, InterruptedException {
        System.out.println("Hello World");
        logger.info("Also Hello");

        new Gui();


        //2 threads for backend (later networking) and frontend should be spawned here
    }
}
