package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.data.DataGenerator;
import java.time.Duration;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

public class PurchasePage {
    //Кнопка "Купить" и заголовок "Оплата по карте":

    private final SelenideElement buyButton = $(byText("Купить"));
    private final SelenideElement buyHeading = $(byText("Оплата по карте"));

    //Кнопка "Купить в кредит" и заголовок "Кредит по данным карты":

    private final SelenideElement creditButton = $(byText("Купить в кредит"));
    private final SelenideElement creditHeading = $(byText("Кредит по данным карты"));

    //Форма для ввода данных, со всеми полями ввода, ошибками полей и кнопкой "Продолжить":

    private final SelenideElement cardNumberField = $("input[placeholder='0000 0000 0000 0000']");
    private final SelenideElement monthField = $("input[placeholder='08']");
    private final SelenideElement yearField = $("input[placeholder='22']");
    private final SelenideElement ownerField = $(byText("Владелец")).parent().$("input");
    private final SelenideElement cvcField = $("input[placeholder='999']");

    private final SelenideElement invalidCardExpirationDate = $(withText("Неверно указан срок действия карты"));
    private final SelenideElement incorrectFormat = $(withText("Неверный формат"));
    private final SelenideElement cardExpired = $(withText("Истёк срок действия карты"));
    private final SelenideElement thisFieldIsRequired = $(withText("Поле обязательно для заполнения"));

    private final SelenideElement notificationSuccessfully = $(".notification_status_ok");
    private final SelenideElement notificationError = $(".notification_status_error");
    private final SelenideElement continueButton = $("form button");

    public void cardPayment() {
        buyButton.click();
        buyHeading.shouldBe(Condition.visible);
    }

    public void cardCredit() {
        creditButton.click();
        creditHeading.shouldBe(Condition.visible);
    }

    public void enterCardInfo(DataGenerator.CardInfo info) {
        cardNumberField.setValue(info.getNumberCard());
        monthField.setValue(info.getMonth());
        yearField.setValue(info.getYear());
        ownerField.setValue(info.getOwner());
        cvcField.setValue(info.getCvc());
        continueButton.click();
    }

    public void emptyForm() {
        continueButton.click();
    }

    public void checkEmptyCardNumberField() {
        incorrectFormat.shouldBe(Condition.visible);
    }

    public void checkEmptyMonthField() {
        incorrectFormat.shouldBe(Condition.visible);
    }

    public void checkEmptyYearField() {
        incorrectFormat.shouldBe(Condition.visible);
    }

    public void checkEmptyOwnerField() {
        thisFieldIsRequired.shouldBe(Condition.visible);
    }

    public void checkEmptyCVCField() {
        incorrectFormat.shouldBe(Condition.visible);
    }

    public void checkInvalidCardNumberField() {
        incorrectFormat.shouldBe(Condition.visible);
    }

    public void checkInvalidMonthField() {
        invalidCardExpirationDate.shouldBe(Condition.visible);
    }

    public void checkOverdueYearField() {
        cardExpired.shouldBe(Condition.visible);
    }

    public void checkYearFromFutureField() {
        invalidCardExpirationDate.shouldBe(Condition.visible);
    }

    public void checkInvalidOwnerField() {
        incorrectFormat.shouldBe(Condition.visible);
    }

    public void checkSpaceOwnerField() {
        thisFieldIsRequired.shouldBe(Condition.visible);
    }

    public void checkInvalidCVCField() {
        incorrectFormat.shouldBe(Condition.visible);
    }

    public void bankApproved() {
        notificationSuccessfully.shouldBe(Condition.visible, Duration.ofSeconds(10));
    }

    public void bankDeclined() {
        notificationError.shouldBe(Condition.visible, Duration.ofSeconds(10));
    }
}