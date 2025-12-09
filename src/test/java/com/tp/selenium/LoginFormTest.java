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
 * Activité 11 : Plan de test et cas de test - Formulaire de connexion
 * Site de test : https://www.saucedemo.com
 * Avec captures d'écran pour chaque cas de test
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Plan de Test - Formulaire de Connexion (Activité 11)")
public class LoginFormTest {

    private static WebDriver driver;
    private static WebDriverWait wait;
    
    private static final String LOGIN_URL = "https://www.saucedemo.com/";
    private static final String VALID_USERNAME = "standard_user";
    private static final String VALID_PASSWORD = "secret_sauce";
    private static final String LOCKED_USERNAME = "locked_out_user";
    private static final String INVALID_PASSWORD = "wrong_password";
    
    @BeforeAll
    static void setup() {
        System.setProperty("webdriver.gecko.driver",
                "C:\\Users\\ACHRAF\\Downloads\\geckodriver-v0.36.0-win32\\geckodriver.exe");
        
        FirefoxOptions options = new FirefoxOptions();
        options.setBinary("C:\\Program Files\\Firefox Developer Edition\\firefox.exe");
        
        driver = new FirefoxDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    
    @AfterAll
    static void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
    @BeforeEach
    void navigateToLoginPage() {
        driver.get(LOGIN_URL);
    }
    
    @Test
    @Order(1)
    @DisplayName("CT-L01 : Connexion réussie avec identifiants valides")
    void testCT_L01_ConnexionReussie() {
        WebElement usernameField = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.id("user-name"))
        );
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        
        usernameField.sendKeys(VALID_USERNAME);
        passwordField.sendKeys(VALID_PASSWORD);
        
        // Screenshot avant clic
        ScreenshotUtils.takeScreenshot(driver, "CTL01_AvantConnexion");
        
        loginButton.click();
        
        wait.until(ExpectedConditions.urlContains("inventory"));
        
