package com.example.ravimathpal.directorysearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements Adapter.onDirectoryAction {

    private RecyclerView list;
    private Directory homeDirectory;
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = (RecyclerView) findViewById(R.id.list);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this));

        homeDirectory = new Directory("Home");
        adapter = new Adapter(getApplicationContext(), homeDirectory, this);
        list.setAdapter(adapter);

        loadData();

    }

    private void loadData() {

        try {
            JSONObject jsonObject = new JSONObject(JsonFile.JSON);
            JSONObject d = jsonObject.getJSONObject("d");
            JSONArray s3 = d.getJSONArray("S3Objects");

            for (int i = 0; i < s3.length(); i++) {
                JSONObject object = s3.getJSONObject(i);
                //ignoring useless data and thumbs
                //thumbs are manually set in setKey method of directoryfile
                if (object.getString("Key").contains("ZurekaTempPatientConfig"))
                    continue;
                else if (object.getString("Key").contains("_thumb."))
                    continue;

                DirectoryFile file = new DirectoryFile();
                file.setName(getFileName(object.getString("Key")));
                file.setKey(object.getString("Key"));
                file.setLastModified(object.getString("LastModified"));
                file.setSize(object.getDouble("Size"));
                file.setPath(removeExtra(object.getString("Key")));
                //this is a recursive method that will keep adding directories until file is set in heirarchy
                addObject(homeDirectory, file, file.getPath());
            }

            adapter.notifyDataSetChanged();
            adapter = new Adapter(getApplicationContext(), homeDirectory, this);
            list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getFileName(String key) {
        //this method just gets the name of file
        String[] split = key.split("/");
        return split[split.length - 1];
    }

    private String removeExtra(String path) {
//        this method removes junk from front
        String[] splitted = path.split("/");
        String reducedString = "";
        for (int i = 3; i < splitted.length; i++) {
            reducedString = reducedString + splitted[i];
            if (i != splitted.length - 1) {
                reducedString = reducedString + "/";
            }
        }
        return reducedString;
    }

    private String removeOneDirectory(String path) {
        //this method trims the path to one directory short
        String newString = path.substring(path.indexOf("/", 0) + 1);
        return newString;
    }

    private void addObject(Directory directory, DirectoryFile file, String path) {
        //recursive method to set file in directory
        String name;
        if (path.contains("/")) {
            name = path.substring(0, path.indexOf("/", 0));
        } else {
            name = path;
        }

        if (isFile(name)) {
            if (!directory.hasFile(name)) {
                directory.addFile(file);
            }
        } else {
            //if it is a folder, then add new folder object in current directory recursively
            if (directory.hasDirectory(name)) {
                addObject(directory.getDirectory(name), file, removeOneDirectory(path));
            } else {
                Directory newDirectory = new Directory(name);
                directory.addDirectory(newDirectory);
                String newPath = removeOneDirectory(path);
                addObject(newDirectory, file, newPath);
            }
        }
    }

    private boolean isFile(String s) {
//        to check if string is a valid file name.. be sure not to include / in string
        s.toLowerCase();
        if (s.endsWith(".jpg")
                || s.endsWith(".png")
                || s.endsWith(".pdf")
                || s.endsWith(".xlx")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDirectoryTouched(Directory directory) {
        adapter = new Adapter(getApplicationContext(), directory, this);
        list.setAdapter(adapter);
    }

    @Override
    public void onImageTouched(DirectoryFile file) {
        //do actions for image
        Intent i = new Intent(this, ImageActivity.class);
        i.putExtra("ImagePath", JsonFile.AMAZON_URL + file.getKey());
        startActivity(i);
    }
}
