package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.kata.spring.boot_security.demo.models.User;

@Service
public class ApiService {

    private final String API_URL = "http://94.198.50.185:7081/api/users";
    private String sessionId = "";

    private final RestTemplate restTemplate;

    // Внедрение RestTemplate через конструктор
    public ApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String executeOperations() {
        StringBuilder finalCode = new StringBuilder();

        // Шаг 1: Получить список всех пользователей (GET)
        ResponseEntity<String> response = restTemplate.getForEntity(API_URL, String.class);
        sessionId = response.getHeaders().getFirst("Set-Cookie");
        System.out.println("Session ID: " + sessionId);

        // Шаг 2: Создать нового пользователя (POST)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Cookie", sessionId);

        User newUser = new User();
        HttpEntity<User> requestEntity = new HttpEntity<>(newUser, headers);
        ResponseEntity<String> postResponse = restTemplate.postForEntity(API_URL, requestEntity, String.class);
        finalCode.append(postResponse.getBody());
        System.out.println("Part 1 of code: " + postResponse.getBody());

        // Шаг 3: Обновить пользователя (PUT)
        User updatedUser = new User();
        HttpEntity<User> putRequestEntity = new HttpEntity<>(updatedUser, headers);
        ResponseEntity<String> putResponse = restTemplate.exchange(API_URL, HttpMethod.PUT, putRequestEntity, String.class);
        finalCode.append(putResponse.getBody());
        System.out.println("Part 2 of code: " + putResponse.getBody());

        // Шаг 4: Удалить пользователя (DELETE)
        HttpEntity<String> deleteRequestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> deleteResponse = restTemplate.exchange(API_URL + "/3", HttpMethod.DELETE, deleteRequestEntity, String.class);
        finalCode.append(deleteResponse.getBody());
        System.out.println("Part 3 of code: " + deleteResponse.getBody());

        // Итоговый код
        System.out.println("Final code: " + finalCode.toString());
        return finalCode.toString();
    }
}