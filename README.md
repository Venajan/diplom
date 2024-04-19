# Дипломный проект профессии «Тестировщик»
* ## Процедура запуска SUT, авто-тестов и генерация отчетов:

* ### Подключение SUT:
1. Запустить Docker Desktop
2. Открыть проект в IntelliJ IDEA
3. В терминале в корне проекта запустить контейнеры:

   `docker-compose up -d`
4. Во втором терминале запустить приложение:

   * ### Для MySQL:
   `java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/base_mysql" -jar artifacts/aqa-shop.jar`
   * ### Для Postgres:
   `java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/base_postgresql" -jar artifacts/aqa-shop.jar`
5. В третьем терминале запустить тесты:

   * ### Для MySQL:
   `./gradlew clean test "-Ddatasource.url=jdbc:mysql://localhost:3306/base_mysql"`
   * ### Для Postgres:
   `./gradlew clean test "-Ddatasource.url=postgresql://localhost:5432/base_postgresql"`
6. Создать отчёт Allure и открыть в браузере

   `./gradlew allureServe`
7. Закрыть отчёт:

   **CTRL + C -> y -> Enter**
8. Остановить приложение во втором терминале:

   **CTRL + C**
9. Остановить контейнеры в первом терминале:

   `docker-compose down`

## Документация

1. [Текст задания](Exercise.md)

2. [План автоматизации](Plan.md)

3. [Отчётные документы по итогам тестирования](Report.md)

4. [Отчётные документы по итогам автоматизации](Summary.md)