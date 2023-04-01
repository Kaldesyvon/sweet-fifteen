import 'package:flutter/material.dart';
import 'package:sweetfifteen/events.dart';

void main() {
  runApp(const MainPage());
}

class MainPage extends StatefulWidget {
  const MainPage({super.key});

  @override
  State<StatefulWidget> createState() {
    return _MainPageState();
  }
}

class _MainPageState extends State<MainPage> {
  int index = 0;
  final pages = [
    const EventsPage(),
    const Center(child: Text('Map', style: TextStyle(fontSize: 15))),
    const Center(child: Text('Profile', style: TextStyle(fontSize: 15)))
  ];

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        home: Scaffold(
          appBar: AppBar(
            elevation: 0.1,
            backgroundColor: const Color.fromRGBO(0, 148, 128, 1.0),
            title: const Center(child: Text('Sweet 15')),
          ),
        body: pages[index],
        bottomNavigationBar: BottomNavigationBar(
          currentIndex: index,
          type: BottomNavigationBarType.fixed,

          unselectedLabelStyle: const TextStyle(color: Colors.black),
          unselectedItemColor: Colors.white,
          selectedLabelStyle: const TextStyle(color: Colors.black, fontWeight: FontWeight.bold),
          selectedItemColor: Colors.white,

          onTap: (index) {
            setState(() => this.index = index);
          },
          backgroundColor: const Color.fromRGBO(0, 148, 128, 1.0),

          items: const [
            BottomNavigationBarItem(icon: Icon(Icons.event, color: Colors.white), label: 'Events'),
            BottomNavigationBarItem
              (icon: Icon(Icons.map, color: Colors.white), label: 'Map'),
            BottomNavigationBarItem(icon: Icon(Icons.person, color: Colors.white), label: 'Profile')
          ],
        ),
      )
    );
  }
}
