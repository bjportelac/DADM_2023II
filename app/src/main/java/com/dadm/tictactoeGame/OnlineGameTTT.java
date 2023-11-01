package com.dadm.tictactoeGame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OnlineGameTTT extends AppCompatActivity {

    DatabaseReference db = null;

    LinearLayout p1Layout, p2Layout;
    ImageView im1, im2, im3, im4, im5, im6, im7, im8, im9;
    TextView p1Tag, p2Tag;

    String playerUniqueId, getPlayerName;
    String rivalUniqueId;

    boolean rivalFound = false;
    String status = "MATCHING";
    String playerTurn;

    final String CONNECTIONS = "connections";
    String connectionId;
    boolean connectionCreated = false;

    private final List<int[]> combinationsList = new ArrayList<>();
    private final List<String> doneBoxes = new ArrayList<>();

    private final String[] boxesSelectedBy = {"","","","","","","","",""};
    ValueEventListener turnsEventListener, wonEventListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_game_ttt);

        FirebaseApp.initializeApp(this);
        db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://tictactoe-ac76c-default-rtdb.firebaseio.com/");

        //Fill the combinations for winner
        fillCombinationsList(combinationsList);

        //Fill the visual elements of the view
        p1Layout = findViewById(R.id.player_1_layout); p2Layout = findViewById(R.id.player_2_layout);
        im1 = findViewById(R.id.image_1); im2 = findViewById(R.id.image_2);
        im3 = findViewById(R.id.image_3); im4 = findViewById(R.id.image_4);
        im5 = findViewById(R.id.image_5); im6 = findViewById(R.id.image_6);
        im7 = findViewById(R.id.image_7); im8 = findViewById(R.id.image_8);
        im9 = findViewById(R.id.image_9);
        p1Tag = findViewById(R.id.player_1_id); p2Tag = findViewById(R.id.player_2_id);

        //player data
        playerUniqueId = String.valueOf(System.currentTimeMillis());
        getPlayerName = getIntent().getStringExtra("playerNickName");
        p1Tag.setText(getPlayerName);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Waiting your rival");
        progressDialog.show();

        db.child(CONNECTIONS). addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!rivalFound){
                    if(snapshot.hasChildren()){
                        for(DataSnapshot connections: snapshot.getChildren()){
                            String connId = String.valueOf(connections.getKey());
                            int getPlayerCount = (int) connections.getChildrenCount();

                            if(status.equalsIgnoreCase("WAITING")){
                                if(getPlayerCount % 2 == 0){
                                    playerTurn = playerUniqueId;
                                    applyPlayerTurn(playerTurn);

                                    boolean playerFound = false;
                                    for(DataSnapshot players: connections.getChildren()){
                                        String getPlayerUniqueId = players.getKey();
                                        if(playerUniqueId.equalsIgnoreCase(getPlayerUniqueId)){
                                            playerFound = true;
                                        }else if(playerFound){
                                            String getRivalPlayerName = players.child("player_name").getValue(String.class);
                                            rivalUniqueId = players.getKey();
                                            p2Tag.setText(getRivalPlayerName);

                                            connectionId = connId;

                                            rivalFound = true;

                                            db.child("turns").child(connectionId).addValueEventListener(turnsEventListener);
                                            db.child("won").child(connectionId).addValueEventListener(wonEventListener);

                                            if(progressDialog.isShowing()){
                                                progressDialog.dismiss();
                                            }

                                            db.child(CONNECTIONS).removeEventListener(this);
                                        }
                                    }
                                }
                            }else{
                                if(getPlayerCount %2 == 1){
                                    connections.child(playerUniqueId).child("player_name").getRef().setValue(getPlayerName);
                                    for(DataSnapshot players: connections.getChildren()){
                                        String getRivalPlayerName = players.child("player_name").getValue(String.class);
                                        rivalUniqueId = players.getKey();
                                        p2Tag.setText(getRivalPlayerName);

                                        playerTurn = rivalUniqueId;
                                        applyPlayerTurn(playerTurn);

                                        connectionId = connId;
                                        rivalFound = true ;

                                        db.child("turns").child(connectionId).addValueEventListener(turnsEventListener);
                                        db.child("won").child(connectionId).addValueEventListener(wonEventListener);

                                        if(progressDialog.isShowing()){
                                            progressDialog.dismiss();
                                        }

                                        db.child(CONNECTIONS).removeEventListener(this);
                                        break;
                                    }
                                }
                            }
                        }

                        if(!rivalFound && !status.equalsIgnoreCase("WAITING") && !connectionCreated){
                            String connectionUniqueId = String.valueOf(System.currentTimeMillis());
                            snapshot.child(connectionUniqueId).child(playerUniqueId).child("player_name").getRef().setValue(getPlayerName);
                            status = "WAITING";
                            connectionCreated = true;

                        }

                    } else{
                        if(!connectionCreated){
                            String connectionUniqueId = String.valueOf(System.currentTimeMillis());
                            snapshot.child(connectionUniqueId).child(playerUniqueId).child("player_name").getRef().setValue(getPlayerName);
                            status = "WAITING";
                            connectionCreated = true;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        turnsEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    if(dataSnapshot.getChildrenCount()==2){
                        final int getBoxPosition = Integer.parseInt(Objects.requireNonNull(dataSnapshot.child("box_position").getValue(String.class)));
                        final String getPlayerId = dataSnapshot.child("player_id").getValue(String.class);

                        if(!doneBoxes.contains(String.valueOf(getBoxPosition))){
                            doneBoxes.add(String.valueOf(getBoxPosition));
                            assert getPlayerId != null;
                            if(getBoxPosition == 1){
                                selectedBox(im1,getBoxPosition,getPlayerId);
                            }else if(getBoxPosition == 2){
                                selectedBox(im2,getBoxPosition,getPlayerId);
                            }else if(getBoxPosition == 3){
                                selectedBox(im3,getBoxPosition,getPlayerId);
                            }else if(getBoxPosition == 4){
                                selectedBox(im4,getBoxPosition,getPlayerId);
                            }else if(getBoxPosition == 5){
                                selectedBox(im5,getBoxPosition,getPlayerId);
                            }else if(getBoxPosition == 6){
                                selectedBox(im6,getBoxPosition,getPlayerId);
                            }else if(getBoxPosition == 7){
                                selectedBox(im7,getBoxPosition,getPlayerId);
                            }else if(getBoxPosition == 8){
                                selectedBox(im8,getBoxPosition,getPlayerId);
                            }else if(getBoxPosition == 9){
                                selectedBox(im9,getBoxPosition,getPlayerId);
                            }

                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        wonEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("player_id")){
                    String getWinnerPlayerId = snapshot.child("player_id").getValue(String.class);
                    final WinDialog winDialog;
                    assert getWinnerPlayerId != null;
                    if(getWinnerPlayerId.equalsIgnoreCase(playerUniqueId)){
                        winDialog = new WinDialog(OnlineGameTTT.this, "You won the game!");
                    }else{
                        winDialog = new WinDialog(OnlineGameTTT.this, "Your rival has won!");
                    }
                    winDialog.setCancelable(false);
                    winDialog.show();

                    db.child("turns").child(connectionId).removeEventListener(turnsEventListener);
                    db.child("won").child(connectionId).removeEventListener(wonEventListener);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        im1.setOnClickListener(v -> {
            if(!doneBoxes.contains("1") && playerTurn.equalsIgnoreCase(playerUniqueId)){
                ((ImageView)v).setImageResource(R.drawable.online_x);
                db.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("box_position").setValue("1");
                db.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("player_id").setValue(playerUniqueId);
                playerTurn = rivalUniqueId;
            }
        });
        im2.setOnClickListener(v -> {
            if(!doneBoxes.contains("2") && playerTurn.equalsIgnoreCase(playerUniqueId)){
                ((ImageView)v).setImageResource(R.drawable.online_x);
                db.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("box_position").setValue("2");
                db.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("player_id").setValue(playerUniqueId);
                playerTurn = rivalUniqueId;
            }
        });
        im3.setOnClickListener(v -> {
            if(!doneBoxes.contains("3") && playerTurn.equalsIgnoreCase(playerUniqueId)){
                ((ImageView)v).setImageResource(R.drawable.online_x);
                db.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("box_position").setValue("3");
                db.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("player_id").setValue(playerUniqueId);
                playerTurn = rivalUniqueId;
            }
        });
        im4.setOnClickListener(v -> {
            if(!doneBoxes.contains("4") && playerTurn.equalsIgnoreCase(playerUniqueId)){
                ((ImageView)v).setImageResource(R.drawable.online_x);
                db.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("box_position").setValue("4");
                db.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("player_id").setValue(playerUniqueId);
                playerTurn = rivalUniqueId;
            }
        });
        im5.setOnClickListener(v -> {
            if(!doneBoxes.contains("5") && playerTurn.equalsIgnoreCase(playerUniqueId)){
                ((ImageView)v).setImageResource(R.drawable.online_x);
                db.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("box_position").setValue("5");
                db.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("player_id").setValue(playerUniqueId);
                playerTurn = rivalUniqueId;
            }
        });
        im6.setOnClickListener(v -> {
            if(!doneBoxes.contains("6") && playerTurn.equalsIgnoreCase(playerUniqueId)){
                ((ImageView)v).setImageResource(R.drawable.online_x);
                db.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("box_position").setValue("6");
                db.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("player_id").setValue(playerUniqueId);
                playerTurn = rivalUniqueId;
            }
        });
        im7.setOnClickListener(v -> {
            if(!doneBoxes.contains("7") && playerTurn.equalsIgnoreCase(playerUniqueId)){
                ((ImageView)v).setImageResource(R.drawable.online_x);
                db.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("box_position").setValue("7");
                db.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("player_id").setValue(playerUniqueId);
                playerTurn = rivalUniqueId;
            }
        });
        im8.setOnClickListener(v -> {
            if(!doneBoxes.contains("8") && playerTurn.equalsIgnoreCase(playerUniqueId)){
                ((ImageView)v).setImageResource(R.drawable.online_x);
                db.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("box_position").setValue("8");
                db.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("player_id").setValue(playerUniqueId);
                playerTurn = rivalUniqueId;
            }
        });
        im9.setOnClickListener(v -> {
            if(!doneBoxes.contains("9") && playerTurn.equalsIgnoreCase(playerUniqueId)){
                ((ImageView)v).setImageResource(R.drawable.online_x);
                db.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("box_position").setValue("9");
                db.child("turns").child(connectionId).child(String.valueOf(doneBoxes.size()+1)).child("player_id").setValue(playerUniqueId);
                playerTurn = rivalUniqueId;
            }
        });

    }

    private void fillCombinationsList(@NonNull List<int[]> combinations){

        combinations.add(new int[]{0, 1, 2});
        combinations.add(new int[]{3, 4, 5});
        combinations.add(new int[]{6, 7, 8});

        combinations.add(new int[]{0, 3, 6});
        combinations.add(new int[]{1, 4, 7});
        combinations.add(new int[]{2, 5, 8});

        combinations.add(new int[]{0, 4, 8});
        combinations.add(new int[]{2, 4, 6});
    }

    private void applyPlayerTurn(String id) {
        if (id.equalsIgnoreCase(playerUniqueId)) {
            p1Layout.setBackgroundColor(125);
            p2Layout.setBackgroundColor(200);
        } else {
            p2Layout.setBackgroundColor(125);
            p1Layout.setBackgroundColor(200);
        }
    }

    private void selectedBox(ImageView imageView, int selectedBoxPosition, String selectedByPlayer){
        boxesSelectedBy[selectedBoxPosition-1] = selectedByPlayer;
        if(selectedByPlayer.equalsIgnoreCase(playerUniqueId)){
            imageView.setImageResource(R.drawable.online_x);
            playerTurn = rivalUniqueId;
        }else{
            imageView.setImageResource(R.drawable.online_o);
            playerTurn = playerUniqueId;
        }

        applyPlayerTurn(playerTurn);
        if(checkPlayerWin(selectedByPlayer)){
            db.child("won").child(connectionId).child("player_id").setValue(selectedByPlayer);
        }

        if(doneBoxes.size() == 9){
            final WinDialog winDialog = new WinDialog(OnlineGameTTT.this,"It's a Draw");
            winDialog.setCancelable(false);
            winDialog.show();
        }
    }

    private boolean checkPlayerWin(String playerId){
        boolean playerWon = false;
        for(int i = 0; i < combinationsList.size(); i++){
            final int[] combination = combinationsList.get(i);
            if(boxesSelectedBy[combination[0]].equals(playerId)
                    && boxesSelectedBy[combination[1]].equals(playerId)
                    && boxesSelectedBy[combination[2]].equals(playerId)){
                playerWon = true;
            }
        }
        return playerWon;
    }

}