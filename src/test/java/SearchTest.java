import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchTest {
    @ParameterizedTest
    @ValueSource(strings = {"Actions", "Locators", "Assertions", "Playwright"})
        // Список слов для поиска
    void searchParameterizedTest(String searchQuery) { // searchQuery — это переменная, куда подставятся слова
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
            Page page = browser.newPage();

            page.navigate("https://playwright.dev/");

            // Открываем поиск
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();

            // Печатаем слово из нашего списка (используем searchQuery)
            Locator input = page.locator("#docsearch-input");
            input.waitFor();
            input.fill(searchQuery);

            // 4. Ждём результат и кликаем
            Locator firstResult = page.locator(".DocSearch-Hit a").first();
            firstResult.waitFor();
            firstResult.click();

            // --- УЛУЧШЕННОЕ ОЖИДАНИЕ ---
            // Ждём, пока в h1 появится текст, похожий на наш запрос (игнорируя регистр)
            page.locator("h1").waitFor(new Locator.WaitForOptions().setTimeout(5000));

            // Дополнительная страховка: ждём, чтобы текст h1 НЕ был текстом главной страницы
            page.waitForCondition(() -> !page.locator("h1").textContent().contains("enables reliable"));

            // 5. Проверяем
            String actualHeader = page.locator("h1").textContent().toLowerCase();
            System.out.println("Для запроса [" + searchQuery + "] нашли заголовок: " + actualHeader);

            assertTrue(actualHeader.contains(searchQuery.toLowerCase()),
                    "Ожидали " + searchQuery + ", но нашли " + actualHeader);

            browser.close();
        }
    }
}