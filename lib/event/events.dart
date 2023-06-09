import 'package:flutter/material.dart';
import 'package:sweetfifteen/event/add-event-dialog.dart';
import 'package:sweetfifteen/event/eventData.dart';

class EventsPage extends StatefulWidget {
  const EventsPage({super.key});

  @override
  State<StatefulWidget> createState() {
    return _EventsPageState();
  }
}

class _EventsPageState extends State<EventsPage> {
  static final events = EventData.events;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body:
        ListView.separated(
          scrollDirection: Axis.vertical,
          itemCount: events.length,
          padding: const EdgeInsets.all(10),
          shrinkWrap: true,
          itemBuilder: (BuildContext context, int index) {
            return events[index];
          },
          separatorBuilder: (context, index) => const SizedBox(
                height: 10,
              )),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          showDialog(
              context: context,
              builder: (context) {
                return AddEventDialog(callback: (event) {
                  events.add(event);
                  setState(() {});
                });
              });
        },
        backgroundColor: const Color.fromRGBO(0, 148, 128, 1.0),
        child: const Icon(Icons.add),
      ),
    );
  }
}
