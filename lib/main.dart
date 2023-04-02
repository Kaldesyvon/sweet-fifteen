import 'package:flutter/material.dart';
import 'package:sweetfifteen/event/events.dart';
import 'package:sweetfifteen/map.dart';
import 'package:sweetfifteen/profile/profile.dart';

void main() {
  runApp(MainPage(index: 0, lat: 48.716385, long: 21.261074));
}

class MainPage extends StatefulWidget {
  int index = 0;
  double lat = 0;
  double long = 0;
  MainPage(
      {super.key, required this.index, required this.lat, required this.long});

  @override
  State<StatefulWidget> createState() {
    return _MainPageState();
  }
}

class _MainPageState extends State<MainPage> {
  @override
  Widget build(BuildContext context) {
    final pages = [
      const EventsPage(),
      MapPage(lat: widget.lat, long: widget.long),
      const ProfilePage()
    ];

    return MaterialApp(
        home: Scaffold(
          appBar: AppBar(
            elevation: 0.1,
            backgroundColor: const Color.fromRGBO(0, 148, 128, 1.0),
            title: const Center(child: Text('Sweet 15')),
          ),
          body: pages[widget.index],
          bottomNavigationBar: BottomNavigationBar(
            currentIndex: widget.index,
            type: BottomNavigationBarType.fixed,
            unselectedLabelStyle: const TextStyle(color: Colors.black),
            unselectedItemColor: Colors.white,
            selectedLabelStyle:
                const TextStyle(color: Colors.black, fontWeight: FontWeight.bold),
            selectedItemColor: Colors.white,
            onTap: (index) {
              setState(() => widget.index = index);
            },
            backgroundColor: const Color.fromRGBO(0, 148, 128, 1.0),
            items: const [
              BottomNavigationBarItem(
                  icon: Icon(Icons.event, color: Colors.white), label: 'Events'),
              BottomNavigationBarItem(
                  icon: Icon(Icons.map, color: Colors.white), label: 'Map'),
              BottomNavigationBarItem(
                  icon: Icon(Icons.person, color: Colors.white), label: 'Profile')
            ],
          ),
        ));
  }
}
