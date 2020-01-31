package com.example.jimmy.mushroomseeker;

import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jimmy.mushroomseeker.model.GameLogic;

public class GameScreenActivity extends AppCompatActivity {

    GameLogic game;

    Button buttons[][];

    MediaPlayer foundSound;
    MediaPlayer bushSound;


    public static String GAMES_COUNTED ="games_count";

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        sharedPref =  getApplicationContext().getSharedPreferences(getString(R.string.app_name),Context.MODE_PRIVATE);

        game = GameLogic.getInstance();

        game.setGamesPlayed(sharedPref.getInt(GAMES_COUNTED,0));
        game.addGamesPlayed();
        editor = sharedPref.edit();
        editor.putInt(GAMES_COUNTED,game.getGamesPlayed());
        editor.apply();
        game.setGameBoardType(sharedPref.getInt(GameSettingActivity.BOARD_STATE,0));
        game.setGameMineType(sharedPref.getInt(GameSettingActivity.MINE_STATE,0));
        buttons = new Button[game.getBoardSizeX()][game.getBoardSizeY()];

        foundSound =  MediaPlayer.create(this,R.raw.found);
        bushSound = MediaPlayer.create(this,R.raw.bush);

        populateButtons();
        updateTextView();
        setStaticTextView();
    }

    private void setStaticTextView(){
        TextView tvHighScoreText = findViewById(R.id.tvHighScoreText);
        int boardType = sharedPref.getInt(GameSettingActivity.BOARD_STATE,0);
        int mineType = sharedPref.getInt(GameSettingActivity.MINE_STATE,0);
        int previousHighScore = sharedPref.getInt(GameSettingActivity.HIGH_SCORE + String.valueOf(boardType) + String.valueOf(mineType),10000);
        System.out.println("THE HIGH SCORE IS: " + sharedPref.getInt(GameSettingActivity.HIGH_SCORE + String.valueOf(boardType) + String.valueOf(mineType),10000));
        if(previousHighScore >= 10000){
            tvHighScoreText.setText("HighScore: N/A");
        } else{
            tvHighScoreText.setText("Highscore: " + String.valueOf(previousHighScore));
        }
    }

    private void populateButtons() {
        TableLayout table = findViewById(R.id.game_table);
        for(int row = 0 ; row < game.getBoardSizeX() ; row++){
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,1.0f));
            table.addView(tableRow);
            for(int col = 0 ; col < game.getBoardSizeY() ; col++){
                final int FINAL_COL = col;
                final int FINAL_ROW = row;
                Button button = new Button(this);
                button.setText("");
                //Make text not clip
                button.setPadding(0,0,0,0);
                button.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,1.0f));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gridButtonClick(FINAL_ROW,FINAL_COL);
                    }
                });
                tableRow.addView(button);

                button.setBackgroundResource(R.drawable.maplestory_bush);

                buttons[row][col] = button;
            }
        }
    }

    private void gridButtonClick(int row, int col){
        Button button = buttons[row][col];

        game.addScans();
        //Lock button size
        lockButtonSize();

        //If found mushroom
        if(game.getCellState(row,col)){
            int newWidth = button.getWidth();
            int newHeight = button.getHeight();
            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mushroom);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
            Resources resource = getResources();
            button.setBackground(new BitmapDrawable(resource, scaledBitmap));
            game.addFoundMushroom(row,col);
            button.setEnabled(false);
            foundSound.start();
        } else {
            //Missed
            bushSound.start();
            button.setText(String.valueOf(game.getMushroomInRowCol(row, col)));
            wiggleRowCol(row,col);
        }
        if(game.checkWin()){
            saveHighScore();
            android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
            GameOverFragment dialog = new GameOverFragment();
            dialog.show(manager,"GameOverDialog");
        }
        updateTextView();
    }

    private void wiggleRowCol(int row,int col){
        for(int i = 0 ; i < game.getBoardSizeX() ; i++){
            for(int j = 0 ; j < game.getBoardSizeY() ; j++){
                Button button = buttons[i][j];
                if(i == row || j == col){
                    Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
                    button.startAnimation(shake);
                }
            }
        }
    }


    private void updateTextView(){
        TextView tvGameState = findViewById(R.id.tvGameState);
        TextView tvScanState = findViewById(R.id.tvScanState);
        TextView tvGamesPlayed = findViewById(R.id.tvGamesPlayed);

        tvGameState.setText(game.getMushroomState());
        tvScanState.setText(game.getScansState());
        tvGamesPlayed.setText(game.getGamesPlayedText());


        for(int row = 0 ; row < game.getBoardSizeX() ; row++){
            for(int col = 0 ; col < game.getBoardSizeY(); col++){
                Button button = buttons[row][col];

                if(!button.getText().equals("")){
                    button.setText(String.valueOf(game.getMushroomInRowCol(row,col)));
                }
            }
        }
    }


    private void lockButtonSize(){
        //Lock Button size
        for(int row = 0 ; row < game.getBoardSizeX() ; row++){
            for(int col = 0 ; col < game.getBoardSizeY(); col++){
                Button button = buttons[row][col];

                int width = button.getWidth();
                button.setMinWidth(width);
                button.setMaxWidth(width);

                int height = button.getHeight();
                button.setMinHeight(height);
                button.setMaxHeight(height);
            }
        }
    }

    private void saveHighScore(){
        int boardType = sharedPref.getInt(GameSettingActivity.BOARD_STATE,0);
        int mineType = sharedPref.getInt(GameSettingActivity.MINE_STATE,0);
        int previousHighScore = sharedPref.getInt(GameSettingActivity.HIGH_SCORE + String.valueOf(boardType) + String.valueOf(mineType),10000);
        if(game.getscans() < previousHighScore){
            previousHighScore = game.getscans();

            System.out.println("ALKDJF:SLKDJF:LSKDJFS SAVIN HIGHSCORE OF " + previousHighScore);
            editor = sharedPref.edit();
            editor.putInt(GameSettingActivity.HIGH_SCORE + String.valueOf(boardType) + String.valueOf(mineType) ,previousHighScore);
            editor.apply();
        }
    }

}
