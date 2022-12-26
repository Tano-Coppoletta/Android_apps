package com.example.mp4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ImageView[][] mImageViews = new ImageView[3][3];
    private Button startGameButton;
    private TextView winText;
    private ArrayList<Integer> statusOfBoard;
    private String TAG = "MainActivity";
    public static final int START_GAME = 0 ;
    public static final int PLAYER1 = 1 ;
    public static final int PLAYER2 = 2 ;
    public static final int MAKE_MOVE1 = 5;
    public static final int MAKE_MOVE2 = 6;
    private static final int MAX_MOVES=30;
    private static final int RESTART=3;
    private boolean gaming,finish;

    private Player1 p1;
    private Player2 p2;
    private int numberOfMoves;
    private int restart;
    Random random = new Random();
    Runnable terminateThread1, terminateThread2;

    public final Handler mHandler = new Handler(Looper.getMainLooper()) {

        public void handleMessage(Message msg) {
            int what = msg.what ;
            int move1,delete1,move2,delete2;
            Message newMsg;
            switch (what) {
                case START_GAME:

                    numberOfMoves=0;
                    Toast.makeText(MainActivity.this,"Game Started!",Toast.LENGTH_LONG).show();
                    //background = board
                    for(int i=0;i<3;i++){
                        for(int j=0;j<3;j++){
                            mImageViews[i][j].setImageResource(R.drawable.white_cell);
                        }
                    }
                    winText.setText("");

                    newMsg = p1.handler.obtainMessage(MAKE_MOVE1);
                    newMsg.arg1=-1;
                    newMsg.arg2=-1;
                    newMsg.sendToTarget();
                    break;
                case MAKE_MOVE1:
                    if(!finish) {
                        numberOfMoves++;
                        //Toast.makeText(MainActivity.this,"1 move",Toast.LENGTH_LONG).show();
                        if ((statusOfBoard.get(0) == PLAYER2 && statusOfBoard.get(1) == PLAYER2 && statusOfBoard.get(2) == PLAYER2) ||
                                (statusOfBoard.get(3) == PLAYER2 && statusOfBoard.get(4) == PLAYER2 && statusOfBoard.get(5) == PLAYER2) ||
                                (statusOfBoard.get(6) == PLAYER2 && statusOfBoard.get(7) == PLAYER2 && statusOfBoard.get(8) == PLAYER2) ||
                                (statusOfBoard.get(0) == PLAYER2 && statusOfBoard.get(3) == PLAYER2 && statusOfBoard.get(6) == PLAYER2) ||
                                (statusOfBoard.get(1) == PLAYER2 && statusOfBoard.get(4) == PLAYER2 && statusOfBoard.get(7) == PLAYER2) ||
                                (statusOfBoard.get(2) == PLAYER2 && statusOfBoard.get(5) == PLAYER2 && statusOfBoard.get(8) == PLAYER2)) {
                            winText.setText("Blue wins");

                            gaming = false;
                            p1.handler.post(terminateThread1);
                            p2.handler.post(terminateThread2);
                            break;

                        }
                        if (numberOfMoves != MAX_MOVES) {
                            //chosen cell
                            move1 = msg.arg1;
                            //update the status of the board
                            statusOfBoard.set(move1, PLAYER1);
                            delete1 = msg.arg2;
                            if (delete1 != -1) {
                                //no image, the cell is now empty
                                mImageViews[(int) delete1 / 3][delete1 % 3].setImageResource(R.drawable.white_cell);
                                //update the status of the board
                                statusOfBoard.set(delete1, 0);

                            }
                            Log.i(TAG, " 1) ROW: " + move1 / 3 + "COL: " + move1 % 3);
                            mImageViews[(int) move1 / 3][move1 % 3].setImageResource(R.drawable.red_cell);

                            newMsg = p2.handler.obtainMessage(MAKE_MOVE2);
                            newMsg.arg1 = move1;
                            newMsg.arg2 = delete1;
                            newMsg.sendToTarget();
                        } else {

                            Toast.makeText(MainActivity.this, "Max number of moves reached!", Toast.LENGTH_SHORT).show();
                            gaming = false;
                            //stop the thread
                            p1.handler.post(terminateThread1);
                            p2.handler.post(terminateThread2);
                        }
                    }
                    break;
                case MAKE_MOVE2:
                    if(!finish) {
                        numberOfMoves++;
                        //Toast.makeText(MainActivity.this,"2 move",Toast.LENGTH_LONG).show();
                        if ((statusOfBoard.get(0) == PLAYER1 && statusOfBoard.get(1) == PLAYER1 && statusOfBoard.get(2) == PLAYER1) ||
                                (statusOfBoard.get(3) == PLAYER1 && statusOfBoard.get(4) == PLAYER1 && statusOfBoard.get(5) == PLAYER1) ||
                                (statusOfBoard.get(6) == PLAYER1 && statusOfBoard.get(7) == PLAYER1 && statusOfBoard.get(8) == PLAYER1) ||
                                (statusOfBoard.get(0) == PLAYER1 && statusOfBoard.get(3) == PLAYER1 && statusOfBoard.get(6) == PLAYER1) ||
                                (statusOfBoard.get(1) == PLAYER1 && statusOfBoard.get(4) == PLAYER1 && statusOfBoard.get(7) == PLAYER1) ||
                                (statusOfBoard.get(2) == PLAYER1 && statusOfBoard.get(5) == PLAYER1 && statusOfBoard.get(8) == PLAYER1)) {

                            winText.setText("Red wins");
                            gaming = false;
                            p1.handler.post(terminateThread1);
                            p2.handler.post(terminateThread2);
                            break;

                        }
                        if (numberOfMoves != MAX_MOVES) {
                            //chosen cell
                            move2 = msg.arg1;
                            //update the status of the board
                            statusOfBoard.set(move2, PLAYER2);
                            delete2 = msg.arg2;
                            Log.i(TAG, " 2) ROW: " + move2 / 3 + "COL: " + move2 % 3);
                            if (delete2 != -1) {
                                //no image, the cell is now empty
                                mImageViews[(int) delete2 / 3][delete2 % 3].setImageResource(R.drawable.white_cell);
                                //update the status of the board
                                statusOfBoard.set(delete2, 0);
                            }
                            mImageViews[(int) move2 / 3][move2 % 3].setImageResource(R.drawable.blue_cell);

                            newMsg = p1.handler.obtainMessage(MAKE_MOVE1);
                            newMsg.arg1 = move2;
                            newMsg.arg2 = delete2;
                            newMsg.sendToTarget();
                        } else {

                            Toast.makeText(MainActivity.this, "Max number of moves reached!", Toast.LENGTH_SHORT).show();
                            gaming = false;
                            //stop the thread
                            p1.handler.post(terminateThread1);
                            p2.handler.post(terminateThread2);
                        }
                    }
                    break;
                case RESTART:
                    //restart the game
                    //wait for two message of this type before restarting
                    //when we receive two messages both thread have completed the reset phase
                    if(restart<1){
                        restart++;
                        break;
                    }
                    finish=false;
                    restart=0;
                    //I am here if both the thread have completed the reset phase
                    numberOfMoves=0;
                    Toast.makeText(MainActivity.this,"Game Started!",Toast.LENGTH_LONG).show();
                    //background = board
                    for(int i=0;i<3;i++){
                        for(int j=0;j<3;j++){
                            mImageViews[i][j].setImageResource(R.drawable.white_cell);
                        }
                    }
                    winText.setText("");

                    newMsg = p1.handler.obtainMessage(MAKE_MOVE1);
                    newMsg.arg1=-1;
                    newMsg.arg2=-1;
                    newMsg.sendToTarget();
                    break;

                default:
                    Log.i(TAG, "We are not supposed to get here!");
                    break;
            }

        }
    }	; // Handler is associated with UI Thread

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        finish=false;
        //retrieve all the imageViews
        mImageViews[0][0]=findViewById(R.id.imageView);
        mImageViews[0][1]=findViewById(R.id.imageView2);
        mImageViews[0][2]= findViewById(R.id.imageView3);
        mImageViews[1][0]=findViewById(R.id.imageView4);
        mImageViews[1][1]=findViewById(R.id.imageView5);
        mImageViews[1][2] = findViewById(R.id.imageView6);
        mImageViews[2][0]=findViewById(R.id.imageView7);
        mImageViews[2][1]=findViewById(R.id.imageView8);
        mImageViews[2][2]=findViewById(R.id.imageView9);

        //background = board
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                mImageViews[i][j].setImageResource(R.drawable.white_cell);
            }
        }

        winText = findViewById(R.id.textView2);
        winText.setText("");
        //button
        startGameButton = findViewById(R.id.button);

        statusOfBoard = new ArrayList<>();
        for(int i=0;i<9;i++){
            statusOfBoard.add(0);
        }
        //true if game started
        gaming=false;


        startGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                 Message msg,msg2;
                if(gaming){
                    msg = p1.handler.obtainMessage(RESTART);
                    msg.sendToTarget();
                    msg2 = p2.handler.obtainMessage(RESTART);
                    msg2.sendToTarget();
                    finish=true;
                    numberOfMoves=0;
                    for(int i=0;i<3;i++){
                        for(int j=0;j<3;j++){
                            mImageViews[i][j].setImageResource(R.drawable.white_cell);
                        }
                    }
                    //reset the status board
                    for(int i=0;i<9;i++){
                        statusOfBoard.set(i,0);
                    }

                }else{
                    for(int i=0;i<9;i++){
                        statusOfBoard.set(i,0);
                    }
                    gaming=true;
                    p1 = new Player1("player1");
                    p2 = new Player2("player2");
                    p1.start();
                    p2.start();
                }
            }
        });
        //create runnable to terminate thread
        terminateThread1 = new Runnable() {
            @Override
            public void run() {
                p1.quit();
            }
        };
        terminateThread2 = new Runnable() {
            @Override
            public void run() {
                p2.quit();
            }
        };
    }


    class Player1 extends HandlerThread{
        public Handler handler;
        int nextCell;
        Message msg2,startGame;
        int cells, toBeDeleted;
        private ArrayList<Integer> board1= new ArrayList<>(9);
        public Player1(String name) {
            super(name);
            cells=0;

            for(int i=0;i<9;i++){
                board1.add(0);
            }
            startGame = mHandler.obtainMessage(MainActivity.START_GAME);
            mHandler.sendMessage(startGame);
        }

        protected void onLooperPrepared(){
            handler=new Handler(getLooper()){
                public void handleMessage(Message msg){

                    int what = msg.what ;
                    switch (what) {
                        case MAKE_MOVE1:

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if(msg.arg1!=-1) {
                                //set the cell used by 2
                                board1.set(msg.arg1, PLAYER2);
                            }
                            //retrieve the message
                            msg2 = mHandler.obtainMessage(MAKE_MOVE1);

                            if(msg.arg2!=-1) {
                                //remove the cell moved by 2
                                board1.set(msg.arg2,0);
                            }
                            //choose next cell
                            nextCell=random.nextInt(9);
                            while(board1.get(nextCell)!=0){
                                nextCell=random.nextInt(9);
                            }
                            if(cells<3){
                                cells++;
                                //no cells deleted, send this information to 2
                                msg2.arg2=-1;
                            }else{
                                toBeDeleted = random.nextInt(9);
                                while(board1.get(toBeDeleted)!=1){
                                    toBeDeleted = random.nextInt(9);
                                }
                                //delete one cell of 1
                                board1.set(toBeDeleted,0);
                                //send the index of the deleted cell
                                msg2.arg2 = toBeDeleted;

                                board1.set(toBeDeleted,0);

                            }
                            //send the index of the cell selected by 1
                            msg2.arg1 = nextCell;
                            //save the move of player 1
                            board1.set(nextCell,PLAYER1);

                            msg2.sendToTarget();


                            break;
                        case RESTART:
                            removeCallbacksAndMessages(null);
                            cells=0;
                            //reset the board
                            for(int i=0;i<9;i++){
                                board1.set(i,0);
                            }
                            msg2 = mHandler.obtainMessage(RESTART);
                            msg2.sendToTarget();
                            break;

                }
                };
            };
        }
    }


    class Player2 extends HandlerThread{
        public Handler handler;
        int nextCell;
        Message msg2,startGame;
        int cells, toBeDeleted;
        private ArrayList<Integer> board2= new ArrayList<>(9);
        public Player2(String name) {
            super(name);
            cells=0;

            for(int i=0;i<9;i++){
                board2.add(0);
            }
        }

        protected void onLooperPrepared(){
            handler=new Handler(getLooper()){
                public void handleMessage(Message msg){

                    int what = msg.what ;
                    switch (what) {
                        case MAKE_MOVE2:

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if(msg.arg1!=-1) {
                                //set the cell used by 2
                                board2.set(msg.arg1, PLAYER1);
                            }
                            //retrieve the message
                            msg2 = mHandler.obtainMessage(MAKE_MOVE2);

                            if(msg.arg2!=-1) {
                                //remove the cell moved by 2
                                board2.set(msg.arg2,0);
                            }
                            //choose next cell
                            int i=0;
                            while(board2.get(i)!=0){
                                i++;
                            }
                            nextCell=i;
                            Log.i(TAG,"NEXT2"+nextCell);
                            if(cells<3){
                                cells++;
                                //no cells deleted, send this information to 2
                                msg2.arg2=-1;
                            }else{
                                i=0;
                                while(board2.get(i)!=2){
                                    i++;
                                }
                                toBeDeleted=i;
                                //delete one cell of 1
                                board2.set(toBeDeleted,0);
                                //send the index of the deleted cell
                                msg2.arg2 = toBeDeleted;

                                board2.set(toBeDeleted,0);

                            }
                            //send the index of the cell selected by 1
                            msg2.arg1 = nextCell;
                            //save the move of player 1
                            board2.set(nextCell,PLAYER2);

                            msg2.sendToTarget();


                            break;
                        case RESTART:
                            removeCallbacksAndMessages(null);
                            cells=0;
                            //reset the board
                            for(i=0;i<9;i++){
                                board2.set(i,0);
                            }
                            msg2 = mHandler.obtainMessage(RESTART);
                            msg2.sendToTarget();
                            break;
                    }
                };
            };
        }
    }
}

