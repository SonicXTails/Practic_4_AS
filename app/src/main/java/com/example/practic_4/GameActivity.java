package com.example.practic_4;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private boolean isXturn = true;
    private Button[][] buttons = new Button[3][3];
    private int roundCount = 0;
    private boolean gameActive = true;
    private String mode;

    private int xWins = 0; // Количество побед X
    private int oWins = 0; // Количество побед O

    private TextView xScoreTextView; // Отображение побед X
    private TextView oScoreTextView; // Отображение побед O

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mode = getIntent().getStringExtra("mode");

        xScoreTextView = findViewById(R.id.x_score); // Инициализируем TextView для счёта
        oScoreTextView = findViewById(R.id.o_score);

        GridLayout gridLayout = findViewById(R.id.gridLayout);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button" + (i * 3 + j + 1);
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (gameActive) {
                            onButtonClicked((Button) v);
                        }
                    }
                });
            }
        }

        updateScoreBoard(); // Обновляем отображение счета при запуске игры
    }

    private boolean checkForWin() {
        String[][] field = new String[3][3];

        // Заполняем поле текущими значениями кнопок
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        // Проверка по горизонталям
        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals("")) {
                return true;
            }
        }

        // Проверка по вертикалям
        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals("")) {
                return true;
            }
        }

        // Проверка по диагоналям
        if (field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals("")) {
            return true;
        }
        if (field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].equals("")) {
            return true;
        }

        return false;
    }


    private void onButtonClicked(Button button) {
        if (!button.getText().toString().equals("")) {
            return;
        }

        if (isXturn) {
            button.setText("X");
        } else {
            button.setText("O");
        }

        roundCount++;

        if (checkForWin()) {
            gameActive = false;
            if (isXturn) {
                xWins++; // Увеличиваем счёт для X
                announceWinner("X");
            } else {
                oWins++; // Увеличиваем счёт для O
                announceWinner("O");
            }
        } else if (roundCount == 9) {
            gameActive = false;
            announceDraw();
        } else {
            isXturn = !isXturn;
            if (mode.equals("play_with_bot") && !isXturn) {
                botMove();
            }
        }
    }

    private void botMove() {
        Random rand = new Random();
        int row, col;
        do {
            row = rand.nextInt(3);
            col = rand.nextInt(3);
        } while (!buttons[row][col].getText().toString().equals(""));

        buttons[row][col].setText("O");
        roundCount++;
        if (checkForWin()) {
            gameActive = false;
            oWins++; // Увеличиваем счёт для O
            announceWinner("O");
        } else if (roundCount == 9) {
            gameActive = false;
            announceDraw();
        } else {
            isXturn = true;
        }
    }

    private void announceWinner(String winner) {
        Toast.makeText(this, "Победитель: " + winner, Toast.LENGTH_SHORT).show();
        updateScoreBoard(); // Обновляем счёт на экране
        resetBoard();
    }

    private void announceDraw() {
        Toast.makeText(this, "Ничья!", Toast.LENGTH_SHORT).show();
        resetBoard();
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        roundCount = 0;
        gameActive = true;
        isXturn = true;
    }

    private void updateScoreBoard() {
        xScoreTextView.setText("X: " + xWins); // Отображаем количество побед X
        oScoreTextView.setText("O: " + oWins); // Отображаем количество побед O
    }
}