        // Screenshot après connexion réussie
        ScreenshotUtils.takeScreenshot(driver, "CTL01_ConnexionReussie");
        
        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("inventory.html"), 
            "L'utilisateur devrait être redirigé vers la page inventory");
        
        System.out.println("✓ CT-L01 Réussi - Connexion valide, redirection vers inventory");
        
        logout();
    }
    
    @Test
    @Order(2)
    @DisplayName("CT-L02 : Rejet avec mot de passe incorrect")
    void testCT_L02_MotDePasseIncorrect() {
        WebElement usernameField = driver.findElement(By.id("user-name"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        
        usernameField.sendKeys(VALID_USERNAME);
        passwordField.sendKeys(INVALID_PASSWORD);
        loginButton.click();
        
        WebElement errorMessage = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-test='error']"))
        );
        
        // Screenshot du message d'erreur
        ScreenshotUtils.takeScreenshot(driver, "CTL02_MotDePasseIncorrect");
        
        assertTrue(errorMessage.isDisplayed(), "Un message d'erreur devrait être affiché");
        
        System.out.println("✓ CT-L02 Réussi - Message d'erreur affiché");
    }
    
    @Test
    @Order(3)
    @DisplayName("CT-L03 : Rejet avec champ login vide")
    void testCT_L03_LoginVide() {
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        
        passwordField.sendKeys(VALID_PASSWORD);
        loginButton.click();
        
        WebElement errorMessage = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-test='error']"))
        );
        
        // Screenshot du message d'erreur
        ScreenshotUtils.takeScreenshot(driver, "CTL03_LoginVide");
        
        assertTrue(errorMessage.isDisplayed(), "Un message d'erreur devrait être affiché");
        assertTrue(errorMessage.getText().contains("Username is required"), 
            "Le message devrait indiquer que le username est requis");
        
        System.out.println("✓ CT-L03 Réussi - Message d'erreur : Username is required");
    }
    
    @Test
    @Order(4)
    @DisplayName("CT-L04 : Rejet avec champ mot de passe vide")
    void testCT_L04_MotDePasseVide() {
        WebElement usernameField = driver.findElement(By.id("user-name"));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        
        usernameField.sendKeys(VALID_USERNAME);
        loginButton.click();
        
        WebElement errorMessage = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-test='error']"))
        );
        
        // Screenshot du message d'erreur
        ScreenshotUtils.takeScreenshot(driver, "CTL04_MotDePasseVide");
        
        assertTrue(errorMessage.isDisplayed(), "Un message d'erreur devrait être affiché");
        assertTrue(errorMessage.getText().contains("Password is required"), 
            "Le message devrait indiquer que le password est requis");
        
        System.out.println("✓ CT-L04 Réussi - Message d'erreur : Password is required");
    }
    
    @Test
    @Order(5)
    @DisplayName("CT-L05 : Rejet pour compte verrouillé")
    void testCT_L05_CompteVerrouille() {
        WebElement usernameField = driver.findElement(By.id("user-name"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        
        usernameField.sendKeys(LOCKED_USERNAME);
        passwordField.sendKeys(VALID_PASSWORD);
        loginButton.click();
        
        WebElement errorMessage = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-test='error']"))
        );
        
        // Screenshot du message d'erreur compte verrouillé
        ScreenshotUtils.takeScreenshot(driver, "CTL05_CompteVerrouille");
        
        assertTrue(errorMessage.isDisplayed(), "Un message d'erreur devrait être affiché");
        assertTrue(errorMessage.getText().toLowerCase().contains("locked"), 
            "Le message devrait indiquer que le compte est verrouillé");
        
        System.out.println("✓ CT-L05 Réussi - Message d'erreur : Compte verrouillé");
    }
    
    @Test
    @Order(6)
    @DisplayName("CT-L06 : Rejet avec les deux champs vides")
    void testCT_L06_DeuxChampsVides() {
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();
        
        WebElement errorMessage = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-test='error']"))
        );
        
        // Screenshot du message d'erreur
        ScreenshotUtils.takeScreenshot(driver, "CTL06_DeuxChampsVides");
        
        assertTrue(errorMessage.isDisplayed(), "Un message d'erreur devrait être affiché");
        
        System.out.println("✓ CT-L06 Réussi - Message d'erreur affiché pour champs vides");
    }
    
    @Test
    @Order(7)
    @DisplayName("CT-L07 : Vérification de la déconnexion")
    void testCT_L07_Deconnexion() {
        // Connexion
        WebElement usernameField = driver.findElement(By.id("user-name"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        
        usernameField.sendKeys(VALID_USERNAME);
        passwordField.sendKeys(VALID_PASSWORD);
        loginButton.click();
        
        wait.until(ExpectedConditions.urlContains("inventory"));
        
        // Ouvrir le menu
        WebElement menuButton = wait.until(
            ExpectedConditions.elementToBeClickable(By.id("react-burger-menu-btn"))
        );
        menuButton.click();
        
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        
        // Screenshot du menu ouvert
        ScreenshotUtils.takeScreenshot(driver, "CTL07_MenuOuvert");
        
        // Cliquer sur Logout
        WebElement logoutLink = wait.until(
            ExpectedConditions.elementToBeClickable(By.id("logout_sidebar_link"))
        );
        logoutLink.click();
        
        wait.until(ExpectedConditions.urlToBe(LOGIN_URL));
        
        // Screenshot après déconnexion
        ScreenshotUtils.takeScreenshot(driver, "CTL07_ApresDeconnexion");
        
        WebElement loginForm = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.id("login-button"))
        );
        assertTrue(loginForm.isDisplayed(), 
            "Le bouton de login devrait être visible après déconnexion");
        
        System.out.println("✓ CT-L07 Réussi - Déconnexion effectuée, retour au login");
    }
    
    private void logout() {
        try {
            WebElement menuButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("react-burger-menu-btn"))
            );
            menuButton.click();
            
            WebElement logoutLink = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("logout_sidebar_link"))
            );
            logoutLink.click();
            
            wait.until(ExpectedConditions.urlToBe(LOGIN_URL));
        } catch (Exception e) {
            driver.get(LOGIN_URL);
        }
    }
}
