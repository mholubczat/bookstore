package controller;
public class BookController {
/*
package controller;

import fi.iki.elonen.NanoHTTPD;

import java.util.List;
import java.util.Map;

public class BookController {
    private final static String BOOK_ID_PARAM_NAME = "book_id";

    private BookStorage bookStorage = new PostgresListBookStorageImpl();

    public NanoHTTPD.Response serveGetBookRequest(NanoHTTPD.IHTTPSession session) {

        Map<String, List<String>> requestParameters = session.getParameters();
        if (requestParameters.containsKey(BOOK_ID_PARAM_NAME)) {
            List<String> bookIdParams = requestParameters.get(BOOK_ID_PARAM_NAME);
            String bookIdParam = bookIdParams.get(0);
            long bookId = 0;
            try {
                bookId = Long.parseLong(bookIdParam);

            } catch (NumberFormatException nfe) {
                System.err.println("Error during convert request param: \n" + nfe);
                return newFixedLengthResponse(BAD_REQUEST, "text/plain", "Request param 'bookId' have to be number");
            }
            System.out.println(bookId);
            Book book = bookStorage.getBook(bookId);
            if (book != null) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    String response = objectMapper.writeValueAsString(book);
                    return newFixedLengthResponse(OK, "application/json", response);
                } catch (JsonProcessingException e) {

                    System.err.println("Error during  process request: \n" + e);
                    return newFixedLengthResponse(INTERNAL_ERROR, "text/plain", "Unhandled request params");

                }
            }
            return newFixedLengthResponse(NOT_FOUND, "application/json", "");
        }
        return newFixedLengthResponse(BAD_REQUEST, "text/plain", "Uncorrected request params");
    }
    public Response serveGetBooksRequest (IHTTPSession session){
        ObjectMapper objectMapper = new ObjectMapper();
        String response = "";
        try {
            response = objectMapper.writeValueAsString(bookStorage.getAllBooks());

        } catch (JsonProcessingException e) {
            System.err.println("Error during process request: \n" + e);
            return newFixedLengthResponse(INTERNAL_ERROR, "text_plain", "Internal error: can't read all books");
        }

        return newFixedLengthResponse(OK, "application/json", response);
    }

    public Response serveAddBookRequest (IHTTPSession session){
        ObjectMapper objectMapper = new ObjectMapper();
        long randomBookId = System.currentTimeMillis();
        String lengthHeader = session.getHeaders().get("content-length");

        int contentLength = Integer.parseInt(lengthHeader);
        byte[] buffer = new byte[contentLength];
        try {
            System.out.println(1);
            session.getInputStream().read(buffer, 0, contentLength);
            System.out.println(2);
            String requestBody = new String(buffer).trim();
/// nastepna linia powoduje walek
            Book requestBook = objectMapper.readValue(requestBody, Book.class);

            requestBook.setId(randomBookId);

            bookStorage.addBook(requestBook);

        } catch (Exception e) {
            System.err.println("Error during process request: \n" + e);
            return newFixedLengthResponse(INTERNAL_ERROR, "text/plain", "Internal error: book hasn't been added");
        }

        return newFixedLengthResponse(OK, "application/json", "Book added ");


    }

}
*/
}
