import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class OkeyGame {

    Player[] players;
    Tile[] tiles;

    Tile lastDiscardedTile;

    int currentPlayerIndex = 0;

    public OkeyGame() {
        players = new Player[4];
    }

    public void createTiles() {
        tiles = new Tile[112];
        int currentTile = 0;

        // two copies of each color-value combination, no jokers
        for (int i = 1; i <= 7; i++) {
            for (int j = 0; j < 4; j++) {
                tiles[currentTile++] = new Tile(i,'Y');
                tiles[currentTile++] = new Tile(i,'B');
                tiles[currentTile++] = new Tile(i,'R');
                tiles[currentTile++] = new Tile(i,'K');
            }
        }
    }

    /*
     * TODO: distributes the starting tiles to the players
     * player at index 0 gets 15 tiles and starts first
     * other players get 14 tiles
     * this method assumes the tiles are already shuffled
     */
    public void distributeTilesToPlayers() {

        currentPlayerIndex = 0;
        // adding 1 tile so that first player has 15 tiles instead of 14
        getTopTile(); 

        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 14; j++)
            {
                getTopTile();
            }
            passTurnToNextPlayer();
        }
        currentPlayerIndex = 0;
    }

    public int getCurrentPlayersTiles(){
        return players[getCurrentPlayerIndex()].numberOfTiles;
    }

    /*
     * TODO: get the last discarded tile for the current player
     * (this simulates picking up the tile discarded by the previous player)
     * it should return the toString method of the tile so that we can print what we picked
     */
    public String getLastDiscardedTile() {
        players[getCurrentPlayerIndex()].addTile(lastDiscardedTile);
        return lastDiscardedTile.toString();
    }

    /*
     * Checks if there are any remaining tiles.
     */
    public boolean tilesFinished(){
        Tile topTile = tiles[0];
        Tile emptyTile = new Tile(8, 'E');
        if (topTile.compareTo(emptyTile) == 0){
            return true;}
        return false;
    }

    /*
     * TODO: get the top tile from tiles array for the current player
     * that tile is no longer in the tiles array (this simulates picking up the top tile)
     * it should return the toString method of the tile so that we can print what we picked
     */
    public String getTopTile() {

        Tile topTile = tiles[0];

        //shifts the remaining tiles to the left
        for (int i = 0; i < tiles.length-1; i++){
            tiles[i] = tiles[i+1];
        }
        //sets the last tile to empty tile
        Tile emptyTile = new Tile(8, 'E');
        tiles[tiles.length-1] = emptyTile;
        players[currentPlayerIndex].addTile(topTile);

        return topTile.toString();

    }

    /*
     * TODO: should randomly shuffle the tiles array before game starts
     */
    public void shuffleTiles() {

        ArrayList<Integer> indexes = new ArrayList<Integer>();
        Random rand = new Random();
        
        for (int i = 0; i < 112; i++){
            int index = rand.nextInt(112);
            while (indexes.contains(index)) // checking the index place is appropriate to fill or not
            {
                index = rand.nextInt(112);
            }
            indexes.add(index); // when the random index has been found adds the tile
            Tile temp = tiles[i];
            tiles[i] = tiles[index];
            tiles[index] = temp;
        }

    }

    /*
     * TODO: check if game still continues, should return true if current player
     * finished the game, use isWinningHand() method of Player to decide
     */
    public boolean didGameFinish() {
        int playerIndex = getCurrentPlayerIndex();

        if ((players[playerIndex]).isWinningHand()){
            return true;
        }
        else if (tilesFinished()){ //for the tie situations
            return true;
        }
        return false;
    }

    /*
     * TODO: Pick a tile for the current computer player using one of the following:
     * - picking from the tiles array using getTopTile()
     * - picking from the lastDiscardedTile using getLastDiscardedTile()
     * You should consider if the discarded tile is useful for the computer in
     * the current status. Print whether computer picks from tiles or discarded ones.
     */
    public void pickTileForComputer() {
        Player currPlayer = players[currentPlayerIndex];

        //here we first check whether the tile is useful for the player or not
        boolean isUsefulTile = false;
        for(int i = 0; i < currPlayer.numberOfTiles; i++){
            if(lastDiscardedTile.canFormChainWith(currPlayer.getTiles()[i])){
                //checks if there are any duplicates of the last discarded (NO)
                if(currPlayer.findPositionOfTile(lastDiscardedTile) == -1){
                    isUsefulTile = true; 
                    break;
                }
                
            }
        }

        //here if the tile is useful we print out the name of the player and the last discarded card
        if(isUsefulTile){ 
            System.out.println(currPlayer.getName() + " picks the last discarded tile: " + getLastDiscardedTile());
            //currPlayer.addTile(lastDiscardedTile); (NO)
            lastDiscardedTile = null; //so the last card is updated 
        }else{
            System.out.println(currPlayer.getName() + " picks the top tile: " + getTopTile()); //it prints out the top tile
        }
    }

    /*
     * TODO: Current computer player will discard the least useful tile.
     * this method should print what tile is discarded since it should be
     * known by other players. You may first discard duplicates and then
     * the single tiles and tiles that contribute to the smallest chains.
     */
    public void discardTileForComputer() {
        Player currPlayer = players[currentPlayerIndex];
        Tile tileDiscardFirst = null;
        boolean isUsefulTile = false; 

        //check for duplicates and if there is one discard it first 
        //while(tileDiscardFirst != null){ (NO)
            for(int i = 0; i < currPlayer.numberOfTiles; i++){
                for(int j = i + 1; j < currPlayer.numberOfTiles; j++){
                    if (currPlayer.getTiles()[i].compareTo(currPlayer.getTiles()[j]) == 0)
                    /* (NO)
                    if(currPlayer.getTiles()[i].getValue() == currPlayer.getTiles()[j].getValue() &&
                    currPlayer.getTiles()[i].getColor() == currPlayer.getTiles()[j].getColor()) */
                    {
                        tileDiscardFirst = currPlayer.getTiles()[i];
                        break;
                    }
                }
            }
        //}

        //if there no duplicates then 
        if(tileDiscardFirst == null){
            for(int i = 0; i < currPlayer.numberOfTiles; i++){
                isUsefulTile = false; 
                for(int j = 0; j < currPlayer.numberOfTiles; j++){
                    if(i != j && currPlayer.getTiles()[i].canFormChainWith(currPlayer.getTiles()[j])){
                        isUsefulTile = true; 
                        break; 
                    }
                }

                if(!isUsefulTile){
                    tileDiscardFirst = currPlayer.getTiles()[i];
                    break;
                }
            }
        }

        if (tileDiscardFirst == null) {
            tileDiscardFirst = currPlayer.getTiles()[0];
        }
        int discardAtIndex = currPlayer.findPositionOfTile(tileDiscardFirst);
        currPlayer.getAndRemoveTile(discardAtIndex);
        lastDiscardedTile = tileDiscardFirst;
    }

    /*
     * TODO: discards the current player's tile at given index
     * this should set lastDiscardedTile variable and remove that tile from
     * that player's tiles
     */
    public void discardTile(int tileIndex) {
        Player currentPlayer = players[currentPlayerIndex];
        /* already implemented in main (NO)
        if (tileIndex < 0 || tileIndex >= currentPlayer.numberOfTiles) {
            System.out.println("Invalid tile index. Please choose a valid tile.");
            return;
        }
        */
        Tile discardedTile = currentPlayer.getAndRemoveTile(tileIndex);
        this.lastDiscardedTile = discardedTile;
        System.out.println(currentPlayer.playerName + " discarded: " + discardedTile.toString());
        //passTurnToNextPlayer();
    }

    public void displayDiscardInformation() {
        if(lastDiscardedTile != null) {
            System.out.println("Last Discarded: " + lastDiscardedTile.toString());
        }
    }

   public void displayCurrentPlayersTiles() {
        players[currentPlayerIndex].displayTiles();
        //Player currentPlayer = players[currentPlayerIndex % players.length];
        //System.out.println(currentPlayer.playerName + "'s tiles:");
        //currentPlayer.displayTiles();
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public String getCurrentPlayerName() {
        return players[currentPlayerIndex].getName();
    }

    public void passTurnToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 4;
    }

    public void setPlayerName(int index, String name) {
        if(index >= 0 && index <= 3) {
            players[index] = new Player(name);
        }
    }

}
