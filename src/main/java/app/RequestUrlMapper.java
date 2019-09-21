
package app;

import controller.BookController;
import fi.iki.elonen.NanoHTTPD;
import static fi.iki.elonen.NanoHTTPD.Method.GET;
import static fi.iki.elonen.NanoHTTPD.Method.POST;
import static fi.iki.elonen.NanoHTTPD.Response.Status.NOT_FOUND;

public class RequestUrlMapper {
    private static final String ADD_BOOK_URL = "/books/add";
    private static final String GET_BOOK_URL = "/books/get";
    private static final String GET_ALL_BOOK_URL = "/books/getAll";
    private BookController bookController = new BookController();
    public NanoHTTPD.Response delegateRequest(NanoHTTPD.IHTTPSession session){
        if(GET.equals(session.getMethod())&& GET_BOOK_URL.equals(session.getUri())){
            return bookController.serveGetBookRequest(session);
        }
        else if(GET.equals(session.getMethod())&& GET_ALL_BOOK_URL.equals(session.getUri())){
            return bookController.serveGetBooksRequest(session);
        }
        else if(POST.equals(session.getMethod())&& ADD_BOOK_URL.equals(session.getUri())){
            return bookController.serveAddBookRequest(session);

        }
        return NanoHTTPD.newFixedLengthResponse(NOT_FOUND, "text/plain","Not Found");
    }

}