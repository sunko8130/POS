package com.example.pos.di.module;

import com.example.pos.ui.home.HomeFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBindingModule {

    @ContributesAndroidInjector
    abstract HomeFragment provideHomeFragment();

}
