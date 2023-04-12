**Code For Java.net COPIED from:**
https://www.youtube.com/watch?v=-xKgxqG411c&t=124s&ab_channel=ThenisH



System design Link:
https://app.diagrams.net/#G1mXm-EJ9jqj7Wen7LumcE5jmvUrwand4U

UML Link:
https://app.diagrams.net/#G1puabpZ2ik9_ltHrRsJk_0B9Z8O2HP2gX

**Jason Lai**
Hello, we are designing a Java desktop/Java.net based version of Clue.


**Goal for Skeletal Requirement**

1. Server Starts
2. Server Initializes game board, rooms (including hallways, secret passages, etc), initializes and shuffles deck
2. Two clients open and connect to server
3. Server creates a player for each client
Server deals cards to each player, assigns them a character and location
 relays data about player back to client (character, location)
4. Player Actions:
	i. Player can ask server:
		a. what room it is in
		b. what cards are in the primaryCaseFile (normally illegal move)
	ii. Player can create a Suggestion and ask Server to check player 2’s cards
Just relay if player 2 has cards that invalidate player 1’s suggestion




**Subsystem Identification**

1. Client-Server Messaging System (Java.net, Radmin VPN)
2. Client Board System
3. Accusation/Suggestion System
4. Game Turn Management System
5. Server Data Structures - Not large systems but have some level of functionality and are required in other systems
Game Player - Linked List
Deck - Array
Game Board - Graph


Hello Brev