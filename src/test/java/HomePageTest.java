import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Сайт Playwright")
@Feature("Главная страница")
public class HomePageTest extends BaseTest {

    @Test
    @DisplayName("Проверка заголовка главной страницы")
    void checkTitleTest() {
        Allure.step("Перейти на сайт Playwright", () -> {
            page.navigate("https://playwright.dev/");
        });

        Allure.step("Проверить, что заголовок содержит 'Playwright'", () -> {
            assertTrue(page.title().contains("Playwright"));
        });
    }
}