package com.grcb.biblianvi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class VersesActivity extends Activity {

    private ListView listView;
    private InputStream inputStream;
    private int book, chapter;
    private Document document;
    private Element elementBook, elementVerse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verses);

        try {
            listView = findViewById(R.id.listViewId);
            getListVerse();
        } catch (Exception e) {
            showMSG(e.getMessage());
        }
    }


    private void getListVerse() {
        try {
            NodeList verseList = loadChapter(getBook(), getChapter()).getElementsByTagName("verse");
            ArrayList arrayVerse = new ArrayList();

            for (int j = 0; j < verseList.getLength(); j++) {
                Node nodeVerse = verseList.item(j).getChildNodes().item(0);
                arrayVerse.add(nodeVerse.getNodeValue());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getApplicationContext(),
                    android.R.layout.simple_list_item_1,
                    arrayVerse
            );

            listView.setAdapter(adapter);
        } catch (Exception e) {
            showMSG(e.getMessage());
        }
    }

    private Element loadChapter(int book, int chapter) {
        try {
            elementBook = (Element) getDocument().getElementsByTagName("book").item(book);
            elementVerse = (Element) elementBook.getElementsByTagName("chapter").item(chapter);
        } catch (Exception e) {
            showMSG(e.getMessage());
        }
        return elementVerse;
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


    private int getBook() {
        try {
            if (getIntent().hasExtra("book")) {
                book = getIntent().getIntExtra("book", 0);
            } else {
                showMSG("Error");
            }
        } catch (Exception e) {
            showMSG(e.getMessage());
        }
        return book;
    }


    private int getChapter() {
        try {
            if (getIntent().hasExtra("chapter")) {
                chapter = getIntent().getIntExtra("chapter", 0);
            } else {
                showMSG("Error");
            }
        } catch (Exception e) {
            showMSG(e.getMessage());
        }
        return chapter;
    }


    private void previousActivity() {
        try {
            inputStream.close();
            Intent intent = new Intent(VersesActivity.this, ChaptersActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("book", getBook());
            startActivity(intent);
            finish();
        } catch (IOException e) {
            showMSG(e.getMessage());
        }
    }


    private void showMSG(String msg) {
        try {
            Toast.makeText(VersesActivity.this, msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            showMSG(e.getMessage());
        }
    }


    @Override
    public void onBackPressed() {
        try {
            previousActivity();
        } catch (Exception e) {
            showMSG(e.getMessage());
        }
    }

}
