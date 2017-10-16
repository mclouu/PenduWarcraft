package fr.long_pixel.penduwarcraft;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout container;
    private TextView letters_tapees;
    private Button btn_send;
    private EditText et_letter;
    private ImageView imagePendu;

    private String word;
    private int found;
    private int error;
    private List<Character> listOfLetters = new ArrayList<>();
    private List<String> wordList = new ArrayList<>();
    private boolean win;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = findViewById(R.id.activity_main_word_container);
        letters_tapees = findViewById(R.id.activity_main_letter_TextView);
        btn_send = findViewById(R.id.activity_main_play_btn);
        et_letter = findViewById(R.id.activity_main_letter_input);
        imagePendu = findViewById(R.id.activity_main_pendu_image);

        initGame();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String letterFromInput = et_letter.getText().toString().toUpperCase();
                et_letter.setText("");

                if (letterFromInput.length() > 0) {
                    if (!letterAlreadyUsed(letterFromInput.charAt(0), listOfLetters)) {
                        listOfLetters.add(letterFromInput.charAt(0));
                        checkLetterIsInWord(letterFromInput, word);

                    }
                    if (found == word.length()) {
                        win = true;
                        createDialog(win);
                    }

                    if (!word.contains(letterFromInput)) {
                        error++;
                    }
                    setImage(error);
                    if (error == 8) {
                        win = false;
                        createDialog(win);
                    }

                    showAllLetters(listOfLetters);
                }
            }
        });
    }

    public void initGame() {
        word = generateWord();
        win = false;
        error = 0;
        found = 0;
        letters_tapees.setText(" ");
        imagePendu.setBackgroundResource(R.drawable.un);
        listOfLetters = new ArrayList<>();
        container.removeAllViews();

        for (int i = 0; i < word.length(); i++) {
            TextView oneLetter = (TextView) getLayoutInflater().inflate(R.layout.textview, null);
            container.addView(oneLetter);
        }
    }

    public boolean letterAlreadyUsed(char a, List<Character> listLetters) {
        for (int i = 0; i < listLetters.size(); i++) {

            if (listLetters.get(i) == a) {
                Toast.makeText(getApplicationContext(), "Vous avez déjà entré cette lettre", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    public void checkLetterIsInWord(String letter, String word) {
        for (int i = 0; i < word.length(); i++) {
            if (letter.equals(String.valueOf(word.charAt(i)))) {
                TextView tv = (TextView) container.getChildAt(i);
                tv.setText((String.valueOf(word.charAt(i))));
                found++;
            }
        }
    }

    public void showAllLetters(List<Character> listLetters) {
        String str = "";
        for (int i = 0; i < listLetters.size(); i++) {
            str += listLetters.get(i) + "\n";
        }
        if (!str.equals(" ")) {
            letters_tapees.setText(str);
        }
    }

    public void setImage(int error) {

        switch (error) {
            case 1:
                imagePendu.setBackgroundResource(R.drawable.deux);
                break;
            case 2:
                imagePendu.setBackgroundResource(R.drawable.trois);
                break;
            case 3:
                imagePendu.setBackgroundResource(R.drawable.quatre);
                break;
            case 4:
                imagePendu.setBackgroundResource(R.drawable.cinq);
                break;
            case 5:
                imagePendu.setBackgroundResource(R.drawable.six);
                break;
            case 6:
                imagePendu.setBackgroundResource(R.drawable.sept);
                break;
            case 7:
                imagePendu.setBackgroundResource(R.drawable.huit);
                break;
            case 8:
                imagePendu.setBackgroundResource(R.drawable.neuf);
                break;
        }
    }

    public void createDialog(Boolean win) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Vous avez gagné");

        if (!win) {
            builder.setTitle("Vous avez perdu");
            builder.setMessage("Le mot à trouver était : " + word);
        }
        builder.setPositiveButton(getResources().getString(R.string.btn_rejouer), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                initGame();
            }
        });
        builder.create().show();
    }

    public List<String> getListOfWord() {
        try {
            BufferedReader buffer = new BufferedReader(new InputStreamReader(getAssets().open("pendu_liste.txt")));
            String line;
            while ((line = buffer.readLine()) != null) {
                wordList.add(line);
            }
            buffer.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return wordList;
    }

    public String generateWord() {
        wordList = getListOfWord();
        int random = (int) (Math.random() * wordList.size());
        return wordList.get(random);
    }
}