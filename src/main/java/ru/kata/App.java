package ru.kata;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.kata.configuration.MyConfig;
import ru.kata.model.User;

import java.util.Arrays;

public class App {
    private static final String USERS_ENDPOINT_URL = "http://94.198.50.185:7081/api/users";
    private static RestTemplate restTemplate;
    private static HttpHeaders httpHeaders;
    private static String resultBody;
    private static User user;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(MyConfig.class);
        restTemplate = context.getBean("getRestTemplate", RestTemplate.class);
        httpHeaders = context.getBean("httpHeaders", HttpHeaders.class);
        getUsers();
        createUser();
        updateUser();
        deleteUser();
        System.out.println("result = " + resultBody);
        context.close();
    }

    private static void getUsers() {
        httpHeaders.add("Cookie", restTemplate
                .exchange(USERS_ENDPOINT_URL, HttpMethod.GET, new HttpEntity<>(httpHeaders), String.class).getHeaders().getFirst(HttpHeaders.SET_COOKIE));
    }

    private static void createUser() {
        user = new User(3L, "James", "Brown", (byte) 10);
        resultBody = restTemplate.exchange(USERS_ENDPOINT_URL, HttpMethod.POST, new HttpEntity<>(user, httpHeaders), String.class).getBody();
    }

    private static void updateUser() {
        user.setName("Thomas");
        user.setLastName("Shelby");
        resultBody += restTemplate.exchange(USERS_ENDPOINT_URL, HttpMethod.PUT, new HttpEntity<>(user, httpHeaders), String.class).getBody();
    }

    private static void deleteUser() {
        resultBody += restTemplate.exchange(USERS_ENDPOINT_URL + "/3", HttpMethod.DELETE, new HttpEntity<>(httpHeaders), String.class).getBody();
    }
}
