import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchTest extends BaseTest {

    @Epic("Документация Playwright")
    @Feature("Поиск по сайту")
    @ParameterizedTest(name = "Поиск по ключевому слову: {0}")
    @ValueSource(strings = {"Actions", "Locators", "Assertions"})
    void searchParameterizedTest(String searchQuery) {

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
            firstResult.waitFor(new Locator.WaitForOptions().setTimeout(3000));
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
            // byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
           // Allure.addAttachment("Скриншот страницы " + searchQuery, new ByteArrayInputStream(screenshot));

            assertTrue(actualHeader.contains(searchQuery.toLowerCase()),
                    "Ожидали " + searchQuery + ", но нашли " + actualHeader);
        });

    }

}