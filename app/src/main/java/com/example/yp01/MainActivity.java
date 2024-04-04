package com.example.yp01;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    private ProgressBar progressBar;
    private TextView textView;
    private RecyclerView recyclerView;
    private List<Item> itemList;
    private Item selectedItem;

    private EditText adresSearch;
    private EditText adresSearch1;
    private ItemAdapter adapter;
    private ItemAdapter1 adapter1;
    private ItemAdapter2 adapter2;
    int gorod;
    private static final int LOADING_DELAY = 3000;
    int step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.launchscreen);

            progressBar = findViewById(R.id.progressBar);

            if (isNetworkAvailable()) {
                progressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        openScreenOnline();
                    }
                }, LOADING_DELAY);
            } else {
                progressBar.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        openScreenOffline();
                    }
                }, LOADING_DELAY);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Произошла ошибка при открытии экрана");
        }
    }

    int start_x = 0;
    int end_x = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    start_x = (int) event.getX();
                    break;
                case MotionEvent.ACTION_DOWN:
                    end_x = (int) event.getX();
                    break;
            }
            if (start_x != 0 && end_x != 0) {
                if (start_x > end_x) {
                    if (step == 2) {
                        setContentView(R.layout.onboardingscreen);
                        step = 1;
                    }
                } else if (start_x < end_x) {
                    if (step == 1) {
                        setContentView(R.layout.onboadringscreen2);
                        step = 2;
                    }

                    if (isNetworkAvailable()) {
                        textView = findViewById(R.id.skip);
                        textView.setVisibility(View.GONE);
                    }
                }
                start_x = 0;
                end_x = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Произошла ошибка при обработке жеста");
        }
        return false;
    }

    public void onLogin(View view) {
        try {
            step = 3;
            EditText emailEditText = findViewById(R.id.email);
            EditText passwordEditText = findViewById(R.id.password);

            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                showErrorDialog("Заполните все поля");
                return;
            }

            if (!isValidEmail(email)) {
                showErrorDialog("Некорректный email");
                return;
            }

            DatabaseHelper dbHelper = new DatabaseHelper(this);

           if (dbHelper.isUserExists(email, password)) {
                onStepMain(view);
            } else {
               showErrorDialog("Пользователь с указанным email и паролем не найден");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Произошла ошибка при входе");
        }
    }

    public void onReg(View view) {
        try {
            step = 3;
            EditText emailEditText = findViewById(R.id.email);
            EditText passwordEditText = findViewById(R.id.password);
            EditText phoneEditText = findViewById(R.id.phone);
            EditText nameEditText = findViewById(R.id.full_name);

            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String phone = phoneEditText.getText().toString();
            String name = nameEditText.getText().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(name)) {
                showErrorDialog("Заполните все поля");
                return;
            }

            if (!isValidEmail(email)) {
                showErrorDialog("Некорректный email");
                return;
            }
            DatabaseHelper dbHelper = new DatabaseHelper(this);

           if (dbHelper.isUserExists(email,password)) {
                showErrorDialog("Пользователь  уже существует");
                return;
            }

            if (dbHelper.insertData(email, password)) {
                onStepMain(view);
            } else {
                showErrorDialog("Произошла ошибка при добавлении данных в базу данных");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Произошла ошибка при регистрации");
        }
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                email.matches("^[a-z0-9]+@[a-z0-9]+\\.[a-z]{2,3}$");
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Ошибка")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    private void openScreenOffline() {
        setContentView(R.layout.onboardingscreen);
        step = 1;
    }

    public void onCountPlus(View view) {
        try {
            step = 3;
            TextView count = findViewById(R.id.count);
            int currentValue = Integer.parseInt(count.getText().toString());
            int newValue = currentValue + 1;
            if (newValue >= 1) {
                count.setText(String.valueOf(newValue));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Произошла ошибка при увеличении счетчика");
        }
    }

    public void onCountMinus(View view) {
        try {
            step = 3;
            TextView count = findViewById(R.id.count);
            int currentValue = Integer.parseInt(count.getText().toString());
            int newValue = currentValue - 1;
            if (newValue >= 1) {
                count.setText(String.valueOf(newValue));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Произошла ошибка при уменьшении счетчика");
        }
    }

    public void onSearch(View view) {
        try {
            step = 3;
            if (gorod == 1) {
                BottomNavigationView bottom_nav2 = findViewById(R.id.bottom_nav2);
                bottom_nav2.setVisibility(View.GONE);
                TextView results = findViewById(R.id.results);
                results.setVisibility(View.VISIBLE);

                EditText adresSearch = findViewById(R.id.adresSearch);
                String searchText = adresSearch.getText().toString();

                if (adapter != null) {
                    adapter.filter(searchText);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Произошла ошибка при выполнении поиска");
        }
    }

    public void onClear(View view) {
        try {
            step = 3;
            TextView results = findViewById(R.id.results);
            results.setVisibility(View.GONE);
            BottomNavigationView bottom_nav2 = findViewById(R.id.bottom_nav2);
            bottom_nav2.setVisibility(View.VISIBLE);
            EditText adresSearch = findViewById(R.id.adresSearch);
            adresSearch.setText("");
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Произошла ошибка при очистке результатов поиска");
        }
    }

    public void onSearchAdress(View view) {
        try {
            ImageView confirmButton = findViewById(R.id.confirmButton);
            EditText adresSearch = findViewById(R.id.adresSearch);
            EditText adresSearch1 = findViewById(R.id.adresSearch1);
            ImageView clear = findViewById(R.id.clear);
            String address = adresSearch1.getText().toString();

            String addressPattern = "^[\\p{L} ]+, [\\p{L} ]+, [\\p{L}0-9 ]+$";

            if (address.isEmpty()) {
                adresSearch1.setError("Поле не может быть пустым");
            } else if (!address.matches(addressPattern)) {
                adresSearch1.setError("Адрес должен быть в формате: Город, Улица, Дом");
            } else {
                adresSearch1.setVisibility(View.GONE);
                adresSearch.setVisibility(View.VISIBLE);
                confirmButton.setVisibility(View.GONE);
                clear.setVisibility(View.VISIBLE);

                gorod = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Произошла ошибка при вводе адреса");
        }
    }

    private void openScreenOnline() {
        setContentView(R.layout.onboardingscreen);
        step = 1;
    }

    public void onStepScreen1(View view) {
        setContentView(R.layout.onboardingscreen);
        step = 1;
    }

    public void onStepScreen2(View view) {
        setContentView(R.layout.onboadringscreen2);
        if (isNetworkAvailable()) {
            textView = findViewById(R.id.skip);
            textView.setVisibility(View.GONE);
        }
        step = 2;
    }

    public void onStepLogin(View view) {
        setContentView(R.layout.signinscreen);
        step = 3;
    }

    public void onStepReg(View view) {
        setContentView(R.layout.signupscreen);
        step = 3;
    }

    public void onStepMain(View view) {
        try {
            setContentView(R.layout.mainscreen);
            step = 3;
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

            itemList = new ArrayList<>();


            DatabaseHelper dbHelper = new DatabaseHelper(this);

            /*dbHelper.insertProduct("Форель с овощами", "N1.900", R.drawable.food1);*/
            Cursor cursor = dbHelper.getAllProducts();


            if (cursor.moveToFirst()) {
                do {

                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String price = cursor.getString(cursor.getColumnIndex("price"));
                    int imageResource = cursor.getInt(cursor.getColumnIndex("image_resource"));


                    itemList.add(new Item(title, price, imageResource));
                } while (cursor.moveToNext());
            }


            cursor.close();


            adapter = new ItemAdapter(this, itemList);
            recyclerView.setAdapter(adapter);


            final TextView adView = findViewById(R.id.adView);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0) {
                        adView.setVisibility(View.GONE);
                    } else {
                        adView.setVisibility(View.VISIBLE);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Произошла ошибка при открытии главного экрана");
        }
    }

    public void onCart(View view) {
        try {
            step = 3;
            LinearLayout SelectedItem = findViewById(R.id.SelectedItem);
            SelectedItem.setVisibility(View.GONE);

            LinearLayout liner1 = findViewById(R.id.liner1);
            liner1.setVisibility(View.VISIBLE);
            LinearLayout liner2 = findViewById(R.id.liner2);
            liner2.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Произошла ошибка при открытии корзины");
        }
    }

    public void onBackCart(View view) {
        try {
            step = 3;
            LinearLayout SelectedItem = findViewById(R.id.SelectedItem);
            SelectedItem.setVisibility(View.VISIBLE);

            LinearLayout liner1 = findViewById(R.id.liner1);
            liner1.setVisibility(View.GONE);
            LinearLayout liner2 = findViewById(R.id.liner2);
            liner2.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Произошла ошибка при возвращении из корзины");
        }
    }

    public void onStepBack(View view) {
        try {

            step = 3;
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

            itemList = new ArrayList<>();


            DatabaseHelper dbHelper = new DatabaseHelper(this);

            /*dbHelper.insertProduct("Форель с овощами", "N1.900", R.drawable.food1);*/
            Cursor cursor = dbHelper.getAllProducts();


            if (cursor.moveToFirst()) {
                do {

                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String price = cursor.getString(cursor.getColumnIndex("price"));
                    int imageResource = cursor.getInt(cursor.getColumnIndex("image_resource"));


                    itemList.add(new Item(title, price, imageResource));
                } while (cursor.moveToNext());
            }


            cursor.close();


            adapter = new ItemAdapter(this, itemList);
            recyclerView.setAdapter(adapter);


            final TextView adView = findViewById(R.id.adView);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0) {
                        adView.setVisibility(View.GONE);
                    } else {
                        adView.setVisibility(View.VISIBLE);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Произошла ошибка при возвращении на предыдущий экран");
        }
    }

    public void onStepFood1(View view) {
        try {
            step = 3;
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 1));


            if (itemList != null && !itemList.isEmpty()) {
                List<Item> singleItemList = Collections.singletonList(itemList.get(0));
                adapter1 = new ItemAdapter1(this, singleItemList);
            } else {

                adapter1 = new ItemAdapter1(this, new ArrayList<>());
            }

            recyclerView.setAdapter(adapter1);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Произошла ошибка при открытии экрана с едой");
        }
    }

    public void onAddToCart(View view) {
        try {
            step = 3;
            Item selectedItem = adapter.getItem(0);
            String name = selectedItem.getTitle();
            String price = selectedItem.getPrice();
            int imageResource = selectedItem.getImageResource();

            setContentView(R.layout.oneitemscreen);

            TextView nameTextView = findViewById(R.id.Name);
            nameTextView.setText(name);

            TextView priceTextView = findViewById(R.id.Price);
            priceTextView.setText(price);

            ImageView imageView = findViewById(R.id.img);
            imageView.setImageResource(imageResource);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Произошла ошибка при добавлении в корзину");
        }
    }

    public void onStepFood(MenuItem item) {
        try {

            step = 3;
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

            itemList = new ArrayList<>();


            DatabaseHelper dbHelper = new DatabaseHelper(this);

            /*dbHelper.insertProduct("Форель с овощами", "N1.900", R.drawable.food1);*/
            Cursor cursor = dbHelper.getAllProducts();


            if (cursor.moveToFirst()) {
                do {

                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String price = cursor.getString(cursor.getColumnIndex("price"));
                    int imageResource = cursor.getInt(cursor.getColumnIndex("image_resource"));


                    itemList.add(new Item(title, price, imageResource));
                } while (cursor.moveToNext());
            }


            cursor.close();


            adapter = new ItemAdapter(this, itemList);
            recyclerView.setAdapter(adapter);


            final TextView adView = findViewById(R.id.adView);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0) {
                        adView.setVisibility(View.GONE);
                    } else {
                        adView.setVisibility(View.VISIBLE);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Произошла ошибка при открытии экрана с едой");
        }
    }

    public void onStepDrinks(MenuItem item) {

            try {

                step = 3;
                recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

                itemList = new ArrayList<>();


                DatabaseHelper dbHelper = new DatabaseHelper(this);


                /*dbHelper.insertDrinks("Пиво", "N1.200", R.drawable.drink1);*/


                Cursor cursor = dbHelper.getAllDrinks();


                if (cursor.moveToFirst()) {
                    do {

                        String title = cursor.getString(cursor.getColumnIndex("title"));
                        String price = cursor.getString(cursor.getColumnIndex("price"));
                        int imageResource = cursor.getInt(cursor.getColumnIndex("image_resource"));


                        itemList.add(new Item(title, price, imageResource));
                    } while (cursor.moveToNext());
                }


                cursor.close();


                adapter = new ItemAdapter(this, itemList);
                recyclerView.setAdapter(adapter);


                final TextView adView = findViewById(R.id.adView);
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (dy > 0) {
                            adView.setVisibility(View.GONE);
                        } else {
                            adView.setVisibility(View.VISIBLE);
                        }
                    }
                });
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Произошла ошибка при открытии экрана с напитками");
        }
    }

    public void onStepSnacks(MenuItem item) {

            try {

                step = 3;
                recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

                itemList = new ArrayList<>();


                DatabaseHelper dbHelper = new DatabaseHelper(this);


                /*dbHelper.insertSnacks("Чипсы", "N1.100", R.drawable.snacks1);*/


                Cursor cursor = dbHelper.getAllSnacks();


                if (cursor.moveToFirst()) {
                    do {

                        String title = cursor.getString(cursor.getColumnIndex("title"));
                        String price = cursor.getString(cursor.getColumnIndex("price"));
                        int imageResource = cursor.getInt(cursor.getColumnIndex("image_resource"));


                        itemList.add(new Item(title, price, imageResource));
                    } while (cursor.moveToNext());
                }


                cursor.close();


                adapter = new ItemAdapter(this, itemList);
                recyclerView.setAdapter(adapter);


                final TextView adView = findViewById(R.id.adView);
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (dy > 0) {
                            adView.setVisibility(View.GONE);
                        } else {
                            adView.setVisibility(View.VISIBLE);
                        }
                    }
                });
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Произошла ошибка при открытии экрана с закусками");
        }
    }

    public void onStepSauce(MenuItem item) {


            try {

                step = 3;
                recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

                itemList = new ArrayList<>();


                DatabaseHelper dbHelper = new DatabaseHelper(this);


                /*dbHelper.insertSauce("Кетчуп", "N1.000", R.drawable.sauce1);*/


                Cursor cursor = dbHelper.getAllSauce();


                if (cursor.moveToFirst()) {
                    do {

                        String title = cursor.getString(cursor.getColumnIndex("title"));
                        String price = cursor.getString(cursor.getColumnIndex("price"));
                        int imageResource = cursor.getInt(cursor.getColumnIndex("image_resource"));


                        itemList.add(new Item(title, price, imageResource));
                    } while (cursor.moveToNext());
                }


                cursor.close();


                adapter = new ItemAdapter(this, itemList);
                recyclerView.setAdapter(adapter);


                final TextView adView = findViewById(R.id.adView);
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (dy > 0) {
                            adView.setVisibility(View.GONE);
                        } else {
                            adView.setVisibility(View.VISIBLE);
                        }
                    }
                });
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Произошла ошибка при открытии экрана с соусами");
        }
    }

    private void addItemToList(List<Item> itemList, String title, String price, int imageResource) {
        itemList.add(new Item(title, price, imageResource));
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}