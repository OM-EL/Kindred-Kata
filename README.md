Here are the step-by-step instructions to get the application running on your local machine, including setting up AWS:

1. **Java Development Kit (JDK)**:
    - Open your terminal and check for a preinstalled Java by running the following command:

        ```bash
        java -version
        ```

    - If JDK 17 isn't installed, you can install it. 
        - On Ubuntu-based distributions, use the following commands:

            ```bash
            sudo apt update
            sudo apt install openjdk-17-jdk
            ```

        - On macOS, use Homebrew:

            ```bash
            brew install openjdk@17
            ```

2. **Apache Maven**:
    - Check for preinstalled Maven:

        ```bash
        mvn -version
        ```

    - If a suitable version isn't installed, you can install it.
        - On Ubuntu-based distributions:

            ```bash
            sudo apt update
            sudo apt install maven
            ```

        - On macOS:

            ```bash
            brew install maven
            ```
6. **Navigate to your project directory**:
    - Now, you need to navigate to the directory where your `pom.xml` file is located. You can use the `cd` command to change directories:

        ```bash
        cd /path/to/your/project
        ```

    - Replace `/path/to/your/project` with the actual path to your project.

7. **Start your Spring Boot application**:
    - Use Maven to compile and run the application. Run this command in your terminal:

        ```bash
        mvn spring-boot:run
        ```


### Technical Decisions and Architecture Overview

#### 1. Use of Data Transfer Objects (DTOs):

DTOs are leveraged for interactions with the client. This approach not only streamlines data flow but also enhances system security by ensuring that only the relevant data gets communicated, thus minimizing the risk of unintended data exposure.

#### 2. Recommendation for Identifiers:

Instead of using basic Long values as identifiers for events and selections, it is recommended to adopt UUIDs. This makes it hard for to guess other ID.

#### 3. Assumptions and Additions:

- **Stake in Bet Entity**: The amount wagered, termed as "mise", is essential to track to accurately determine payout amounts.
- **Odds in Bet Entity**: Odds at the time of placing a bet are crucial. Capturing this ensures that the payout is based on the odds agreed upon at the bet's inception.
- **Betting Direction**: The current design allows customers to bet on a Selection's outcome being true. It doesn't yet support betting against a Selection.
- **Client Identification**: The addition of the client ID to post requests aids in improved tracking.
- **HTTP Response Consistency**: For uniformity, i supposed that when calling a the post end point with wrong selectionID we should return a 404 HTTP response.

#### 4. Enhanced Exception Handling:

The `ExceptionHttpTranslator` and `ExceptionType` were restructured to provide more nuanced exception handling and to relay custom HTTP response codes. Note: While some exceptions currently return their original error messages, this could expose internal system details. Future iterations should reassess this for tighter security.

#### 5. Optimistic Locking Mechanism:

To safeguard against placing bets on stale odds, particularly during multiple concurrent operations, optimistic locking has been introduced:

1. Each selection is tagged with a version number. This version is referenced when placing bets.
2. If there's any alteration in odds before the bet confirmation, the version number updates.
3. When the bet is about to be finalized, a version mismatch between the current and database records triggers an `OptimisticLockException`.
4. Users are notified with "The odds for the selection have changed. Please try again." in such cases, ensuring integrity.

The enhancements and architecture choices detailed above aim to ensure a secure, coherent, and efficient betting system, even under high concurrency loads.

---

### Known Issues:

Some tests might not execute successfully due to inconsistencies in the database's object IDs upon each application launch. Given that certain test values are hardcoded, this discrepancy can lead to test failures. Time constraints prevented the rectification of this issue, and it remains an area for future improvement.

