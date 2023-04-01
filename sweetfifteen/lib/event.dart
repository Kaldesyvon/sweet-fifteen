import 'package:flutter/material.dart';
import 'package:sweetfifteen/events.dart';


class Event extends StatefulWidget {
  final String eventName;
  final String eventPlace;

  const Event({super.key, required this.eventName, required this.eventPlace});

  @override
  State<StatefulWidget> createState() {
    return _EventState();
  }
}

class _EventState extends State<Event> {
  _EventState();

  @override
  Widget build(BuildContext context) {
    return Text("${widget.eventName} ${widget.eventPlace}");
  }
}
