package appsiam.tua.saran.mysiam;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DirectionCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;//แผนที่ที่อยู่บนโลก
    private Criteria criteria;//เป็นตัวกำหนดค่าของแผนที่
    private double latADouble = 13.719156, lngADouble = 100.451818;
    private LatLng userLatLng;//คือตำแหน่งของแผนที่
    private int[] mkInts = new int[]{R.mipmap.mk_marker, R.mipmap.mk_friend};
    private String[] userStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Setup//เป็นการเปิด service ของ location บน map
        setUp();

        //


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        createFragment();


    }   //Main Method

    @Override
    protected void onResume() {
        super.onResume();

        locationManager.removeUpdates(locationListener);

        //For NETWORK
        Location networkLocation = myFindLocation(LocationManager.NETWORK_PROVIDER);
        if (networkLocation != null) {
            latADouble = networkLocation.getLatitude();
            lngADouble = networkLocation.getLongitude();
        }

        //For GPS
        Location gpsLocation = myFindLocation(LocationManager.GPS_PROVIDER);
        if (gpsLocation != null) {
            latADouble = gpsLocation.getLatitude();
            lngADouble = gpsLocation.getLongitude();
        }

        Log.d("SiamV2", "Lat ==> " + latADouble);
        Log.d("SiamV2", "Lng ==> " + lngADouble);



    }


    private void CheckAndEditLocation() {

        MyConstant myConstant = new MyConstant();
        String tag = "SiamV3";
        boolean b = true;
        String urlPHP = null;


        try {

            //Check
            GetAllData getAllData = new GetAllData(MapsActivity.this);
            getAllData.execute(myConstant.getUrlGetAllLocation());
            String strjSON = getAllData.get();
            Log.d(tag, "JSON ==> " + strjSON);

            JSONArray jsonArray = new JSONArray(strjSON);
            for (int i = 0; i<jsonArray.length(); i+=1) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (userStrings[1].equals(jsonObject.getString("Name"))) {
                    b = false;
                } else {
                    myCreateMarker(jsonObject.getString("Name"),
                            new LatLng(Double.parseDouble(jsonObject.getString("Lat")), Double.parseDouble(jsonObject.getString("Lng"))), mkInts[1]);
                }



            }//for

            if (b) {
                //No Name
                Log.d(tag, "No Name");
                urlPHP = myConstant.getUrlAddLocation();

            } else {
                //Have Name
                Log.d(tag, "Have Name");
                urlPHP = myConstant.getUrlEditLocation();
            }


            AddAndEditLocation addAndEditLocation = new AddAndEditLocation(MapsActivity.this);
            addAndEditLocation.execute(userStrings[1],
                    Double.toString(latADouble),
                    Double.toString(lngADouble),
                    urlPHP);

            Log.d(tag, "Result ==> " + addAndEditLocation.get());

        } catch (Exception e) {
            Log.d(tag, "e check ==> " + e.toString());
        }

    }

    @Override
    //เมื่อปิด app มันจะไปทำการปิด service ด้วย
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }

    public Location myFindLocation(String strPovider) {

        Location location = null;

        if (locationManager.isProviderEnabled(strPovider)) {
            //ถ้าจำไม่ได้ต้องใส่อะไรบ้างใน method ให้กด ctrl+p มันจะแสดง suggestion ให้ดู
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            locationManager.requestLocationUpdates(strPovider, 1000, 10, locationListener);

        }

        return location;
    }

    // เป็นการค้นหา location
    public LocationListener locationListener = new LocationListener() {
        @Override
        //เมื่อไหร่ก็ตามที่เราขยับตำแหน่งมันจะขยับตาม
        public void onLocationChanged(Location location) {
            latADouble = location.getLatitude();
            lngADouble = location.getLongitude();

        }

        @Override
        //สถานะของการเชื่อมต่อ เช่น มีคนปิดสถานะการติดตามสามารถให้ฟังชั่นนี้แจ้งเตือนได้
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        //ถ้ามีสัญญาณจะให้ทำอะไร
        public void onProviderEnabled(String provider) {

        }

        @Override
        //ถ้ามือถือไม่มีสัญญาณหายจะให้ทำอะไร
        public void onProviderDisabled(String provider) {

        }
    };

    private void setUp() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);// เป็นความละเอียด Fine ชัดสุด
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);// setaltitudeRequired กับ setBearingRequired ต้องใช้คู่กัน
        userStrings = getIntent().getStringArrayExtra("Login");
    }

    private void createFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Setup Center Map
        userLatLng = new LatLng(latADouble, lngADouble);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15));

        myCreateMarker(userStrings[1], userLatLng, mkInts[0]);

        CheckAndEditLocation();

        // Click Marker
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Log.d("SiamV4", "Marker lat ==> " + marker.getPosition().latitude);
                Log.d("SiamV4", "Marker lng ==> " + marker.getPosition().longitude);

                GoogleDirection.withServerKey("AIzaSyB05ZoxF0OskpS4SwAvGbhe64JMsflwN1E")
                        .from(new LatLng(latADouble, lngADouble))
                        .to(marker.getPosition())
                        .transportMode(TransportMode.DRIVING)
                        .execute(MapsActivity.this);
                return true;
            }
        });

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    } // เกี่ยวกับการวาดแผนที่ onMapReady

    private void myCreateMarker(String strName, LatLng latLng, int intImage) {
        mMap.addMarker(new MarkerOptions().position(latLng).title(strName).icon(BitmapDescriptorFactory.fromResource(intImage)));
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {

        if (direction.isOK()) {
            ArrayList<LatLng> arrayList = direction.getRouteList()
                    .get(0)
                    .getLegList()
                    .get(0)
                    .getDirectionPoint();
            mMap.addPolyline(DirectionConverter
                    .createPolyline(MapsActivity.this, arrayList, 5, Color.BLUE));
        }

    }

    @Override
    public void onDirectionFailure(Throwable t) {

    }
}//Main Class
