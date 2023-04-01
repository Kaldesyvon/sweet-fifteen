import 'package:flutter/material.dart';
import 'package:sweetfifteen/event/events.dart';


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
    return ListTile(
        shape: RoundedRectangleBorder(
          side: const BorderSide(color: Colors.black, width: 1),
          borderRadius: BorderRadius.circular(20)
        ),
        onTap: () => {

        },
        contentPadding: const EdgeInsets.symmetric(horizontal: 20.0, vertical: 10.0),
        leading: Container(
          padding: const EdgeInsets.all(10),
          decoration: const BoxDecoration(
              border: Border(
              right: BorderSide(width: 1.0, color: Colors.black))),
          child: const Icon(Icons.autorenew, color: Colors.black),
        ),
        title: Text(
          widget.eventName,
          style: const TextStyle(color: Colors.black, fontWeight: FontWeight.bold),
        ),

        subtitle: Row(
          children: <Widget>[
            const Icon(Icons.place, color: Colors.black),
            Text(widget.eventPlace, style: const TextStyle(color: Colors.black))
          ],
        ),
        trailing:
        const Icon(Icons.keyboard_arrow_right, color: Colors.black, size: 30.0));
  }
}
