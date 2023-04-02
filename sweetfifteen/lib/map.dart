import 'package:flutter/material.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:sweetfifteen/event/event-detail-dialog.dart';
import 'package:sweetfifteen/event/eventData.dart';

class MapPage extends StatefulWidget {
  final LatLng initialLatLng;

  const MapPage({
    Key? key,
    this.initialLatLng = const LatLng(48.716385, 21.261074),
  }) : super(key: key);

  @override
  State<StatefulWidget> createState() {
    return _MapPageState();
  }
}

class _MapPageState extends State<MapPage> {
  int index = 1;
  late GoogleMapController mapController;

  static const _initialLatLng = LatLng(48.716385, 21.261074);
  // static const _initialLatLng = widget.initialLatLng;

  final _initialCameraPosition = const CameraPosition(
    target: _initialLatLng,
    zoom: 15.0,
  );

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
