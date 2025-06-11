package com.smokezone

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.smokezone.data.Comment
import com.smokezone.data.SmokeArea
import com.smokezone.databinding.ActivityMapBinding
import com.smokezone.firebase.firebasedb
import com.smokezone.ui.community.CommentsAdapter

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private var currentLocationMarker: Marker? = null
    private lateinit var binding: ActivityMapBinding
    private lateinit var locationCallback: LocationCallback

    lateinit var naverMap: NaverMap
    var smokeAreaList: ArrayList<SmokeArea> = arrayListOf()

    val auth = FirebaseAuth.getInstance()

    val areaRef = firebasedb.getReference("/area")

    var isFirstLocationUpdate = true
    var lastClickedMarker: Marker? = null
    var isEditMode = false

    private lateinit var locationsSource: FusedLocationSource
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    currentLocationMarker?.map = null
                    currentLocationMarker = Marker().apply {
                        position = currentLatLng
                        map = naverMap
                    }
                    if (isFirstLocationUpdate) {
                        naverMap.moveCamera(CameraUpdate.scrollTo(currentLatLng))
                        isFirstLocationUpdate = false
                    }
                }
            }
        }

        // 전체 영역 맵으로 바꾸기
        val window = window
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        binding.mapView.getMapAsync(this)

        areaRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val newSmokeAreaList = ArrayList<SmokeArea>()

                Log.e(">>>>>>", snapshot.toString())

                for (snap in snapshot.children) {
                    val data = snap.getValue(SmokeArea::class.java)
                    if (data != null) {
                        data.id = snap.key.toString()
                    };
                    if (data != null) {
                        newSmokeAreaList.add(data)
                    }
                }

                smokeAreaList = newSmokeAreaList

                mapDataInitialize()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(">>>>>>", "$error")

            }

        })

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1000
            )
        }

        binding.navigateButton.setOnClickListener {
            if (lastClickedMarker == null) {
                Toast.makeText(this, "길찾기 할 마커를 선택해주세요.", Toast.LENGTH_SHORT).show()
            }

            lastClickedMarker?.let { marker ->
                runCatching {
                    // 네이버 지도 앱으로 길찾기
                    val url = "nmap://route/walk?dlat=${marker.position.latitude}&dlng=${marker.position.longitude}&dname=마커"

                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(url)
                    )
                    intent.addCategory(Intent.CATEGORY_BROWSABLE)
                    intent.setPackage("com.nhn.android.nmap")
                    startActivity(intent)
                }.onFailure {
                    Toast.makeText(this, "네이버 맵 지도가 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.editModeButton.setOnClickListener {
            isEditMode = !isEditMode
        }

        binding.findNearBtn.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this)
            val bottomSheetView = layoutInflater.inflate(R.layout.activity_find_near, null)

            val adapter = SmokeAreaAdapter()
            val areaRecyclerView = bottomSheetView.findViewById<RecyclerView>(R.id.areaRecyclerView)

            areaRecyclerView.adapter = adapter

            bottomSheetDialog.setContentView(bottomSheetView)

            val latLngBounds = naverMap.contentBounds

            val smokeAreaList = smokeAreaList.filter {
                latLngBounds.contains(LatLng(it.latitude, it.longitude))
            }

            // 현재위치를 가져와서 거리 계산하여 객체 mapping
            fusedLocationClient.lastLocation.addOnSuccessListener {
                val currentLocation = it
                adapter.setSmokeAreas(smokeAreaList.map {
                    it.copy(
                        currentLocation = Location(
                            latitude = currentLocation.latitude,
                            longitude = currentLocation.longitude
                        ),
                        targetLocation = Location(
                            latitude = it.latitude,
                            longitude = it.longitude
                        )
                    )
                })
                bottomSheetDialog.show()
            }

        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        mapDataInitialize()

        naverMap.setOnMapClickListener { _, coord ->
            if (isEditMode) {
                // 마커 추가

                val inflater = layoutInflater
                val dialogView = inflater.inflate(R.layout.dialog_area, null)

                // 레이아웃에서 EditText 찾기
                val editTextLocationName =
                    dialogView.findViewById<EditText>(R.id.editTextLocationName)

                // AlertDialog 생성
                val dialog = AlertDialog.Builder(this)
                    .setTitle("지역 이름을 입력해주세요")
                    .setView(dialogView)
                    .setNegativeButton("취소", null)
                    .setPositiveButton("확인") { dialog, which ->

                        // 여기서 editTextLocationName.text.toString()으로 입력 값을 가져와서 처리
                        val locationName = editTextLocationName.text.toString()

                        val marker = Marker()

                        marker.position = coord
                        marker.map = naverMap

                        val smokeAreaId = areaRef.push().key

                        val smokeArea = SmokeArea(
                            id = smokeAreaId,
                            latitude = coord.latitude,
                            longitude = coord.longitude,
                            title = locationName, // 지역 이름 입력
                            userId = "사용자 ID", // 사용자 ID 입력
                            comments = mapOf() // 코멘트 내용 입력
                        )

                        smokeAreaList.add(smokeArea)


                        smokeAreaId?.let {
                            areaRef.child(it).setValue(smokeArea.copy(id = it))
                        }

//                        marker.setOnClickListener { _ ->
//                            lastClickedMarker = marker
//                            openInfoDrawer(smokeArea)
//                            true
//                        }
                        mapDataInitialize()

                    }.create()

                dialog.show()
            }
        }


        // 현 위치 설정
        binding.currentLocationButton.map = naverMap

        // 현위치 적용
        locationsSource = FusedLocationSource(this@MapActivity, LOCATION_PERMISSION_REQUEST_CODE)
        naverMap.locationSource = locationsSource
    }

    fun openInfoDrawer(smokeArea: SmokeArea) {
        Log.e(">>>", "openInfoDrawer: ${smokeArea}")

        val databaseReference =
            firebasedb.getReference("/area/${smokeArea.id}/comments")

        val bottomSheetDialog = BottomSheetDialog(this)

        val view = layoutInflater.inflate(R.layout.info_drawer, null)
        bottomSheetDialog.setContentView(view)

        val title = view.findViewById<TextView>(R.id.title)
        val commentEdit = view.findViewById<EditText>(R.id.commentEdit)
        val commentList = view.findViewById<RecyclerView>(R.id.commentList)
        val sendBtn = view.findViewById<ImageButton>(R.id.sendBtn)

        commentList.layoutManager = LinearLayoutManager(this)

        title.text = smokeArea.title

        // 전송 버튼 누를 시 해당 마커 아래에 코멘트 추가
        sendBtn.setOnClickListener {
            val comment = Comment(
                authorName = auth.currentUser?.displayName.toString(),
                content = commentEdit.text.toString()
            )

            val commentKey = areaRef.child(smokeArea.id.orEmpty())
                .child("comments")
                .push()
                .key

            areaRef.child(smokeArea.id.orEmpty())
                .child("comments")
                .child(commentKey.orEmpty())
                .setValue(comment)

            commentEdit.text.clear()
        }

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val comments = ArrayList<Comment>()
                    for (snapshot in dataSnapshot.children) {
                        val data = snapshot.getValue(Comment::class.java)
                        if (data != null) {
                            comments.add(data)
                        }
                    }
                    val adapter = CommentsAdapter(comments)
                    commentList.adapter = adapter
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
        bottomSheetDialog.show()
    }

    fun mapDataInitialize() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        for (smokeArea in smokeAreaList) {

            val marker = Marker()
            marker.position = LatLng(
                smokeArea.latitude,
                smokeArea.longitude
            )

            Log.d(">>>", "initMarkers: " + marker.position)
            marker.map = naverMap
            marker.setOnClickListener { _ ->
                lastClickedMarker = marker
                openInfoDrawer(smokeArea)
                true
            }
        }
    }

    // 권한 요청 코드
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) return

        if (locationsSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationsSource.isActivated) {
                // 현위치를 표시하지만 지도가 유저를 따라가진 않게 함
                naverMap.locationTrackingMode = LocationTrackingMode.NoFollow
            }
            return // 권한이 없다면 거절
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}

data class Location(val latitude: Double, val longitude: Double)