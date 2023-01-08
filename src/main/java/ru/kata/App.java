package ru.kata;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.kata.configuration.MyConfig;
import ru.kata.model.User;

import java.util.Arrays;

public class App {
    private static final String GET_USERS_ENDPOINT_URL = "http://94.198.50.185:7081/api/users";
    private static final String CREATE_USER_ENDPOINT_URL = "http://94.198.50.185:7081/api/users";
    private static final String UPDATE_USER_ENDPOINT_URL = "http://94.198.50.185:7081/api/users";
    private static final String DELETE_USER_ENDPOINT_URL = "http://94.198.50.185:7081/api/users/{id}";
    private static RestTemplate restTemplate;
    private static HttpHeaders httpHeaders;
    private static String resultBody;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(MyConfig.class);
        restTemplate = context.getBean("getRestTemplate", RestTemplate.class);
        httpHeaders = context.getBean("httpHeaders", HttpHeaders.class);
        getUsers();
        createUser();
        updateUser();
        deleteUser();
        context.close();
    }

    private static void getUsers() {
        ResponseEntity<String> response = restTemplate.exchange(GET_USERS_ENDPOINT_URL, HttpMethod.GET, new HttpEntity<>(httpHeaders), String.class);
        httpHeaders.add("Cookie", response.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
    }

    private static void createUser() {
        User user = new User(3L, "James", "Brown", (byte) 10);
        resultBody = restTemplate.exchange(CREATE_USER_ENDPOINT_URL, HttpMethod.POST, new HttpEntity<>(user, httpHeaders), String.class).getBody();
        System.out.println("1 part of result = " + resultBody);
    }

    private static void updateUser() {
        User updatedUser = new User(3L, "Thomas", "Shelby", (byte) 10);
        resultBody += restTemplate.exchange(UPDATE_USER_ENDPOINT_URL, HttpMethod.PUT, new HttpEntity<>(updatedUser, httpHeaders), String.class).getBody();
        System.out.println("1 and 2 parts of result = " + resultBody);
    }

    private static void deleteUser() {
        resultBody += restTemplate.exchange(DELETE_USER_ENDPOINT_URL.replace("{id}", "3"), HttpMethod.DELETE, new HttpEntity<>(httpHeaders), String.class).getBody();
        System.out.println("Full result = " + resultBody);
    }
}
