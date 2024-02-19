import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SimplifiedOkeyGame {

    Player[] players;
    Tile[] tiles;
    int tileCount;
    int tileIndex;

    Tile lastDiscardedTile;

    int currentPlayerIndex = 0;

    public SimplifiedOkeyGame() {
        players = new Player[4];
    }

    public void sortAscendingOrder() {
        for (int i = 0; i < players.length; i++) {
            Arrays.sort(players[i].getTiles());
        }
    }

    public void createTiles() {
        tiles = new Tile[104];
        int currentTile = 0;

        // four copies of each value, no jokers
        for (int i = 1; i <= 26; i++) {
            for (int j = 0; j < 4; j++) {
                tiles[currentTile++] = new Tile(i);
            }
        }

        tileCount = 104;
    }

    /*
     * TODO: distributes the starting tiles to the players
     * player at index 0 gets 15 tiles and starts first
     * other players get 14 tiles, this method assumes the tiles are already
     * shuffled
     */
    public void distributeTilesToPlayers() {
        for (int i = 0; i < players.length; i++) {
            List<Tile> tileList = Arrays.asList(tiles);
            int a = 14;
            if (i == 0)
                a = 15;
            else {
                players[i].playerTiles = Arrays.copyOf(players[i].getTiles(), 14);
            }
            for (int times = 0; times < a; times++) {
                players[i].playerTiles[times] = tileList.get(tileIndex);
                players[i].numberOfTiles++;
                tileIndex++;
                tileCount--;
            }
            tileList.toArray(tiles);
        }
    }

    /*
     * TODO: get the last discarded tile for the current player
     * (this simulates picking up the tile discarded by the previous player)
     * it should return the toString method of the tile so that we can print what we
     * picked
     */
    // Amina
    public String getLastDiscardedTile() {
        if (lastDiscardedTile != null) {
            players[currentPlayerIndex].addTile(lastDiscardedTile);
            return lastDiscardedTile.toString();

        }
        return null;
    }

    /*
     * TODO: get the top tile from tiles array for the current player
     * that tile is no longer in the tiles array (this simulates picking up the top
     * tile)
     * and it will be given to the current player
     * returns the toString method of the tile so that we can print what we picked
     */
    // Amina
    public String getTopTile() {
        int lastT = Integer.MIN_VALUE; // index of last tile
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i] != null) {
                lastT = i;
            }
        }

        if (lastT >= 0) {
            Tile topT = tiles[lastT];
            players[currentPlayerIndex].addTile(topT);
            tiles[lastT] = null;
            tileCount --;
            return topT.toString();
        }

        return null;
    }

    /*
     * TODO: should randomly shuffle the tiles array before game starts
     */
    // Amina
    public void shuffleTiles() {
        Random rand = new Random();
        for (int i = tiles.length - 1; i > 0; i--) {
            int rTile = rand.nextInt(i + 1);

            Tile curr = tiles[i];
            tiles[i] = tiles[rTile];
            tiles[rTile] = curr;
        }
    }

    /*
     * TODO: check if game still continues, should return true if current player
     * finished the game. use checkWinning method of the player class to determine
     */
     // Yağmur
     public boolean didGameFinish() {
        boolean didFinish = false;
        if ( players[currentPlayerIndex].checkWinning() == true) 
        {
            didFinish = true;
        }
        else if ( tileCount == 0)
        {
            didFinish = true;
        }
        return didFinish;
    }

    /*
     * TODO: finds the player who has the highest number for the longest chain
     * if multiple players have the same length may return multiple players
     */
    //Yağmur
    public ArrayList<Player> getPlayerWithHighestLongestChain() {
        ArrayList <Player> winners = new ArrayList<>();
        int highestLongestChain = players[0].findLongestChain();
        for (int i = 1; i < players.length; i++) {
            if ( players[i].findLongestChain() > highestLongestChain) {
                highestLongestChain = players[i].findLongestChain();
            }
        }

        for (Player player : players) {
            if ( player.findLongestChain() == highestLongestChain) 
            {
                winners.add(player);
            }
        }

        return winners;
    }

    /*
     * checks if there are more tiles on the stack to continue the game
     */
    public boolean hasMoreTileInStack() {
        return tileCount != 0;
    }

    /*
     * TODO: pick a tile for the current computer player using one of the following:
     * - picking from the tiles array using getTopTile()
     * - picking from the lastDiscardedTile using getLastDiscardedTile()
     * you should check if getting the discarded tile is useful for the computer
     * by checking if it increases the longest chain length, if not get the top tile
     */
    public void pickTileForComputer() throws CloneNotSupportedException {
        Player compare = (Player) players[currentPlayerIndex].clone();
        compare.addTile(lastDiscardedTile);
        boolean doesDiscardedIncrease = compare.findLongestChain() > players[currentPlayerIndex].findLongestChain();
        if (doesDiscardedIncrease)
            System.out.println("Discarded tile gained: " + getLastDiscardedTile());
        else
            System.out.println("Top tile gained: " + getTopTile());
    }

    /*
     * TODO: Current computer player will discard the least useful tile.
     * you may choose based on how useful each tile is
     */
    public void discardTileForComputer() throws CloneNotSupportedException {
        Player compare = (Player) players[currentPlayerIndex].clone();
        boolean notDiscarded = true;
        for (int i = 0; i < compare.numberOfTiles; i++) {
            compare = (Player) players[currentPlayerIndex].clone();
            for (int a = i + 1; a < compare.numberOfTiles; a++) {
                if (notDiscarded && compare.getTiles()[i].compareTo(compare.getTiles()[a]) == 0) {
                    discardTile(i);
                    notDiscarded = false;
                    return;
                }
            }
            Tile tile = compare.getAndRemoveTile(i);
            if (notDiscarded && compare.findLongestChain() == players[currentPlayerIndex].findLongestChain()) {
                discardTile(i);
                notDiscarded = false;
                return;
            }
        }
        if (notDiscarded) {
            discardTile(0);
        }
        players[currentPlayerIndex].numberOfTiles--;
    }

    /*
     * TODO: discards the current player's tile at given index
     * this should set lastDiscardedTile variable and remove that tile from
     * that player's tiles
     */
    public void discardTile(int tileIndex) {
        lastDiscardedTile = players[currentPlayerIndex].playerTiles[tileIndex];
        System.out.println("Discarded " + lastDiscardedTile + " by " + players[currentPlayerIndex].getName());
        players[currentPlayerIndex].getAndRemoveTile(tileIndex);
        //added by Amina
        players[currentPlayerIndex].numberOfTiles--;
    }

    public void displayDiscardInformation() {
        if (lastDiscardedTile != null) {
            System.out.println("Last Discarded: " + lastDiscardedTile.toString());
        }
    }

    public void displayCurrentPlayersTiles() {
        players[currentPlayerIndex].displayTiles();
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
        if (index >= 0 && index <= 3) {
            players[index] = new Player(name);
        }
    }

}
