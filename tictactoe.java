// Blake Haddad
// CS-570
// Tic-Tac-Toe Assignment

import java.util.Iterator;
import java.util.Scanner;
import java.util.HashMap;
import javafx.util.Pair;
import java.io.*;

class tictactoe{

    int boardSize; //dimensions of board will be boardSize x boardSize (square)
    int numPlayers; //number of players between 2-26
    int currentPlayerNumber;
    int previousPlayerNumber;
    int winSequence;
    Pair lastPlayedCell; //used to check if previous played move caused a win
    HashMap movesPlayed = new HashMap<Pair,String>(); //maps a pair (coordinate board cell) to a string (player tile who owns the cell)
    Scanner reader = new Scanner(System.in);  // Reading from System.in
    String[] playerTiles = {"X","O","A","B","C","D","E","F","G","H","I","J","K","L","M","N","P","Q","R","S","T","U","V","W","Y","Z"};


    public static void main(String[] args){
        tictactoe game = new tictactoe();
        if(game.promptUserResumeGame()){ //Ask user if they want to resume the saved game
            game.loadGame(); //load previous values from saved game file
            System.out.println("\n\n-----Loaded Values-----");
            System.out.println("Number of players = "+game.getNumPlayers()+"\nBoard size = "+game.getBoardSize()+"\nCurrent player = " +game.getPlayerTileFromPlayerNumber(game.getCurrentPlayerNumber())+"\nWin sequence = "+game.getWinSequence());
            System.out.println("-----------------------\n");
        }else{ //New Game
            game.setNumPlayers(game.getNumberOfPlayersFromUser()); //prompt user for number of players
            boolean boardSizeCheck = false;
            while(boardSizeCheck == false){
                game.setBoardSize(game.getBoardSizeFromUser());
                boardSizeCheck = game.boardSizeCheck(game.getBoardSize(), game.getNumPlayers()); //check to see if inputted board 
                if(boardSizeCheck == false){
                    System.err.print("Error: The specified board size will not accomodate the number of players entered.\n\n");
                    return;
                }
                game.setWinSequence(game.getWinSequenceFromUser());
            }
            game.setCurrentPlayerNumber(0);
            game.setPreviousPlayerNumber(0);
        }
        game.setLastPlayedCell(new Pair(0,0));
        game.DisplayBoard();
        while(!game.checkWin(game.getLastPlayedCell(), game.getPlayerTileFromPlayerNumber(game.getPreviousPlayerNumber())) && !game.checkDraw()){
            String playerTile = game.getPlayerTileFromPlayerNumber(game.getCurrentPlayerNumber());
            game.addMove(game.promptForMove(playerTile),playerTile);
            game.DisplayBoard();
            game.setPreviousPlayerNumber(game.getCurrentPlayerNumber());
            game.setCurrentPlayerNumber(game.getCurrentPlayerNumber()+1);
            if(game.getCurrentPlayerNumber() == game.getNumPlayers()){
                game.setCurrentPlayerNumber(0);
            }
        }
        if(game.checkWin(game.getLastPlayedCell(), game.getPlayerTileFromPlayerNumber(game.getPreviousPlayerNumber()))){
            System.out.print("\nPlayer "+game.getPlayerTileFromPlayerNumber(game.getPreviousPlayerNumber())+ " wins!\n");
        }else{
            System.out.print("\nDraw!\n");
        }

    }

    public boolean promptUserResumeGame(){
        int resumeGame = -1;
        while(true){
                System.out.print("Resume saved game? (0 = No; 1=Yes): ");
                resumeGame = reader.nextInt(); // Scans the next token of the input as an int.
                if(resumeGame == 0 || resumeGame == 1){
                    break;
                }
        }
        if(resumeGame==0){
            return false;
        }else{
            return true;
        }
    }

    public int getBoardSizeFromUser(){
            
        int inputtedSize = 0;
        
        while (inputtedSize<=0 || inputtedSize>999){
            System.out.print("Enter a game board size: ");
            inputtedSize = reader.nextInt(); // Scans the next token of the input as an int.
        }
        System.out.print("\n");

        return inputtedSize;
    }

    public int getWinSequenceFromUser(){
            
        int winSequence = 0;
        
        while (winSequence<3 || winSequence>getBoardSize()){
            System.out.print("Enter a win sequence size (3 for normal tic-tac-toe style): ");
            winSequence = reader.nextInt(); // Scans the next token of the input as an int.
        }
        return winSequence;
    }

    public int getNumberOfPlayersFromUser(){
            
        int numberOfPlayers = 0;
        
        while (numberOfPlayers<2 || numberOfPlayers>27){
                System.out.print("How many players? (2-26): ");
                numberOfPlayers = reader.nextInt(); // Scans the next token of the input as an int.
        }
        System.out.print("\n");

        return numberOfPlayers;
    }

