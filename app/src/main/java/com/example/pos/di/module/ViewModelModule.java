package com.example.pos.di.module;

import com.example.pos.common.ViewModelFactory;
import com.example.pos.di.keys.ViewModelKey;
import com.example.pos.ui.category.CategoryViewModel;
import com.example.pos.ui.home.HomeViewModel;
import com.example.pos.ui.items.SearchItemViewModel;
import com.example.pos.ui.login.LoginViewModel;
import com.example.pos.ui.payment.PaymentViewModel;
import com.example.pos.ui.pricing.PricingViewModel;
import com.example.pos.ui.receive_items.ReceiveItemsViewModel;
import com.example.pos.ui.register.RegisterViewModel;
import com.example.pos.ui.sales.SalesViewModel;
import com.example.pos.ui.stock_balance.StockBalanceViewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel.class)
    abstract ViewModel bindRegisterViewModel(RegisterViewModel registerViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    abstract ViewModel bindLoginViewModel(LoginViewModel loginViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ReceiveItemsViewModel.class)
    abstract ViewModel bindReceiveItemsViewModel(ReceiveItemsViewModel receiveItemsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CategoryViewModel.class)
    abstract ViewModel bindCategoryViewModel(CategoryViewModel categoryViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(StockBalanceViewModel.class)
    abstract ViewModel bindStockBalanceViewModel(StockBalanceViewModel stockBalanceViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PricingViewModel.class)
    abstract ViewModel bindPricingViewModel(PricingViewModel pricingViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SalesViewModel.class)
    abstract ViewModel bindSalesViewModel(SalesViewModel salesViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel.class)
    abstract ViewModel bindHomeViewModel(HomeViewModel homeViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SearchItemViewModel.class)
    abstract ViewModel bindSearchItemViewModel(SearchItemViewModel searchItemViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(PaymentViewModel.class)
    abstract ViewModel bindPaymentViewModel(PaymentViewModel paymentViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

}
