import 'dart:math';

import 'package:flutter/material.dart';
import 'package:sweetfifteen/event/add-event-dialog.dart';

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
    Event(eventName: "TUKE", eventPlace: "TUKE", joined: false, description: "Join us in a coffee & programming session, where we work on our school projects and provide for other help", keywords: const ["IT", "programming"], eventDate: DateTime(2023, 4, 3, 10)),
    Event(eventName: "Upratovanie Galerie", eventPlace: "OC Galeria", joined: false, description: "Join us in cleaning of OC Galeria, because we don't like our shopping centre dirty!", keywords: const ["volunteer", "cleaning"], eventDate: DateTime(2023, 4, 5, 11)),
    Event(eventName: "Bike meet", eventPlace: "Dom sv. alzbety", joined: false, description: "Join us in biking around Kosice Old Town and talk about bikes and stuff like that...", keywords: const ["biking", "outdoor", "sport"], eventDate: DateTime(2023, 4, 6, 15)),
  ];

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
          )

      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          showDialog(
              context: context,
              builder: (context) {
                return AddEventDialog(
                    callback: (event) {
                      events.add(event);
                      setState(() {});
                    }
                );
              }
          );
        },
        backgroundColor: const Color.fromRGBO(0, 148, 128, 1.0),
        child: const Icon(Icons.add),
      ),
    );
  }
}
