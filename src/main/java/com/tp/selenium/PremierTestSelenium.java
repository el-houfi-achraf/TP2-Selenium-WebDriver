package com.tp.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;


public class PremierTestSelenium {
    
    public static void main(String[] args) {
        // Configuration du chemin vers geckodriver
        System.setProperty("webdriver.gecko.driver",
                "C:\\Users\\ACHRAF\\Downloads\\geckodriver-v0.36.0-win32\\geckodriver.exe");
        
        // Configuration de Firefox Developer Edition
        FirefoxOptions options = new FirefoxOptions();
        options.setBinary("C:\\Program Files\\Firefox Developer Edition\\firefox.exe");
        
        // Instancier le navigateur Firefox
        WebDriver driver = new FirefoxDriver(options);
        
        try {
            // Ouvrir la page Google
            driver.get("https://www.google.com");
            
            // Maximiser la fenêtre
            driver.manage().window().maximize();
            
            // Afficher le titre de la page
            String titre = driver.getTitle();
            System.out.println("Titre de la page : " + titre);
            
            // Afficher l'URL actuelle
            String url = driver.getCurrentUrl();
            System.out.println("URL actuelle : " + url);
            
            // Attendre un peu pour voir le résultat
            Thread.sleep(2000);
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Fermer le navigateur
            driver.quit();
        }
    }
}
