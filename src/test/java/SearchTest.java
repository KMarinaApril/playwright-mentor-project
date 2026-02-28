import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchTest {
    @Test
    void searchActionTest() {
        try (Playwright playwright = Playwright.create()) {
            // Запускаем браузер (можешь поставить headless(false), если хочешь посмотреть глазами)
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
            Page page = browser.newPage();

            // 1. Идем на сайт
            page.navigate("https://playwright.dev/");

            // 2. Открываем поиск
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();

            // 3. Печатаем слово
            page.locator("#docsearch-input").click();
            page.locator("#docsearch-input").fill("Actions");

            // 4. Ждём, пока появится результат, и кликаем прямо по ссылке в результате
            Locator firstResult = page.locator(".DocSearch-Hit a").first();
            firstResult.waitFor(); // Ждём, когда список результатов прогрузится
            firstResult.click();

            // 5. Ждём загрузки
            page.locator("h1").waitFor();

            // 6. Проверяем
            assertTrue(page.locator("h1").textContent().contains("Actions"));
            browser.close();
        }
    }
}