package ru.kata.spring.boot_security.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.kata.spring.boot_security.demo.models.User;

@SpringBootApplication
public class SpringBootSecurityDemoApplication {

    private static final String BASE_URL = "http://94.198.50.185:7081/api/users";
    private static String sessionId = "";

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        // Шаг 1: Получение списка всех пользователей и сохранение session id
        ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL, String.class);
        sessionId = response.getHeaders().getFirst("Set-Cookie");
        System.out.println("Session ID: " + sessionId);

        // Шаг 2: Добавление нового пользователя
        User newUser = new User();
        newUser.setId(3L); // Убедитесь, что этот ID уникален
        newUser.setName("James");
        newUser.setLastName("Brown");
        newUser.setAge((byte) 30);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // Устанавливаем Content-Type
        headers.set("Cookie", sessionId);
        HttpEntity<User> requestEntity = new HttpEntity<>(newUser, headers);

        ResponseEntity<String> addUserResponse = restTemplate.exchange(
                BASE_URL,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        String firstPartCode = null;
        if (addUserResponse.getStatusCode() == HttpStatus.OK) {
            firstPartCode = addUserResponse.getBody();
            System.out.println("First part of code: " + firstPartCode);
        } else {
            System.out.println("Error while adding user: " + addUserResponse.getStatusCode());
        }
        // Шаг 3: Изменение пользователя
        User updatedUser = new User();
        updatedUser.setId(3L); // ID должен совпадать с существующим пользователем
        updatedUser.setName("Thomas");
        updatedUser.setLastName("Shelby");
        updatedUser.setAge((byte) 35);

        HttpEntity<User> updateRequestEntity = new HttpEntity<>(updatedUser, headers); // Создаем HttpEntity с заголовками и телом запроса

        ResponseEntity<String> updateUserResponse = restTemplate.exchange(
                BASE_URL,
                HttpMethod.PUT,
                updateRequestEntity,
                String.class
        );

        String secondPartCode = null;
        if (updateUserResponse.getStatusCode() == HttpStatus.OK) {
            secondPartCode = updateUserResponse.getBody();
            System.out.println("Second part of code: " + secondPartCode);
        } else {
            System.out.println("Error while updating user: " + updateUserResponse.getStatusCode());
        }
        // Шаг 4: Удаление пользователя
        ResponseEntity<String> deleteUserResponse = restTemplate.exchange(
                BASE_URL + "/3",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                String.class
        );
        String thirdPartCode = null;
        if (deleteUserResponse.getStatusCode() == HttpStatus.OK) {
            thirdPartCode = deleteUserResponse.getBody();
            System.out.println("Third part of code: " + thirdPartCode);
        } else {
            System.out.println("Error while deleting user: " + deleteUserResponse.getStatusCode());
        }

        // Шаг 5: Конкатенация кода
        String finalCode = firstPartCode + secondPartCode + thirdPartCode;
        System.out.println("Final code: " + finalCode);
    }
}