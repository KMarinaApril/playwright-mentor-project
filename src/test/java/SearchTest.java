import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

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

            // 4. Ждём, пока появится результат, и кликаем
            Locator firstResult = page.locator(".DocSearch-Hit a").first();
            firstResult.waitFor();
            firstResult.click();

            // --- НОВАЯ СТРОКА: Ждём, когда URL изменится на страницу docs ---
            page.waitForURL("**/docs/**");

            // 5. Теперь ждём h1. Так как URL уже новый, Playwright найдёт h1 именно на НОВОЙ странице
            page.locator("h1").waitFor();

            // 6. Проверяем заголовок (теперь там точно будет не главная)
            String actualHeader = page.locator("h1").textContent().toLowerCase();
            System.out.println("ТЕПЕРЬ ЗАГОЛОВОК ТАКОЙ: " + actualHeader);

            assertTrue(actualHeader.contains("actions"), "В заголовке нет слова 'actions'!");

            // 7. Делаем скриншот
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("screenshot-search.png")));
            browser.close();
        }
    }
}