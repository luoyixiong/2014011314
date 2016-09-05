package com.example.listviewtest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {
    private final static String PRODUCT = "product";
    private final static String PRICE = "price";
    private final static String CONFIGURATION = "configuration";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] products = {"张三", "李四", "王二"};
        String[] prices = {"1401", "1402", "1403"};
        String[] configurations = {"2014011311", "2014011312", "2014011313"};
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < products.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put(PRODUCT, products[i]);
            item.put(PRICE, prices[i]);
            item.put(CONFIGURATION, configurations[i]);
            items.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, items, R.layout.item, new String[]{PRODUCT,
                PRICE, CONFIGURATION}, new int[]{R.id.txtProduct, R.id.txtPrice, R.id.txtConfiguration});
        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
    }
}
