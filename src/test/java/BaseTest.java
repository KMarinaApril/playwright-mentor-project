import com.microsoft.playwright.*;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import java.io.ByteArrayInputStream;

public class BaseTest {
    protected Playwright playwright;
    protected Browser browser;
    protected Page page;

    @BeforeEach
    void setUp() {
        playwright = Playwright.create();
        // Оставляем false, чтобы видеть процесс
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        // Делаем скриншот ПЕРЕД закрытием браузера
        if (page != null && !page.isClosed()) {
            try {
                byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
                Allure.addAttachment("Скриншот: " + testInfo.getDisplayName(), new ByteArrayInputStream(screenshot));
            } catch (Exception e) {
                System.out.println("Не удалось сделать скриншот: " + e.getMessage());
            }
        }

        // Теперь закрываем всё
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}