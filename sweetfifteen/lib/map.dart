import 'package:flutter/material.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';

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

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: GoogleMap(
          onMapCreated: _onMapCreated,
          initialCameraPosition: _initialCameraPosition,
          markers: <Marker>{
            const Marker(
                markerId: MarkerId('first'),
                position: LatLng(48.716385, 21.261074))
          },
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
