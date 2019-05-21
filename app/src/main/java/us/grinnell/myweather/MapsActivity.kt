package us.grinnell.myweather

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ScaleDrawable
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_maps.*
import us.grinnell.myweather.R

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, MyLocationProvider.OnNewLocationAvailable {
    override fun onNewLocation(location: Location) {
        tvNumber.text =
            "Loc: ${location.latitude}, ${location.longitude}"
    }


    private lateinit var mMap: GoogleMap
    internal lateinit var MarkerPoints: ArrayList<LatLng>
    internal var RouteNumber: Int = 0
    var status: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)



        btnDetail.setOnClickListener {
            if (RouteNumber == 0){
                Toast.makeText(
                    this@MapsActivity, "Please select a route",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else if(RouteNumber == 1){
                var Detail = Intent()
                Detail.setClass(this@MapsActivity, MainActivity::class.java)
                Detail.putExtra("RouteNumber", RouteNumber)
                startActivity(Detail)
                //finish()
            }
            else if(RouteNumber == 2){
                var Detail = Intent()
                Detail.setClass(this@MapsActivity, MainActivity::class.java)
                Detail.putExtra("RouteNumber", RouteNumber)
                startActivity(Detail)
                //finish()
            }

        }

        btnStart.setOnClickListener {
            if (RouteNumber == 0){
                Toast.makeText(
                    this@MapsActivity, "Please select a route",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else if(RouteNumber == 1){
                var RouteDetail = Intent()
                RouteDetail.setClass(this@MapsActivity, RouteActivity::class.java)
                RouteDetail.putExtra("RouteNumber", RouteNumber)
                startActivity(RouteDetail)
                //finish()
            }
            else if(RouteNumber == 2){
                var RouteDetail = Intent()
                RouteDetail.setClass(this@MapsActivity, RouteActivity::class.java)
                RouteDetail.putExtra("RouteNumber", RouteNumber)
                startActivity(RouteDetail)
                //finish()
            }

        }

        MarkerPoints = ArrayList<LatLng>()
    }


    private lateinit var myLocationProvider: MyLocationProvider

    override fun onStart() {
        super.onStart()
        startLocation()
    }
//

//    @WithPermissions(
//        permissions = [android.Manifest.permission.ACCESS_FINE_LOCATION]
//    )


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
        addMarkers()


        //Route 2
        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(47.499646, 19.065874))
            .zoom(15f)
            .bearing(90f)
            .tilt(30f)
            .build()

        mMap.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                cameraPosition
            )
        )


//        mMap.setOnMapClickListener {
//            val markerOpt = MarkerOptions().position(it).title("My marker ${it.latitude}, ${it.longitude}")
//            val marker = mMap.addMarker(markerOpt)
//            marker.isDraggable = true
//
//
//            mMap.animateCamera(CameraUpdateFactory.newLatLng(it))
//        }


        mMap.setOnMarkerClickListener {



            if(it.id.toString() == "m1" || it.id.toString() == "m2"|| it.id.toString() == "m3"|| it.id.toString() == "m4" ){
                btnStart.visibility = View.VISIBLE
                btnDetail.visibility = View.VISIBLE
                tvRouteName.text = "Route 1: Ruin Pups"
                tvDistance.text = "1.4 km"
                tvNumber.text = "35 Attendees"
                RouteNumber = 1

            } else if(it.id.toString() == "m0"){
                RouteNumber = 0
            }
            else{
                btnStart.visibility = View.VISIBLE
                btnDetail.visibility = View.VISIBLE
                tvRouteName.text = "Route 2: Pups & Club"
                tvDistance.text = "1.3 km"
                tvNumber.text = "43 Attendees"
                RouteNumber = 2
            }
            Toast.makeText(
                this@MapsActivity, it.id,
                Toast.LENGTH_LONG
            ).show()

            true

        }

        val polyLine1: PolylineOptions = PolylineOptions().add(
            LatLng(47.495666, 19.062605),
            LatLng(47.497214, 19.063352),
            LatLng(47.498393, 19.066505),
            LatLng(47.497993, 19.069188)

        ).color(Color.BLUE).clickable(true)


        val polyline1: Polyline = mMap.addPolyline(polyLine1)

        val polyLine2: PolylineOptions = PolylineOptions().add(
            LatLng(47.499953, 19.062848),
            LatLng(47.500646, 19.065874),
            LatLng(47.501748, 19.065874),
            LatLng(47.502753, 19.066267),
            LatLng(47.499842, 19.069647)

        ).color(Color.BLUE).clickable(true)


        val polyline2: Polyline = mMap.addPolyline(polyLine2)





        mMap.setOnPolylineClickListener {
            Toast.makeText(
                this@MapsActivity, it.id,
                Toast.LENGTH_LONG
            ).show()

            btnStart.visibility = View.VISIBLE
            btnDetail.visibility = View.VISIBLE

            if (it.id.toString() == "pl0"){
                tvRouteName.text = "Route 1: Ruin Pups"
                tvDistance.text = "1.4 km"
                tvNumber.text = "35 Attendees"
                RouteNumber = 1
            }else if (it.id.toString() == "pl1"){
                tvRouteName.text = "Route 2: Pups & Club"
                tvDistance.text = "1.3 km"
                tvNumber.text = "43 Attendees"
                RouteNumber = 2
            }

        }





    }

    private fun addMarkers() {
        val originalBitmap:Bitmap = BitmapFactory.decodeResource(resources, R.drawable.octopus)
        val resizedOctupus:Bitmap = Bitmap.createScaledBitmap(originalBitmap,100, 100, false)

        mMap.addMarker(
            MarkerOptions().position(LatLng(47.499533, 19.058996))
                .title("User")
                .icon(BitmapDescriptorFactory.fromBitmap(resizedOctupus))
        )


        mMap.addMarker(
            MarkerOptions()
                .position((LatLng(47.495666, 19.062605)))
                .title("Champs Sports Pub")

        )


        mMap.addMarker(
            MarkerOptions()
                .position((LatLng(47.497214, 19.063352)))
                .title("Szimpla")
        )


        mMap.addMarker(
            MarkerOptions()
                .position((LatLng(47.498393, 19.066505)))
                .title("Fuge Udvar")


        )

        mMap.addMarker(
            MarkerOptions()
                .position((LatLng(47.497993, 19.069188)))
                .title("Hetker Pub")

        )
        // Route 1


        // Route 2

        mMap.addMarker(
            MarkerOptions()
                .position((LatLng(47.500646, 19.065874)))
                .title("Instant")

        )
        mMap.addMarker(
            MarkerOptions()
                .position((LatLng(47.501748, 19.065874)))
                .title("Kandalló K. Pub")

        )
        mMap.addMarker(
            MarkerOptions()
                .position((LatLng(47.502753, 19.066267)))
                .title("Blokk Bar")

        )
        mMap.addMarker(
            MarkerOptions()
                .position((LatLng(47.499842, 19.069647)))
                .title("Martt Bar")

        )

        mMap.addMarker(
            MarkerOptions()
                .position((LatLng(47.499953, 19.062848)))
                .title("DOBLO Wine Bar")

        )


    }
}
