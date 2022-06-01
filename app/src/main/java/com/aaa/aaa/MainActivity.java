package com.aaa.aaa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private NaverMap naverMap;
    private FusedLocationSource locationSource;
    public double user_latitude, user_longitude;
    public LocationManager locationManager;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    List<LatLng> lstLatLng = new ArrayList<>();

    public int r = 3000; // 판매처 표시 반경 (미터 단위)
    public String[][] info = new String[][]{
            // 판매처 위치 정보 {"판매처 이름", "위도", "경도"}
            {"진주우리먹거리협동조합 진주텃밭 진양호점", "35.16695409999988", "128.03590219999933"},
            {"카페옴마", "35.16656729999968", "128.04476910000008"},
            {"진주시농산물도매시장", "35.21175809999977", "128.1146698999997"},
            {"새봄청과평거점", "35.173310000000114", "128.0451059000001"},
            {"자연드림 평거점", "35.17380999999966", "128.0319081"},
            {"하나로마트 동부농협", "35.186777500000254", "128.1075986999997"},
            {"서부시장", "35.193876299999985", "128.07202819999932"},
             {"천전시장", "35.18530949999965", "128.08013329999997"},
             {"중앙청과", "35.210848399999804", "128.11491889999962"},
             {"중부농협로컬푸드직매장", "35.20974370000024", "128.09849779999934"}, //10
             {"청록청과", "35.1832717999997", "128.0708196"},
             {"한소쿠리", "35.19625450000002", "128.08194459999933"},
             {"도동청과", "35.17969899999999", "128.10908230000024"},
             {"신안청과", "35.182023799999904", "128.0591548999996"},
             {"청송청과", "35.182023799999904", "128.0591548999996"},
             {"진양청과", "35.19638299999998", "128.07840879999975"},
             {"달달하이", "35.17055959999964", "128.10568489999952"},
             {"일주청과", "35.18881300000013", "128.11458679999959"},
             {"혜덕농산", "35.21095740000024", "128.11478119999947"},
             {"농산물한식뷔폐", "35.21095740000024", "128.11478119999947"}, //20
             {"과일명가", "35.210925199999636", "128.11514929999984"},
             {"아주상회", "35.210925199999636", "128.11514929999984"},
             {"진양청과", "35.19638299999998", "128.07840879999975"},
             {"오목상회", "35.21095740000024", "128.11478119999947"},
             {"이현상회", "35.21095740000024", "128.11478119999947"},
             {"삼육농산", "35.19596870000025", "128.07699859999988"},
             {"농산물 공판장", "35.21095740000024", "128.11478119999947"},
             {"제일청과", "35.19651400000007", "128.07811079999937"},
             {"청과시장", "35.21091489999966", "128.1149496999995"},
             {"대창청과", "35.19652340000012", "128.0781266999993"}, //30
             {"야곱이네청과", "35.19926459999979", "128.07764859999975"},
             {"다복청과", "35.188349400000064", "128.1100929999998"},
             {"고려청과", "35.19641599999981", "128.07784179999956"},
             {"우주상회", "35.211185999999685", "128.11498579999935"},
             {"과일바구니","35.15212429999969", "128.11199139999962"},
             {"미미네채소", "35.195825999999805", "128.07795789999975"},
             {"대화상회", "35.19432099999995", "128.0764229999999"},
             {"버킷리스트", "35.186749900000265", "128.1128882999997"},
             {"참좋은과일나라", "35.177321599999985", "128.0831341999998"},
             {"진지한과일", "35.20932309999993", "128.10272849999933"}, //40
             {"아저씨네과일", "35.16174879999963", "128.1035637999997"},
             {"과일나라", "35.200929599999775", "128.06001830000002"},
             {"창대유통", "35.195161200000044", "128.06691969999952"},
             {"과일명당", "35.18634899999983", "128.10743480000002"},
             {"단감상회", "35.19654800000015", "128.0783227999998"},
             {"부림상회", "35.180476799999894", "128.10205029999986"},
             {"영각상회", "35.196404200000295", "128.0785716999993"},
             {"새부산상회", "35.196404200000295", "128.0785716999993"},
             {"서경프룻", "35.188789699999774", "128.11441539999976"},
             {"늘푸르네", "35.20153130000014", "128.10678779999955"}, // 진주지역 재래시장,상회 50개
             {"고성시장", "34.976830300000216", "128.3147223999995"},
             {"양산시장", "35.332795400000194", "129.04578229999967"},
             {"김해장유전통시장", "35.200548999999995", "128.8063327999997"},
             {"통영중앙전통시장", "34.8453263000001", "128.41587439999932"},
             {"삼천포종합시장", "34.93172259999979", "128.0697784999997"},
             {"창원 명서전통시장", "35.2452000000001", "128.62774159999935"},
             {"거창 전통시장", "35.68669799999993", "127.77547049999957"},
             {"대구 불로전통시장(5일장)", "35.910121500000095", "128.6335320999999"},
             {"무안재래시장", "34.982150799999985", "126.4675629999996"},
             {"의령전통시장", "35.318833100000276", "128.25272739999977"}, //60
             {"부산 구포시장", "35.20873180000006", "128.9953369999998"},
             {"김해 동상재래시장", "35.2360404999998", "128.87500739999942"},
             {"청도시장", "35.64739899999983", "128.46585290000013"},
             {"구례 5일장", "35.210876299999626", "127.4501448999997"},
             {"문경전통시장", "36.73546300000013", "128.09884179999992"},
             {"담양시장", "35.322964200000094", "126.97215389999943"},
             {"울산 전통재래시장", "35.55494000000008", "129.3151407999993"},
             {"동래시장", "35.20376730000027", "129.07754759999975"},
             {"벌교 4일장", "34.84322359999961", "127.33744579999936"},
             {"청주 육거리전통시장", "36.62820720000003", "127.48116689999979"}, //70
             {"대구 송강전통시장", "36.43517599999984", "127.37852479999938"},
             {"부산 진시장", "35.13635779999964", "129.05046659999948"},
             {"횡성전통시장", "37.49180299999969", "127.7168868999999"},
             {"임실시장", "35.61427360000001", "127.2748457999999"},
             {"이천 관고전통시장", "37.2812500000001", "127.4314957999993"},
             {"광주 말바우시장", "35.17257699999975", "126.91264580000008"},
             {"여수 서시장", "34.74123089999994", "127.72005540000008"},
             {"울산 남창옹기종기시장", "35.416428499999846", "129.27849710000007"},
             {"부산 용호시장", "35.11604899999996", "129.1088644999995"},
             {"구미 선산시장", "36.23970120000019", "128.29253319999958"}, //80, 진주이외지역 재래시장30개
             {"대구 북구 매천동 효성청과", "35.90095440000028", "128.5341749999999"},
             {"천안 서북구 신당동 천안청과", "36.86122349999974", "127.14155769999991"},
             {"대구 북구 매천동 대양청과", "35.90329539999994", "128.53377989999998"},
             {"순천 해룡면 해광로 남일청과", "34.917341699999774", "127.5297207"},
             {"서울 송파구 가락동 동화청과", "37.49399419999986", "127.10513359999993"},
             {"대전 유성구 노은동 중앙청과", "36.3649317999997", "127.31231959999997"},
             {"인천 남동구 남촌동 덕풍청과", "37.424309199999975", "126.71014630000005"},
             {"천안 서북구 신당동 유기농청과", "36.859705600000154", "127.14182489999953"},
             {"서울 동작구 신대방동 대아청과", "37.489534600000276", "126.9013207999995"},
             {"광주 서구 쌍촌동", "35.14750020000003", "126.85093739999981"}, //90
             {"김천 부곡동 형제청과", "36.125776799999926", "128.09233680000003"},
             {"서울 동대문구 제기동 청량리청과물시장", "37.57976669999969", "127.0327082999995"},
             {"구리 안창동 인터넷청과", "37.61192170000004", "127.13714829999994"},
             {"서울 송파구 가락동 서울청과", "37.492647500000004", "127.1032479999994"},
             {"부산 사상구 엄궁동 엄궁 농산물 도매시장", "35.129009600000074", "128.9559964999994"},
             {"구미 고아읍 농산물 구미 도매시장", "36.157190299999776", "128.34164579999933"},
             {"창원 의장구 팔용동 청과시장", "35.23016799999996", "128.62953379999934"},
             {"포항 북구 흥해읍 농산물도매시장", "36.0822974999997", "129.32830009999938"},
             {"부산 동구 범일동 자유도매시장", "35.14101400000025", "129.05275050000017"},
             {"서울 송파구 가락동 가락시장 가락e몰", "37.495087699999694", "127.1071749999999"}, // 100, 진주이외지역 도매시장,청과
            {"GS더프레시 진주센트럴점", "35.17467160000011", "128.13411749999923"},
            {"이마트 사천점", "34.958031100000234", "128.05335399999998"},
            {"GS더프레시 진주평거점", "35.17877260000021", "128.04906249999945"},
            {"사자마트", "35.15555249999966", "128.1125987"},
            {"하나로마트 중부농협본점", "35.24069529999981", "128.0749296999999"}, // 진주 지역 마트
            {"이마트 충주점", "36.97217630000014", "127.65555239999952"},
            {"홈플러스 안동점", "36.59167180000005", "128.38439520000006"},
            {"롯데마트 중계점", "37.64586549999984", "127.03182329999996"},
            {"코스트코코리아 양평점", "37.52764570000019", "126.85867429999963"},
            {"이마트 이천점", "37.31106769999959", "127.37817899999953"}, // 진주 외 지역 마트 //110
            {"광양원예농협 로컬푸드직매장", "34.97354069999961", "126.52155129999966"},
            {"천북농협 로컬푸드직매장", "35.988910999999845", "128.4881039999993"},
            {"가창농협로컬푸드직매장", "35.77014119999965", "128.3794599999997"},
            {"용진농협로컬푸드", "35.84294439999959", "126.92599059999975"},
            {"광주남구로컬푸드직매장", "35.111956699999844", "126.63270629999941"},
            {"천안농협로컬푸드직매장", "36.78978029999989", "126.86971359999951"},
            {"덕산농협로컬푸드직매장", "36.725997399999926", "126.50769679999962"},
            {"수원로컬푸드직매장 광교산점", "37.327148800000224", "126.94710079999935"},
            {"고촌농협 로컬푸드직매장 장곡점", "37.60574199999995", "126.71958269999982"},
            {"정읍원예농협로컬푸드직매장", "35.53293690000028", "126.7266017999996"} // 전국 로컬푸드 직매장 //120

    };


    Marker marker_curr = new Marker();
    List<Marker> marker_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( MainActivity.this, new String[] {
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 0 );
        }
        else{
            // 가장최근 위치정보 가져오기
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null) {
                String provider = location.getProvider();
                user_latitude = location.getLatitude();
                user_longitude = location.getLongitude();
                double altitude = location.getAltitude();

                Log.d("Main", "[LastKnown] 위치정보 : " + provider + "\n" +
                        "위도 : " + user_latitude + "\n" +
                        "경도 : " + user_longitude + "\n" +
                        "고도  : " + altitude);
            } else {
                Log.d("Main","location NULL");
            }

            // 위치정보를 원하는 시간, 거리마다 갱신해준다.
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
        }
    }

    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            String provider = location.getProvider();  // 위치정보
            user_latitude = location.getLatitude(); // 위도
            user_longitude = location.getLongitude(); // 경도
            double altitude = location.getAltitude(); // 고도
            Log.d("Main","[Changed] 위치정보 : " + provider + "\n" + "위도 : " + user_latitude + "\n" + "경도 : " + user_longitude + "\n" + "고도 : " + altitude);

            System.out.println("[changed] 마커 위도 : " + String.valueOf(user_latitude) + "\n 마커 경도: " + String.valueOf(user_longitude));

            // 기존 사용자 현재 위치 마커 삭제
            marker_curr.setMap(null);
            // 사용자 현재 위치 마커 표시
            marker_curr.setPosition(new LatLng(user_latitude, user_longitude));
            marker_curr.setMap(naverMap);
            // 사용자 현재 위치 정보창 표시
            InfoWindow infoWindow_curr = new InfoWindow();
            infoWindow_curr.setAdapter(new InfoWindow.DefaultTextAdapter(getApplicationContext()) {
                @NonNull
                @Override
                public CharSequence getText(@NonNull InfoWindow infoWindow) {
                    return "현재 위치";
                }
            });
            infoWindow_curr.open(marker_curr);

            // 기존 판매처 마커 삭제
            for(int i = 0; i < marker_list.size(); i++){
                marker_list.get(i).setMap(null);
            }
            for(int i = 0; i < info.length; i++) {

                // 사용자 위치와 판매처 사이 거리 (미터 단위)
                double distance = distance(user_latitude, user_longitude, Double.parseDouble(info[i][1]), Double.parseDouble(info[i][2]));

                if(distance < r) {

                    // 마커 표시
                    Marker marker = new Marker();
                    marker.setPosition(new LatLng(Double.parseDouble(info[i][1]), Double.parseDouble(info[i][2])));
                    marker.setMap(naverMap);
                    // 정보창 표시
                    InfoWindow infoWindow = new InfoWindow();
                    int finalI = i;
                    infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(getApplicationContext()) {
                        @NonNull
                        @Override
                        public CharSequence getText(@NonNull InfoWindow infoWindow) {
                            return info[finalI][0];
                        }
                    });
                    infoWindow.open(marker);
                    marker_list.add(marker);
                }
            }

            if(user_latitude != 0.0 && user_longitude != 0.0 && naverMap != null) {

                CameraUpdate cameraUpdate = CameraUpdate.scrollAndZoomTo(
                        new LatLng(user_latitude, user_longitude), 12.3)
                        .animate(CameraAnimation.Fly, 3000);

                naverMap.moveCamera(cameraUpdate);
            }

        } public void onStatusChanged(String provider, int status, Bundle extras) {

        } public void onProviderEnabled(String provider) {

        } public void onProviderDisabled(String provider) {

        }
    };

    private static double distance(double lat1, double lon1, double lat2, double lon2) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        dist = dist * 1609.344;

        return (dist);
    }

    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }


    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);
        ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);

        System.out.println("마커 위도 : " + String.valueOf(user_latitude) + "\n 마커 경도: " + String.valueOf(user_longitude));

        // 기존 사용자 현재 위치 마커 삭제
        marker_curr.setMap(null);
        // 사용자 현재 위치 마커 표시
        marker_curr.setPosition(new LatLng(user_latitude, user_longitude));
        marker_curr.setMap(naverMap);
        // 사용자 현재 위치 정보창 표시
        InfoWindow infoWindow_curr = new InfoWindow();
        infoWindow_curr.setAdapter(new InfoWindow.DefaultTextAdapter(getApplicationContext()) {
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                return "현재 위치";
            }
        });
        infoWindow_curr.open(marker_curr);

        // 기존 판매처 마커 삭제
        for(int i = 0; i < marker_list.size(); i++){
            marker_list.get(i).setMap(null);
        }
        for(int i = 0; i < info.length; i++) {

            // 사용자 위치와 판매처 사이 거리 (미터 단위)
            double distance = distance(user_latitude, user_longitude, Double.parseDouble(info[i][1]), Double.parseDouble(info[i][2]));

            if(distance < r) {

                // 마커 표시
                Marker marker = new Marker();
                marker.setPosition(new LatLng(Double.parseDouble(info[i][1]), Double.parseDouble(info[i][2])));
                marker.setMap(naverMap);
                // 정보창 표시
                InfoWindow infoWindow = new InfoWindow();
                int finalI = i;
                infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(getApplicationContext()) {
                    @NonNull
                    @Override
                    public CharSequence getText(@NonNull InfoWindow infoWindow) {
                        return info[finalI][0];
                    }
                });
                infoWindow.open(marker);
                marker_list.add(marker);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
                if (!locationSource.isActivated()) {
                    naverMap.setLocationTrackingMode(LocationTrackingMode.None);
                    return;
                } else {
                    naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
                }
            }
            super.onRequestPermissionsResult(
                    requestCode, permissions, grantResults);
        }

    }
    public void setActionBarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

}