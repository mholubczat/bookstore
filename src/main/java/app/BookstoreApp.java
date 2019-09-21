package app;

import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;

public class BookstoreApp extends NanoHTTPD {


        private BookstoreApp(int port) throws IOException {
            super(port);
            start(5000, false);
            System.out.println("Server has been started");
        }

        public static void main(String[] args) {

            try {
                new BookstoreApp(8080);
            } catch (IOException e) {
                System.err.println("Server can't start due to an error: \n" + e);
            }
        }

    }

