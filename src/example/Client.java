package example;

import javax.net.SocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client implements DateService {

    @Override
    public Date getCurrentDate() throws ServiceException {
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
                outputStream.write(commandName.length());
                outputStream.write(commandName.getBytes());
                outputStream.flush();

                System.out.println("[DEBUG] Send the request: " + commandName);
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
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    return dateFormat.parse(resp);
                } catch (ParseException e) {
                    throw new ServiceException("Invalid response data: " + resp, e);
                }
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
        Client client = new Client("localhost", 8080);
        Date date = client.getCurrentDate();
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
    }
}
