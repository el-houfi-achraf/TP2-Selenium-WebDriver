package com.tp.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;


public class RechercheGoogle {
    
    public static void main(String[] args) {
        // Configuration du chemin vers geckodriver
        System.setProperty("webdriver.gecko.driver",
                "C:\\Users\\ACHRAF\\Downloads\\geckodriver-v0.36.0-win32\\geckodriver.exe");
        
        // Configuration de Firefox Developer Edition
        FirefoxOptions options = new FirefoxOptions();
        options.setBinary("C:\\Program Files\\Firefox Developer Edition\\firefox.exe");
        
        WebDriver driver = new FirefoxDriver(options);
        
        try {
            // Ouvrir la page Google
            driver.get("https://www.google.com");
            driver.manage().window().maximize();
            
            // Attendre un peu pour le chargement
            Thread.sleep(1000);
            
            // Gérer le popup de consentement des cookies si présent
            try {
                WebElement acceptButton = driver.findElement(By.id("L2AGLb"));
                acceptButton.click();
                Thread.sleep(500);
            } catch (Exception e) {
                System.out.println("Pas de popup de cookies à fermer");
            }
            
            // Localiser le champ de recherche par son name
            WebElement champRecherche = driver.findElement(By.name("q"));
            
            // Saisir le mot clé
            champRecherche.sendKeys("Selenium WebDriver");
            
            // Soumettre le formulaire
            champRecherche.submit();
            
            // Attendre quelques secondes pour les résultats
            Thread.sleep(3000);
            
            // Vérifier le titre
            String titre = driver.getTitle();
            System.out.println("Titre après recherche : " + titre);
            
            // Vérifier que le titre contient le mot clé
            if (titre.contains("Selenium WebDriver")) {
                System.out.println("✓ Test réussi : Le titre contient le mot clé recherché");
            } else {
                System.out.println("✗ Test échoué : Le titre ne contient pas le mot clé");
            }
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
