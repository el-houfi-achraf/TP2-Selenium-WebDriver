# Rapport TP Selenium WebDriver

## 1. Environnement utilisé

| Composant | Version / Détails |
|-----------|-------------------|
| **Système d'exploitation** | Windows 11 |
| **IDE** | VS Code / IntelliJ IDEA |
| **Java Development Kit** | JDK 17 ou supérieur |
| **Gestionnaire de projet** | Maven 3.9+ |
| **Selenium WebDriver** | 4.24.0 |
| **JUnit** | 5.10.1 |
| **WebDriverManager** | 5.6.2 |
| **Navigateur de test** | Google Chrome (dernière version) |

## 2. Structure du projet

```
TP_Selenium_WebDriver/
├── pom.xml                                    # Configuration Maven
├── src/
│   ├── main/java/com/tp/selenium/
│   │   ├── PremierTestSelenium.java          # Activité 1 - Premier script
│   │   └── RechercheGoogle.java              # Activité 2 - Recherche Google
│   └── test/java/com/tp/selenium/
│       ├── RechercheGoogleTest.java          # Activité 9 - JUnit Test
│       ├── GoogleSearchPlanTest.java         # Activité 10 - Plan de test Google
│       └── LoginFormTest.java                # Activité 11 - Plan de test Login
└── RAPPORT.md                                # Ce rapport
```

## 3. Scénarios de test implémentés

### 3.1 Activité 9 : Classe de test JUnit (RechercheGoogleTest)

Conversion de la classe `RechercheGoogle` en classe de test JUnit avec les assertions suivantes :

| Test | Description | Assertion |
|------|-------------|-----------|
| Test 1 | Vérification du titre de la page Google | `assertTrue(titre.contains("Google"))` |
| Test 2 | Présence du champ de recherche | `assertNotNull()`, `assertTrue(isDisplayed())` |
| Test 3 | Recherche avec mot clé valide | `assertTrue(titre.contains(motCle))` |
| Test 4 | Présence des résultats de recherche | `assertNotNull()`, `assertTrue(isDisplayed())` |

### 3.2 Activité 10 : Plan de test - Recherche Google (GoogleSearchPlanTest)

#### Plan de test

- **Objet** : Fonctionnalité de recherche sur la page d'accueil de Google
- **Portée** : Recherche valide, champ vide, caractères spéciaux, longue chaîne, filtre images
- **Environnement** : Chrome, Windows, Selenium WebDriver + JUnit 5

#### Cas de test implémentés

| ID | Scénario | Données de test | Résultat attendu |
|----|----------|-----------------|------------------|
| CT-001 | Recherche simple valide | "Selenium WebDriver" | Résultats affichés, titre contient le mot clé |
| CT-002 | Champ de recherche vide | (vide) | Utilisateur reste sur Google |
| CT-003 | Longue chaîne de caractères | 200+ caractères | Pas d'erreur |
| CT-004 | Caractères spéciaux | "@#$%^&*!?" | Pas d'erreur serveur |
| CT-005 | Filtre Images | "paysage montagne" | Redirection vers Google Images |
| CT-006 | Suggestions de recherche | "selenium" | Suggestions apparaissent |
| CT-007 | Test paramétré | Java, Python, JavaScript | Résultats pour chaque langage |

#### Risques identifiés

1. Changements fréquents de l'interface Google (sélecteurs CSS)
2. Captcha ou vérifications anti-bot
3. Variabilité des résultats selon la localisation géographique
4. Popup de consentement des cookies

#### Hypothèses

- Connexion internet stable
- Pas de blocage par Google
- Interface Google standard (pas de version beta)

### 3.3 Activité 11 : Plan de test - Formulaire de connexion (LoginFormTest)

#### Site de test utilisé

