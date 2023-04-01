import 'package:flutter/material.dart';

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
    const Center(child: Text('Map', style: TextStyle(fontSize: 15))),
    const Center(child: Text('Events', style: TextStyle(fontSize: 15))),
    const Center(child: Text('Profile', style: TextStyle(fontSize: 15)))
  ];

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        home: Scaffold(
        body: pages[index],
        bottomNavigationBar: NavigationBar(
          height: 60,
          selectedIndex: index,
          onDestinationSelected: (index) {
            setState(() => this.index = index);
          },
          destinations: const [
            NavigationDestination
              (icon: Icon(Icons.map), label: 'Map'),
            NavigationDestination(icon: Icon(Icons.event), label: 'Events'),
            NavigationDestination(icon: Icon(Icons.person), label: 'Profile')
          ],
        ),
      )
    );
  }
}
