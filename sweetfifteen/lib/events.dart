import 'package:flutter/material.dart';

import 'event.dart';

class EventsPage extends StatefulWidget {
  const EventsPage({super.key});

  @override
  State<StatefulWidget> createState() {
    return _EventsPageState();
  }
}

class _EventsPageState extends State<EventsPage> {
  final events = [
    const Event(eventName: "name of event1", eventPlace: "place1"),
    const Event(eventName: "name of event2", eventPlace: "place2")
  ];

  @override
  Widget build(BuildContext context) {
    return ListView(
        children: events
      );
  }
}
