package com.fastart.project.aliengame;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import static com.fastart.project.aliengame.ads.AdUniIds.interstitialId;

public class AndroidLauncher extends AndroidApplication implements AdController{

	private InterstitialAd interstitialAd;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initAds();
		initUi();
	}

	private void initAds(){

		interstitialAd = new InterstitialAd(this);
		interstitialAd.setAdUnitId(interstitialId);
	}

	private void initUi(){
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new FlappyBird(this), config);

		interstitialAd.setAdListener(new AdListener(){
			@Override
			public void onAdClosed() {
				loadInterstitial();
			}
		});
		loadInterstitial();
	}
	@Override
	public void showInterstitial() {

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showInterstitialInternal();
			}
		});

	}

	private void showInterstitialInternal(){
		if (interstitialAd.isLoaded()){
			interstitialAd.show();
		}
	}

	private void loadInterstitial(){

		//load ad if internet is there
		if(isNetworkConnected()) {
			AdRequest adRequest = new AdRequest.Builder().build();
			interstitialAd.loadAd(adRequest);
		}
	}

	@Override
	public boolean isNetworkConnected() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		return networkInfo !=null && networkInfo.isConnected();
	}
}
