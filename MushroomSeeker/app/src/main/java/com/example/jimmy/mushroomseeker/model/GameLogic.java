package com.example.jimmy.mushroomseeker.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Jimmy on 2/16/2018.
 */

public class GameLogic {
    private int gamesPlayed;
    private int boardSizeX;
    private int boardSizeY;
    private int boardMushroom;
    private int scans;
    private int mushroomFound;
    private String mushroomState;
    private String scansState;
    private boolean[][] gameState;

    /*
        Singleton support
     */
    private static GameLogic instance;
    public static GameLogic getInstance(){
        if(instance == null){
            instance = new GameLogic();
            instance.gamesPlayed = 0;
            instance.setBoardSizeX(4);
            instance.setBoardSizeY(6);
            instance.setboardMushroom(6);
        }
        instance.setMushroomFound(0);
        instance.setscans(0);
        instance.setUpGameBoard();
        return instance;
    }

    private GameLogic(){
        //Private to prevent anyone from instanciate;
    }

    private void setUpGameBoard(){
        int gameSize = getBoardSizeX() * getBoardSizeY();
        gameState = new boolean[getBoardSizeX()][getBoardSizeY()];
        List<Boolean> gameSetup = new ArrayList<Boolean>();
        for(int i = 0 ; i < gameSize; i++){
            if(i < getboardMushroom()){
                gameSetup.add(true);
            } else {
                gameSetup.add(false);
            }

            System.out.println(gameSetup.get(i));
        }
        Collections.shuffle(gameSetup);
        int k = 0;
        for(int i = 0 ; i < getBoardSizeX(); i ++){
            for(int j = 0 ; j < getBoardSizeY() ; j++){
                gameState[i][j] = gameSetup.get(k);
                System.out.println(gameSetup.get(k));
                k++;
            }
        }
    }

    public boolean getCellState(int row, int col){
        return gameState[row][col];
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public String getGamesPlayedText() {
        return "Total number of games played: " + String.valueOf(gamesPlayed);
    }

    public void setGamesPlayed(int i){
        this.gamesPlayed = i;
    }

    public void addGamesPlayed() {
        this.gamesPlayed = gamesPlayed + 1;
    }

    public void resetGamesPlayed(){
        gamesPlayed = 0;
    }

    public int getBoardSizeX() {
        return boardSizeX;
    }

    public void setBoardSizeX(int boardSizeX) {
        this.boardSizeX = boardSizeX;
    }

    public int getBoardSizeY() {
        return boardSizeY;
    }

    public void setBoardSizeY(int boardSizeY) {
        this.boardSizeY = boardSizeY;
    }

    public int getboardMushroom() {
        return boardMushroom;
    }

    public void setboardMushroom(int boardMushroom) {
        this.boardMushroom = boardMushroom;
    }

    public int getscans() {
        return scans;
    }

    public void addScans() {
        this.scans = scans + 1;
    }

    public String getMushroomState() {
        mushroomState = "Found " + getMushroomFound() + " of " + getboardMushroom() + " Mushrooms.";
        return mushroomState;
    }

    public int getMushroomFound() {
        return mushroomFound;
    }

    public void setMushroomFound(int mushroomFound) {
        this.mushroomFound = mushroomFound;
    }

    public String getScansState() {
        scansState = "# Scans used: " + getscans();
        return scansState;
    }

    public void setscans(int scans) {
        this.scans = scans;
    }

    public void addFoundMushroom(int row,int col) {
        gameState[row][col] = false;
        this.mushroomFound = mushroomFound + 1;
    }

    public boolean checkWin(){
        if(getMushroomFound() == boardMushroom){
            return true;
        }
        return false;
    }

    public int getMushroomInRowCol(int row, int col){
        int number = 0 ;
        for(int i = 0 ; i < getBoardSizeX() ; i++){
            if(getCellState(i,col)){
                number++;
            }
        }
        for(int j = 0 ; j < getBoardSizeY() ; j++){
            //Remove double counting of mushroom
            if(getCellState(row,j)){
                number++;
            }
        }
        return number;
    }

    public void setGameBoardType(int i){
        switch(i){
            //4 x 6
            case 0:
                instance.setBoardSizeX(4);
                instance.setBoardSizeY(6);
                break;
            //5 x 10
            case 1:
                instance.setBoardSizeX(5);
                instance.setBoardSizeY(10);
                break;
            //6 x 15
            case 2:
                instance.setBoardSizeY(6);
                instance.setBoardSizeY(15);
                break;
            default:
                break;
        }
    }

    public void setGameMineType(int i){
        switch(i){
            //6 mines
            case 0:
                instance.setboardMushroom(6);

                break;
            //10 mines
            case 1:
                instance.setboardMushroom(10);
                break;
            //15 mines
            case 2:
                instance.setboardMushroom(15);
                break;
            //20 mines
            case 3:
                instance.setboardMushroom(20);
                break;
            default:
                break;
        }
    }

}
