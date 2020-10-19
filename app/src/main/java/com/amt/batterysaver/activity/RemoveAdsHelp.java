package com.amt.batterysaver.activity;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.ads.control.AdControl;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

public class RemoveAdsHelp {

    private static String Code_InApp = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiOuydQZF//vYUY4kB4m1+PGANRR6dN6cqjEjXDh+qiCGU+GbavDDjOPeZzxX/8Uld4wGsqPdA0MwyehCofZeWuQz81pk+2k8TaDVdfvvJ100HcqtkTXk24uIM49gBWcj3KslCNT0S7egbbnnlQ+BSV2OnqdlDDynm5e/Mr+reb9WaHWr44UiIVQ59T8mf4W5SHp4mBJDjY2cH+IbW0NMlq4bTrJc69XlFLqAzdTERaiuGY43brpGSMCbpS8EzGtypTgbjgkm3ogptGtqlrM9VnSC3nVhmenoY4eiNnccxfxI/TDgu5CriOc/VkEFswQwD8AlllAbRgBQT/k8GPcpfQIDAQAB";
    private static String Code_ProductId = "com.amt.batterysaver.removeads";
    private static String merchantID = "5794897387132212509";
    public static BillingProcessor bp;
    private static AdControl adControl;
    private static RemoveAdsHelp instance;
    private static Context context;
    private static PurchasedListener purchasedListener;

    public interface PurchasedListener {
        void Purchased();
    }

    private static BillingProcessor.IBillingHandler iBillingHandler = new BillingProcessor.IBillingHandler() {
        @Override
        public void onProductPurchased(String productId, TransactionDetails details) {
            adControl.remove_ads(true);
            if (purchasedListener != null)
                purchasedListener.Purchased();
            Log.v(RemoveAdsActivity.TagPurchase, "onProductPurchased");
        }

        @Override
        public void onPurchaseHistoryRestored() {
            adControl.remove_ads(bp.isPurchased(Code_ProductId));
            Log.v(RemoveAdsActivity.TagPurchase, "onPurchaseHistoryRestored");
        }

        @Override
        public void onBillingError(int errorCode, Throwable error) {
            Log.v(RemoveAdsActivity.TagPurchase, "onBillingError");
            Toast.makeText(context, "Up! something wong!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBillingInitialized() {
            adControl.remove_ads(bp.isPurchased(Code_ProductId));
            Log.v(RemoveAdsActivity.TagPurchase, adControl.remove_ads() + "");
        }
    };

    public static RemoveAdsHelp getInstance(Context value, PurchasedListener purchasedListener) {
        Log.v(RemoveAdsActivity.TagPurchase, "Get getInstance");
        if (instance == null) {
            instance = new RemoveAdsHelp();
        }
        context = value;
        adControl = AdControl.getInstance(context);
        RemoveAdsHelp.purchasedListener = purchasedListener;
        bp = new BillingProcessor(context, Code_InApp, merchantID, iBillingHandler);
        return instance;
    }

    public void Purchase_ads(Activity activity) {
        Log.v(RemoveAdsActivity.TagPurchase, "Purchase_ads");
        try {
            bp.purchase(activity, Code_ProductId);
            Log.v(RemoveAdsActivity.TagPurchase, "purchase");
        } catch (Exception ignored) {
            Log.v(RemoveAdsActivity.TagPurchase, "Exception purchase");
            Toast.makeText(context, "Oops, Something Went Wrong", Toast.LENGTH_SHORT).show();
        }
    }
}

