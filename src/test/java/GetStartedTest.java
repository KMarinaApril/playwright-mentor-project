import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("Сайт Playwright")
@Feature("Навигация")
public class GetStartedTest extends BaseTest {
    @Test
    @DisplayName("Переход в раздел документации")
    void navigateToGetStartedTest() {
        //1. Открываем главную страницу
        Allure.step("Открыть главную страницу", () -> {
            page.navigate("https://playwright.dev/");
        });

        // 2. Нажимаем на кнопку Get Started (она находится по тексту или классу)
        Allure.step("Нажимаем на кнопку Get Started ", () -> {
            page.getByText("Get Started").first().click();
        });

        // 3. Убедиться, что открылась нужная страница и проверить, что заголовок совпадает с ожидаемым
        Allure.step("Убедиться, что открылась нужная страница ", () -> {
        //Ждём, пока главный заголовок страницы (h1) станет видимым
            page.locator("h1").waitFor();

            //Проверяем, что заголовок совпадает с ожидаемым
            assertEquals("Installation | Playwright", page.title());
        });
        }
    }