package dcn.spbstu.mysticalcards;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class DownloadCardsActivity extends AppCompatActivity implements View.OnClickListener {

    Button backToMenu;
    Button download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_cards);

        backToMenu = (Button) findViewById(R.id.backToMenu3);
        backToMenu.setOnClickListener(this);

        download = (Button) findViewById(R.id.download);
        download.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backToMenu3:
                finish();
                break;
            case R.id.download:
                FileInputStream fStream = null;
                InputStreamReader inputStream = null;
                EditText edText = (EditText) findViewById(R.id.edtext2);
                String fileName = edText.getText().toString();
                File sdPath = Environment.getExternalStorageDirectory();
                File file = new File(sdPath, fileName);
                int k = 0;
                try {
                    fStream = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Неверный путь", Toast.LENGTH_SHORT);
                    toast.show();
                    k = 1;
                }
                if (k != 1) {
                    try {
                        inputStream = new InputStreamReader(fStream, "Cp1251");
                    } catch (UnsupportedEncodingException e) {

                        e.printStackTrace();
                    }
                    BufferedReader bufferedReader = null;
                    bufferedReader = new BufferedReader(inputStream);

                    String line;
                    List<String> list = new ArrayList<String>();
                    try {
                        while ((line = bufferedReader.readLine()) != null) {
                            String[] arrayMessage = line.split(":");
                            String[] translations = arrayMessage[1].split(", ");
                            String[] box = arrayMessage[2].split(";");
                            for (int i = 0; i < translations.length; i++) {
                                int r = 0;
                                for (int j = 0; j < Storage.cards_.size(); j++) {
                                    if (Storage.cards_.get(j).getEn().equals(arrayMessage[0]) && Storage.cards_.get(j).getRu().equals(translations[i])) {
                                        r = 1;
                                    }
                                }
                                if (r != 1) {
                                    Card card = new Card(Integer.valueOf(box[0]), arrayMessage[0], translations[i]);
                                    Storage.cards_.add(card);
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Карточки добавлены", Toast.LENGTH_SHORT);
                toast.show();
                break;
            default:
                break;
        }
    }
}