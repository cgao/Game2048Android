package edu.mit.cgao.game2048;

import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.Random;


public class MainActivity extends ActionBarActivity {

    private int[][] cellIDs = new int[4][4];
    private int[][] matrix = new int[4][4];
    private boolean newSeed = false;
    private int score = 0;
    private int zeroCounts = 16;
    private ActivitySwipeDetector swipeDetector;
    private TableLayout myTable;

    private int[] textColors = new int[12];
    private int[] bgColors = new int[12];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        textColors[0] = Color.BLACK;
        textColors[1] = Color.BLACK;
        textColors[2] = Color.BLACK;
        textColors[3] = Color.BLACK;
        textColors[4] = Color.BLACK;
        textColors[5] = Color.WHITE;
        textColors[6] = Color.WHITE;
        textColors[7] = Color.WHITE;
        textColors[8] = Color.WHITE;
        textColors[9] = Color.WHITE;
        textColors[10] = Color.WHITE;
        textColors[11] = Color.WHITE;

        bgColors[0] = Color.rgb(250,235,215);
        bgColors[1] = Color.rgb(255,235,205);
        bgColors[2] = Color.rgb(255,228,196);
        bgColors[3] = Color.rgb(245,222,179);
        bgColors[4] = Color.rgb(255,192,203);
        bgColors[5] = Color.rgb(255,105,180);
        bgColors[6] = Color.rgb(255,20,147);
        bgColors[7] = Color.rgb(199,21,133);
        bgColors[8] = Color.rgb(128,0,128);
        bgColors[9] = Color.rgb(205,92,92);
        bgColors[10] = Color.rgb(255,51,51);
        bgColors[11] = Color.rgb(255,0,0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initCellIDs();
        initMatrix();
        writeTextView();


        swipeDetector = new ActivitySwipeDetector(this);
        myTable = (TableLayout)findViewById(R.id.tableLayout);
        myTable.setOnTouchListener(swipeDetector);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_refresh){
            //showResetDialog();
            showGameOverDialog();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private int random(){
        // generate random number between 1 to zeroCounts
        int max = zeroCounts;
        int min = 1;
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    private int[] findRandom(int rd){
        int count  = 0;
        for (int i = 0; i < 4; ++i){
            for (int j = 0; j < 4; ++j){
                if (matrix[i][j] == 0){
                    ++count;
                    if(count == rd){
                        int[] result = {i,j};
                        return result;
                    }
                }
            }
        }
        int[] result = {-1,-1};
        return result;
    }

    private void initMatrix() {
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                setMatrixValue(0, i, j);
            }
        }
        int rd = random();
        int[] index = findRandom(rd);
        setMatrixValue(2,index[0],index[1]);
        newSeed = false;
        setScore(0);
    }

    public void resetGame(View view){
        initMatrix();
    }

    private void initCellIDs(){
        for (int i = 0; i < 4; ++i)
            for (int j = 0; j < 4; ++j) {
                String cellId = "cell" + i + j;
                cellIDs[i][j] = getResources().getIdentifier(cellId, "id", getPackageName());
            }
    }

    private void writeTextView() {

        CellTextView textViewToChange;
        for(int i = 0; i<4; ++i){
            for(int j = 0; j < 4; ++j) {
                textViewToChange = (CellTextView) findViewById(cellIDs[i][j]);
                if(textViewToChange != null){
                    if (matrix[i][j] != 0){
                        textViewToChange.setText(String.valueOf(matrix[i][j]));
                    }
                    else{
                        textViewToChange.setText("");
                    }

                }
            }
        }
    }

    private void setMatrixValue(int value, int i, int j){
        if(matrix[i][j] == 0 && value != 0) --zeroCounts;
        if(matrix[i][j] != 0 && value == 0) ++zeroCounts;
        matrix[i][j] = value;
        CellTextView textViewToChange = (CellTextView) findViewById(cellIDs[i][j]);
        if (matrix[i][j] != 0){
            textViewToChange.setText(String.valueOf(matrix[i][j]));
        }
        else{
            textViewToChange.setText("");
        }
        int colorIndex = 0;
        if (value != 0){
            colorIndex = (int) (Math.log(value)/Math.log(2));
        }
        textViewToChange.setTextColor(textColors[colorIndex]);
        textViewToChange.setBackgroundColor(bgColors[colorIndex]);
    }

    private void setScore(int score_){
        score = score_;
        TextView textViewToChange = (TextView) findViewById(R.id.score);
        textViewToChange.setText("Score: \n " + score);
    }

