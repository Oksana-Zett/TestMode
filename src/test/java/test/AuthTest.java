package test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import data.DataGenerator;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;
//import static data.DataGenerator.Registration.getRegisteredUser;
//import static data.DataGenerator.Registration.getUser;
//import static data.DataGenerator.getRandomLogin;
//import static data.DataGenerator.getRandomPassword;


public class AuthTest {

//    private static RequestSpecification requestSpec = new RequestSpecBuilder()
//            .setBaseUri("http://localhost")
//            .setPort(9999)
//            .setAccept(ContentType.JSON)
//            .setContentType(ContentType.JSON)
//            .log(LogDetail.ALL)
//            .build();

    @BeforeEach
    void setup() {
        Configuration.headless = true;
        open("http://localhost:9999");
    }

    @BeforeAll
    static void setUpAll() {
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        DataGenerator.RegistrationDto user=DataGenerator.Registration.getRegisteredUser("active");
        $("[data-test-id='login'] input").sendKeys(user.getLogin());
        $("[data-test-id='password'] input").sendKeys(user.getPassword());
        $(byText("Продолжить")).click();
        $("[class='App_appContainer__3jRx1']").shouldHave(Condition.text("  Личный кабинет"));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        DataGenerator.RegistrationDto user=DataGenerator.Registration.getRegisteredUser("active");
        DataGenerator.RegistrationDto notRegisteredUser=DataGenerator.Registration.getUser("active");
        $("[data-test-id='login'] input").sendKeys(notRegisteredUser.getLogin());
        $("[data-test-id='password'] input").sendKeys(user.getPassword());
        $(byText("Продолжить")).click();
        $("[class='notification__content']").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
//        $("[data-test-id='error-notification'] notification__content").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        DataGenerator.RegistrationDto blockedUser=DataGenerator.Registration.getRegisteredUser("blocked");
        $("[data-test-id='login'] input").sendKeys(blockedUser.getLogin());
        $("[data-test-id='password'] input").sendKeys(blockedUser.getPassword());
        $(byText("Продолжить")).click();
        $("[class='notification__content']").shouldHave(Condition.text("Ошибка! Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        DataGenerator.RegistrationDto user=DataGenerator.Registration.getRegisteredUser("active");
        String wrongLogin = DataGenerator.getRandomLogin();
        $("[data-test-id='login'] input").sendKeys(wrongLogin);
        $("[data-test-id='password'] input").sendKeys(user.getPassword());
        $(byText("Продолжить")).click();
        $("[class='notification__content']").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        DataGenerator.RegistrationDto user=DataGenerator.Registration.getRegisteredUser("active");
        String wrongPassword = DataGenerator.getRandomPassword();
        $("[data-test-id='login'] input").sendKeys(user.getLogin());
        $("[data-test-id='password'] input").sendKeys(wrongPassword);
        $(byText("Продолжить")).click();
        $("[class='notification__content']").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }
}
