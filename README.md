# CSOPESY S13 MP2

### Group 8 Members
- **ALABANZA**, Chelsea Alexandra R.
- **ANTONIO**, Luciana Francesca B.
- **LICUP**, Priscilla Mariah C.

### Submitted to:
**MANTUA**, Jonathan C. 

### Date submitted:
**2023**, December 4

---
# Documentation

## Synchronization Technique

The method synchronizes threads by using Java's `synchronized` keyword. This keyword ensures that only one thread at a time can complete a synchronized procedure or block by restricting access to critical sections. It gives a simple and effective method to avoid race problems and maintain data consistency in a multithreaded system.

## List of Synchronized Variables and Their Use

### `nTanks`, `nHealers`, `nDPS`
- **Use:** These variables represent the number of available tanks, healers, and DPS players, respectively.
- **Synchronization:** These variables are decremented within synchronized blocks to ensure atomicity and prevent race problems when multiple threads are modifying them.

### `dungeonThreads`
- **Use:** An array of `Dungeon` instances representing concurrent dungeons.
- **Synchronization:** Access to the `dungeonThreads` array is synchronized to avoid concurrent modification by multiple threads.

### `nUpdate`
- **Use:** Keeps track of the number of updates performed.
- **Synchronization:** Incremented within synchronized blocks to avoid race problems during updates.

### `Dungeon.isActive`, `Dungeon.nPartyServed`, `Dungeon.totalTime`, `Dungeon.currentPartyClearTime`
- **Use:** Represents the state and statistics of each dungeon instance.
- **Synchronization:** Access to these variables is synchronized to maintain consistency when multiple threads operate on the same dungeon instance.

## Code Explanation

### `Dungeon.emptyDungeon(Party party)`
- **Use:** Marks a dungeon as inactive and updates statistics when a party finishes clearing the dungeon.
- **Synchronization:** The method is synchronized to prevent multiple threads from concurrently modifying the state of the same dungeon.

### `Dungeon.setCurrentPartyClearTime(int clearTime)`
- **Use:** Sets the clear time for the active party in the dungeon.
- **Synchronization:** Synchronized to ensure that setting the clear time is atomic and consistent.

### `DungeonManager.printStatuses()`
- **Use:** Prints the current status of dungeons and player counts.
- **Synchronization:** The method is synchronized to prevent inconsistencies in the printed status when multiple threads are updating the program state simultaneously.

### `DungeonManager.printSummary()`
- **Use:** Prints the summary of each dungeon's performance.
- **Synchronization:** Synchronized to avoid race problems when printing the summary at the end of the program.

### `DungeonManager.enqueueParty()`
- **Use:** Enqueues a new party to an available dungeon.
- **Synchronization:** The method is synchronized to prevent race problems when checking for available dungeons and assigning parties.

### `DungeonManager.checkForAvailableDungeon()`
- **Use:** Checks if there are available dungeons and enqueues a party if found.
- **Synchronization:** Synchronized to ensure consistent checking and party enqueuing.

### Thread Sleep and Dungeon Update
- **Use:** The `Thread.sleep` method is used to simulate the time taken for a party to clear a dungeon.
- **Synchronization:** While not explicitly synchronized, the sleep occurs within the context of a new thread, preventing it from affecting the synchronization of other parts of the program.

