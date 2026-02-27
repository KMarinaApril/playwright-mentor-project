import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetStarted {
    @Test
    void myFirstTest() {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            Page page = browser.newPage();

            // 1. Идем на сайт
            page.navigate("https://playwright.dev/");

            // 2. Нажимаем на кнопку Get Started (она находится по тексту или классу)
            page.getByText("Get Started").first().click();

            // 3. Проверяем, что заголовок совпадает с ожидаемым
            assertEquals("Installation | Playwright", page.title());

            System.out.println ("Заголовок страницы: " + page.title());

            browser.close();
        }
    }
}