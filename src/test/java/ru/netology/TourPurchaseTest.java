package ru.netology;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.page.PurchasePage;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataGenerator.*;
import static ru.netology.data.DataHelper.*;
import static ru.netology.data.DataHelper.getPaymentInfo;

public class TourPurchaseTest {

    @BeforeAll
    public static void setUpAll() {

        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void openPage() {

        open("http://localhost:8080");
    }

    @Nested
    //Тесты на оплату и получения кредита по валидной карте:
    public class ValidCard {

        @Test
        @DisplayName("Покупка валидной картой")
        public void shouldPaymentValidCard() {
            var purchasePage = new PurchasePage();
            purchasePage.cardPayment();
            var info = getApprovedCard();
            purchasePage.enterCardInfo(info);
            var expected = "APPROVED";
            var paymentInfo = getPaymentInfo();
            var orderInfo = getOrderInfo();
            //Проверка соответствия статуса в базе данных в таблице покупок:
            assertEquals(expected, paymentInfo.getStatus());
            //Проверка соответствия в базе данных id в таблице покупок и в таблице заявок:
            assertEquals(paymentInfo.getTransaction_id(), orderInfo.getPayment_id());
            //Проверка вывода соответствующего уведомления пользователю на странице покупок:
            purchasePage.bankApproved();
        }

        @Test
        @DisplayName("Получение кредита на покупку по валидной карте")
        public void shouldCreditValidCard() {
            var purchasePage = new PurchasePage();
            purchasePage.cardCredit();
            var info = getApprovedCard();
            purchasePage.enterCardInfo(info);
            var expected = "APPROVED";
            var creditRequestInfo = getCreditRequestInfo();
            var orderInfo = getOrderInfo();
            //Проверка соответствия статуса в базе данных в таблице запросов кредита:
            assertEquals(expected, creditRequestInfo.getStatus());
            //Проверка соответствия в базе данных id в таблице запросов кредита и в таблице заявок:
            assertEquals(creditRequestInfo.getBank_id(), orderInfo.getCredit_id());
            //Проверка вывода соответствующего уведомления пользователю на странице покупок:
            purchasePage.bankApproved();
        }
    }

    @Nested
    //Тесты на оплату и получения кредита по не валидной карте:
    public class InvalidCard {

        @Test
        @DisplayName("Покупка невалидной картой")
        public void shouldPaymentInvalidCard() {
            var purchasePage = new PurchasePage();
            purchasePage.cardPayment();
            var info = getDeclinedCard();
            purchasePage.enterCardInfo(info);
            var expected = "DECLINED";
            var paymentInfo = getPaymentInfo();
            var orderInfo = getOrderInfo();
            //Проверка соответствия статуса в базе данных в таблице покупок:
            assertEquals(expected, paymentInfo.getStatus());
            //Проверка соответствия в базе данных id в таблице покупок и в таблице заявок:
            assertEquals(paymentInfo.getTransaction_id(), orderInfo.getPayment_id());
            //Проверка вывода соответствующего уведомления пользователю на странице покупок:
            purchasePage.bankDeclined();
        }

        @Test
        @DisplayName("Получение кредита на покупку по невалидной карте")
        public void shouldCreditInvalidCard() {
            var purchasePage = new PurchasePage();
            purchasePage.cardCredit();
            var info = getDeclinedCard();
            purchasePage.enterCardInfo(info);
            var expected = "DECLINED";
            var creditRequestInfo = getCreditRequestInfo();
            var orderInfo = getOrderInfo();
            //Проверка соответствия статуса в базе данных в таблице запросов кредита:
            assertEquals(expected, creditRequestInfo.getStatus());
            //Проверка соответствия в базе данных id в таблице запросов кредита и в таблице заявок:
            assertEquals(creditRequestInfo.getBank_id(), orderInfo.getCredit_id());
            //Проверка вывода соответствующего уведомления пользователю на странице покупок:
            purchasePage.bankApproved();
        }
    }

    @Nested
    //Тесты на валидацию полей платежной формы:
    public class PaymentFormFieldValidation {

        @BeforeEach
        public void setPayment() {
            var purchasePage = new PurchasePage();
            purchasePage.cardPayment();
        }

        @Test
        @DisplayName("Отправка пустой формы")
        public void shouldEmpty() {
            var purchasePage = new PurchasePage();
            purchasePage.emptyForm();
        }

        @Test
        @DisplayName("Поле 'Номер карты', пустое поле")
        public void shouldEmptyCardNumberField() {
            var purchasePage = new PurchasePage();
            var emptyCardInformation = getCardWithEmptyCardNumberField();
            purchasePage.enterCardInfo(emptyCardInformation);
            purchasePage.checkEmptyCardNumberField();
        }

        @Test
        @DisplayName("Поле 'Номер карты', неполный номер карты")
        public void shouldCardWithIncompleteCardNumber() {
            var purchasePage = new PurchasePage();
            var cardWithIncompleteCardNumber = getCardWithIncompleteCardNumber();
            purchasePage.enterCardInfo(cardWithIncompleteCardNumber);
            purchasePage.checkInvalidCardNumberField();
        }

        @Test
        @DisplayName("Поле 'Месяц', пустое поле")
        public void shouldEmptyMonthField() {
            var purchasePage = new PurchasePage();
            var emptyMonthField = getCardWithEmptyMonthField();
            purchasePage.enterCardInfo(emptyMonthField);
            purchasePage.checkEmptyMonthField();
        }

        @Test
        @DisplayName("Поле 'Месяц', просроченный месяц")
        public void shouldCardWithOverdueMonth() {
            var purchasePage = new PurchasePage();
            var cardWithOverdueMonth = getCardWithOverdueMonth();
            purchasePage.enterCardInfo(cardWithOverdueMonth);
            purchasePage.checkInvalidMonthField();
        }

        @Test
        @DisplayName("Поле 'Месяц', нижнее негативное значение '00'")
        public void shouldCardWithLowerMonthValue() {
            var purchasePage = new PurchasePage();
            var cardWithLowerMonthValue = getCardWithLowerMonthValue();
            purchasePage.enterCardInfo(cardWithLowerMonthValue);
            purchasePage.checkInvalidMonthField();
        }

        @Test
        @DisplayName("Поле 'Месяц', верхнее негативное значение '13'")
        public void shouldCardWithGreaterMonthValue() {
            var purchasePage = new PurchasePage();
            var cardWithGreaterMonthValue = getCardWithGreaterMonthValue();
            purchasePage.enterCardInfo(cardWithGreaterMonthValue);
            purchasePage.checkInvalidMonthField();
        }

        @Test
        @DisplayName("Поле 'Год', пустое поле")
        public void shouldEmptyYearField() {
            var purchasePage = new PurchasePage();
            var emptyYearField = getCardWithEmptyYearField();
            purchasePage.enterCardInfo(emptyYearField);
            purchasePage.checkEmptyYearField();
        }

        @Test
        @DisplayName("Поле 'Год', просроченный год")
        public void shouldCardWithOverdueYear() {
            var purchasePage = new PurchasePage();
            var cardWithOverdueYear = getCardWithOverdueYear();
            purchasePage.enterCardInfo(cardWithOverdueYear);
            purchasePage.checkOverdueYearField();
        }

        @Test
        @DisplayName("Поле 'Год', год из отдаленного будущего")
        public void shouldCardWithYearFromFuture() {
            var purchasePage = new PurchasePage();
            var cardWithYearFromFuture = getCardWithYearFromFuture();
            purchasePage.enterCardInfo(cardWithYearFromFuture);
            purchasePage.checkYearFromFutureField();
        }

        @Test
        @DisplayName("Поле 'Владелец', пустое поле")
        public void shouldEmptyOwnerField() {
            var purchasePage = new PurchasePage();
            var emptyOwnerField = getCardWithEmptyOwnerField();
            purchasePage.enterCardInfo(emptyOwnerField);
            purchasePage.checkEmptyOwnerField();
        }

        @Test
        @DisplayName("Поле 'Владелец', с пробелом или дефисом")
        public void shouldCardWithSpaceOrHyphenOwner() {
            var purchasePage = new PurchasePage();
            var cardWithSpaceOrHyphenOwner = getCardWithSpaceOrHyphenOwner();
            purchasePage.enterCardInfo(cardWithSpaceOrHyphenOwner);
            purchasePage.checkSpaceOwnerField();
        }

        @Test
        @DisplayName("Поле 'Владелец', с несколькими спец символами")
        public void shouldCardWithSpecialSymbolsOwner() {
            var purchasePage = new PurchasePage();
            var cardWithSpecialSymbolsOwner = getCardWithSpecialSymbolsOwner();
            purchasePage.enterCardInfo(cardWithSpecialSymbolsOwner);
            purchasePage.checkInvalidOwnerField();
        }

        @Test
        @DisplayName("Поле 'Владелец', с цифрами")
        public void shouldCardWithNumbersOwner() {
            var purchasePage = new PurchasePage();
            var cardWithNumbersOwner = getCardWithNumbersOwner();
            purchasePage.enterCardInfo(cardWithNumbersOwner);
            purchasePage.checkInvalidOwnerField();
        }

        @Test
        @DisplayName("Поле 'CVC/CVV', пустое поле")
        public void shouldEmptyCVCField() {
            var purchasePage = new PurchasePage();
            var emptyCVCField = getCardWithEmptyCVCField();
            purchasePage.enterCardInfo(emptyCVCField);
            purchasePage.checkEmptyCVCField();
        }

        @Test
        @DisplayName("Поле 'CVC/CVV', не полный номер")
        public void shouldCardWithIncompleteCVC() {
            var purchasePage = new PurchasePage();
            var cardWithIncompleteCVC = getCardWithIncompleteCVC();
            purchasePage.enterCardInfo(cardWithIncompleteCVC);
            purchasePage.checkInvalidCVCField();
        }

    }

    @Nested
    //Тесты на валидацию полей кредитной формы:
    public class CreditFormFieldValidation {

        @BeforeEach
        public void setPayment() {
            var purchasePage = new PurchasePage();
            purchasePage.cardCredit();
        }

        @Test
        @DisplayName("Отправка пустой формы")
        public void shouldEmpty() {
            var purchasePage = new PurchasePage();
            purchasePage.emptyForm();
        }

        @Test
        @DisplayName("Поле 'Номер карты', пустое поле")
        public void shouldEmptyCardNumberField() {
            var purchasePage = new PurchasePage();
            var emptyCardInformation = getCardWithEmptyCardNumberField();
            purchasePage.enterCardInfo(emptyCardInformation);
            purchasePage.checkEmptyCardNumberField();
        }

        @Test
        @DisplayName("Поле 'Номер карты', неполный номер карты")
        public void shouldCardWithIncompleteCardNumber() {
            var purchasePage = new PurchasePage();
            var cardWithIncompleteCardNumber = getCardWithIncompleteCardNumber();
            purchasePage.enterCardInfo(cardWithIncompleteCardNumber);
            purchasePage.checkInvalidCardNumberField();
        }

        @Test
        @DisplayName("Поле 'Месяц', пустое поле")
        public void shouldEmptyMonthField() {
            var purchasePage = new PurchasePage();
            var emptyMonthField = getCardWithEmptyMonthField();
            purchasePage.enterCardInfo(emptyMonthField);
            purchasePage.checkEmptyMonthField();
        }

        @Test
        @DisplayName("Поле 'Месяц', просроченный месяц")
        public void shouldCardWithOverdueMonth() {
            var purchasePage = new PurchasePage();
            var cardWithOverdueMonth = getCardWithOverdueMonth();
            purchasePage.enterCardInfo(cardWithOverdueMonth);
            purchasePage.checkInvalidMonthField();
        }

        @Test
        @DisplayName("Поле 'Месяц', нижнее негативное значение '00'")
        public void shouldCardWithLowerMonthValue() {
            var purchasePage = new PurchasePage();
            var cardWithLowerMonthValue = getCardWithLowerMonthValue();
            purchasePage.enterCardInfo(cardWithLowerMonthValue);
            purchasePage.checkInvalidMonthField();
        }

        @Test
        @DisplayName("Поле 'Месяц', верхнее негативное значение '13'")
        public void shouldCardWithGreaterMonthValue() {
            var purchasePage = new PurchasePage();
            var cardWithGreaterMonthValue = getCardWithGreaterMonthValue();
            purchasePage.enterCardInfo(cardWithGreaterMonthValue);
            purchasePage.checkInvalidMonthField();
        }

        @Test
        @DisplayName("Поле 'Год', пустое поле")
        public void shouldEmptyYearField() {
            var purchasePage = new PurchasePage();
            var emptyYearField = getCardWithEmptyYearField();
            purchasePage.enterCardInfo(emptyYearField);
            purchasePage.checkEmptyYearField();
        }

        @Test
        @DisplayName("Поле 'Год', просроченный год")
        public void shouldCardWithOverdueYear() {
            var purchasePage = new PurchasePage();
            var cardWithOverdueYear = getCardWithOverdueYear();
            purchasePage.enterCardInfo(cardWithOverdueYear);
            purchasePage.checkOverdueYearField();
        }

        @Test
        @DisplayName("Поле 'Год', год из отдаленного будущего")
        public void shouldCardWithYearFromFuture() {
            var purchasePage = new PurchasePage();
            var cardWithYearFromFuture = getCardWithYearFromFuture();
            purchasePage.enterCardInfo(cardWithYearFromFuture);
            purchasePage.checkYearFromFutureField();
        }

        @Test
        @DisplayName("Поле 'Владелец', пустое поле")
        public void shouldEmptyOwnerField() {
            var purchasePage = new PurchasePage();
            var emptyOwnerField = getCardWithEmptyOwnerField();
            purchasePage.enterCardInfo(emptyOwnerField);
            purchasePage.checkEmptyOwnerField();
        }

        @Test
        @DisplayName("Поле 'Владелец', с пробелом или дефисом")
        public void shouldCardWithSpaceOrHyphenOwner() {
            var purchasePage = new PurchasePage();
            var cardWithSpaceOrHyphenOwner = getCardWithSpaceOrHyphenOwner();
            purchasePage.enterCardInfo(cardWithSpaceOrHyphenOwner);
            purchasePage.checkSpaceOwnerField();
        }

        @Test
        @DisplayName("Поле 'Владелец', с несколькими спец символами")
        public void shouldCardWithSpecialSymbolsOwner() {
            var purchasePage = new PurchasePage();
            var cardWithSpecialSymbolsOwner = getCardWithSpecialSymbolsOwner();
            purchasePage.enterCardInfo(cardWithSpecialSymbolsOwner);
            purchasePage.checkInvalidOwnerField();
        }

        @Test
        @DisplayName("Поле 'Владелец', с цифрами")
        public void shouldCardWithNumbersOwner() {
            var purchasePage = new PurchasePage();
            var cardWithNumbersOwner = getCardWithNumbersOwner();
            purchasePage.enterCardInfo(cardWithNumbersOwner);
            purchasePage.checkInvalidOwnerField();
        }

        @Test
        @DisplayName("Поле 'CVC/CVV', пустое поле")
        public void shouldEmptyCVCField() {
            var purchasePage = new PurchasePage();
            var emptyCVCField = getCardWithEmptyCVCField();
            purchasePage.enterCardInfo(emptyCVCField);
            purchasePage.checkEmptyCVCField();
        }

        @Test
        @DisplayName("Поле 'CVC/CVV', не полный номер")
        public void shouldCardWithIncompleteCVC() {
            var purchasePage = new PurchasePage();
            var cardWithIncompleteCVC = getCardWithIncompleteCVC();
            purchasePage.enterCardInfo(cardWithIncompleteCVC);
            purchasePage.checkInvalidCVCField();
        }
    }
}