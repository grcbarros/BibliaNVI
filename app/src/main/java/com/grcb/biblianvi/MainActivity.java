package com.grcb.biblianvi;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private NodeList bookList, chapterList;
    private Document document;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listViewId);

        try {
            InputStream inputStream = getAssets().open("nvi_min.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            document = dBuilder.parse(inputStream);

            getListBook();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getListBook(){

        Element elementBook = document.getDocumentElement();
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
                getListChapter(position);
            }
        });
    }

    private void getListChapter(int book) {
        Element elementChapter = (Element) bookList.item(book);
        chapterList = elementChapter.getElementsByTagName("chapter");
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
                getListVerse(position);
            }
        });

    }

    private void getListVerse(int chapter) {
        Element elementVerse = (Element) chapterList.item(chapter);
        NodeList verseList = elementVerse.getElementsByTagName("verse");
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
    }

}