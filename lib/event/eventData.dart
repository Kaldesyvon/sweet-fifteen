// ignore: file_names
import 'package:sweetfifteen/event/event.dart';

class EventData {
  static final events = [
    Event(
        eventName: "TUKE",
        eventPlace: "TUKE",
        joined: false,
        description:
            "Join us in a coffee & programming session, where we work on our school projects and provide for other help",
        keywords: const ["IT", "programming"],
        eventDate: DateTime(2023, 4, 3, 10)),
    Event(
        eventName: "Upratovanie Galerie",
        eventPlace: "OC Galeria",
        joined: false,
        description:
            "Join us in cleaning of OC Galeria, because we don't like our shopping centre dirty!",
        keywords: const ["volunteer", "cleaning"],
        eventDate: DateTime(2023, 4, 5, 11)),
    Event(
        eventName: "Bike meet",
        eventPlace: "Dom sv. alzbety",
        joined: false,
        description:
            "Join us in biking around Kosice Old Town and talk about bikes and stuff like that...",
        keywords: const ["biking", "outdoor", "sport"],
        eventDate: DateTime(2023, 4, 6, 15)),
  ];
}
