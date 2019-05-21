package us.grinnell.myweather

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.route_maps.*
import java.lang.reflect.Array.getInt
import kotlin.math.round


class RouteActivity : AppCompatActivity(), OnMapReadyCallback, MyLocationProvider.OnNewLocationAvailable {
    override fun onNewLocation(location: Location) {
//        tvNumber.text =
//            "Loc: ${location.latitude}, ${location.longitude}"
    }

    private lateinit var mMap: GoogleMap
    internal lateinit var MarkerPoints: ArrayList<LatLng>
    internal var RouteNumber: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.route_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        RouteNumber = intent.getIntExtra("RouteNumber", 0)
        Log.d("debug",RouteNumber.toString())

        MarkerPoints = ArrayList<LatLng>()
    }


    private lateinit var myLocationProvider: MyLocationProvider

    override fun onStart() {
        super.onStart()
        startLocation()
    }

    fun startLocation() {
        myLocationProvider = MyLocationProvider(
            this,
            this
        )
        myLocationProvider.startLocationMonitoring()
    }


    override fun onStop() {
        super.onStop()
//        myLocationProvider.stopLocationMonitoring()
    }

//    override fun onNewLocation(location: Location) {
//        tvNumber.text =
//            "Loc: ${location.latitude}, ${location.longitude}"
//    }


    @SuppressLint("WrongConstant")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

//        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

//        val marker = LatLng(47.0, 19.0)
//        mMap.addMarker(MarkerOptions().position(marker).title("Marker in Hungary"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker))

        mMap.isTrafficEnabled = true
//        mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE


        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true


        // Route 1
        addMarkers(RouteNumber)


        //Route 2
//        val cameraPosition = CameraPosition.Builder()
//            .target(LatLng(47.499646, 19.065874))
//            .zoom(15f)
//            .bearing(90f)
//            .tilt(30f)
//            .build()
        if (RouteNumber == 1){
            val cameraPosition = CameraPosition.Builder()
                .target(LatLng(47.498393, 19.066505))
                .zoom(15f)
                .bearing(90f)
                .tilt(30f)
                .build()

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                cameraPosition
            ))
        }else{
            val cameraPosition = CameraPosition.Builder()
                .target(LatLng(47.501748, 19.065874))
                .zoom(15f)
                .bearing(90f)
                .tilt(30f)
                .build()

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                cameraPosition
            ))
        }





//        mMap.setOnMapClickListener {
//            val markerOpt = MarkerOptions().position(it).title("My marker ${it.latitude}, ${it.longitude}")
//            val marker = mMap.addMarker(markerOpt)
//            marker.isDraggable = true
//
//
//            mMap.animateCamera(CameraUpdateFactory.newLatLng(it))



        mMap.setOnMarkerClickListener {
            val destLat = it.position.latitude
            val destLon = it.position.longitude
            val originLat = 47.499533
            val originLon = 19.058996
            val distance = round(calculteDistance(originLat,destLat,originLon, destLon))/1000
            val distanceKm= round(distance * 10)/10


            //val distanceString = String.toString().format("%.2f", distance.toString())
            Toast.makeText(
                this@RouteActivity, distance.toString(),
                Toast.LENGTH_SHORT
            ).show()


                tvStopName.text = "Stop " + it.title
                tvStopDistance.text = distanceKm.toString() + " km"
            true


            }





        val polyLine1: PolylineOptions = PolylineOptions().add(
            LatLng(47.495666, 19.062605),
            LatLng(47.497214, 19.063352),
            LatLng(47.498393, 19.066505),
            LatLng(47.497993, 19.069188)

        ).color(Color.BLUE).clickable(true)

        val polyLine2: PolylineOptions = PolylineOptions().add(
            LatLng(47.499953, 19.062848),
            LatLng(47.500646, 19.065874),
            LatLng(47.501748, 19.065874),
            LatLng(47.502753, 19.066267),
            LatLng(47.499842, 19.069647)

        ).color(Color.BLUE).clickable(true)



        if (RouteNumber == 1){
            val polyline1: Polyline = mMap.addPolyline(polyLine1)
        }else{
            val polyline2: Polyline = mMap.addPolyline(polyLine2)
        }




        mMap.setOnPolylineClickListener {
//            Toast.makeText(
//                this@RouteActivity, it.id,
//                Toast.LENGTH_LONG
//            ).show()
//
//            btnStart.visibility = View.VISIBLE
//            btnDetail.visibility = View.VISIBLE
//
//            if (it.id.toString() == "pl0"){
//                tvRouteName.text = "Route 1: Ruin Pups"
//                tvDistance.text = "1.4 km"
//                tvNumber.text = "35 Attendees"
//                RouteNumber = 1
//            }else if (it.id.toString() == "pl1"){
//                tvRouteName.text = "Route 2: Pups & Club"
//                tvDistance.text = "1.3 km"
//                tvNumber.text = "43 Attendees"
//                RouteNumber = 2
//            }

        }

    }




    private fun addMarkers(RouteNumber:Int) {

        val originalBitmap:Bitmap = BitmapFactory.decodeResource(resources, R.drawable.octopus)
        val resizedOctupus:Bitmap = Bitmap.createScaledBitmap(originalBitmap,100, 100, false)

        mMap.addMarker(
            MarkerOptions().position(LatLng(47.499533, 19.058996))
                .title("User")
                .icon(BitmapDescriptorFactory.fromBitmap(resizedOctupus))

        )

        if (RouteNumber == 1) {

            mMap.addMarker(
                MarkerOptions()
                    .position((LatLng(47.495666, 19.062605)))
                    .title("1: Champs Sports Pub")

            )


            mMap.addMarker(
                MarkerOptions()
                    .position((LatLng(47.497214, 19.063352)))
                    .title("2: Szimpla")
            )


            mMap.addMarker(
                MarkerOptions()
                    .position((LatLng(47.498393, 19.066505)))
                    .title("3: Fuge Udvar")


            )

            mMap.addMarker(
                MarkerOptions()
                    .position((LatLng(47.497993, 19.069188)))
                    .title("4: Hetker Pub")

            )
        }
        // Route 1


        // Route 2
        else {
            mMap.addMarker(
                MarkerOptions()
                    .position((LatLng(47.500646, 19.065874)))
                    .title("1: Instant")

            )
            mMap.addMarker(
                MarkerOptions()
                    .position((LatLng(47.501748, 19.065874)))
                    .title("2: Kandalló K. Pub")

            )
            mMap.addMarker(
                MarkerOptions()
                    .position((LatLng(47.502753, 19.066267)))
                    .title("3: Blokk Bar")

            )
            mMap.addMarker(
                MarkerOptions()
                    .position((LatLng(47.499842, 19.069647)))
                    .title("4: Martt Bar")

            )

            mMap.addMarker(
                MarkerOptions()
                    .position((LatLng(47.499953, 19.062848)))
                    .title("5: DOBLO Wine Bar")

            )

        }



    }

    private fun calculteDistance(lat1: Double,lat2: Double, lon1:Double,lon2:Double) : Double{
        val R = 6371 // Radius of the earth

        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)
        val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + (Math.cos(Math.toRadians(lat1)) * Math.cos(
            Math.toRadians(lat2)
        )
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2))
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        var distance = R.toDouble() * c * 1000.0 // convert to meters


        return distance

    }


}
