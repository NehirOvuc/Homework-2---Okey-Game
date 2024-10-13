import java.util.Arrays;

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
     * Removes and returns the tile in given index
     */
    public Tile getAndRemoveTile(int index) {
        Tile removedTile = playerTiles[index];
        // Shift all tiles after the removed one to the left
        for (int i = index; i < numberOfTiles - 1; i++) {
            playerTiles[i] = playerTiles[i + 1];
        }
        numberOfTiles--;  // Decrease the count of tiles
        Tile emptyTile = new Tile(8, 'E');
        playerTiles[numberOfTiles] = emptyTile;
        return removedTile;
    }

    /*
     * Adds the given tile to the playerTiles in order
     * should also update numberOfTiles accordingly.
     * Checks for playerTiles to be not more than 15 at any time
     */
    public void addTile(Tile t) {
        // Do nothing if player already has 15 tiles
        boolean enoughTiles = false;
        if (numberOfTiles == 15){
            enoughTiles = true;
        }
        int setTile;
        setTile = 0;
        if (numberOfTiles > 1  && !enoughTiles){
            // Find the correct position to insert the new tile
            for (int i = (numberOfTiles - 1); i >= 0 && !enoughTiles; i--) {
                if (playerTiles[i].compareTo(t) >= 0) {
                    setTile = i + 1;
                    //If greater than all, add to the end of the array
                    if (setTile == numberOfTiles){
                        playerTiles[i+1] = t;
                    }
                    // Shift the tiles to the right
                    else {
                        for (int j=numberOfTiles-1; j>=(setTile); j--){
                            playerTiles[j+1] = playerTiles[j];                  
                        }

                        playerTiles[setTile] = t;  // Insert the new tile
                    }
                    enoughTiles = true;
                }
            }
            //If there is no such position place the tile to the first index
            if (!enoughTiles){
                for (int k=numberOfTiles-1; k>=0; k--){
                    playerTiles[k+1] = playerTiles[k];                  
                }
                playerTiles[0] = t;
                enoughTiles = true;
            }
        }
        //special cases
        else if (numberOfTiles == 0){
            playerTiles[0] = t;
        }
        else if (numberOfTiles == 1){
            if(playerTiles[0].compareTo(t)>0){
                playerTiles[1] = t;
            }
            else{
                playerTiles[1] = playerTiles[0];
                playerTiles[0] = t;
            }
        }
        numberOfTiles++;
        
    }

    /*
     * Checks if this player's hand satisfies the winning condition
     * to win this player should have 3 chains of length 4, extra tiles
     * does not disturb the winning condition
     *
     */
    public boolean isWinningHand() {
        int playerChains = 0;

        int noOfTilesInChain = 1;
        Tile checkTile = playerTiles[0];

        for (int i = 0; i < numberOfTiles-2; i++){
            
            if(checkTile.canFormChainWith(playerTiles[i+1])){
                noOfTilesInChain ++;
            }
            //checks for four tiles in a chain to form a complete chain
            if(noOfTilesInChain == 4){
                playerChains ++;
                noOfTilesInChain = 1;
            }
            if(checkTile.getValue() != playerTiles[i+1].getValue()){
                noOfTilesInChain = 1;
            }
            if(checkTile.compareTo(playerTiles[i+1]) != 0){
                checkTile = playerTiles[i+1];
            }
            
        }
        if(checkTile.canFormChainWith(playerTiles[numberOfTiles-1])){
            noOfTilesInChain ++;
        }
        //checks for four tiles in a chain to form a complete chain
        if(noOfTilesInChain == 4){
            playerChains ++;
            noOfTilesInChain = 1;
        }
        //the player can only win when there are 3 complete chains
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

    //displays player's tiles
    public void displayTiles() {
        
        //prints out the tiles
        System.out.print(playerName + "'s Tiles: ");
        System.out.print("(");
        for (int i = 0; i < numberOfTiles-1; i++) {
            System.out.print(playerTiles[i].toString() + "-");
        }
        System.out.print(playerTiles[numberOfTiles-1] + ")");
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

    public void setTiles(Tile[] arr){
        playerTiles = arr;
    }

}
