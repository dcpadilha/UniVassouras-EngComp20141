package com.uss.desenvolvimentomobile.diogomatheus.imccalculator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private Intromanager intromanager;
    private int[] layouts;
    private TextView[] dots;
    private LinearLayout dotsLayout;
    Button next,skip;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intromanager = new Intromanager(this);

        if(!intromanager.Check())
        {
            intromanager.setFirst(false);
            Intent i = new Intent(MainActivity.this, Imc.class);
            startActivity(i);
            finish();
        }
        if(Build.VERSION.SDK_INT>=21){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_FULLSCREEN);
        }

        setContentView(R.layout.activity_main);

        viewPager = (ViewPager)findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout)findViewById(R.id.layoutDots);
        skip = (Button)findViewById(R.id.btn_skip);
        next = (Button)findViewById(R.id.btn_next);

        layouts = new int[]{R.layout.activity_tela_1, R.layout.activity_tela_2, R.layout.activity_tela_3, R.layout.activity_tela_4, R.layout.activity_tela_5};
        addBottonDots(0);
        changeStatusBarColor();
        viewPagerAdapter = new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(viewListener);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Imc.class);
                startActivity(i);
                finish();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current = getItem(+1);
                if(current<layouts.length){
                    viewPager.setCurrentItem(current);
                }else{
                    Intent i = new Intent(MainActivity.this, Imc.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    private void addBottonDots(int position)
    {
        dots = new TextView[layouts.length];
        int[] colorActive = getResources().getIntArray(R.array.dot_active);
        int[] colorInactive = getResources().getIntArray(R.array.dot_inactive);
        dotsLayout.removeAllViews();
        for(int i=0; i<dots.length; i++)
        {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorInactive[position]);
            dotsLayout.addView(dots[i]);
        }
        if(dots.length>0)
            dots[position].setTextColor(colorActive[position]);

    }

    private int getItem(int i){
        return viewPager.getCurrentItem() + 1;
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addBottonDots(position);
            if(position==layouts.length-1)
            {
                next.setText("CALCULAR");
                skip.setVisibility(View.GONE);
            }else{
                next.setText("PRÓXIMO");
                skip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    private void changeStatusBarColor(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }


    public class ViewPagerAdapter extends PagerAdapter
    {

        private LayoutInflater layoutInflater;

        @Override
        public Object instantiateItem(ViewGroup container, int position){
            layoutInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(layouts[position],container,false);
            container.addView(v);
            return v;
            //return super.instantiateItem(container, position);
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            View v = (View)object;
            container.removeView(v);
        }
    }
}
