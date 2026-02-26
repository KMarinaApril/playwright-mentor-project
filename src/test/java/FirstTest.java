import com.microsoft.playwright.*;
import org.junit.jupiter.api.Test;

public class FirstTest {
    @Test
    void myFirstTest() {
        try (Playwright playwright = Playwright.create()) {
            // Запускаем браузер в видимом режиме (headless = false)
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            Page page = browser.newPage();

            // Переходим на сайт
            page.navigate("https://playwright.dev/");

            // Печатаем заголовок в консоль
            System.out.println("Заголовок страницы: " + page.title());

            // Закрываем браузер
            browser.close();
        }
    }
}