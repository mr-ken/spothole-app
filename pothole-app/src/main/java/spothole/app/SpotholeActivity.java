package spothole.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

import spothole.client.SpotRequest;
import spothole.client.SpotResponse;

public class SpotholeActivity extends Activity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    private static final String LOG_TAG = "SpotholeActivity";

    private static final String BASE_URL = "http://spotholes.herokuapp.com/api";

    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private RequestQueue mRequestQueue;

    private LocationClient mLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spothole);

        mRequestQueue = Volley.newRequestQueue(this);
        mLocationClient =  new LocationClient(this, this, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (servicesConnected()) {
            mLocationClient.connect();
        }
    }

    @Override
    protected void onStop() {
        mLocationClient.disconnect();
        super.onStop();
    }

    public void onClickSmall(View view) {
        reportPotHole(Size.SMALL);
    }

    public void onClickMedium(View view) {
        reportPotHole(Size.MEDIUM);
    }

    public void onClickLarge(View view) {
        reportPotHole(Size.LARGE);
    }

    private void reportPotHole(Size potHoleSize) {
        Location currentLocation = mLocationClient.getLastLocation();
        Log.i(LOG_TAG, String.format("reporting pot hole at %s/%s", currentLocation.getLongitude(), currentLocation.getLatitude()));
        mRequestQueue.add(new SpotRequest(
                BASE_URL,
                potHoleSize.toString(),
                currentLocation.getLongitude(),
                currentLocation.getLatitude(),
                new Response.Listener<SpotResponse>() {
                    @Override
                    public void onResponse(SpotResponse response) {
                        Log.i(LOG_TAG, "successful response");
//                        showProgress(false);
                        showDialog(getString(R.string.thank_you));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        showProgress(false);
                        try {
                            throw error;
                        } catch (VolleyError e) {
                            Log.e(LOG_TAG, "spot request failed", error);
                            showDialog(getString(R.string.error_server));
                        }
                    }
                }
        ));
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisconnected() {
        Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);

            } catch (IntentSender.SendIntentException e) {
                Log.e(LOG_TAG, "failed to connect to Google Play services", e);
            }
        } else {
            showDialog(Integer.toString(connectionResult.getErrorCode()));
        }
    }

    enum Size {
        SMALL,
        MEDIUM,
        LARGE
    }

    private void showDialog(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        mRequestQueue.cancelAll(this);
        super.onDestroy();
    }

    public static class ErrorDialogFragment extends DialogFragment {

        private Dialog mDialog;

        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }

    }

    private boolean servicesConnected() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (ConnectionResult.SUCCESS == resultCode) {
            Log.d(LOG_TAG, "Google Play services is available");
            return true;
        } else {
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            if (errorDialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                errorFragment.show(getFragmentManager(), "Location Updates");
            }
            return false;
        }

    }
}
