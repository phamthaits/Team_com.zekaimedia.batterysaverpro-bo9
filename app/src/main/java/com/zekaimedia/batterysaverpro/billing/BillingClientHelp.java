package com.zekaimedia.batterysaverpro.billing;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ads.control.AdControl;
import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.util.ArrayList;
import java.util.List;

public class BillingClientHelp {

    private static String Code_ProductId = "com.qamob.batterysaver.removeads";
    private static AdControl adControl;
    private static SkuDetails skuDetails;
    private static BillingClient billingClient;
    private static Activity activity;

    private static void querySkuDetails(SkuDetailsRequest skuDetailsRequest) {
        List<String> skuList = new ArrayList<>();
        skuList.add(Code_ProductId);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        billingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<SkuDetails> skuDetailsList) {
                Log.v("ads", skuDetailsList.toString());
                if (skuDetailsList == null || skuDetailsList.isEmpty()) {
                    skuDetails = null;
                } else skuDetails = skuDetailsList.get(0);
                if (skuDetailsRequest != null)
                    skuDetailsRequest.onSkuDetailsResponse();
            }
        });
    }

    private static PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
        @Override
        public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchases) {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (Purchase purchase : purchases) {
                    handlePurchase(purchase);
                }
            } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
                Toast.makeText(activity, "USER CANCELED", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(activity, "ERROR", Toast.LENGTH_LONG).show();
            }
        }
    };

    private static void handlePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();
                billingClient.acknowledgePurchase(acknowledgePurchaseParams, new AcknowledgePurchaseResponseListener() {//Xác nhận giao dịch
                    @Override
                    public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
                        adControl.remove_ads(true);//Mua hàng thành công
                        activity.finish();
                    }
                });
            }
        }
    }

    private static BillingInitListener billingInitListener;

    public static BillingClient initBillingClient(Activity tActivity, Context tContext, BillingInitListener tBillingInitListener) {
        activity = tActivity;
        adControl = AdControl.getInstance(activity);
        billingInitListener = tBillingInitListener;
//        if (billingClient != null && billingClient.isReady() && skuDetails != null) {
//            if (tBillingInitListener != null)
//                tBillingInitListener.onBillingInitialized();
//            return billingClient;//Không cần khởi tạo lại khi Billing đã sẵn sàng sử dụng
//        }

        billingClient = BillingClient.newBuilder(activity)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    querySkuDetails(new SkuDetailsRequest() {
                        @Override
                        public void onSkuDetailsResponse() {
                            if (skuDetails == null) {
                                if (billingInitListener != null)
                                    billingInitListener.onBillingInitialized();
                                return;
                            }
                            billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP, new PurchaseHistoryResponseListener() {
                                @Override
                                public void onPurchaseHistoryResponse(@NonNull BillingResult billingResult, @Nullable List<PurchaseHistoryRecord> listHistory) {
                                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                        if (!listHistory.isEmpty()) {//Xem lại lịch sử giao dịch.
                                            {
                                                Log.v("ads", listHistory.toString());
                                                for (PurchaseHistoryRecord historyRecord : listHistory) {
                                                    ArrayList<String> prdIds = historyRecord.getSkus();
                                                    if (prdIds != null && !prdIds.isEmpty())
                                                        for (String prdId : prdIds) {
                                                            if (prdId.equals(Code_ProductId)) {
                                                                adControl.remove_ads(true);
                                                            }
                                                        }
                                                }
                                            }
                                        } else adControl.remove_ads(false);
                                    }
                                    if (billingInitListener != null)
                                        billingInitListener.onBillingInitialized();
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
            }
        });
        return billingClient;
    }

    public static void buyProduct(Activity activity) {
        if (adControl.remove_ads()) {
            activity.finish();
            return;
        }
        initBillingClient(activity, activity, new BillingInitListener() {
            @Override
            public void onBillingInitialized() {
                if (adControl.remove_ads()) {
                    activity.finish();
                    return;
                }
                if (skuDetails == null) {
                    Toast.makeText(activity, "The item is not ready, please try again later.", Toast.LENGTH_LONG).show();
                    return;
                }
                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuDetails)
                        .build();
                billingClient.launchBillingFlow(activity, billingFlowParams).getResponseCode();
            }
        });
    }

    public interface BillingInitListener {
        void onBillingInitialized();
    }

    public interface SkuDetailsRequest {
        void onSkuDetailsResponse();
    }
}

