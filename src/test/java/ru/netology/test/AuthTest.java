package ru.netology.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataGenerator.Registration.*;
import static ru.netology.data.DataGenerator.getRandomLogin;
import static ru.netology.data.DataGenerator.getRandomPassword;

public class AuthTest {

    String errorTitle = "Ошибка";
    String wrongLoginOrPasswordMsg = "Неверно указан логин или пароль";
    String userBlocked = "Пользователь заблокирован";
    String lk = "Личный кабинет";


    public void stepsToAuth(String login, String password) {
        $("[data-test-id=login] input").setValue(login);
        $("[data-test-id=password] input").setValue(password);
        $("[data-test-id=action-login]").click();
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        stepsToAuth(registeredUser.getLogin(), registeredUser.getPassword());
        $(".heading").should(visible, text(lk));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        stepsToAuth(notRegisteredUser.getLogin(), notRegisteredUser.getPassword());
        $("[data-test-id=error-notification] .notification__title").should(visible, text(errorTitle));
        $(".notification__content").should(visible, text(wrongLoginOrPasswordMsg));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        stepsToAuth(blockedUser.getLogin(), blockedUser.getPassword());
        $("[data-test-id=error-notification] .notification__title").should(visible, text(errorTitle));
        $(".notification__content").should(visible, text(userBlocked));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        stepsToAuth(wrongLogin, registeredUser.getPassword());
        $("[data-test-id=error-notification] .notification__title").should(visible, text(errorTitle));
        $(".notification__content").should(visible, text(wrongLoginOrPasswordMsg));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        stepsToAuth(registeredUser.getLogin(), wrongPassword);
        $("[data-test-id=error-notification] .notification__title").should(visible, text(errorTitle));
        $(".notification__content").should(visible, text(wrongLoginOrPasswordMsg));
    }
}
