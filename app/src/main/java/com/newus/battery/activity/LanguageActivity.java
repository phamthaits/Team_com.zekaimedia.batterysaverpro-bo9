package com.newus.battery.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.newus.battery.R;
import com.newus.battery.Utilsb.SharePreferenceUtils;
import com.newus.battery.Utilsb.Utils;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity implements  View.OnClickListener {

    ImageView imgDe,imgEn,imgEs,imgFr,imgIndo,imgIt,imgPt,imgRu,imgTr,imgAr,imgTh,imgVietNam,imgJa,imgKo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setLocate(this);
        setContentView(R.layout.activity_language);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_activity_language));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_48dp);

        /* ------------------- StatusBar text dark bg white ----------------- */
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.windowBackground));
        /* ------------------------------------------------------------------ */

        intView();
        intEvent();
        intData();


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                break;


            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }

    public void intView(){
        imgDe = findViewById(R.id.imgDe);
        imgEn = findViewById(R.id.imgEn);
        imgEs = findViewById(R.id.imgEs);
        imgFr = findViewById(R.id.imgFr);
        imgIndo = findViewById(R.id.imgIndo);
        imgIt = findViewById(R.id.imgIt);
        imgPt = findViewById(R.id.imgPt);
        imgRu = findViewById(R.id.imgRu);
        imgTr = findViewById(R.id.imgTr);
        imgAr = findViewById(R.id.imgAr);
        imgTh = findViewById(R.id.imgTh);
        imgVietNam = findViewById(R.id.imgVietNam);
        imgJa = findViewById(R.id.imgJa);
        imgKo = findViewById(R.id.imgKo);

    }
    public void intEvent(){
        findViewById(R.id.btnDe).setOnClickListener(this);
        findViewById(R.id.btnEn).setOnClickListener(this);
        findViewById(R.id.btnEs).setOnClickListener(this);
        findViewById(R.id.btnFr).setOnClickListener(this);
        findViewById(R.id.btnIndo).setOnClickListener(this);
        findViewById(R.id.btnIt).setOnClickListener(this);
        findViewById(R.id.btnPt).setOnClickListener(this);
        findViewById(R.id.btnRu).setOnClickListener(this);
        findViewById(R.id.btnTr).setOnClickListener(this);
        findViewById(R.id.btnAr).setOnClickListener(this);
        findViewById(R.id.btnTh).setOnClickListener(this);
        findViewById(R.id.btnVietnam).setOnClickListener(this);
        findViewById(R.id.btnJa).setOnClickListener(this);
        findViewById(R.id.btnKo).setOnClickListener(this);

    }
    public void intData(){

        imgDe.setVisibility(View.GONE);
        imgEn.setVisibility(View.GONE);
        imgEs.setVisibility(View.GONE);
        imgFr.setVisibility(View.GONE);
        imgIndo.setVisibility(View.GONE);
        imgIt.setVisibility(View.GONE);
        imgPt.setVisibility(View.GONE);
        imgRu.setVisibility(View.GONE);
        imgTr.setVisibility(View.GONE);
        imgAr.setVisibility(View.GONE);
        imgTh.setVisibility(View.GONE);
        imgVietNam.setVisibility(View.GONE);
        imgJa.setVisibility(View.GONE);
        imgKo.setVisibility(View.GONE);
        switch (SharePreferenceUtils.getInstance(this).getLanguage()){
            case "de":
                imgDe.setVisibility(View.VISIBLE);
                break;
            case "en":
                imgEn.setVisibility(View.VISIBLE);
                break;
            case "es":
                imgEs.setVisibility(View.VISIBLE);
                break;
            case "fr":
                imgFr.setVisibility(View.VISIBLE);
                break;
            case "in":
                imgIndo.setVisibility(View.VISIBLE);
                break;
            case "it":
                imgIt.setVisibility(View.VISIBLE);
                break;
            case "pt":
                imgPt.setVisibility(View.VISIBLE);
                break;
            case "ru":
                imgRu.setVisibility(View.VISIBLE);
                break;
            case "tr":
                imgTr.setVisibility(View.VISIBLE);
                break;
            case "ar":
                imgAr.setVisibility(View.VISIBLE);
                break;
            case "th":
                imgTh.setVisibility(View.VISIBLE);
                break;
            case "vi":
                imgVietNam.setVisibility(View.VISIBLE);
                break;
            case "ja":
                imgJa.setVisibility(View.VISIBLE);
                break;
            case "ko":
                imgKo.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnDe:
                Utils.setLocale(this,"de");
                break;
            case R.id.btnEn:
                Utils.setLocale(this,"en");
                break;
            case R.id.btnEs:
                Utils.setLocale(this,"es");
                break;
            case R.id.btnFr:
                Utils.setLocale(this,"fr");
                break;
            case R.id.btnIndo:
                Utils.setLocale(this,"in");
                break;
            case R.id.btnIt:
                Utils.setLocale(this,"it");
                break;
            case R.id.btnPt:
                Utils.setLocale(this,"pt");
                break;
            case R.id.btnRu:
                Utils.setLocale(this,"ru");
                break;
            case R.id.btnTr:
                Utils.setLocale(this,"tr");
                break;
            case R.id.btnAr:
                Utils.setLocale(this,"ar");
                break;
            case R.id.btnTh:
                Utils.setLocale(this,"th");
                break;
            case R.id.btnVietnam:
                Utils.setLocale(this,"vi");
                break;
            case R.id.btnJa:
                Utils.setLocale(this,"ja");
                break;
            case R.id.btnKo:
                Utils.setLocale(this,"ko");
                break;
            default:
                break;
        }
        intData();
        SharePreferenceUtils.getInstance(this).saveLanguageChange(true);
        setResult(RESULT_OK, new Intent());
        finish();
    }
    public void setLocale() {
        String language = SharePreferenceUtils.getInstance(this).getLanguage();

        if (SharePreferenceUtils.getInstance(this).getFirstRun()) {
            language = Locale.getDefault().getLanguage();
            if (language.equalsIgnoreCase("ar")) {
                SharePreferenceUtils.getInstance(this).saveLanguage(language);
            }
            if (language.equalsIgnoreCase("de")) {
                SharePreferenceUtils.getInstance(this).saveLanguage(language);
            }
            if (language.equalsIgnoreCase("es")) {
                SharePreferenceUtils.getInstance(this).saveLanguage(language);
            }
            if (language.equalsIgnoreCase("fr")) {
                SharePreferenceUtils.getInstance(this).saveLanguage(language);
            }
            if (language.equalsIgnoreCase("in")) {
                SharePreferenceUtils.getInstance(this).saveLanguage(language);
            }
            if (language.equalsIgnoreCase("it")) {
                SharePreferenceUtils.getInstance(this).saveLanguage(language);
            }
            if (language.equalsIgnoreCase("ja")) {
                SharePreferenceUtils.getInstance(this).saveLanguage(language);
            }
            if (language.equalsIgnoreCase("ko")) {
                SharePreferenceUtils.getInstance(this).saveLanguage(language);
            }
            if (language.equalsIgnoreCase("pt")) {
                SharePreferenceUtils.getInstance(this).saveLanguage(language);
            }
            if (language.equalsIgnoreCase("ru")) {
                SharePreferenceUtils.getInstance(this).saveLanguage(language);
            }
            if (language.equalsIgnoreCase("th")) {
                SharePreferenceUtils.getInstance(this).saveLanguage(language);
            }
            if (language.equalsIgnoreCase("tr")) {
                SharePreferenceUtils.getInstance(this).saveLanguage(language);
            }
            if (language.equalsIgnoreCase("vi")) {
                SharePreferenceUtils.getInstance(this).saveLanguage(language);
            }
        }

        Locale myLocale = new Locale(language);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
}
