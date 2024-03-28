# Дипломный проект профессии «Тестировщик»
* ## Процедура запуска SUT, авто-тестов и генерация отчетов:
* В файле *application.properties* удалить строку 3 - spring.datasource.url=jdbc:mysql://localhost:3306/app (По умолчанию указано подключение к MySQL)

* ### Подключение SUT к MySQL:
1. Запустить Docker Desktop
2. Открыть проект в IntelliJ IDEA
3. В терминале в корне проекта запустить контейнеры:

   `docker-compose up -d`
4. Во втором терминале запустить приложение:

   `java -jar .\artifacts\aqa-shop.jar --spring.datasource.url=jdbc:mysql://localhost:3306/app`
5. В третьем терминале запустить тесты:

   `.\gradlew clean test -DdbUrl=jdbc:mysql://localhost:3306/app`
6. Создать отчёт Allure и открыть в браузере

   `.\gradlew allureServe`
7. Закрыть отчёт:

   **CTRL + C -> y -> Enter**
8. Остановить приложение во втором терминале:

   **CTRL + C**
9. Остановить контейнеры в первом терминале:

   `docker-compose down`

* ### Подключение SUT к PostgreSQL:
1. Запустить Docker Desktop
2. Открыть проект в IntelliJ IDEA
3. В терминале в корне проекта запустить контейнеры:

   `docker-compose up -d`
4. Во втором терминале запустить приложение:

   `java -jar .\artifacts\aqa-shop.jar --spring.datasource.url=jdbc:postgresql://localhost:5432/app`
5. В третьем терминале запустить тесты:

   `.\gradlew clean test -DdbUrl=jdbc:postgresql://localhost:5432/app`
6. Создать отчёт Allure и открыть в браузере

   `.\gradlew allureServe`
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