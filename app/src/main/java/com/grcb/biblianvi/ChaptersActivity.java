package com.grcb.biblianvi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ChaptersActivity extends Activity {

    private ListView listView;
    private InputStream inputStream;
    private int book;
    private Document document;
    private Element elementChapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);

        try {
            listView = findViewById(R.id.listViewId);
            getListChapter(getBook());
        } catch (Exception e) {
            showMSG(e.getMessage());
        }

    }

    private void getListChapter(int book) {
        try {
            NodeList chapterList = loadBook(book).getElementsByTagName("chapter");
            ArrayList arrayChapter = new ArrayList();

            for (int i = 0; i < chapterList.getLength(); i++) {
                String chapter = chapterList.item(i).getAttributes().getNamedItem("number").getTextContent();
                arrayChapter.add("Capítulo " + chapter);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getApplicationContext(),
                    android.R.layout.simple_list_item_1,
                    arrayChapter
            );

            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        activityVerses(position);
                    } catch (Exception e) {
                        showMSG(e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private Element loadBook(int book) {
        try {
            elementChapter = (Element) getDocument().getElementsByTagName("book").item(book);
        } catch (Exception e) {
            showMSG(e.getMessage());
        }
        return elementChapter;
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
            e.getMessage();
        }
        return book;
    }


    private void previousActivity() {
        try {
            inputStream.close();
            Intent intent = new Intent(ChaptersActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.getMessage();
        }
    }


    protected void activityVerses(int chapter) {
        try {
            inputStream.close();
            Intent intent = new Intent(ChaptersActivity.this, VersesActivity.class);
            intent.putExtra("book", getBook());
            intent.putExtra("chapter", chapter);
            startActivity(intent);
        } catch (IOException e) {
            showMSG(e.getMessage());
        }
    }


    private void showMSG(String msg) {
        try {
            Toast.makeText(ChaptersActivity.this, msg, Toast.LENGTH_SHORT).show();
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
