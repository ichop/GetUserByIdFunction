package com.rd.project;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.rd.project.model.User;
import com.rd.project.service.UserService;


public class Handler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {


    private static UserService userService;


    static {
        userService = new UserService();
    }


    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent requestEvent, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);
        try {

            Gson gson = new Gson();

            Integer userId = Integer.valueOf(requestEvent.getPathParameters().get("id"));

            User user = userService.getUserById(userId);

            String output = gson.toJson(user);

            return response
                    .withStatusCode(200)
                    .withBody(output);
        } catch (Exception e) {
            return response
                    .withBody(e.getMessage())
                    .withStatusCode(500);
        }
    }
}
