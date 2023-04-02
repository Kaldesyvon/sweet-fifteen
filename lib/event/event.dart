import 'package:flutter/material.dart';
import 'package:sweetfifteen/event/events.dart';

import 'event-detail-dialog.dart';

getLongFromEventPlace(String eventPlace) {
  if (eventPlace == "Kunstahale") {
    return 21.260932;
  } else if (eventPlace == "Dom sv. alzbety") {
    return 21.258228333171918;
  } else if (eventPlace == "OC Galeria") {
    return 21.238536932663322;
  } else {
    return 21.24524555520934; // TUKE
  }
}

getLatFromEventPlace(String eventPlace) {
  if (eventPlace == "Kunsthalle") {
    return 48.725452580297116;
  } else if (eventPlace == "Dom sv. alzbety") {
    return 48.72066798420253;
  } else if (eventPlace == "OC Galeria") {
    return 48.71574103673905;
  } else {
    return 48.73110340057184; // TUKE
  }
}

class Event extends StatefulWidget {
  final String eventName;
  final String eventPlace;
  final List<String> keywords;
  final String description;
  final DateTime eventDate;
  double lat;
  double long;
  late bool joined;

  Event({
    super.key,
    required this.eventName,
    required this.eventPlace,
    required this.joined,
    required this.description,
    required this.keywords,
    required this.eventDate
  })  : lat = getLatFromEventPlace(eventPlace),
        long = getLongFromEventPlace(eventPlace);

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
            borderRadius: BorderRadius.circular(20)),
        onTap: () {
          showDialog(
              context: context,
              builder: (context) {
                return EventDetailDialog(event: widget, openedFromMap: false);
              });
        },
        contentPadding: const EdgeInsets.symmetric(horizontal: 20.0, vertical: 10.0),
        leading: GestureDetector(
          onTap: () {
            widget.joined = !widget.joined;
            setState(() {});
          },
          child: Container(
            padding: const EdgeInsets.all(10),
            decoration: const BoxDecoration(
                border: Border(
                right: BorderSide(width: 1.0, color: Colors.black))),
            child: (() {
              if(widget.joined) {
                return const Icon(Icons.star, color: Colors.black);
              }
              return const Icon(Icons.star_border, color: Colors.black);
            })()
          )

        ),
        title: Text(
          widget.eventName,
          style:
              const TextStyle(color: Colors.black, fontWeight: FontWeight.bold),
        ),
        subtitle: Row(
          children: <Widget>[
            const Icon(Icons.place, color: Colors.black),
            Text(widget.eventPlace, style: const TextStyle(color: Colors.black))
          ],
        ),
        trailing: const Icon(Icons.keyboard_arrow_right,
            color: Colors.black, size: 30.0));
  }
}
