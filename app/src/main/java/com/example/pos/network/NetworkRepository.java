package com.example.pos.network;

import com.example.pos.model.BalanceResponse;
import com.example.pos.model.CategoryResponse;
import com.example.pos.model.DeleteSaleItemResponse;
import com.example.pos.model.DeliverNumbersResponse;
import com.example.pos.model.DeliveryOrdersResponse;
import com.example.pos.model.GenerateReceiveNoResponse;
import com.example.pos.model.GenerateSaleNoResponse;
import com.example.pos.model.ItemSearchResponse;
import com.example.pos.model.ItemsPriceResponse;
import com.example.pos.model.ItemsResponse;
import com.example.pos.model.LoginResponse;
import com.example.pos.model.MerchantItemsResponse;
import com.example.pos.model.NRCFormatResponse;
import com.example.pos.model.NewReceiveSaveResponse;
import com.example.pos.model.PaymentResponse;
import com.example.pos.model.PreSaleResponse;
import com.example.pos.model.ReceiveItemViewResponse;
import com.example.pos.model.ReceiveItemsResponse;
import com.example.pos.model.ReceiveNumbersResponse;
import com.example.pos.model.SaleNumbersResponse;
import com.example.pos.model.SaleOrderDetailResponse;
import com.example.pos.model.SaleOrdersSaveResponse;
import com.example.pos.model.SaleOrdersSearchResponse;
import com.example.pos.model.SetSellingPriceResponse;
import com.example.pos.model.StateCityResponse;
import com.example.pos.model.StockBalanceResponse;
import com.example.pos.model.UOMResponse;
import com.example.pos.model.UpdateSaleItemResponse;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Single;

public class NetworkRepository {

    private RetrofitService retrofitService;

    @Inject
    public NetworkRepository(RetrofitService retrofitService) {
        this.retrofitService = retrofitService;
    }

    public Single<StateCityResponse> stateCity() {
        return retrofitService.stateCity();
    }

    public Single<NRCFormatResponse> nrcFormat(Map<String, Object> body) {
        return retrofitService.nrcFormat(body);
    }

    public Single<LoginResponse> login(Map<String, Object> fields) {
        return retrofitService.login(fields);
    }

    public Single<GenerateReceiveNoResponse> generateReceiveNo() {
        return retrofitService.generateReceiveNo();
    }

    public Single<DeliverNumbersResponse> deliverNumbers(int id) {
        return retrofitService.deliverNumbers(id);
    }

    public Single<DeliverNumbersResponse> deliveredNumbers(int id) {
        return retrofitService.deliveredNumbers(id);
    }

    public Single<DeliveryOrdersResponse> deliveryOrders(String orderNumber) {
        return retrofitService.deliveryOrders(orderNumber);
    }

    public Single<NewReceiveSaveResponse> newReceiveSave(String receiveNo, String receiveBy, String orderNo, int merchantId) {
        return retrofitService.newReceiveSave(receiveNo, receiveBy, orderNo, merchantId);
    }

    public Single<ReceiveNumbersResponse> receiveNumbers(int id) {
        return retrofitService.receiverNumbers(id);
    }

    public Single<ReceiveItemsResponse> receiveItems(String receiveNo, String startDate, String endDate, int merchantId) {
        return retrofitService.receiveItems(receiveNo, startDate, endDate, merchantId);
    }

    public Single<ReceiveItemViewResponse> receiveItemView(String receiveNo, String receiverName) {
        return retrofitService.receiveItemView(receiveNo, receiverName);
    }

    public Single<CategoryResponse> category() {
        return retrofitService.category();
    }

    public Single<CategoryResponse> allCategory() {
        return retrofitService.allCategory();
    }

    public Single<ItemsResponse> items() {
        return retrofitService.items();
    }

    public Single<MerchantItemsResponse> merchantItems(int id) {
        return retrofitService.merchantItems(id);
    }

    public Single<MerchantItemsResponse> allMerchantItems(int id) {
        return retrofitService.allMerchantItems(id);
    }

    public Single<ItemsPriceResponse> itemsPriceSearch(String itemCode, int id) {
        return retrofitService.itemsPriceSearch(itemCode, id);
    }

    public Single<SetSellingPriceResponse> setSellingPrice(int id, double price) {
        return retrofitService.setSellingPrice(id, price);
    }

    public Single<UOMResponse> UOM(String itemCode, int id) {
        return retrofitService.UOM(itemCode, id);
    }

    public Single<GenerateSaleNoResponse> generateSaleNo() {
        return retrofitService.generateSaleNo();
    }

    public Single<PreSaleResponse> preSale(String itemCode, int uomId, int merchantId, int qty) {
        return retrofitService.preSale(itemCode, uomId, merchantId, qty);
    }

    public Single<SaleOrdersSaveResponse> saleOrdersSave(Map<String, Object> fields) {
        return retrofitService.saleOrdersSave(fields);
    }

    public Single<BalanceResponse> balance(int id) {
        return retrofitService.balance(id);
    }

    public Single<SaleOrdersSearchResponse> saleOrdersSearch(String saleNo,
                                                             String startDate,
                                                             String endDate,
                                                             int merchantId) {
        return retrofitService.saleOrdersSearch(saleNo, startDate, endDate, merchantId);
    }

    public Single<SaleNumbersResponse> saleNumbers(int id) {
        return retrofitService.saleNumbers(id);
    }

    public Single<SaleOrderDetailResponse> saleOrdersDatail(String receiveNo, double price) {
        return retrofitService.saleOrderDetail(receiveNo, price);
    }

    public Single<StockBalanceResponse> stockBalance(int categoryId, int itemId, int merchantId) {
        return retrofitService.stockBalance(categoryId, itemId, merchantId);
    }

    public Single<ItemSearchResponse> searchItems(int categoryId, int itemId, int merchantId) {
        return retrofitService.searchItems(categoryId, itemId, merchantId);
    }

    public Single<PaymentResponse> payment(int merchantId, String orderNo, String startDate, String endDate) {
        return retrofitService.payment(merchantId, orderNo, startDate, endDate);
    }

    public Single<DeleteSaleItemResponse> deleteSaleItem(int id, String itemCode, int uomId, int merchantId, int qty, String salesNo, int isLast) {
        return retrofitService.deleteSaleItem(id, itemCode, uomId, merchantId, qty, salesNo, isLast);
    }

    public Single<UpdateSaleItemResponse> updateSaleItem(int id, String itemCode, int uomId, int merchantId, int qty, String salesNo) {
        return retrofitService.updateSaleItem(id, itemCode, uomId, merchantId, qty, salesNo);
    }

}
