import 'package:flutter/material.dart';
import 'package:sweetfifteen/map.dart';

import '../main.dart';
import 'event.dart';

class EventDetailDialog extends StatefulWidget {
  final Event event;
  final bool openedFromMap;
  const EventDetailDialog({super.key, required this.event, required this.openedFromMap});

  @override
  State<StatefulWidget> createState() {
    return _EventDetailDialogState();
  }
}

class _EventDetailDialogState extends State<EventDetailDialog> {
  @override
  Widget build(BuildContext context) {
    return AlertDialog(
        shape: const RoundedRectangleBorder(
          borderRadius: BorderRadius.all(
            Radius.circular(
              20.0,
            ),
          ),
        ),
        contentPadding: const EdgeInsets.only(
          top: 10.0,
        ),
        title: Text(
          widget.event.eventName,
          style: const TextStyle(fontSize: 24.0, fontWeight: FontWeight.bold),
        ),
        content: SizedBox(
          height: 500,
          width: 400,
          child: SingleChildScrollView(
            padding: const EdgeInsets.all(8.0),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.start,
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisSize: MainAxisSize.min,
              children: <Widget>[
                const Padding(
                  padding: EdgeInsets.all(8.0),
                  child: Text(
                    "Event Place",
                    style: TextStyle(fontWeight: FontWeight.bold),
                  ),
                ),
                Container(
                  padding: const EdgeInsets.all(8.0),
                  child: Text(widget.event.eventPlace),
                ),
                const Padding(
                  padding: EdgeInsets.all(8.0),
                  child: Text(
                    "Event Date",
                    style: TextStyle(fontWeight: FontWeight.bold),
                  ),
                ),
                Container(
                  padding: const EdgeInsets.all(8.0),
                  child: Text(
                      "${widget.event.eventDate.day.toString()}/${widget.event.eventDate.month.toString()}/2023 ${widget.event.eventDate.hour}:00"
                  ),
                ),
                const Padding(
                  padding: EdgeInsets.all(8.0),
                  child: Text(
                    "Event Description",
                    style: TextStyle(fontWeight: FontWeight.bold),
                  ),
                ),
                Container(
                  padding: const EdgeInsets.all(8.0),
                  child: Text(widget.event.description),
                ),
                const Padding(
                  padding: EdgeInsets.all(8.0),
                  child: Text(
                    "Event Keywords",
                    style: TextStyle(fontWeight: FontWeight.bold),
                  ),
                ),
                Container(
                  padding: const EdgeInsets.all(8.0),
                  child: Text(widget.event.keywords.join(", ")),
                ),
                (() {
                  if(!widget.openedFromMap) {
                    return Container(
                      width: double.infinity,
                      height: 60,
                      padding: const EdgeInsets.all(8.0),
                      child: ElevatedButton(
                          onPressed: () {
                            Navigator.pop(context);
                            setState(() {});
                            Navigator.of(context).push(MaterialPageRoute(
                                builder: (context) => MainPage(
                                  index: 1,
                                  lat: widget.event.lat,
                                  long: widget.event.long
                                )
                            ));
                          },
                          style: ElevatedButton.styleFrom(
                            backgroundColor: const Color.fromRGBO(0, 148, 128, 1.0),
                          ),
                          child:
                          const Text(
                            "Show on Map",
                          )
                      ),
                    );
                  }
                  return Container();
                })(),
                Container(
                  width: double.infinity,
                  height: 60,
                  padding: const EdgeInsets.all(8.0),
                  child: ElevatedButton(
                    onPressed: () {
                      widget.event.joined = !widget.event.joined;
                      setState(() {});
                    },
                    style: ElevatedButton.styleFrom(
                      backgroundColor: (() => (widget.event.joined)
                          ? const Color.fromRGBO(200, 100, 0, 1.0)
                          : const Color.fromRGBO(0, 148, 128, 1.0))(),
                    ),
                    child: Text((() => (widget.event.joined)
                        ? "Leave Event"
                        : "Join Event")()),
                  ),
                ),
              ],
            ),
          ),
        ));
  }
}
