package app.tahachi.batterydoctor.activity;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import app.ads.control.AdControl;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

public class RemoveAdsHelp {

    private static String Code_InApp = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzaB9U9HDRVZHgaUVqL41WJRS6nhYdMYcM/HS1pMrDDFlNcGF4EhIhZpNQiRfGhScYE/aLRTHNTmhXhLxg+XGoVvlTk60c0F5Lc9AZ945yJOnF5qG0iefEgFxj2LWB/b7OMq1hxviyx5drOjs8Cf37UrUinMM7cz20tk3eAkX8DFauUvk/Ar4tOzLf+OqbhANDtH/pSKQvvx5fqiwr/H52fHTfJwpOPmhK8fd9kd0Vm3kQBYuib9L9BPidGbSDkP3bYUAoTYtXNJ8ONmp8wmppceGSsTPJOQUw/oeq0r2gRF/F5YFLw8ptjYyOaEI0qitq3APQYDo/Je5W9bMYc4StQIDAQAB";
    private static String Code_ProductId = "app.tahachi.batterydoctor.removeads";
    private static String merchantID = "9101010923570468146";
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

