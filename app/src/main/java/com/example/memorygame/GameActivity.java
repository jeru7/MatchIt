package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    private Integer[] cardStickers = {
            R.drawable.jett_card, R.drawable.raze_boom_card, R.drawable.phx_boom_card,
            R.drawable.raze_cute_card, R.drawable.viper_card, R.drawable.brim_card, R.drawable.jett_card, R.drawable.raze_boom_card, R.drawable.phx_boom_card,
            R.drawable.raze_cute_card, R.drawable.viper_card, R.drawable.brim_card
    };

    private ImageButton[] cards;

    private int clicks = 0;
    private ImageButton firstClicked = null;
    private ImageButton secondClicked = null;
    private boolean isChecking = false;

    private int matchedCount = 0;

    private View resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        cards = new ImageButton[] {
                findViewById(R.id.card1), findViewById(R.id.card2), findViewById(R.id.card3), findViewById(R.id.card4),
                findViewById(R.id.card5), findViewById(R.id.card6), findViewById(R.id.card7), findViewById(R.id.card8),
                findViewById(R.id.card9), findViewById(R.id.card10), findViewById(R.id.card11), findViewById(R.id.card12)
        };

        resetButton = findViewById(R.id.resetButton);
        resetButton.setVisibility(View.GONE);

        List<Integer> stickerList = Arrays.asList(cardStickers);
        Collections.shuffle(stickerList);
        cardStickers = stickerList.toArray(new Integer[0]);

        assignBackCards();
        cardClickHandler();
    }

    private void reset() {
        matchedCount = 0;
        List<Integer> stickerList = Arrays.asList(cardStickers);
        Collections.shuffle(stickerList);
        cardStickers = stickerList.toArray(new Integer[0]);
        assignBackCards();
        resetButton.setVisibility(View.GONE);
    }

    protected void onResume() {
        super.onResume();
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });
    }

    private void flipCard(ImageButton card) {
        if (card.getTag() == null || !(boolean) card.getTag()) {
            int index = Arrays.asList(cards).indexOf(card);
            card.setImageResource(cardStickers[index]);
            card.setTag(true);
        } else {
            card.setImageResource(R.drawable.custom_card);
            card.setTag(false);
        }
    }

    private void cardClickHandler() {
        for (ImageButton card : cards) {
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isChecking) return;

                    if (clicks < 2) {
                        flipCard((ImageButton) v);
                        if (clicks == 0) {
                            firstClicked = (ImageButton) v;
                            clicks++;
                        } else {
                            secondClicked = (ImageButton) v;
                            isChecking = true;
                            clicks++;
                            checkCards();
                        }
                    }
                }
            });
        }
    }

    private void checkCards() {
        if (firstClicked == null || secondClicked == null) {
            resetChecker();
            return;
        }

        int firstIndex = Arrays.asList(cards).indexOf(firstClicked);
        int secondIndex = Arrays.asList(cards).indexOf(secondClicked);

        if (firstIndex == -1 || secondIndex == -1) {
            resetChecker();
            return;
        }

        int firstSticker = cardStickers[firstIndex];
        int secondSticker = cardStickers[secondIndex];

        if (firstSticker == secondSticker) {
            firstClicked.setEnabled(false);
            secondClicked.setEnabled(false);
            matchedCount++;
            Toast.makeText(this, "MATCHED!", Toast.LENGTH_SHORT).show();
            if(matchedCount == cardStickers.length / 2) {
                Toast.makeText(this, "ALL CARDS ARE MATCHED! YEY!", Toast.LENGTH_SHORT).show();
                resetButton.setVisibility(View.VISIBLE);
            }
            resetChecker();
        } else {
            Toast.makeText(this, "NOT MATCH!", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    flipCard(firstClicked);
                    flipCard(secondClicked);
                    resetChecker();
                }
            }, 500);
        }
    }

    private void resetChecker() {
        clicks = 0;
        firstClicked = null;
        secondClicked = null;
        isChecking = false;
    }

    private void assignBackCards() {
        for (ImageButton card : cards) {
            card.setImageResource(R.drawable.custom_card);
            card.setTag(false);
            card.setEnabled(true);
        }
    }
}
