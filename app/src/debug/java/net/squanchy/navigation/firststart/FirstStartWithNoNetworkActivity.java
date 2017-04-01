package net.squanchy.navigation.firststart;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import net.squanchy.R;
import net.squanchy.fonts.TypefaceStyleableActivity;

public class FirstStartWithNoNetworkActivity extends TypefaceStyleableActivity {

    private static final String EXTRA_CONTINUATION_INTENT = FirstStartWithNoNetworkActivity.class.getCanonicalName() + ".continuation_intent";

    private Intent continuationIntent;

    private ConnectivityManager.NetworkCallback networkCallback;
    private ConnectivityManager connectivityManager;

    public static Intent createIntentContinuingTo(Context context, Intent continuationIntent) {
        Intent intent = new Intent(context, FirstStartWithNoNetworkActivity.class);
        intent.putExtra(EXTRA_CONTINUATION_INTENT, continuationIntent);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_first_start_with_no_network);

        findViewById(R.id.first_start_nevermind_button).setOnClickListener(view -> finish());

        continuationIntent = getIntent().getParcelableExtra(EXTRA_CONTINUATION_INTENT);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkCallback = new NetworkConnectedCallback();
    }

    @Override
    protected void onStart() {
        super.onStart();

        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build();
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
    }

    @Override
    protected void onStop() {
        super.onStop();
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }

    private class NetworkConnectedCallback extends ConnectivityManager.NetworkCallback {

        private boolean receivedOnAvailable = false;

        @Override
        public void onAvailable(Network network) {
            if (receivedOnAvailable) {
                return;
            }

            receivedOnAvailable = true;

            Toast.makeText(FirstStartWithNoNetworkActivity.this, R.string.network_connected, Toast.LENGTH_SHORT).show();
            startActivity(continuationIntent);      // We don't use the navigator here, we basically want to restart the whole flow
            finish();
        }
    }
}
