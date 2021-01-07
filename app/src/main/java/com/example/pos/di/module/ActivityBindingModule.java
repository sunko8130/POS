package com.example.pos.di.module;

import com.example.pos.ui.category.CategorySearchActivity;
import com.example.pos.ui.home.MainActivity;
import com.example.pos.ui.items.ItemsActivity;
import com.example.pos.ui.login.LoginActivity;
import com.example.pos.ui.sales.SaleItemUpdateFragment;
import com.example.pos.ui.search_categories.MainCategoriesActivity;
import com.example.pos.ui.payment.AddNewPaymentActivity;
import com.example.pos.ui.payment.PaymentActivity;
import com.example.pos.ui.pricing.PricingActivity;
import com.example.pos.ui.receive_items.AddNewReceiveItemActivity;
import com.example.pos.ui.receive_items.AddReceiveOrderItemActivity;
import com.example.pos.ui.receive_items.ReceiveItemDetailFragment;
import com.example.pos.ui.receive_items.ReceiveItemViewActivity;
import com.example.pos.ui.receive_items.ReceiveItemsActivity;
import com.example.pos.ui.register.RegisterActivity;
import com.example.pos.ui.sales.AddNewSaleItemFragment;
import com.example.pos.ui.sales.AddSalesActivity;
import com.example.pos.ui.sales.SaleItemDetailFragment;
import com.example.pos.ui.sales.SalesActivity;
import com.example.pos.ui.sales.SalesOrdersActivity;
import com.example.pos.ui.stock_balance.StockBalanceActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = {MainFragmentBindingModule.class})
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector()
    abstract LoginActivity bindLoginActivity();

    @ContributesAndroidInjector()
    abstract RegisterActivity bindRegisterActivity();

    @ContributesAndroidInjector()
    abstract SalesOrdersActivity bindSalesOrdersActivity();

    @ContributesAndroidInjector()
    abstract SalesActivity bindSalesActivity();

    @ContributesAndroidInjector()
    abstract ItemsActivity bindItemsActivity();

    @ContributesAndroidInjector()
    abstract CategorySearchActivity bindSubCategoryActivity();

    @ContributesAndroidInjector()
    abstract MainCategoriesActivity bindMainCategoriesActivity();

    @ContributesAndroidInjector()
    abstract ReceiveItemViewActivity bindReceiveItemViewActivity();

    @ContributesAndroidInjector()
    abstract AddNewPaymentActivity bindAddNewPaymentActivity();

    @ContributesAndroidInjector()
    abstract PaymentActivity bindPaymentActivity();

    @ContributesAndroidInjector()
    abstract AddReceiveOrderItemActivity bindAddReceiveOrderItemActivity();

    @ContributesAndroidInjector()
    abstract StockBalanceActivity bindStockBalanceActivity();

    @ContributesAndroidInjector()
    abstract AddSalesActivity bindAddSalesActivity();

    @ContributesAndroidInjector()
    abstract AddNewReceiveItemActivity bindAddNewReceiveItemActivity();

    @ContributesAndroidInjector()
    abstract ReceiveItemsActivity bindReceiveItemsActivity();

    @ContributesAndroidInjector()
    abstract PricingActivity bindPricingActivity();

    @ContributesAndroidInjector
    abstract AddNewSaleItemFragment bindAddNewSaleItemFragment();

    @ContributesAndroidInjector
    abstract ReceiveItemDetailFragment bindReceiveItemDetailFragment();

    @ContributesAndroidInjector
    abstract SaleItemDetailFragment bindSaleItemDetailFragment();

    @ContributesAndroidInjector
    abstract SaleItemUpdateFragment bindSaleItemUpdateFragment();

}
