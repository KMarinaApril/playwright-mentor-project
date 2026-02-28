import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Документация Playwright") @Feature("Поиск по сайту") @ParameterizedTest(name = "Поиск по ключевому слову: {0}") @ValueSource(strings = {"Actions", "Locators", "Assertions"})
            void searchParameterizedTest(String searchQuery){
                    try(Playwright playwright=Playwright.create()){
                    Browser browser=playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
                    Page page=browser.newPage();

                    // 1. Шаг открытия сайта
                    Allure.step("Открыть главную страницу Playwright",()->{
                    page.navigate("https://playwright.dev/");
                    });

                    // 2. Шаг поиска
                    Allure.step("Ввести запрос '"+searchQuery+"' в поиск",()->{
                    page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Search")).click();
                    Locator input=page.locator("#docsearch-input");
                    input.waitFor();
                    input.fill(searchQuery);

                    Locator firstResult=page.locator(".DocSearch-Hit a").first();
                    firstResult.waitFor();
                    firstResult.click();
                    });

                    // 3. Шаг проверки
                    Allure.step("Проверить, что заголовок содержит '"+searchQuery+"'",()->{
                    page.waitForURL("**/docs/**");
                    String actualHeader=page.locator("h1").textContent().toLowerCase();

                    // Добавляем скриншот прямо ВНУТРЬ шага проверки
                    byte[]screenshot=page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
                    Allure.addAttachment("Скриншот страницы "+searchQuery,new ByteArrayInputStream(screenshot));

                    assertTrue(actualHeader.contains(searchQuery.toLowerCase()),
                    "Ожидали "+searchQuery+", но нашли "+actualHeader);
                    });

                    browser.close();
                    }
                    }