**SauceDemo** (https://www.saucedemo.com) - Site officiel de démonstration Selenium

#### Cas de test implémentés

| ID | Scénario | Données de test | Résultat attendu |
|----|----------|-----------------|------------------|
| CT-L01 | Connexion réussie | standard_user / secret_sauce | Redirection vers inventory |
| CT-L02 | Mot de passe incorrect | standard_user / wrong_password | Message d'erreur |
| CT-L03 | Login vide | (vide) / secret_sauce | "Username is required" |
| CT-L04 | Mot de passe vide | standard_user / (vide) | "Password is required" |
| CT-L05 | Compte verrouillé | locked_out_user / secret_sauce | Message "locked" |
| CT-L06 | Deux champs vides | (vide) / (vide) | Message d'erreur |
| CT-L07 | Déconnexion | Après connexion | Retour à la page login |

## 4. Difficultés rencontrées et solutions adoptées

### 4.1 Gestion des cookies Google

**Problème** : Google affiche un popup de consentement des cookies qui bloque les interactions.

**Solution** : Implémentation d'une méthode `acceptCookiesIfPresent()` qui détecte et clique sur le bouton d'acceptation si présent.

```java
try {
    WebElement acceptButton = wait.until(
        ExpectedConditions.elementToBeClickable(By.id("L2AGLb"))
    );
    acceptButton.click();
} catch (Exception e) {
    // Pas de popup
}
```

### 4.2 Attentes et synchronisation

**Problème** : Les éléments ne sont pas toujours immédiatement disponibles après le chargement de la page.

**Solution** : Utilisation de WebDriverWait avec ExpectedConditions au lieu de Thread.sleep().

```java
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
WebElement element = wait.until(
    ExpectedConditions.presenceOfElementLocated(By.name("q"))
);
```

### 4.3 Gestion du ChromeDriver

**Problème** : Nécessité de télécharger et configurer manuellement le ChromeDriver.

**Solution** : Utilisation de WebDriverManager qui télécharge automatiquement la version compatible.

```java
WebDriverManager.chromedriver().setup();
```

### 4.4 Variabilité des sélecteurs Google

**Problème** : Les sélecteurs CSS/ID de Google changent fréquemment.

**Solution** : Utilisation de sélecteurs stables comme `By.name("q")` pour le champ de recherche.

## 5. Questions de réflexion

### 5.1 Avantages de Selenium WebDriver

1. **Open source et gratuit**
2. **Multi-navigateurs** : Chrome, Firefox, Edge, Safari
3. **Multi-langages** : Java, Python, C#, JavaScript
4. **Intégration CI/CD** : Jenkins, GitHub Actions
5. **Grande communauté** et documentation riche

### 5.2 Inconvénients/Limites de Selenium

1. **Pas de support natif du reporting**
2. **Maintenance élevée** des sélecteurs
3. **Tests uniquement web** (pas d'apps desktop/mobile natives)
4. **Pas de gestion des captchas**
5. **Lenteur** comparé aux tests unitaires

### 5.3 Différence entre wait implicite et explicite

| Wait Implicite | Wait Explicite |
|----------------|----------------|
| S'applique globalement | S'applique à un élément spécifique |
| Défini une seule fois | Défini pour chaque attente |
| `driver.manage().timeouts().implicitlyWait()` | `WebDriverWait` + `ExpectedConditions` |
| Moins flexible | Plus flexible et précis |

### 5.4 Bonnes pratiques Selenium

1. **Utiliser le Page Object Model (POM)**
2. **Préférer les waits explicites** aux Thread.sleep()
3. **Utiliser des sélecteurs stables** (id, name plutôt que xpath complexes)
4. **Organiser les tests** avec @BeforeAll, @BeforeEach
5. **Capturer des screenshots** en cas d'échec
6. **Fermer proprement** le navigateur avec driver.quit()

## 6. Comment exécuter les tests

### Prérequis

- Java 17+
- Maven 3.9+
- Google Chrome installé

### Commandes

```bash
# Compiler le projet
mvn compile

# Exécuter tous les tests
mvn test

# Exécuter une classe de test spécifique
mvn test -Dtest=RechercheGoogleTest

# Exécuter avec rapport détaillé
mvn test -Dsurefire.useFile=false
```




