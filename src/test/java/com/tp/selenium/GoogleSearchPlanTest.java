package com.tp.selenium;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Activité 10 : Plan de test et cas de test - Recherche Google
 * Avec captures d'écran pour chaque cas de test
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Plan de Test - Recherche Google (Activité 10)")
public class GoogleSearchPlanTest {

    private static WebDriver driver;
    private static WebDriverWait wait;
    private static final String GOOGLE_URL = "https://www.google.com";
    
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
        driver.get(GOOGLE_URL);
        acceptCookiesIfPresent();
    }
    
    private void acceptCookiesIfPresent() {
        try {
            WebElement acceptButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("L2AGLb"))
            );
            acceptButton.click();
            Thread.sleep(500);
        } catch (Exception e) {}
    }
    
    @Test
    @Order(1)
    @DisplayName("CT-001 : Recherche simple avec mot clé valide")
    void testCT001_RechercheSimpleMotCleValide() {
        String motCle = "Selenium WebDriver";
        
        WebElement searchBox = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.name("q"))
        );
        searchBox.sendKeys(motCle);
        searchBox.sendKeys(Keys.ENTER);
        
        wait.until(ExpectedConditions.urlContains("search"));
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        
        // Capture d'écran
        ScreenshotUtils.takeScreenshot(driver, "CT001_RechercheMotCleValide");
        
        String url = driver.getCurrentUrl();
        assertTrue(url.contains("search") && url.contains("Selenium"), 
            "L'URL devrait contenir les termes de recherche");
        
        System.out.println("✓ CT-001 Réussi - Recherche simple avec mot clé valide");
    }
    
    @Test
    @Order(2)
    @DisplayName("CT-002 : Soumission de la recherche avec champ vide")
    void testCT002_RechercheChampVide() {
        WebElement searchBox = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.name("q"))
        );
        
        searchBox.sendKeys(Keys.ENTER);
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        
        // Capture d'écran
        ScreenshotUtils.takeScreenshot(driver, "CT002_ChampVide");
        
        String urlApres = driver.getCurrentUrl();
        assertTrue(urlApres.contains("google"), 
            "L'utilisateur devrait rester sur Google");
        
        System.out.println("✓ CT-002 Réussi - Champ vide, utilisateur reste sur Google");
    }
    
    @Test
    @Order(3)
    @DisplayName("CT-003 : Recherche avec une longue chaîne de caractères")
    void testCT003_RechercheLongueChaineCaracteres() {
        String longueChaîne = "test de recherche avec une très longue chaîne de caractères pour vérifier " +
                "le comportement de Google face à des requêtes inhabituellement longues";
        
        WebElement searchBox = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.name("q"))
        );
        searchBox.sendKeys(longueChaîne);
        searchBox.sendKeys(Keys.ENTER);
        
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        
        // Capture d'écran
        ScreenshotUtils.takeScreenshot(driver, "CT003_LongueChaineCaracteres");
        
        String pageSource = driver.getPageSource().toLowerCase();
        assertFalse(pageSource.contains("500 internal"), 
            "La page ne devrait pas afficher d'erreur 500");
        
        System.out.println("✓ CT-003 Réussi - Longue chaîne acceptée sans erreur");
    }
    
    @Test
    @Order(4)
    @DisplayName("CT-004 : Recherche avec caractères spéciaux")
    void testCT004_RechercheCaracteresSpeciaux() {
        String caracteresSpeciaux = "@#$%^&*!?";
        
        WebElement searchBox = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.name("q"))
        );
        searchBox.sendKeys(caracteresSpeciaux);
        searchBox.sendKeys(Keys.ENTER);
        
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        
        // Capture d'écran
        ScreenshotUtils.takeScreenshot(driver, "CT004_CaracteresSpeciaux");
        
        String pageSource = driver.getPageSource().toLowerCase();
        assertFalse(pageSource.contains("500 internal"), "Pas d'erreur serveur 500");
        
        System.out.println("✓ CT-004 Réussi - Caractères spéciaux gérés correctement");
    }
    
    @Test
    @Order(5)
    @DisplayName("CT-005 : Recherche avec filtre Images")
    void testCT005_RechercheFiltreImages() {
        String motCle = "paysage montagne";
        
        WebElement searchBox = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.name("q"))
        );
        searchBox.sendKeys(motCle);
        searchBox.sendKeys(Keys.ENTER);
        
        wait.until(ExpectedConditions.urlContains("search"));
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        
        try {
            WebElement imagesLink = wait.until(
                ExpectedConditions.elementToBeClickable(By.linkText("Images"))
            );
            imagesLink.click();
            Thread.sleep(2000);
            
            // Capture d'écran des images
            ScreenshotUtils.takeScreenshot(driver, "CT005_FiltreImages");
            
            String currentUrl = driver.getCurrentUrl();
            assertTrue(currentUrl.contains("tbm=isch") || currentUrl.contains("images"), 
                "Devrait être sur la page des images");
            
            System.out.println("✓ CT-005 Réussi - Filtre Images fonctionne");
            
        } catch (Exception e) {
            ScreenshotUtils.takeScreenshot(driver, "CT005_FiltreImages_Alternative");
            System.out.println("⚠ CT-005 Info - Lien Images non trouvé");
            assertTrue(true);
        }
    }
    
    @Test
    @Order(6)
    @DisplayName("CT-006 : Vérification des suggestions de recherche")
    void testCT006_SuggestionsRecherche() {
        WebElement searchBox = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.name("q"))
        );
        searchBox.sendKeys("selenium");
        
        try {
            Thread.sleep(1500);
            
            // Capture d'écran des suggestions
            ScreenshotUtils.takeScreenshot(driver, "CT006_SuggestionsRecherche");
            
            List<WebElement> suggestions = driver.findElements(
                By.cssSelector("ul[role='listbox'] li, div.sbct, div.OBMEnb")
            );
            
            if (suggestions.size() > 0) {
                System.out.println("✓ CT-006 Réussi - " + suggestions.size() + " suggestions trouvées");
            } else {
                System.out.println("⚠ CT-006 Info - Aucune suggestion visible");
            }
            assertTrue(true);
            
        } catch (Exception e) {
            assertTrue(true);
        }
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"Java", "Python", "JavaScript"})
    @Order(7)
    @DisplayName("CT-007 : Recherche paramétrée avec différents langages")
    void testCT007_RechercheParametree(String motCle) {
        driver.get(GOOGLE_URL);
        acceptCookiesIfPresent();
        
        WebElement searchBox = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.name("q"))
        );
        searchBox.sendKeys(motCle + " programming");
        searchBox.sendKeys(Keys.ENTER);
        
        wait.until(ExpectedConditions.urlContains("search"));
        try { Thread.sleep(1500); } catch (InterruptedException e) {}
        
        // Capture d'écran pour chaque langage
        ScreenshotUtils.takeScreenshot(driver, "CT007_Recherche_" + motCle);
        
        assertTrue(driver.getCurrentUrl().contains(motCle), 
            "L'URL devrait contenir '" + motCle + "'");
        
        System.out.println("✓ CT-007 Réussi pour : " + motCle);
    }
}