    public boolean boardSizeCheck(int boardSize, int numberOfPlayers){
        if(boardSize*boardSize > numberOfPlayers*2+1){
            return true;
        }else{
            return false;
        }
    }

    public String getPlayerTileFromPlayerNumber(int playerNum){
        return this.playerTiles[playerNum];
    }

    public int getBoardSize(){
        return this.boardSize;
    }

    public void setBoardSize(int boardSize){
        this.boardSize = boardSize;
    }

    public int getWinSequence(){
        return this.winSequence;
    }

    public void setWinSequence(int winSequence){
        this.winSequence = winSequence;
    }

    public int getNumPlayers(){
        return this.numPlayers;
    }

    public void setNumPlayers(int numPlayers){
        this.numPlayers = numPlayers;
    }

    public int getCurrentPlayerNumber(){
        return this.currentPlayerNumber;
    }

    public void setCurrentPlayerNumber(int currentPlayerNumber){
        this.currentPlayerNumber = currentPlayerNumber;
    }

    public int getPreviousPlayerNumber(){
        return this.previousPlayerNumber;
    }

    public void setPreviousPlayerNumber(int previousPlayerNumber){
        this.previousPlayerNumber = previousPlayerNumber;
    }

    public HashMap getMovesPlayed(){
        return this.movesPlayed;
    }

    public void setMovesPlayed(HashMap movesPlayed){
        this.movesPlayed.putAll(movesPlayed);;
    }

    public void addMove(Pair cell, String owner){
        setLastPlayedCell(cell);
        this.movesPlayed.put(cell, owner);
    }

    public void setLastPlayedCell(Pair lastPlayedCell){
        this.lastPlayedCell = lastPlayedCell;
    }

    public Pair getLastPlayedCell(){
        return this.lastPlayedCell;
    }

    public boolean checkWin(Pair p, String player){
        return checkHorizontalWin(p,player) || checkVerticalWin(p,player) || checkNegativeDiagonal(p, player) || checkPositiveDiagonal(p, player);
    }

