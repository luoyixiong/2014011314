package com.example.sqlitetest;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    WordsDBHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView list = (ListView) findViewById(R.id.listWords);
        registerForContextMenu(list);

        mDbHelper = new WordsDBHelper(this);
        ArrayList<Map<String, String>> items = getAll();
        setWordsListView(items);

    }

    private ArrayList<Map<String, String>> getAll() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                Words.Word._ID,
                Words.Word.COLUMN_NAME_WORD,
                Words.Word.COLUMN_NAME_MEANING,
                Words.Word.COLUMN_NAME_SAMPLE
        };

        //排序
        String sortOrder =
                Words.Word.COLUMN_NAME_WORD + " DESC";


        Cursor c = db.query(
                Words.Word.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        return ConvertCursor2List(c);
    }

    private ArrayList<Map<String, String>> ConvertCursor2List(Cursor cursor) {
        ArrayList<Map<String, String>> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<>();
            map.put(Words.Word._ID, String.valueOf(cursor.getInt(0)));
            map.put(Words.Word.COLUMN_NAME_WORD, cursor.getString(1));
            map.put(Words.Word.COLUMN_NAME_MEANING, cursor.getString(2));
            map.put(Words.Word.COLUMN_NAME_SAMPLE, cursor.getString(3));
            result.add(map);
        }
        return result;
    }


    private void setWordsListView(ArrayList<Map<String, String>> items) {
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, items, R.layout.item,
                new String[]{Words.Word._ID, Words.Word.COLUMN_NAME_WORD,
                        Words.Word.COLUMN_NAME_MEANING, Words.Word.COLUMN_NAME_SAMPLE},
                new int[]{R.id.textid, R.id.textWord, R.id.textMeaning, R.id.textSample});
        ListView list = (ListView) findViewById(R.id.listWords);
        list.setAdapter(simpleAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDbHelper.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_search:
                SearchDialog();
                break;
            case R.id.action_insert:
                InsertDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_content, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        TextView textId = null;
        TextView textWord = null;
        TextView textMeaning = null;
        TextView textSample = null;
        AdapterView.AdapterContextMenuInfo info = null;
        View itemView = null;

        switch (item.getItemId()) {
            case R.id.action_delete:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                itemView = info.targetView;
                textId = (TextView) itemView.findViewById(R.id.textid);
                if(textId!=null){
                    String strId=textId.getText().toString();
                    DeleteDialog(strId);
                }
                break;
            case R.id.action_update:
                //修改单词
                info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                itemView=info.targetView;
                textId =(TextView)itemView.findViewById(R.id.textid);
                textWord =(TextView)itemView.findViewById(R.id.textWord);
                textMeaning =(TextView)itemView.findViewById(R.id.textMeaning);
                textSample =(TextView)itemView.findViewById(R.id.textSample);
                if(textId!=null && textWord!=null && textMeaning!=null && textSample!=null){
                    String strId=textId.getText().toString();
                    String strWord=textWord.getText().toString();
                    String strMeaning=textMeaning.getText().toString();
                    String strSample=textSample.getText().toString();
                    UpdateDialog(strId, strWord, strMeaning, strSample);
                }
                break;
        }
        return true;
    }

    //使用Sql语句插入单词
    private void InsertUserSql(String strWord, String strMeaning, String strSample) {
        String sql = "insert into  words(word,meaning,sample) values(?,?,?)";

        //Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(sql, new String[]{strWord, strMeaning, strSample});
    }


    //新增对话框
    private void InsertDialog() {
        final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.insert, null);
        new AlertDialog.Builder(this)
                .setTitle("新增单词")//标题
                .setView(tableLayout)//设置视图
                //确定按钮及其动作
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strWord = ((EditText) tableLayout.findViewById(R.id.textWord_dialog)).getText().toString();
                        String strMeaning = ((EditText) tableLayout.findViewById(R.id.textMeaning_dialog)).getText().toString();
                        String strSample = ((EditText) tableLayout.findViewById(R.id.textSample_dialog)).getText().toString();

                        //既可以使用Sql语句插入，也可以使用使用insert方法插入
                        InsertUserSql(strWord, strMeaning, strSample);
                        ArrayList<Map<String, String>> items = getAll();
                        setWordsListView(items);

                    }
                })
                //取消按钮及其动作
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .create()//创建对话框
                .show();//显示对话框
    }

    //删除单词
    private void Delete(String strId) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // 定义where子句
        String selection = Words.Word._ID + " = ?";

        // 指定占位符对应的实际参数
        String[] selectionArgs = {strId};

        // Issue SQL statement.
        db.delete(Words.Word.TABLE_NAME, selection, selectionArgs);
    }


    //删除对话框
    private void DeleteDialog(final String strId){
        new AlertDialog.Builder(this).setTitle("删除单词").setMessage("是否真的删除单词?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Delete(strId);
                setWordsListView(getAll());
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }

    //使用Sql语句更新单词
    private void UpdateUseSql(String strId,String strWord, String strMeaning, String strSample) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql="update words set word=?,meaning=?,sample=? where _id=?";
        db.execSQL(sql, new String[]{strWord, strMeaning, strSample,strId});
    }
    //修改对话框
    private void UpdateDialog(final String strId, final String strWord, final String strMeaning, final String strSample) {
        final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.insert, null);
        ((EditText) tableLayout.findViewById(R.id.textWord_dialog)).setText(strWord);
        ((EditText) tableLayout.findViewById(R.id.textMeaning_dialog)).setText(strMeaning);
        ((EditText) tableLayout.findViewById(R.id.textSample_dialog)).setText(strSample);
        new AlertDialog.Builder(this)
                .setTitle("修改单词")//标题
                .setView(tableLayout)//设置视图
                //确定按钮及其动作
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strNewWord = ((EditText) tableLayout.findViewById(R.id.textWord_dialog)).getText().toString();
                        String strNewMeaning = ((EditText) tableLayout.findViewById(R.id.textMeaning_dialog)).getText().toString();
                        String strNewSample = ((EditText) tableLayout.findViewById(R.id.textSample_dialog)).getText().toString();

                        UpdateUseSql(strId, strNewWord, strNewMeaning, strNewSample);

                        setWordsListView(getAll());
                    }
                })
                //取消按钮及其动作
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()//创建对话框
                .show();//显示对话框
    }
    //使用Sql语句查找
    private ArrayList<Map<String, String>> SearchUseSql(String strWordSearch) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String sql="select * from words where word like ? order by word desc";
        Cursor c=db.rawQuery(sql,new String[]{"%"+strWordSearch+"%"});

        return ConvertCursor2List(c);
    }

    //查询对话框
    private void SearchDialog() {
        final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.search, null);
        new AlertDialog.Builder(this)
                .setTitle("搜索单词")//标题
                .setView(tableLayout)//设置视图
                //确定按钮及其动作
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String txtSearchWord=((EditText)tableLayout.findViewById(R.id.textSearch)).getText().toString();
                        ArrayList<Map<String, String>> items=null;
                        //既可以使用Sql语句查询，也可以使用方法查询
                        items=SearchUseSql(txtSearchWord);
                        if(items.size()>0) {
                            Bundle bundle=new Bundle();
                            bundle.putSerializable("result",items);
                            Intent intent=new Intent(MainActivity.this,SearchActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }else
                            Toast.makeText(MainActivity.this,"没有找到", Toast.LENGTH_LONG).show();
                    }
                })
                //取消按钮及其动作
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()//创建对话框
                .show();//显示对话框

    }



}


