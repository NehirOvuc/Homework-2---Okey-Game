public class Player {
    String playerName;
    Tile[] playerTiles;
    int numberOfTiles;

    public Player(String name) {
        setName(name);
        playerTiles = new Tile[15]; // there are at most 15 tiles a player owns at any time
        numberOfTiles = 0; // currently this player owns 0 tiles, will pick tiles at the beggining of the game
    }

    /*
     * TODO: removes and returns the tile in given index
     */
    public Tile getAndRemoveTile(int index) {
        Tile removedTile = playerTiles[index];
        // Shift all tiles after the removed one to the left
        for (int i = index; i < numberOfTiles - 1; i++) {
            playerTiles[i] = playerTiles[i + 1];
        }
        numberOfTiles--;  // Decrease the count of tiles
        return removedTile;
    }

    /*
     * TODO: adds the given tile to the playerTiles in order
     * should also update numberOfTiles accordingly.
     * make sure playerTiles are not more than 15 at any time
     */
    public void addTile(Tile t) {
        // Do nothing if player already has 15 tiles
        boolean enoughTiles = true;

        // Find the correct position to insert the new tile
        int i;
        if (numberOfTiles < 15){
        for (i = numberOfTiles - 1; i >= 0; i--) {
            if (playerTiles[i].compareTo(t) > 0) {
                playerTiles[i + 1] = playerTiles[i];  // Shift the tile to the right
            } else {
                enoughTiles = false;
            }
          }
        }

        if(enoughTiles == true){
            playerTiles[i + 1] = t;  // Insert the new tile
            numberOfTiles++;
        }
    }

    /*
     * TODO: checks if this player's hand satisfies the winning condition
     * to win this player should have 3 chains of length 4, extra tiles
     * does not disturb the winning condition
     * @return
     */
    public boolean isWinningHand() {
        int playerChains = 0;
        int i = 0;
         while(i < numberOfTiles){
            if (playerTiles[i + 1].canFormChainWith(playerTiles[i]) &&
                playerTiles[i + 2].canFormChainWith(playerTiles[i]) &&
                playerTiles[i + 3].canFormChainWith(playerTiles[i])) {
                playerChains++;
                i += 3;  // checks 3 tiles at once
            }
        }
        return playerChains == 3;
        
    }

    public int findPositionOfTile(Tile t) {
        int tilePosition = -1;
        for (int i = 0; i < numberOfTiles; i++) {
            if(playerTiles[i].compareTo(t) == 0) {
                tilePosition = i;
            }
        }
        return tilePosition;
    }

    public void displayTiles() {
        System.out.println(playerName + "'s Tiles:");
        for (int i = 0; i < numberOfTiles; i++) {
            System.out.print(playerTiles[i].toString() + " ");
        }
        System.out.println();
    }

    public Tile[] getTiles() {
        return playerTiles;
    }

    public void setName(String name) {
        playerName = name;
    }

    public String getName() {
        return playerName;
    }
}
