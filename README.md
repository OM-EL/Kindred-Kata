
### Technical Decisions and Architecture Overview

#### 1. Use of Data Transfer Objects (DTOs):

DTOs are leveraged for interactions with the client. This approach not only streamlines data flow but also enhances system security by ensuring that only the relevant data gets communicated, thus minimizing the risk of unintended data exposure.

#### 2. Recommendation for Identifiers:

Instead of using basic Long values as identifiers for events and selections, it is recommended to adopt UUIDs (Universally Unique Identifiers). This provides an added layer of security by preventing malicious users from easily guessing or predicting IDs.

#### 3. Assumptions and Additions:

- **Stake in Bet Entity**: The amount wagered, termed as "mise", is essential to track to accurately determine payout amounts.
- **Odds in Bet Entity**: Odds at the time of placing a bet are crucial. Capturing this ensures that the payout is based on the odds agreed upon at the bet's inception.
- **Betting Direction**: The current design allows customers to bet on a Selection's outcome being true. It doesn't yet support betting against a Selection.
- **Client Identification**: The addition of the client ID to post requests aids in improved tracking and authentication.
- **HTTP Response Consistency**: For uniformity, specific scenarios, like an incorrect selection ID, should return a standardized 404 HTTP response.

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

