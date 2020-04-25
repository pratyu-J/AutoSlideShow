package com.example.autoslideshow;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import com.smarteist.autoimageslider.SliderView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    int count = 0;


/*        sliderView = findViewById(R.id.imageSlider);

        adapter = new SliderAdapter(this);
        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimations.THIN_WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.POPTRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(5);
        sliderView.setAutoCycle(true);*/
        //sliderView.startAutoCycle();

        SliderLayout sliderLayout;
        HashMap<String, Uri> Hash_file_maps = new HashMap<>();
        Button toGallery;

        @Override
        protected void onCreate (Bundle savedInstanceState){

            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main);

            //Hash_file_maps = new HashMap<String, Bitmap>();

            sliderLayout = (SliderLayout) findViewById(R.id.slider);

            sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            sliderLayout.setCustomAnimation(new DescriptionAnimation());
            sliderLayout.setDuration(3000);
            sliderLayout.addOnPageChangeListener(this);

            getImages();

            toGallery = findViewById(R.id.togallery);

            toGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sliderLayout.removeAllSliders();
                    getImages();
                }
            });



/*            Hash_file_maps.put("Android CupCake", R.drawable.image1);
            Hash_file_maps.put("Android Donut", R.drawable.image2);
            Hash_file_maps.put("Android Eclair", R.drawable.image3);
            Hash_file_maps.put("Android Froyo", R.drawable.image1);
            Hash_file_maps.put("Android GingerBread", R.drawable.image3);*/

/*            for (String name : Hash_file_maps.keySet()) {

                TextSliderView textSliderView = new TextSliderView(MainActivity.this);
                Log.d("output", String.valueOf(Hash_file_maps.get(name)));
                textSliderView
                        .description(name)
                        .image(Hash_file_maps.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this);
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra", name);
                sliderLayout.addSlider(textSliderView);
            }
            sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            sliderLayout.setCustomAnimation(new DescriptionAnimation());
            sliderLayout.setDuration(3000);
            sliderLayout.addOnPageChangeListener(this);*/
        }
            @Override
        protected void onStop() {

            sliderLayout.stopAutoCycle();

            super.onStop();
        }

        @Override
        public void onSliderClick(BaseSliderView slider) {

            Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {

            Log.d("Slider Demo", "Page Changed: " + position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {}


        public void getImages(){
            if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            final List<Bitmap> bm = new ArrayList<>();

            ClipData clipData = data.getClipData();
            if(clipData != null) {
                Toast.makeText(MainActivity.this, "its far", Toast.LENGTH_SHORT).show();
                Log.d("mainActivity", "we r cliping data");
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Log.d("mainActivity2", "inside for");
                    Uri imgurl = clipData.getItemAt(i).getUri();
                    try {
                        InputStream is = getContentResolver().openInputStream(imgurl);
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        bm.add(bitmap);
                        Hash_file_maps.put(String.valueOf(count++), clipData.getItemAt(i).getUri());

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                for (String name : Hash_file_maps.keySet()) {

                    TextSliderView textSliderView = new TextSliderView(MainActivity.this);
                    Log.d("output", String.valueOf(Hash_file_maps.get(name)));
                    textSliderView
                            .description(name)
                            .image(String.valueOf(Hash_file_maps.get(name)))
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener(this);
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle()
                            .putString("extra", name);
                    sliderLayout.addSlider(textSliderView);
                }
                sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
                sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                sliderLayout.setCustomAnimation(new DescriptionAnimation());
                sliderLayout.startAutoCycle();
                sliderLayout.setDuration(5000);
                sliderLayout.addOnPageChangeListener(this);

            }
            else {
                Uri imageUrl  = data.getData();

                try {
                    InputStream is = getContentResolver().openInputStream(imageUrl);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    bm.add(bitmap);
                    Hash_file_maps.put(String.valueOf(count++), clipData.getItemAt(0).getUri());

                    for (String name : Hash_file_maps.keySet()) {

                        TextSliderView textSliderView = new TextSliderView(MainActivity.this);
                        Log.d("output2", String.valueOf(Hash_file_maps.get(name)));
                        textSliderView
                                .description(name)
                                .image(String.valueOf(Hash_file_maps.get(name)))
                                .setScaleType(BaseSliderView.ScaleType.Fit)
                                .setOnSliderClickListener(this);
                        textSliderView.bundle(new Bundle());
                        textSliderView.getBundle()
                                .putString("extra", name);
                        sliderLayout.addSlider(textSliderView);
                    }
                    sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
                    sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                    sliderLayout.setCustomAnimation(new DescriptionAnimation());
                    sliderLayout.setDuration(3000);
                    sliderLayout.addOnPageChangeListener(this);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    }
                }





                                /*SliderItem sliderItem = new SliderItem();
                                sliderItem.setImageUrl(b);
                                sliderItemList.add(sliderItem);
                                sliderItem.setDescription("Slider Item Added Manually");
                                adapter.renewItems(sliderItemList);*/
                            }
                        }
}








    /*public void renewItems(View view) {
       *//* List<SliderItem> sliderItemList = new ArrayList<>();*//*
        //dummy data
        for (int i = 0; i < 5; i++) {
            SliderItem sliderItem = new SliderItem();
            sliderItem.setDescription("Slider Item " + i);
            if (i % 2 == 0) {
               // sliderItem.setImageUrl(R.drawable.image3);
            } else {
                //sliderItem.setImageUrl(R.drawable.image2);
            }
            sliderItemList.add(sliderItem);
        }
        adapter.renewItems(sliderItemList);
    }

    public void removeLastItem(View view) {
        if (adapter.getCount() - 1 >= 0)
            adapter.deleteItem(adapter.getCount() - 1);
    }

    public void addNewItem(View view) {


        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                return;
        }

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(intent, 1);


*//*        SliderItem sliderItem = new SliderItem();
        sliderItem.setDescription("Slider Item Added Manually");
        sliderItem.setImageUrl(R.drawable.image1);
        adapter.addItem(sliderItem);*//*
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(MainActivity.this, "hello", Toast.LENGTH_SHORT).show();
        Log.d("mainActivity", "we r here");

        if (requestCode == 1 && resultCode == RESULT_OK) {
            final List<Bitmap> bm = new ArrayList<>();
            Log.d("mainActivity", "we r in requestcode");

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                ClipData clipData = data.getClipData();
                Log.d("mainActivity", "we checked version");
                if(clipData != null){
                    Toast.makeText(MainActivity.this, "its far", Toast.LENGTH_SHORT).show();
                    Log.d("mainActivity", "we r cliping data");
                    for(int i = 0; i<clipData.getItemCount(); i++){
                        Uri imgurl = clipData.getItemAt(i).getUri();
                        try {
                            InputStream is = getContentResolver().openInputStream(imgurl);
                            Bitmap bitmap = BitmapFactory.decodeStream(is);
                            bm.add(bitmap);

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }

                else {
                    Uri imageUrl  = data.getData();

                    try {
                        InputStream is = getContentResolver().openInputStream(imageUrl);
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        bm.add(bitmap);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(final Bitmap b: bm){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("mainActivity", "we r in thread");
                                    SliderItem sliderItem = new SliderItem();
                                    sliderItem.setImageUrl(b);
                                    sliderItemList.add(sliderItem);
                                    sliderItem.setDescription("Slider Item Added Manually");
                                    adapter.renewItems(sliderItemList);
                                }
                            });

                        }

                    }
                }).start();

        }
    }
*/


