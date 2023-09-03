import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestNegativeChecksBankCard {
    private WebDriver driver;

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void emptyNameFieldTest() {
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79271234567");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button.button")).click();

        assertEquals("Поле обязательно для заполнения",
                driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim());
    }

    @Test
    void invalidCheckNameFieldTest() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Liliya");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79271234567");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        assertEquals("Имя и Фамилия указаны неверно. Допустимы только русские буквы, пробелы, дефисы.",
                driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim());
    }

    @Test
    void invalidCheckPhoneFieldTest() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Васильев Иван");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+792700");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.",
                driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim());
    }

    @Test
    void emptyPhoneFieldTest() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Васильев Иван");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.cssSelector("button.button")).click();
        assertEquals("Поле обязательно для заполнения",
                driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim());
    }

    @Test
    void invalidCheckboxTest() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Васильев Иван");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79271234567");
        driver.findElement(By.cssSelector("button.button")).click();
        assertTrue(driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid")).isDisplayed());
    }
}
