package controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Book;
import storage.BookStorage;

import java.util.List;
import java.util.Map;

import static fi.iki.elonen.NanoHTTPD.*;
import static fi.iki.elonen.NanoHTTPD.Response.Status.*;

public class BookController {

    private final static String BOOK_ID_PARAM_NAME = "book_id";


    private BookStorage bookStorage = new BookStorage();

    public Response serveGetBookRequest(IHTTPSession session) {

        Map<String, List<String>> requestParameters = session.getParameters();
        if (requestParameters.containsKey(BOOK_ID_PARAM_NAME)) {
            List<String> bookIdParams = requestParameters.get(BOOK_ID_PARAM_NAME);
            String bookIdParam = bookIdParams.get(0);

            try {
               long bookId = Long.parseLong(bookIdParam);

            } catch (NumberFormatException nfe) {
                System.err.println("Error during convert request param: \n" + nfe);
                return newFixedLengthResponse(BAD_REQUEST, "text/plain", "Request param 'bookId' have to be number");
            }

            long bookId = 0;
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

            session.getInputStream().read(buffer, 0, contentLength);

            String requestBody = new String(buffer).trim();
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