package com.tp.selenium;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Activité 9 : Conversion d'une classe en classe de test JUnit
 * 
 * Tests de recherche Google avec captures d'écran
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Tests de Recherche Google - Activité 9")
public class RechercheGoogleTest {

    private static WebDriver driver;
    private static WebDriverWait wait;
    
    @BeforeAll
    static void setup() {
        System.setProperty("webdriver.gecko.driver",
                "C:\\Users\\ACHRAF\\Downloads\\geckodriver-v0.36.0-win32\\geckodriver.exe");
        
        FirefoxOptions options = new FirefoxOptions();
        options.setBinary("C:\\Program Files\\Firefox Developer Edition\\firefox.exe");
        
        driver = new FirefoxDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }
    
    @AfterAll
    static void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    @BeforeEach
    void navigateToGoogle() {
        driver.get("https://www.google.com");
        
        try {
            WebElement acceptButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("L2AGLb"))
            );
            acceptButton.click();
            Thread.sleep(500);
        } catch (Exception e) {
            // Pas de popup de cookies
        }
    }
    
    @Test
    @Order(1)
    @DisplayName("Test 1 : Vérification du titre de la page d'accueil Google")
    void testTitrePageGoogle() {
        String titre = driver.getTitle();
        String url = driver.getCurrentUrl();
        
        // Capture d'écran
        ScreenshotUtils.takeScreenshot(driver, "Test1_PageAccueilGoogle");
        
        assertTrue(titre.toLowerCase().contains("google") || url.toLowerCase().contains("google"), 
            "Le titre ou URL de la page devrait contenir 'google'");
        
        System.out.println("✓ Test réussi - Titre de la page : " + titre);
    }
    
    @Test
    @Order(2)
    @DisplayName("Test 2 : Vérification de la présence du champ de recherche")
    void testPresenceChampRecherche() {
        WebElement champRecherche = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.name("q"))
        );
        
        // Capture d'écran
        ScreenshotUtils.takeScreenshot(driver, "Test2_ChampRecherchePresent");
        
        assertNotNull(champRecherche, "Le champ de recherche ne devrait pas être null");
        assertTrue(champRecherche.isDisplayed() || champRecherche.isEnabled(), 
            "Le champ de recherche devrait être visible ou activé");
        
        System.out.println("✓ Test réussi - Champ de recherche trouvé et visible");
    }
    
    @Test
    @Order(3)
    @DisplayName("Test 3 : Recherche avec mot clé valide et vérification des résultats")
    void testRechercheMotCleValide() {
        String motCle = "Selenium WebDriver";
        
        WebElement champRecherche = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.name("q"))
        );
        
        champRecherche.sendKeys(motCle);
        champRecherche.submit();
        
        wait.until(ExpectedConditions.urlContains("search"));
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        
        // Capture d'écran des résultats
        ScreenshotUtils.takeScreenshot(driver, "Test3_ResultatsRecherche_SeleniumWebDriver");
        
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("search") && currentUrl.contains("Selenium"), 
            "L'URL devrait contenir les termes de recherche");
        
        System.out.println("✓ Test réussi - Recherche effectuée");
    }
    
    @Test
    @Order(4)
    @DisplayName("Test 4 : Vérification de la présence des résultats de recherche")
    void testPresenceResultatsRecherche() {
        String motCle = "Java programming";
        
        WebElement champRecherche = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.name("q"))
        );
        champRecherche.sendKeys(motCle);
        champRecherche.submit();
        
        wait.until(ExpectedConditions.urlContains("search"));
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        
        // Capture d'écran
        ScreenshotUtils.takeScreenshot(driver, "Test4_ResultatsRecherche_JavaProgramming");
        
        String pageSource = driver.getPageSource();
        boolean hasResults = pageSource.contains("Java") || 
                            driver.getCurrentUrl().contains("Java");
        
        assertTrue(hasResults, "La page devrait contenir des résultats");
        
        System.out.println("✓ Test réussi - Résultats de recherche affichés");
    }
}
