package ru.netology.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


public class CardWithDeliveryTest {

    @BeforeAll
    static void setupAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldPlanAndReplanMeetValid() {

        $("[data-test-id='city'] input").setValue(DataGenerator.genCity());
        String firstDate = DataGenerator.genDate(DataGenerator.random(), "dd.MM.yyyy");
        String secondDate = DataGenerator.genDate(DataGenerator.random(), "dd.MM.yyyy");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").sendKeys(firstDate);
        $("[data-test-id='name'] input").setValue(DataGenerator.genName("ru"));
        $("[data-test-id='phone'] input").setValue(DataGenerator.genPhone("ru"));
        $("[data-test-id='agreement']").click();
        $("button.button").click();

        $(byText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $(".notification__content").shouldHave(Condition.exactText("Встреча успешно запланирована на " + firstDate));

        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(secondDate);
        $(byText("Запланировать")).click();

        $("[data-test-id='replan-notification'] .notification__content").shouldBe(visible);
        $("[data-test-id='replan-notification'] .notification__content").shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $(byText("Перепланировать")).click();

        $("[data-test-id='success-notification']")
                .shouldHave(text("Встреча успешно запланирована на " + secondDate));
    }

    @Test
    void shouldGetErrorIfPastDate() {
        $("[data-test-id='city'] input").setValue(DataGenerator.genCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").sendKeys("01.09.2023");
        $("[data-test-id='name'] input").setValue(DataGenerator.genName("ru"));
        $("[data-test-id='phone'] input").setValue(DataGenerator.genPhone("ru"));
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $("[data-test-id='date'] .input__sub").shouldHave(Condition.exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldGetErrorIfInvalidDate() {
        $("[data-test-id='city'] input").setValue(DataGenerator.genCity());
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").sendKeys("41.19.2023");
        $("[data-test-id='name'] input").setValue(DataGenerator.genName("ru"));
        $("[data-test-id='phone'] input").setValue(DataGenerator.genPhone("ru"));
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $("[data-test-id='date'] .input__sub").shouldHave(Condition.exactText("Неверно введена дата"));
    }

    @Test
    void shouldGetErrorIfInvalidPhone() {
        $("[data-test-id='city'] input").setValue(DataGenerator.genCity());
        String firstDate = DataGenerator.genDate(DataGenerator.random(), "dd.MM.yyyy");
        $("[data-test-id='date'] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] input").sendKeys(firstDate);
        $("[data-test-id='name'] input").setValue(DataGenerator.genName("ru"));
        $("[data-test-id='phone'] input").setValue("0111222");
        $("[data-test-id='agreement']").click();
        $("button.button").click();
        $("[data-test-id='phone'] .input__sub").shouldHave(Condition.exactText("Неверный формат номера мобильного телефона"));
    }

}