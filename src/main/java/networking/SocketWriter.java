package networking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class SocketWriter implements Runnable {
    private final Object lock;
    private final Object message;
    private final OutputStream outputStream;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public SocketWriter(Object lock, Object message, OutputStream outputStream) {
        this.lock = lock;
        this.message = message;
        this.outputStream = outputStream;
    }

    public Object getLock() {
        return lock;
    }

    public Object getMessage() {
        return message;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    @Override
    public void run() {
        synchronized (getLock()) {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(getOutputStream());
                oos.writeObject(getMessage());
                oos.flush();
            } catch (IOException e) {
                logger.error("failed to send message '{}'",getMessage(),e);
            }
        }
    }
}
