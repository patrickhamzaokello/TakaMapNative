package com.pkasemer.takamap.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pkasemer.takamap.Adapters.UserOrdersAdapter;
import com.pkasemer.takamap.Apis.MovieApi;
import com.pkasemer.takamap.Apis.MovieService;
import com.pkasemer.takamap.Dialogs.OrderNotFound;
import com.pkasemer.takamap.ManageOrders;
import com.pkasemer.takamap.Models.HomeFeed;
import com.pkasemer.takamap.Models.Infrastructure;
import com.pkasemer.takamap.Models.UserOrders;
import com.pkasemer.takamap.Models.UserOrdersResult;
import com.pkasemer.takamap.R;
import com.pkasemer.takamap.RootActivity;
import com.pkasemer.takamap.Utils.PaginationScrollListener;

import org.w3c.dom.Text;

import java.util.List;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Home extends Fragment {

    private static final int LOCATION_MIN_UPDATE_TIME = 10;
    private static final int LOCATION_MIN_UPDATE_DISTANCE = 1000;
    private static final String TAG = "HomeFragment";


    private MapView mapView;
    private GoogleMap googleMap;
    private Location location = null;


    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private static int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;

    private MovieService movieService;
    List<Infrastructure> all_infrastructures;


    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            drawMarker(location, getText(R.string.i_am_here).toString());
            locationManager.removeUpdates(locationListener);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private LocationManager locationManager;

    public Home() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        movieService = MovieApi.getClient(getContext()).create(MovieService.class);

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        initView(view, savedInstanceState);

        loadFirstPage();
        return view;
    }

    private void initView(View view, Bundle savedInstanceState) {
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mapView_onMapReady(googleMap);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        getCurrentLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private void initMap() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (googleMap != null) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.getUiSettings().setAllGesturesEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(true);

                try {
                    // Customise the styling of the base map using a JSON object defined
                    // in a raw resource file.
                    boolean success = googleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    getContext(), R.raw.style));

                    if (!success) {
                        Log.e(TAG, "Style parsing failed.");
                    }
                } catch (Resources.NotFoundException e) {
                    Log.e(TAG, "Can't find style.", e);
                }
            }
        } else {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12);
            }
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 13);
            }
        }
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                Toast.makeText(getContext(), getText(R.string.provider_failed), Toast.LENGTH_LONG).show();
            } else {
                location = null;
                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_MIN_UPDATE_TIME, LOCATION_MIN_UPDATE_DISTANCE, locationListener);
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_MIN_UPDATE_TIME, LOCATION_MIN_UPDATE_DISTANCE, locationListener);
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                if (location != null) {
                    drawMarker(location, getText(R.string.i_am_here).toString());

                }
            }
        } else {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12);
            }
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 13);
            }
        }
    }

    private void mapView_onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        initMap();
        getCurrentLocation();
    }

    private void drawMarker(Location location, String title) {
        if (this.googleMap != null) {
//            googleMap.clear();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.position(latLng);
//            markerOptions.title(title);
//            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//            googleMap.addMarker(markerOptions);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(12.8f));

        }
    }


    private void loadFirstPage() {

//        hideErrorView();
        currentPage = PAGE_START;

        callInfrastructure().enqueue(new Callback<HomeFeed>() {
            @Override
            public void onResponse(Call<HomeFeed> call, Response<HomeFeed> response) {
//                Log.i(TAG, "onResponse: " + (response.raw().cacheResponse() != null ? "Cache" : "Network"));

                // Got data. Send it to adapter
                List<Infrastructure> infrastructures = fetchResults(response);
//                progressBar.setVisibility(View.GONE);
                if (infrastructures.isEmpty()) {
                    Log.i("inf_empty", String.valueOf(infrastructures));
                    return;
                } else {
//                    if infrastructures are available
                    Log.i("inf_available", String.valueOf(infrastructures));

                    all_infrastructures = infrastructures;

                    initMarker(all_infrastructures);

                }

            }

            @Override
            public void onFailure(Call<HomeFeed> call, Throwable t) {
                t.printStackTrace();
                Log.i("onFailure", String.valueOf(t));

            }
        });
    }

    private void initMarker(List<Infrastructure> listData){
        //iterasi semua data dan tampilkan markernya
        for (int i=0; i<listData.size(); i++){
            //set latlng nya
            LatLng location = new LatLng(Double.parseDouble(listData.get(i).getLatitude()), Double.parseDouble(listData.get(i).getLongitude()));
            //tambahkan markernya
            if((listData.get(i).getType()).equals("big banks")){
                googleMap.addMarker(new MarkerOptions().position(location).zIndex(i).title(listData.get(i).getType()).icon(bitmapDescriptor(getContext(), R.drawable.ic_dashicons_trash)));

            } else {
                googleMap.addMarker(new MarkerOptions().position(location).zIndex(i).title(listData.get(i).getType()).icon(bitmapDescriptor(getContext(), R.drawable.ic_heroicons_outline_trash__1_)));

            }
            //set latlng index ke 0
//            LatLng latLng = new LatLng(Double.parseDouble(listData.get(0).getLatitude()), Double.parseDouble(listData.get(0).getLongitude()));
            //lalu arahkan zooming ke marker index ke 0
//            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLng.latitude,latLng.longitude), 14.0f));
        }

        // adding on click listener to marker of google maps.
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // on marker click we are getting the title of our marker
                // which is clicked and displaying it in a toast message.
                int markerId = (int) marker.getZIndex();
                showDialog(listData.get(markerId));
                return false;
            }
        });
    }



    private void showDialog(Infrastructure infrastructure) {

        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_show_round_dialog);

        TextView type_text = dialog.findViewById(R.id.type_text);
        TextView aim_lable = dialog.findViewById(R.id.aim_lable);
        TextView latittude = dialog.findViewById(R.id.latittude);
        TextView longitude = dialog.findViewById(R.id.longitude);
        TextView waste_infras_desc = dialog.findViewById(R.id.waste_infras_desc);

        type_text.setText(infrastructure.getType());
        aim_lable.setText(infrastructure.getAim());
        latittude.setText("Lat: " + infrastructure.getLatitude());
        longitude.setText("Long: " + infrastructure.getLongitude());
        waste_infras_desc.setText(infrastructure.getDescription());


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }


    private BitmapDescriptor bitmapDescriptor(Context context, int resourceId){
        Drawable vectordrawable = ContextCompat.getDrawable(context, resourceId);
        vectordrawable.setBounds(0,0,vectordrawable.getIntrinsicWidth(), vectordrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectordrawable.getIntrinsicWidth(), vectordrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectordrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    private List<Infrastructure> fetchResults(Response<HomeFeed> response) {
        HomeFeed homeFeed = response.body();
        TOTAL_PAGES = homeFeed.getTotalPages();
        System.out.println("total pages" + TOTAL_PAGES);
        return homeFeed.getInfrastructure();
    }


    private Call<HomeFeed> callInfrastructure() {
        return movieService.getAllInfrastructure(
                currentPage
        );
    }


}