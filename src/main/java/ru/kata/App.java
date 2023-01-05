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
    private static String session;
    private static String resultBody;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(MyConfig.class);
        restTemplate = context.getBean("getRestTemplate", RestTemplate.class);
        getUsers();
        createUser();
        updateUser();
        deleteUser();
        context.close();
    }

    private static void getUsers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(GET_USERS_ENDPOINT_URL, HttpMethod.GET, entity, String.class);
        session = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
    }

    private static void createUser() {
        User user = new User(3L, "James", "Brown", (byte) 10);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Cookie", session);
        HttpEntity<User> entity = new HttpEntity<>(user, headers);
        resultBody = restTemplate.exchange(CREATE_USER_ENDPOINT_URL, HttpMethod.POST, entity, String.class).getBody();
        System.out.println("1 part of result = " + resultBody);
    }

    private static void updateUser() {
        User updatedUser = new User(3L, "Thomas", "Shelby", (byte) 10);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Cookie", session);
        HttpEntity<User> entity = new HttpEntity<>(updatedUser, headers);
        resultBody += restTemplate.exchange(UPDATE_USER_ENDPOINT_URL, HttpMethod.PUT, entity, String.class).getBody();
        System.out.println("1 and 2 parts of result = " + resultBody);
    }

    private static void deleteUser() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Cookie", session);
        HttpEntity<User> entity = new HttpEntity<>(headers);
        resultBody += restTemplate.exchange(DELETE_USER_ENDPOINT_URL.replace("{id}", "3"), HttpMethod.DELETE, entity, String.class).getBody();
        System.out.println("Full result = " + resultBody);
    }
}
