import 'package:flutter/material.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:sweetfifteen/event/event-detail-dialog.dart';
import 'package:sweetfifteen/event/eventData.dart';

class MapPage extends StatefulWidget {
  final double lat;
  final double long;

  final LatLng initialLatLng;

  MapPage({
    super.key,
    required this.lat,
    required this.long,
    // this.initialLatLng = const LatLng(0,0),
  }) : initialLatLng = LatLng(lat, long);

  @override
  State<StatefulWidget> createState() {
    return _MapPageState();
  }
}

class _MapPageState extends State<MapPage> {
  int index = 1;
  late GoogleMapController mapController;

  // static const _initialLatLng = LatLng(48.716385, 21.261074);

  void _onMapCreated(GoogleMapController controller) {
    mapController = controller;
  }

  Set<Marker> _createMarkers() {
    Set<Marker> markers = {};
    for (var event in EventData.events) {
      markers.add(
        Marker(
          markerId: MarkerId(event.eventName),
          position: LatLng(event.lat, event.long),
          onTap: () {
            showDialog(
              context: context,
              builder: (BuildContext context) {
                return EventDetailDialog(
                  event: event,
                  openedFromMap: true,
                );
              },
            );
          },
        ),
      );
    }

    return markers;
  }

  @override
  Widget build(BuildContext context) {
    CameraPosition initialCameraPosition = CameraPosition(
      target: widget.initialLatLng,
      zoom: 15.0,
    );

    return Scaffold(
      body: GoogleMap(
          onMapCreated: _onMapCreated,
          initialCameraPosition: initialCameraPosition,
          markers: _createMarkers(),
          circles: {
            const Circle(
                circleId: CircleId('radius'),
                center: LatLng(48.716385, 21.261074),
                radius: 600,
                strokeWidth: 3,
                strokeColor: Colors.blue,
                fillColor: Color.fromARGB(130, 33, 150, 243))
          }),
    );
  }
}
