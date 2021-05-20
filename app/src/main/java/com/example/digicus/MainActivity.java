package com.example.digicus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.digicus.fragment.AddExistingFragment;
import com.example.digicus.fragment.AddNewFragment;
import com.example.digicus.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView buttonAddExisting,buttonAddNew;
    ImageButton buttonHome;

    @Override
    public void onBackPressed() {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }
        this.finishAffinity();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        loadFragment(new HomeFragment(),"home");

        buttonHome      = findViewById(R.id.button_homeScreen_home);
        buttonAddNew  = findViewById(R.id.button_homeScreen_addNew);
        buttonAddExisting   = findViewById(R.id.button_homeScreen_addExisting);

        buttonHome.setOnClickListener(this);
        buttonAddNew.setOnClickListener(this);
        buttonAddExisting.setOnClickListener(this);
    }

    private boolean loadFragment(Fragment fragment,String id) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment,id);
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransaction.commit();
            return true;
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_homeScreen_home :
                loadFragment(new HomeFragment(),"home");
                break;
            case R.id.button_homeScreen_addNew:
                loadFragment(new AddNewFragment(),"addnew");
                break;
            case R.id.button_homeScreen_addExisting :
                loadFragment(new AddExistingFragment(),"addexist");
                break;
        }
    }
}

