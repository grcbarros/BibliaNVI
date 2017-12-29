package com.grcb.biblianvi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class ChaptersActivity extends MainActivity {

    private ListView listView;
    private int book, chapter;
    private Element elementChapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);

        try {
            listView = findViewById(R.id.listViewId);
            loadListChapter();
        } catch (Exception e) {
            showMSG(e.getMessage());
        }

    }

    private void loadListChapter() {
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
                        chapter = position;
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

    protected int getChapter() {
        return chapter;
    }

    private Element loadBook(int book) {
        try {
            elementChapter = (Element) getDocument().getElementsByTagName("book").item(book);
        } catch (Exception e) {
            showMSG(e.getMessage());
        }
        return elementChapter;
    }


    protected int getBook() {
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
            Intent intent = new Intent(ChaptersActivity.this, VersesActivity.class);
            intent.putExtra("book", getBook());
            intent.putExtra("chapter", chapter);
            startActivity(intent);
        } catch (Exception e) {
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
