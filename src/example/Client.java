package example;

import javax.net.SocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client implements DateService {

    @Override
    public String getCurrentDate(String dateFormat) throws ServiceException {
        SocketFactory socketFactory = SocketFactory.getDefault();
        Socket socket = null;
        try {
            try {
                socket = socketFactory.createSocket(hostname, port);
                System.out.println("[DEBUG] Created a connection to <" + hostname + ">:<" + port + ">");
            } catch (IOException e) {
                throw new ServiceException("Can't establish a connection to <" + hostname + ">:<" + port + ">", e);
            }

            try {
                OutputStream outputStream = socket.getOutputStream();

                String commandName = "getCurrentDate";
                Utility.writeString(outputStream, commandName);
                Utility.writeString(outputStream, dateFormat);

                outputStream.flush();

                System.out.println("[DEBUG] Send the request: " + commandName + "(" + dateFormat + ")");
            } catch (IOException e) {
                throw new ServiceException("I/O error occurs during the request data is writing", e);
            }

            String status;
            String resp;
            try {
                InputStream inputStream = socket.getInputStream();

                status = Utility.readString(inputStream);
                resp = Utility.readString(inputStream);

            } catch (IOException e) {
                throw new ServiceException("I/O error occurs during the response data is reading", e);
            }

            System.out.println("[DEBUG] Received the response [" + status + "]: " + resp);

            if ("OK".equals(status)) {
                return resp;
            } else {
                throw new ServiceException(resp);
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new ServiceException("Unexpected I/O error occurs during the socket is closing", e);
                }
            }
        }
    }

    private String hostname;
    private int port;

    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public static void main(String[] args) throws ServiceException {
        Client client = new Client("localhost", 8888);
        String date = client.getCurrentDate("yyyy-MM-dd HH:mm:ss");
        System.out.println(date);
    }
}
