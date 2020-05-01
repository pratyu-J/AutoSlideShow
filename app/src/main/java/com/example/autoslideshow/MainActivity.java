package com.example.autoslideshow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
//import android.widget.LinearLayout;
import android.widget.LinearLayout;
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
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    int count = 0;



        SliderLayout sliderLayout;
        HashMap<String, Uri> Hash_file_maps = new HashMap<>();
        ArrayList<Uri> imgurl = new ArrayList<>();
        Button toGallery;
        CardView card;

        @Override
        protected void onCreate (Bundle savedInstanceState){

            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main);

            //Hash_file_maps = new HashMap<String, Bitmap>();
            //card = findViewById(R.id.card);

            sliderLayout = (SliderLayout) findViewById(R.id.slider);
           /* SliderLayout sliderLayout = new SliderLayout(this);
            LinearLayout.LayoutParams orientationparam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 550);
            orientationparam.setMargins(5,5,5,5);
            SliderLayout.setLayoutParams(orientationparam);
            card.addView(SliderLayout);*/

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

            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                    //Uri imgurl = clipData.getItemAt(i).getUri();
                    imgurl.add(clipData.getItemAt(i).getUri());
                        //InputStream is = getContentResolver().openInputStream(imgurl);
                        //Bitmap bitmap = BitmapFactory.decodeStream(is);
                        //bm.add(bitmap);
                        Hash_file_maps.put(String.valueOf(count++), clipData.getItemAt(i).getUri());

                }
                for (String name : Hash_file_maps.keySet()) {

                    TextSliderView textSliderView = new TextSliderView(MainActivity.this);
                    Log.d("output", String.valueOf(Hash_file_maps.get(name)));
                    textSliderView
                            .description(name)
                            .image(String.valueOf(Hash_file_maps.get(name)))
                            .setScaleType(BaseSliderView.ScaleType.FitCenterCrop)
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    sliderLayout.setLayoutMode(ViewGroup.LAYOUT_MODE_CLIP_BOUNDS);
                }
                sliderLayout.setDuration(5000);
                sliderLayout.addOnPageChangeListener(this);

            }
            else {
                //Uri imageUrl  = data.getData();
                imgurl.add(data.getData());

                //try {
                    //InputStream is = getContentResolver().openInputStream(imageUrl);
                    //Bitmap bitmap = BitmapFactory.decodeStream(is);
                   // bm.add(bitmap);
                    Hash_file_maps.put(String.valueOf(count++), imgurl.get(0));

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

                /*} catch (FileNotFoundException e) {
                    e.printStackTrace();
                    }*/
                }





                                /*SliderItem sliderItem = new SliderItem();
                                sliderItem.setImageUrl(b);
                                sliderItemList.add(sliderItem);
                                sliderItem.setDescription("Slider Item Added Manually");
                                adapter.renewItems(sliderItemList);*/
                            }
                        }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
           // SliderLayout sliderLayout = new SliderLayout(this);
            LinearLayout.LayoutParams orientationparam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            orientationparam.setMargins(5,5,5,5);
            sliderLayout.setLayoutParams(orientationparam);
           // card.addView(sliderLayout);


        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
          //  SliderLayout sliderLayout = new SliderLayout(this);
            LinearLayout.LayoutParams orientationparam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1500);
            orientationparam.setMargins(0,0,0,0);
            sliderLayout.setLayoutParams(orientationparam);
           // card.addView(sliderLayout);
        }
    }
}










