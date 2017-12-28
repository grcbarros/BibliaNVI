package com.grcb.biblianvi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private NodeList bookList;
    private InputStream inputStream;
    private Document document;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            listView = findViewById(R.id.listViewId);

            getListBook();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Document getDocument() {
        try {
            inputStream = getAssets().open("nvi_min.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            document = dBuilder.parse(inputStream);
        } catch (Exception e) {
            showMSG(e.getMessage());
        }
        return document;
    }

    private void getListBook() {

        try {
            Element elementBook = getDocument().getDocumentElement();
            bookList = elementBook.getElementsByTagName("book");
            ArrayList arrayBook = new ArrayList();

            for (int i = 0; i < bookList.getLength(); i++) {
                String book = bookList.item(i).getAttributes().getNamedItem("name").getTextContent();
                arrayBook.add(book);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getApplicationContext(),
                    android.R.layout.simple_list_item_1,
                    arrayBook
            );

            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    activityChapters(position);
                }
            });
        } catch (Exception e) {
            showMSG(e.getMessage());
        }

    }

    protected void activityChapters(int book) {
        try {
            // inputStream.close();
            Intent intent = new Intent(MainActivity.this, ChaptersActivity.class);
            intent.putExtra("book", book);
            startActivity(intent);
        } catch (Exception e) {
            showMSG(e.getMessage());
        }
    }

    private void showMSG(String msg) {
        try {
            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            showMSG(e.getMessage());
        }
    }
}