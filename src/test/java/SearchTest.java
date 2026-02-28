import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
public class SearchTest {
    // Создаем TestWatcher. чтобы скриншоты делать только для failed tests
    public class ScreenshotOnFailure implements TestWatcher {
        private final Page page;

        public ScreenshotOnFailure(Page page) {
            this.page = page;
        }

        @Override
        public void testFailed(ExtensionContext context, Throwable cause) {
            // Этот код сработает ТОЛЬКО если assertTrue или другой ассерт упал
            byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
            Allure.addAttachment("Screenshot on Failure", new ByteArrayInputStream(screenshot));
        }
    }

    @Epic("Документация Playwright")
    @Feature("Поиск по сайту")
    @ParameterizedTest(name = "Поиск по ключевому слову: {0}")
    @ValueSource(strings = {"Actions", "Locators", "Assertions"})
    void searchParameterizedTest(String searchQuery) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
            Page page = browser.newPage();

            // 1. Шаг открытия сайта
            Allure.step("Открыть главную страницу Playwright", () -> {
                page.navigate("https://playwright.dev/");
            });


            // 2. Шаг поиска
            Allure.step("Ввести запрос '" + searchQuery + "' в поиск", () -> {
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
                Locator input = page.locator("#docsearch-input");
                input.waitFor();
                input.fill(searchQuery);

                Locator firstResult = page.locator(".DocSearch-Hit a").first();
                firstResult.waitFor();
                firstResult.click();
            });

            // 3. Шаг проверки
            Allure.step("Проверить, что заголовок содержит '" + searchQuery + "'", () -> {
                // Ждём, пока URL станет содержать /docs/
                page.waitForURL("**/docs/**");

                // Ждём, пока h1 на НОВОЙ странице действительно появится и обновится
                Locator header = page.locator("h1");
                header.waitFor();

                // ХИТРОСТЬ: Ждём, пока текст в h1 перестанет быть текстом с главной страницы
                page.waitForCondition(() -> !header.textContent().contains("enables reliable"));

                String actualHeader = header.textContent().toLowerCase();

                // Делаем скриншот ПОСЛЕ того, как убедились, что страница сменилась
                byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
                Allure.addAttachment("Скриншот страницы " + searchQuery, new ByteArrayInputStream(screenshot));

                assertTrue(actualHeader.contains(searchQuery.toLowerCase()),
                        "Ожидали " + searchQuery + ", но нашли " + actualHeader);
            });

            browser.close();
        }

    }
}