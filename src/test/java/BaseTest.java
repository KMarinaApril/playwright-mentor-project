import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.*;
import java.io.ByteArrayInputStream;
import java.nio.file.Paths;

public class BaseTest {
    protected static Playwright playwright;
    protected static Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        // Можно добавить headless: false, если хочешь видеть процесс глазами
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }

    @BeforeEach
    void createContext() {
        Allure.step("Подготовка окружения: запуск браузера", () -> {
            context = browser.newContext();
            page = context.newPage();
        });
    }

    @AfterEach
    void closeContext() {
        // Делаем скриншот перед закрытием, чтобы запечатлеть финал теста
        byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
        Allure.addAttachment("Финальное состояние страницы", new ByteArrayInputStream(screenshot));

        Allure.step("Закрытие сессии", () -> {
            context.close();
        });
    }

    @AfterAll
    static void closeBrowser() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}