    public boolean checkDraw(){
        HashMap moves = getMovesPlayed();
        int size = getBoardSize();
        for(int i = 1; i<=size; i++){
            for(int j = 1; j<=size; j++){
                if(!moves.containsKey(new Pair(i,j))){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkNegativeDiagonal(Pair p, String player){
        
        int upLeftCount = checkUpLeft(p, player);
        int downRightCount = checkDownRight(p, player);
        if(upLeftCount + downRightCount + 1 >= getWinSequence()){
            return true;
        }else{
            return false;
        }
    }

    public int checkUpLeft(Pair p, String player){
        HashMap playedMoves = getMovesPlayed();
        if(p.getKey().hashCode()>1 && p.getValue().hashCode()>1){
            Pair adjecentPiece = getAdjecentPair(p, "upleft");
            if(playedMoves.containsKey(adjecentPiece)){
                if(playedMoves.get(adjecentPiece).equals(player)){
                    return checkUpLeft(adjecentPiece, player) + 1;
                }else{
                    return 0;
                }
            }else{
                return 0;
            }
        }
        return 0;
    }

    public int checkDownRight(Pair p, String player){
        HashMap playedMoves = getMovesPlayed();
        if(p.getKey().hashCode()!=getBoardSize() && p.getValue().hashCode()!=getBoardSize() && p.getKey().hashCode()>1){
            Pair adjecentPiece = getAdjecentPair(p, "downright");
            if(playedMoves.containsKey(adjecentPiece)){
                if(playedMoves.get(adjecentPiece).equals(player)){
                    return checkDownRight(adjecentPiece, player) + 1;
                }else{
                    return 0;
                }
            }else{
                return 0;
            }
        }
        return 0;
    }

    public boolean checkPositiveDiagonal(Pair p, String player){
        
        int upRightCount = checkUpRight(p, player);
        int downLeftCount = checkDownLeft(p, player);
        if(upRightCount + downLeftCount + 1 >= getWinSequence()){
            return true;
        }else{
            return false;
        }
    }

    public int checkUpRight(Pair p, String player){
        HashMap playedMoves = getMovesPlayed();
        if(p.getKey().hashCode()>1 && p.getValue().hashCode()!=getBoardSize()){
            Pair adjecentPiece = getAdjecentPair(p, "upright");
            if(playedMoves.containsKey(adjecentPiece)){
                if(playedMoves.get(adjecentPiece).equals(player)){
                    return checkUpRight(adjecentPiece, player) + 1;
                }else{
                    return 0;
                }
            }else{
                return 0;
            }
        }
        return 0;
    }

    public int checkDownLeft(Pair p, String player){
        HashMap playedMoves = getMovesPlayed();
        if(p.getKey().hashCode()!=getBoardSize() && p.getValue().hashCode()>1){
            Pair adjecentPiece = getAdjecentPair(p, "downleft");
            if(playedMoves.containsKey(adjecentPiece)){
                if(playedMoves.get(adjecentPiece).equals(player)){
                    return checkDownLeft(adjecentPiece, player) + 1;
                }else{
                    return 0;
                }
            }else{
                return 0;
            }
        }
        return 0;
    }

    public boolean checkVerticalWin(Pair p, String player){
        
        int upCount = checkUpVertical(p, player);
        int downCount = checkDownVertical(p, player);
        if(upCount + downCount + 1 >= getWinSequence()){
            return true;
        }else{
            return false;
        }
    }

    public int checkUpVertical(Pair p, String player){
        HashMap playedMoves = getMovesPlayed();
        if(p.getKey().hashCode()>1){
            Pair adjecentPiece = getAdjecentPair(p, "up");
            if(playedMoves.containsKey(adjecentPiece)){
                if(playedMoves.get(adjecentPiece).equals(player)){
                    return checkUpVertical(adjecentPiece, player) + 1;
                }else{
                    return 0;
                }
            }else{
                return 0;
            }
        }
        return 0;
    }

    public int checkDownVertical(Pair p, String player){
        HashMap playedMoves = getMovesPlayed();
        if(p.getKey().hashCode()!=getBoardSize() && p.getKey().hashCode()>1){
            Pair adjecentPiece = getAdjecentPair(p, "down");
            if(playedMoves.containsKey(adjecentPiece)){
                if(playedMoves.get(adjecentPiece).equals(player)){
                    return checkDownVertical(adjecentPiece, player) + 1;
                }else{
                    return 0;
                }
            }else{
                return 0;
            }
        }
        return 0;
    }

    public boolean checkHorizontalWin(Pair p, String player){
        
        int leftCount = checkLeftHorizontal(p, player);
        int rightCount = checkRightHorizontal(p, player);
        if(leftCount + rightCount + 1 >= getWinSequence()){
            return true;
        }else{
            return false;
        }
    }

    public int checkLeftHorizontal(Pair p, String player){
        HashMap playedMoves = getMovesPlayed();
        if(p.getValue().hashCode()>1){ //checking to see if we are at the left edge of the board
            Pair adjecentPiece = getAdjecentPair(p, "left");
            if(playedMoves.containsKey(adjecentPiece)){
                if(playedMoves.get(adjecentPiece).toString().equals(player)){
                    return checkLeftHorizontal(adjecentPiece, player) + 1;
                }else{
                    return 0;
                }
            }else{
                return 0;
            }
        }
        return 0;
    }

    public int checkRightHorizontal(Pair p, String player){
        HashMap playedMoves = getMovesPlayed();
        if(p.getValue().hashCode()!=getBoardSize() && p.getValue().hashCode()>1){
            Pair adjecentPiece = getAdjecentPair(p, "right");
            if(playedMoves.containsKey(adjecentPiece)){
                if(playedMoves.get(adjecentPiece).equals(player)){
                    return checkRightHorizontal(adjecentPiece, player) + 1;
                }else{
                    return 0;
                }
            }else{
                return 0;
            }
        }
        return 0;
    }

    public Pair getAdjecentPair(Pair p,String position){
        switch(position){
            case "left":
                return new Pair(p.getKey().hashCode(),p.getValue().hashCode()-1);
            case "right":
                return new Pair(p.getKey().hashCode(),p.getValue().hashCode()+1);
            case "up":
                return new Pair(p.getKey().hashCode()-1,p.getValue().hashCode());
            case "down":
                return new Pair(p.getKey().hashCode()+1,p.getValue().hashCode());
            case "upleft":
                return new Pair(p.getKey().hashCode()-1,p.getValue().hashCode()-1);
            case "upright":
                return new Pair(p.getKey().hashCode()-1,p.getValue().hashCode()+1);
            case "downleft":
                return new Pair(p.getKey().hashCode()+1,p.getValue().hashCode()-1);
            case "downright":
                return new Pair(p.getKey().hashCode()+1,p.getValue().hashCode()+1);
            default :
                return p;
        }
    }

    public Pair promptForMove(String playerLetter){
        HashMap playedMoves = getMovesPlayed(); // Need to check if placement is already in use
        int size = getBoardSize();
        int row = 0;
        int col = 0;
        String move;
        boolean moveCheck = true;
        boolean emptyCellCheck = false;
        System.out.print("Player "+playerLetter+" it's your turn.\n");
        while(emptyCellCheck == false){
            while (moveCheck){
                System.out.print("Enter row & column number separated by a space (Enter Q to save and quit): ");
                move = reader.nextLine(); // Scans the next token of the input as an int.
                while(move.isEmpty()){
                    move = reader.nextLine();
                }
                if(move.length() == 1 && (move.charAt(0) == 'Q' || move.charAt(0) == 'q')){
                    saveGame();
                    System.exit(0);
                }
                String[] numbers = move.split(" ");
                if(numbers.length == 2){
                    row = Integer.parseInt(numbers[0]);
                    col = Integer.parseInt(numbers[1]);
                    System.out.println("\nrow & col = "+row+" "+col);
                    moveCheck = false;
                }

            }

            if(playedMoves.containsKey(new Pair(row,col))){
                System.out.print("\nMust choose a cell that is not already taken.\n\n");
                row = 0;
                col = 0;
                emptyCellCheck = false;
                moveCheck = true;
            }else{
                emptyCellCheck = true;
            }
        }
        return new Pair(row,col);
    }
    
    public String checkForOwnedCell(Pair p){
        HashMap playedMoves = getMovesPlayed();
        boolean containsPair = playedMoves.containsKey(p);
        if(containsPair){
            return playedMoves.get(p).toString();
        }else{
            return " ";
        }

    }

    public void DisplayBoard(){
        int size = getBoardSize();
        System.out.print(" ");
        for(int row = 1; row<=size; row++){
            
            if(row == 1){
                for(int col = 1; col<=size; col++){
                    if(col>100){
                        System.out.print(" "+col);
                    }else if(col==100){
                        System.out.print("  "+col);
                    }else if(col>=10){
                        System.out.print("  "+col);
                    }else{
                        System.out.print("   "+col);
                    }
                }
            }
            
            System.out.print("\n");

            for(int col = 1; col<=size; col++){
                if(col == 1){
                    if(row>=100){
                        System.out.print(row);
                    }else if(row>=10){
                        System.out.print(row+" ");
                    }else{
                        System.out.print(row+"  ");
                    }
                }
                if(col != size || row != size){
                    System.out.print(" "+checkForOwnedCell(new Pair(row,col))+" ");
                    if(col != size){
                        System.out.print("|");
                    }
                }
                if(col == size && row == size){
                    System.out.print(" "+checkForOwnedCell(new Pair(row,col)));
                }
            }

            System.out.print("\n");
            
            if(row !=size){
                for(int col = 1; col<=size; col++){
                    if(col == 1){
                        System.out.print("   ");
                    }
                    if(col != size && row != size){
                        System.out.print("---+");
                }   else if(col == size){
                        System.out.print("---");
                    }
                }
            }
        }
    }

    public void saveGame(){
        String filename;
        System.out.print("Enter a file name (do not include file extension): ");
        filename = reader.nextLine(); // Scans the next token of the input.
        try{
        //saveMap();
        saveGameValues(filename);
        System.out.print("Game saved successfully! Exiting...\n");
        System.exit(0);
        }catch(Exception e){
            System.err.print("Error saving game with exception: "+e);
            System.exit(0);
        }
    }

    public void loadGame(){
        try{
            String filename;
            System.out.print("Enter a saved file name (do not include file extension): ");
            filename = reader.nextLine(); // Scans the next token of the input.
            while(filename.isEmpty()){
                filename = reader.nextLine();
            }
            //loadSavedMap();
            loadGameValues(filename);
            System.out.print("Game loaded successfully!");
        }catch(Exception e){
                System.err.print("Error loading game with exception:\n"+e);
                System.exit(0);
        }
    }

    public void saveGameValues(String filename){
        try {
            FileOutputStream fileOut = new FileOutputStream("./"+filename+".txt");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            // write something in the file
            out.writeObject(getMovesPlayed());
            out.writeObject(getNumPlayers());
            out.writeObject(getBoardSize());
            out.writeObject(getCurrentPlayerNumber());
            out.writeObject(getPreviousPlayerNumber());
            out.writeObject(getWinSequence());
            out.writeObject(getLastPlayedCell());
            out.close();
            fileOut.close();
         } catch (IOException i) {
            System.err.print("Error saving game with exception:\n"+i);
            i.printStackTrace();
            System.exit(0);
         }
    }

    public void loadGameValues(String filename){
        try {
            FileInputStream fileIn = new FileInputStream("./"+filename+".txt");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            setMovesPlayed((HashMap<Pair,String>) in.readObject());
            setNumPlayers((int) in.readObject());
            setBoardSize((int) in.readObject());
            setCurrentPlayerNumber((int) in.readObject());
            setPreviousPlayerNumber((int) in.readObject());
            setWinSequence((int) in.readObject());
            setLastPlayedCell((Pair) in.readObject());
            in.close();
            fileIn.close();

         } catch (IOException i) {
            System.err.print("Error loading game with exception:\n"+i);
            i.printStackTrace();
            System.exit(0);
         } catch (ClassNotFoundException c) {
            System.out.println("Error loading game with exception:\n"+c);
            c.printStackTrace();
            System.exit(0);
         }
    }
}