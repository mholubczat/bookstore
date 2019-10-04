
package app;

import controller.BookController;
import controller.CustomerController;
import fi.iki.elonen.NanoHTTPD;
import models.Customer;

import static fi.iki.elonen.NanoHTTPD.Method.GET;
import static fi.iki.elonen.NanoHTTPD.Method.POST;
import static fi.iki.elonen.NanoHTTPD.Response.Status.NOT_FOUND;

public class RequestUrlMapper {
    private static final String ADD_BOOK_URL = "/books/add";
    private static final String GET_BOOK_URL = "/books/get";
    private static final String GET_ALL_BOOK_URL = "/books/getAll";
    private static final String ADD_CUSTOMER_URL = "/customers/add";
    private static final String GET_CUSTOMER_URL = "/customers/get";
    private static final String GET_ALL_CUSTOMERS_URL = "/customers/getAll";
    private BookController bookController = new BookController();
    private CustomerController customerController = new CustomerController();

    public NanoHTTPD.Response delegateRequest(NanoHTTPD.IHTTPSession session) {
        if (GET.equals(session.getMethod()) && GET_BOOK_URL.equals(session.getUri())) {
            return bookController.serveGetBookRequest(session);
        } else if (GET.equals(session.getMethod()) && GET_ALL_BOOK_URL.equals(session.getUri())) {
            return bookController.serveGetBooksRequest(session);
        } else if (POST.equals(session.getMethod()) && ADD_BOOK_URL.equals(session.getUri())) {
            return bookController.serveAddBookRequest(session);
        } else if (GET.equals(session.getMethod()) && GET_CUSTOMER_URL.equals(session.getUri())) {
            return customerController.serveGetCustomerRequest(session);
        } else if (GET.equals(session.getMethod()) && GET_ALL_CUSTOMERS_URL.equals(session.getUri())) {
            return customerController.serveGetCustomersRequest(session);
        } else if (POST.equals(session.getMethod()) && ADD_CUSTOMER_URL.equals(session.getUri())) {
            return customerController.serveAddCustomerRequest(session);

        }
        return NanoHTTPD.newFixedLengthResponse(NOT_FOUND, "text/plain", "Not Found");
    }

}