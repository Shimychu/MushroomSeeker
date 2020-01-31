package com.example.jimmy.mushroomseeker;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jimmy.mushroomseeker.model.GameLogic;

public class GameSettingActivity extends AppCompatActivity {

    GameLogic gameLogic;

    Spinner boardSpinner;
    Spinner mineSpinner;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public static String BOARD_STATE = "board_key";
    public static String MINE_STATE ="mine_key";
    public static String HIGH_SCORE = "high_score";
    private int boardCase;
    private int mineCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setting);

        gameLogic = GameLogic.getInstance();
        sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.app_name),Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        getViews();
        setSpinner();
    }

    private void getViews() {
        boardSpinner = findViewById(R.id.board_size_spinner);
        mineSpinner = findViewById(R.id.mine_size_spinner);
        Button b = findViewById(R.id.resetGamesPlayed);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameLogic.resetGamesPlayed();
                editor.putInt(GameScreenActivity.GAMES_COUNTED,gameLogic.getGamesPlayed());
                editor.clear();
                editor.apply();
                Toast.makeText(getApplicationContext(),"Reset games played as well as high scores.",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setSpinner(){
        //Board Spinner adapter
        ArrayAdapter<CharSequence> boardAdapter = ArrayAdapter.createFromResource(this,
                R.array.board_size,android.R.layout.simple_spinner_dropdown_item);
        boardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        boardSpinner.setAdapter(boardAdapter);
        boardCase = sharedPref.getInt(BOARD_STATE,0);
        boardSpinner.setSelection(boardCase);
        boardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    //4 x 6
                    case 0:
                        gameLogic.setBoardSizeX(4);
                        gameLogic.setBoardSizeY(6);
                        boardCase = i;
                        break;
                    //5 x 10
                    case 1:
                        gameLogic.setBoardSizeX(5);
                        gameLogic.setBoardSizeY(10);
                        boardCase = i;
                        break;
                    //6 x 15
                    case 2:
                        gameLogic.setBoardSizeY(6);
                        gameLogic.setBoardSizeY(15);
                        boardCase = i;
                        break;
                    default:
                        break;
                }
                editor.putInt(BOARD_STATE,boardCase);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //Mine Spinner adapter
        ArrayAdapter<CharSequence> mineAdapter = ArrayAdapter.createFromResource(this,
                R.array.mine_size,android.R.layout.simple_spinner_dropdown_item);
        mineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mineSpinner.setAdapter(mineAdapter);
        mineCase = sharedPref.getInt(MINE_STATE,0);
        mineSpinner.setSelection(mineCase);
        mineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    //6 mines
                    case 0:
                        gameLogic.setboardMushroom(6);
                        mineCase = i;
                        break;
                    //10 mines
                    case 1:
                        gameLogic.setboardMushroom(10);
                        mineCase = i;
                        break;
                    //15 mines
                    case 2:
                        gameLogic.setboardMushroom(15);
                        mineCase = i;
                        break;
                    //20 mines
                    case 3:
                        gameLogic.setboardMushroom(20);
                        mineCase = i;
                        break;
                    default:
                        break;
                }
                editor.putInt(MINE_STATE,mineCase);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}
