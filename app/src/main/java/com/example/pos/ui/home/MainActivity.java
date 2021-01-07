package com.example.pos.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;

import com.example.pos.R;
import com.example.pos.model.LoginData;
import com.example.pos.ui.base.BaseActivity;
import com.example.pos.ui.login.LoginActivity;
import com.google.android.material.navigation.NavigationView;

import org.mmtextview.components.complex.MMNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;

import static com.example.pos.util.Constant.LOGIN_DATA;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nav_view)
    MMNavigationView navView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);
        displaySelectedScreen(R.id.nav_home);

        //login data
        LoginData loginData = Paper.book().read(LOGIN_DATA);

        if (loginData != null) {
            ToolbarViewHolder toolbarViewHolder = new ToolbarViewHolder(toolbar);
            toolbarViewHolder.tvToolbarName.setText(Html.fromHtml(getResources().getString(R.string.toolbar_name, loginData.getUserName())));
        }
    }

    private void displaySelectedScreen(int itemId) {
        navView.setCheckedItem(itemId);
        Fragment fragment = null;
        switch (itemId) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;
            case R.id.nav_logout:
                logout();
                break;
            default:
                break;
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment)
                    .commit();
        }
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        displaySelectedScreen(id);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment currentFragment = getSupportFragmentManager()
                    .findFragmentById(R.id.frame);
            if (currentFragment instanceof HomeFragment) {
                finishAffinity();
            } else {
                int count = getSupportFragmentManager().getBackStackEntryCount();
                if (count == 0) {
                    navView.getMenu().getItem(0).setChecked(true);
                    displaySelectedScreen(R.id.nav_home);
                } else {
                    getSupportFragmentManager().popBackStackImmediate();
                }
            }
        }
    }

    private void logout() {
        Paper.book().destroy();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}