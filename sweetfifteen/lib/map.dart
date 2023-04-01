import 'package:flutter/material.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';

class MapPage extends StatefulWidget {
  const MapPage({super.key});

  @override
  State<StatefulWidget> createState() {
    return _MapPageState();
  }
}

class _MapPageState extends State<MapPage> {
  int index = 1;

  late GoogleMapController mapController;

  final pages = [
    const Center(child: Text('Map', style: TextStyle(fontSize: 15))),
    const Center(child: Text('Events', style: TextStyle(fontSize: 15))),
    const Center(child: Text('Profile', style: TextStyle(fontSize: 15)))
  ];

  final CameraPosition _initialCameraPosition = const CameraPosition(
    target: LatLng(48.716385, 21.261074),
    zoom: 11.0,
  );

  void _onMapCreated(GoogleMapController controller) {
    setState(() {
      mapController = controller;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Map')),
      body: GoogleMap(
        onMapCreated: _onMapCreated,
        initialCameraPosition: _initialCameraPosition,
        // onCameraMove: (CameraPosition position) {
        //   setState(() {
        //     _initialCameraPosition = position;
        //   });
        // },
        markers: <Marker>{
          const Marker(
              markerId: MarkerId('first'),
              position: LatLng(48.716385, 21.261074))
        },
      ),
    );
  }
}
