## Guide d'exécution de l'application

Suivez ces instructions pas à pas pour faire fonctionner l'application sur votre machine locale, y compris la configuration d'AWS :

### 1. Java Development Kit (JDK) :
   - Ouvrez votre terminal et vérifiez si Java est déjà installé en exécutant la commande suivante :
     ```bash
     java -version
     ```

   - Si le JDK 17 n'est pas installé, procédez à son installation :
     - Sur les distributions basées sur Ubuntu :
       ```bash
       sudo apt update
       sudo apt install openjdk-17-jdk
       ```

     - Sur macOS, utilisez Homebrew :
       ```bash
       brew install openjdk@17
       ```

### 2. Apache Maven :
   - Vérifiez si Maven est déjà installé :
     ```bash
     mvn -version
     ```

   - Si une version appropriée n'est pas installée :
     - Sur Ubuntu :
       ```bash
       sudo apt update
       sudo apt install maven
       ```

     - Sur macOS :
       ```bash
       brew install maven
       ```

### 3. Naviguer vers le répertoire de votre projet :
   - Naviguez vers le répertoire contenant votre fichier `pom.xml` :
     ```bash
     cd /chemin/vers/votre/projet
     ```

     Remplacez `/chemin/vers/votre/projet` par le chemin réel de votre projet.

### 4. Démarrer votre application Spring Boot :
   - Utilisez Maven pour compiler et exécuter l'application :
     ```bash
     mvn clean package && java -jar target/unibet-live-test-0.0.1-SNAPSHOT.jar
     ```

