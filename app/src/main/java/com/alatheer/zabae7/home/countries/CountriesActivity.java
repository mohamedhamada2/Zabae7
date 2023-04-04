package com.alatheer.zabae7.home.countries;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.room.Room;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alatheer.zabae7.R;
import com.alatheer.zabae7.data.CitySharedPreferance;
import com.alatheer.zabae7.data.DatabaseClass;
import com.alatheer.zabae7.databinding.ActivityCountriesBinding;
import com.alatheer.zabae7.home.HomeActivity;
import com.alatheer.zabae7.signup.AllCity;
import com.alatheer.zabae7.signup.SignUpActivity;

import java.util.List;

public class CountriesActivity extends AppCompatActivity {
    ActivityCountriesBinding activityCountriesBinding;
    CountriesViewModel countriesViewModel;
    ArrayAdapter<String> allcitiesadapter;
    List<AllCity> allCities;
    String city_id,city_name;
    CitySharedPreferance citySharedPreferance;
    AllCity allCity;
    DatabaseClass databaseClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries);
        activityCountriesBinding = DataBindingUtil.setContentView(this,R.layout.activity_countries);
        countriesViewModel = new CountriesViewModel(this);
        activityCountriesBinding.setCountriesviewmodel(countriesViewModel);
        databaseClass = Room.databaseBuilder(getApplicationContext(), DatabaseClass.class, "my_orders").allowMainThreadQueries().build();
        citySharedPreferance = CitySharedPreferance.getInstance();
        allCity = new AllCity();
        countriesViewModel.getCities();
        activityCountriesBinding.citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city_id = allCities.get(position).getCityId();
                city_name = allCities.get(position).getCityName();
                allCity.setCityId(city_id);
                allCity.setCityName(city_name);
                try {
                    TextView textView = (TextView) view;
                    textView.setTextColor(getResources().getColor(R.color.dark_silver));

                } catch (Exception e) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        activityCountriesBinding.whatsAppImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://api.whatsapp.com/send?phone=+9660500676337";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        activityCountriesBinding.twitterImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                try{
                    // Get Twitter app
                    getPackageManager().getPackageInfo("com.twitter.android", 0);
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://thabaeh?t=uvozWH4eiTuI7HF9CmYycg&s=08"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception e) {
                    // If no Twitter app found, open on browser
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/thabaeh?t=uvozWH4eiTuI7HF9CmYycg&s=08"));
                    startActivity(intent);
                }
            }
        });
    }

    public void Confirm(View view) {
        if (city_id.equals("0")) {
            Toast.makeText(this, "ادخل المدينة من فضلك", Toast.LENGTH_SHORT).show();
        } else {
            try {
                if (!city_id.equals(citySharedPreferance.Get_UserData(this).getCityId())){
                    databaseClass.getDao().deleteAllproduct();
                }
                citySharedPreferance.Create_Update_UserData(this, allCity);
                Intent intent = new Intent(CountriesActivity.this, HomeActivity.class);
                intent.putExtra("flag",1);
                startActivity(intent);
            }catch (Exception e){
                citySharedPreferance.Create_Update_UserData(this, allCity);
                Intent intent = new Intent(CountriesActivity.this, HomeActivity.class);
                intent.putExtra("flag",1);
                startActivity(intent);
            }
        }
    }

    public void initSpinner(List<String> allcitiestitle, List<AllCity> allCities) {
        allcitiestitle.add(0, "اختر المدينة");
        AllCity allCity = new AllCity();
        allCity.setCityName("اختر المدينة");
        allCity.setCityId("0");
        allCities.add(0,allCity);
        allcitiesadapter = new ArrayAdapter<String>(CountriesActivity.this, R.layout.spinner_item, allcitiestitle);
        activityCountriesBinding.citySpinner.setAdapter(allcitiesadapter);
        this.allCities = allCities;
    }
}
