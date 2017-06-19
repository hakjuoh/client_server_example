package example;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utility {

    public static String readString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int readSize = inputStream.read();

        byte[] buf = new byte[4 * 1024];
        int read = 0;
        while (readSize > 0 && (read = inputStream.read(buf, 0, Math.min(readSize, buf.length))) != -1) {
            byteArrayOutputStream.write(buf, 0, read);
            readSize -= read;
        }

        return byteArrayOutputStream.toString();
    }
}
