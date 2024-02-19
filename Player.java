import java.util.ArrayList;
import java.util.Arrays;

public class Player implements Cloneable {
    String playerName;
    Tile[] playerTiles;
    int numberOfTiles;

    public Player(String name) {
        setName(name);
        playerTiles = new Tile[15]; // there are at most 15 tiles a player owns at any time
        numberOfTiles = 0; // currently this player owns 0 tiles, will pick tiles at the beggining of the
                           // game
    }

    /*
     * TODO: checks this player's hand to determine if this player is winning
     * the player with a complete chain of 14 consecutive numbers wins the game
     * note that the player whose turn is now draws one extra tile to have 15 tiles
     * in hand,
     * and the extra tile does not disturb the longest chain and therefore the
     * winning condition
     * check the assigment text for more details on winning condition
     */
    // Yağmur
    public boolean checkWinning() {
        int consecutiveNumbers = 1;
        for (int i = 1; i < playerTiles.length; i++) {
            if (playerTiles[i - 1].getValue() + 1 == playerTiles[i].getValue()) {
                consecutiveNumbers += 1;
            }
        }
        if (consecutiveNumbers == 14) {
            return true;
        } else {
            return false;
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /*
     * TODO: used for finding the longest chain in this player hand
     * this method should iterate over playerTiles to find the longest chain
     * of consecutive numbers, used for checking the winning condition
     * and also for determining the winner if tile stack has no tiles
     */
    // Yağmur
    public int findLongestChain() {
        int longestChain;
        ArrayList<Integer> chainNumbers = new ArrayList<>();
        int tileNumberInChain = 1;

        for (int i = 1; i < playerTiles.length; i++) {
            if (playerTiles[i - 1].getValue() + 1 == playerTiles[i].getValue()) {
                tileNumberInChain += 1;
            }

            else if (playerTiles[i].getValue() - playerTiles[i - 1].getValue() > 1) {
                chainNumbers.add(tileNumberInChain);
                tileNumberInChain = 1;
            }
        }
        longestChain = chainNumbers.get(0);
        for (int i = 1; i < chainNumbers.size(); i++) {
            if (chainNumbers.get(i) > longestChain) {
                longestChain = chainNumbers.get(i);
            }
        }
        return longestChain;
    }

    /*
     * TODO: removes and returns the tile in given index position
     */
    
    public Tile getAndRemoveTile(int index) {
        ArrayList <Tile> tiles = new ArrayList<>();
        
        for (int i = 0; i < playerTiles.length; i ++){
            tiles.add(playerTiles [i]);
        }

        Tile removedTile = tiles.get(index);
        
        tiles.remove(index);
        playerTiles = tiles.toArray(new Tile [tiles.size()]);
        numberOfTiles --;
        return removedTile;

    }
    
    /*
     * TODO: adds the given tile to this player's hand keeping the ascending order
     * this requires you to loop over the existing tiles to find the correct
     * position,
     * then shift the remaining tiles to the right by one
     */
    // Zeynep
    public void addTile(Tile t) {
        
        playerTiles = Arrays.copyOf(playerTiles, playerTiles.length + 1);
        playerTiles[playerTiles.length - 1] = t;

        boolean findPlacement = false;

        for(int i = 0; i < playerTiles.length - 1 && !findPlacement; i++){
            
            if(t.getValue() < playerTiles[i].getValue()){
                for(int n = playerTiles.length - 1; n > i; n--){
                    playerTiles[n] = playerTiles[n-1];
                }

                playerTiles[i] = t;
                findPlacement = true;
            }
        } 
        numberOfTiles++;
    }

    /*
     * finds the index for a given tile in this player's hand
     */
    public int findPositionOfTile(Tile t) {
        int tilePosition = -1;
        for (int i = 0; i < numberOfTiles; i++) {
            if (playerTiles[i].matchingTiles(t)) {
                tilePosition = i;
            }
        }
        return tilePosition;
    }

    /*
     * displays the tiles of this player
     */
    public void displayTiles() {
        System.out.println(playerName + "'s Tiles:");
        for (int i = 0; i < playerTiles.length; i++) {
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
