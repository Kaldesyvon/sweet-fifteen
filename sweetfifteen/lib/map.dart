import 'package:flutter/material.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:sweetfifteen/event/event.dart';

// import 'event/event.dart';

class MapPage extends StatefulWidget {
  // const MapPage({super.key});

  const MapPage({Key? key, events}) : super(key: key);

  @override
  State<StatefulWidget> createState() {
    return _MapPageState();
  }
}

class _MapPageState extends State<MapPage> {
  int index = 1;
  late GoogleMapController mapController;

  final _initialCameraPosition = const CameraPosition(
    target: LatLng(48.716385, 21.261074),
    zoom: 15.0,
  );

  void _onMapCreated(GoogleMapController controller) {
    mapController = controller;
  }

  var events = [
    {'name': 'lol1', 'latitude': 48.73535, 'longitude': 21.3},
    {'name': 'lol2', 'latitude': 48.72535, 'longitude': 21.35},
    {'name': 'lol3', 'latitude': 48.71535, 'longitude': 21.21},
  ];

  Set<Marker> _createMarkers() {
    Set<Marker> markers = {};
    for (var event in events) {
      markers.add(
        Marker(
          markerId: MarkerId(event['name']! as String),
          position: LatLng(
              event['latitude']! as double, event['longitude']! as double),
          infoWindow: InfoWindow(title: event['name']! as String),
        ),
      );
    }
    return markers;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: GoogleMap(
          onMapCreated: _onMapCreated,
          initialCameraPosition: _initialCameraPosition,
          markers: _createMarkers(),
          circles: {
            Circle(
                circleId: const CircleId('radius'),
                center: _initialCameraPosition.target,
                radius: 600,
                strokeWidth: 3,
                strokeColor: Colors.blue,
                fillColor: const Color.fromARGB(130, 33, 150, 243))
          }),
    );
  }
}
