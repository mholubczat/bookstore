package controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Order;
import models.OrderItem;
import storage.Database;

import java.util.List;
import java.util.Map;

import static fi.iki.elonen.NanoHTTPD.*;
import static fi.iki.elonen.NanoHTTPD.Response.Status.*;

public class OrderController {

    private final static String ORDER_ID_PARAM_NAME = "order_id";

    private Database database = new Database();

    public Response serveGetOrderRequest(IHTTPSession session) {

        Map<String, List<String>> requestParameters = session.getParameters();
        if (requestParameters.containsKey(ORDER_ID_PARAM_NAME)) {
            List<String> orderIdParams = requestParameters.get(ORDER_ID_PARAM_NAME);
            String orderIdParam = orderIdParams.get(0);
            long orderId;
            try {
                orderId = Long.parseLong(orderIdParam);

            } catch (NumberFormatException nfe) {
                System.err.println("Error during convert request param: \n" + nfe);
                return newFixedLengthResponse(BAD_REQUEST, "text/plain", "Request param 'orderId' have to be number");
            }


            Order order = database.getOrder(orderId);
            if (order != null) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    String response = objectMapper.writeValueAsString(order);
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

    public Response serveGetOrdersRequest(IHTTPSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        String response;
        try {
            response = objectMapper.writeValueAsString(database.getAllOrders());

        } catch (JsonProcessingException e) {
            System.err.println("Error during process request: \n" + e);
            return newFixedLengthResponse(INTERNAL_ERROR, "text_plain", "Internal error: can't read all orders");
        }

        return newFixedLengthResponse(OK, "application/json", response);
    }

    public Response serveAddOrderRequest(IHTTPSession session) {
        ObjectMapper objectMapper = new ObjectMapper();
        long randomOrderId = System.currentTimeMillis();
        String lengthHeader = session.getHeaders().get("content-length");

        int contentLength = Integer.parseInt(lengthHeader);
        byte[] buffer = new byte[contentLength];
        try {

            session.getInputStream().read(buffer, 0, contentLength);

            String requestBody = new String(buffer).trim();
            Order requestOrder = objectMapper.readValue(requestBody, Order.class);

            requestOrder.setOrderId(randomOrderId);

            database.addOrder(requestOrder);

        } catch (Exception e) {
            System.err.println("Error during process request: \n" + e);
            return newFixedLengthResponse(INTERNAL_ERROR, "text/plain", "Internal error: order hasn't been added");
        }
        return newFixedLengthResponse(OK, "application/json", "Order added ");
    }

    public Response serveAddOrderItemRequest(IHTTPSession session) {
        Map<String, List<String>> requestParameters = session.getParameters();
        if(requestParameters.containsKey(ORDER_ID_PARAM_NAME)) {
            List<String> orderIdParams = requestParameters.get(ORDER_ID_PARAM_NAME);
            String orderIdParam = orderIdParams.get(0);

        long orderId;
        try {
            orderId = Long.parseLong(orderIdParam);

        } catch (NumberFormatException nfe) {
            System.err.println("Error during convert request param: \n" + nfe);
            return newFixedLengthResponse(BAD_REQUEST, "text/plain", "Request param 'orderId' have to be number");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        long randomOrderItemId = System.currentTimeMillis();
        String lengthHeader = session.getHeaders().get("content-length");

        int contentLength = Integer.parseInt(lengthHeader);
        byte[] buffer = new byte[contentLength];
        try {

            session.getInputStream().read(buffer, 0, contentLength);

            String requestBody = new String(buffer).trim();
            OrderItem requestOrder = objectMapper.readValue(requestBody, OrderItem.class);

            requestOrder.setOrderItemId(randomOrderItemId);

            database.addOrderItem(orderId,requestOrder);

        } catch (Exception e) {
            System.err.println("Error during process request: \n" + e);
            return newFixedLengthResponse(INTERNAL_ERROR, "text/plain", "Internal error: order hasn't been added");
        }
        return newFixedLengthResponse(OK, "application/json", "Order added ");
    }return newFixedLengthResponse(BAD_REQUEST, "text/plain", "Uncorrected request params");
    }

}