    private void gameEngine(int[] arr){
        boolean[] changed = {false,false,false,false};
        //arr[3]: no changes

        //arr[2]
        if (arr[2] != 0){
            if (arr[3] == 0){
                arr[3] = arr[2];
                arr[2] = 0;
                newSeed = true;
            }
            else{
                if (arr[3] == arr[2]){
                    arr[3] *= 2;
                    setScore(score + arr[3]);
                    arr[2] = 0;
                    changed[3] = true;
                    newSeed = true;
                }
            }
        }
        //arr[1]
        if (arr[1] != 0){
            if (arr[2] == 0 && arr[3] == 0){
                arr[3] = arr[1];
                arr[1] = 0;
                newSeed = true;
            }
            else if (arr[2] == 0){ //arr[3]!=0
                if (arr[3] == arr[1] && !changed[3]){
                    arr[3] *= 2;
                    setScore(score + arr[3]);
                }
                else{
                    arr[2] = arr[1];
                }
                arr[1] = 0;
                newSeed = true;
            }
            else{// arr[3]!=0, arr[2]!=0
                if (arr[2] == arr[1]){
                    arr[2] *= 2;
                    setScore(score + arr[2]);
                    changed[2] = true;
                    arr[1] = 0;
                    newSeed = true;
                }
            }
        }
        //arr[0]
        if (arr[0]!=0){
            if (arr[1] == 0 && arr[2] == 0 && arr[3] == 0){
                arr[3] = arr[0];
                arr[0] = 0;
                newSeed = true;
            }
            else if(arr[1] == 0 && arr[2] ==0){  //arr[3]!=0
                if (arr[3] == arr[0] && !changed[3]){
                    arr[3] *= 2;
                    setScore(score + arr[3]);
                    changed[3] = true;
                }
                else{
                    arr[2] = arr[0];
                }
                arr[0] = 0;
                newSeed = true;
            }
            else if(arr[1] == 0 ){  //arr[2]!=0, arr[3]!=0
                if(arr[2] == arr[0] && !changed[2]){
                    arr[2] *= 2;
                    setScore(score + arr[2]);
                }
                else{
                    arr[1] = arr[0];
                }
                arr[0] = 0;
                newSeed = true;
            }
            else { //arr[1]!=0, arr[2]!=0, arr[3]!=0
                if(arr[1] == arr[0]){
                    arr[1] *= 2;
                    setScore(score + arr[1]);
                    //changed[1] = true; //not necessary
                    arr[0] = 0;
                    newSeed = true;
                }
            }
        }
    }

    private boolean isGameOver(){
        if (zeroCounts != 0) return false;
        for (int i = 1; i < 4; ++i){
            for (int j = 1; j < 4; ++j){
                if (matrix[i][j] == matrix[i][j-1]) return false;
            }
        }
        for (int i = 1; i < 4; ++i){
            for (int j = 0 ; j < 4; ++j){
                if (matrix[i][j] == matrix[i-1][j]) return false;
            }
        }
        return true;
    }

    public void onSwipe(int indicator){
        if (isGameOver()){
            showGameOverDialog();
        }
        switch(indicator){
            case 1:
                newSeed = false;
                for (int j = 0; j < 4; ++j ){
                    int[] arr = {matrix[3][j],matrix[2][j],matrix[1][j],matrix[0][j]};
                    gameEngine(arr);
                    setMatrixValue(arr[0],3,j);
                    setMatrixValue(arr[1],2,j);
                    setMatrixValue(arr[2],1,j);
                    setMatrixValue(arr[3],0,j);
                }
                if(newSeed) {
                    int rd = random();
                    int[] index = findRandom(rd);
                    setMatrixValue(2, index[0], index[1]);
                    newSeed = false;
                }
                break;
            case 2:
                newSeed = false;
                for (int j = 0; j < 4; ++j ){
                    int[] arr = {matrix[0][j],matrix[1][j],matrix[2][j],matrix[3][j]};
                    gameEngine(arr);
                    setMatrixValue(arr[0],0,j);
                    setMatrixValue(arr[1],1,j);
                    setMatrixValue(arr[2],2,j);
                    setMatrixValue(arr[3],3,j);
                }
                if(newSeed){

                    int rd = random();
                    int[] index = findRandom(rd);
                    setMatrixValue(2,index[0],index[1]);
                    newSeed = false;
                }
                break;
            case 3:
                newSeed = false;
                for (int i = 0; i < 4; ++i ){
                    int[] arr = {matrix[i][3],matrix[i][2],matrix[i][1],matrix[i][0]};
                    gameEngine(arr);
                    setMatrixValue(arr[0],i,3);
                    setMatrixValue(arr[1],i,2);
                    setMatrixValue(arr[2],i,1);
                    setMatrixValue(arr[3],i,0);
                }
                if(newSeed){
                    int rd = random();
                    int[] index = findRandom(rd);
                    setMatrixValue(2,index[0],index[1]);
                    newSeed = false;
                }
                break;
            case 4:
                newSeed = false;
                for (int i = 0; i < 4; ++i ){
                    int[] arr = {matrix[i][0],matrix[i][1],matrix[i][2],matrix[i][3]};
                    gameEngine(arr);
                    setMatrixValue(arr[0],i,0);
                    setMatrixValue(arr[1],i,1);
                    setMatrixValue(arr[2],i,2);
                    setMatrixValue(arr[3],i,3);
                }
                if(newSeed){
                    int rd = random();
                    int[] index = findRandom(rd);
                    setMatrixValue(2,index[0],index[1]);
                    newSeed = false;
                }
                break;
            default:
                break;
        }
    }



    void showResetDialog() {
        String reset_title = getString(R.string.reset_title);
        String reset_msg = getString(R.string.reset_msg);
        DialogFragment newFragment = MainDialogFragment.newInstance
                (reset_title, reset_msg);
        newFragment.show(getFragmentManager(), "dialogReset");
    }

    void showGameOverDialog(){
        String gameover_title = getString(R.string.gameover_title);
        String gameover_msg = getString(R.string.gameover_msg,score);
        DialogFragment newFragment = MainDialogFragment.newInstance(gameover_title, gameover_msg);
        newFragment.show(getFragmentManager(), "dialogGameover");
    }


    public void doOkClick(){
        initMatrix();
    }

    public void doCancelClick(){
    
    }

}
