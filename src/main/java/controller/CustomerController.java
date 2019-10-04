package controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Customer;
import storage.CustomerStorage;

import java.util.List;
import java.util.Map;

import static fi.iki.elonen.NanoHTTPD.*;
import static fi.iki.elonen.NanoHTTPD.Response.Status.*;

public class CustomerController {

    private final static String CUSTOMER_ID_PARAM_NAME = "customer_id";

    private CustomerStorage customerStorage = new CustomerStorage();

    public Response serveGetCustomerRequest(IHTTPSession session) {

        Map<String, List<String>> requestParameters = session.getParameters();
        if (requestParameters.containsKey(CUSTOMER_ID_PARAM_NAME)) {
            List<String> customerIdParams = requestParameters.get(CUSTOMER_ID_PARAM_NAME);
            String customerIdParam = customerIdParams.get(0);
            long customerId;
            try {
                 customerId = Long.parseLong(customerIdParam);

            } catch (NumberFormatException nfe) {
                System.err.println("Error during convert request param: \n" + nfe);
                return newFixedLengthResponse(BAD_REQUEST, "text/plain", "Request param 'customerId' have to be number");
            }


            Customer customer = customerStorage.getCustomer(customerId);
            if (customer != null) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    String response = objectMapper.writeValueAsString(customer);
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

    public Response serveGetCustomersRequest(IHTTPSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        String response;
        try {
            response = objectMapper.writeValueAsString(customerStorage.getAllCustomers());

        } catch (JsonProcessingException e) {
            System.err.println("Error during process request: \n" + e);
            return newFixedLengthResponse(INTERNAL_ERROR, "text_plain", "Internal error: can't read all customers");
        }

        return newFixedLengthResponse(OK, "application/json", response);
    }

    public Response serveAddCustomerRequest(IHTTPSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        long randomCustomerId = System.currentTimeMillis();
        String lengthHeader = session.getHeaders().get("content-length");

        int contentLength = Integer.parseInt(lengthHeader);
        byte[] buffer = new byte[contentLength];
        try {

            session.getInputStream().read(buffer, 0, contentLength);

            String requestBody = new String(buffer).trim();
            Customer requestCustomer = objectMapper.readValue(requestBody, Customer.class);

            requestCustomer.setCustomerId(randomCustomerId);

            customerStorage.addCustomer(requestCustomer);

        } catch (Exception e) {
            System.err.println("Error during process request: \n" + e);
            return newFixedLengthResponse(INTERNAL_ERROR, "text/plain", "Internal error: customer hasn't been added");
        }
        return newFixedLengthResponse(OK, "application/json", "Customer added ");
    }
}