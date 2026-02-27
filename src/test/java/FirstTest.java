import com.microsoft.playwright.*;
import org.junit.jupiter.api.Test;

public class FirstTest {
    @Test
    void myFirstTest() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            Page page = browser.newPage();

            // 1. Идем на сайт
            page.navigate("https://playwright.dev/");

            // 2. Нажимаем на кнопку поиска (она находится по тексту или классу)
            page.locator(".DocSearch-Button").click();

            // 3. Вводим слово "Locators" в появившееся поле
            page.locator("#docsearch-input").fill("Locators");

            // 4. Ждем секунду, чтобы увидеть результат (в реальных тестах так не делают, но нам для красоты надо)
            page.waitForTimeout(2000);

            System.out.println("Поиск успешно выполнен!");

            browser.close();
        }
    }
}