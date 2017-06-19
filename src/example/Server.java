package example;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server implements DateService {

    @Override
    public String getCurrentDate() throws ServiceException {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    private int port;

    public Server(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        ServerSocketFactory serverSocketFactory = ServerSocketFactory.getDefault();
        ServerSocket serverSocket = serverSocketFactory.createServerSocket(port);
        serverSocket.setReuseAddress(true);

        System.out.println("[DEBUG] Opened the server port " + port);

        while (true) {
            System.out.println("[DEBUG] Waiting for request...");
            Socket socket = serverSocket.accept();
            System.out.println("[DEBUG] Request accepted: " + socket);

            ServiceException serviceException = null;
            try {
                String req = null;
                try {
                    InputStream inputStream = socket.getInputStream();
                    req = Utility.readString(inputStream);
                } catch (IOException e) {
                    serviceException = new ServiceException("I/O error occurs during the request data is reading", e);
                }

                System.out.println("[DEBUG] Received the request: " + req);

                String resp = null;
                switch (req) {
                    case "getCurrentDate":
                        try {
                            resp = getCurrentDate();
                        } catch (ServiceException e) {
                            serviceException = e;
                        }
                        break;

                    default:
                        serviceException = new ServiceException("Unknown request command: " + req);
                }

                String status;
                if (serviceException != null) {
                    status = "ERROR";
                    resp = serviceException.getMessage();
                } else {
                    status = "OK";
                }

                OutputStream outputStream = socket.getOutputStream();

                Utility.writeString(outputStream, status);
                Utility.writeString(outputStream, resp);

                outputStream.flush();

                System.out.println("[DEBUG] Send the response [" + status + "]: " + resp);
            } finally {
                socket.close();
            }
        }
    }


    public static void main(String[] args) throws IOException {
        int port = 8080;
        Server server = new Server(port);
        server.start();
    }
}
