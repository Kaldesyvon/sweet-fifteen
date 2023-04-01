import 'package:flutter/material.dart';

import 'event.dart';

typedef AddEventCallback = void Function(Event event);

class AddEventDialog extends StatefulWidget {
  final AddEventCallback callback;
  const AddEventDialog({super.key, required this.callback});

  @override
  State<StatefulWidget> createState() {
    return _AddEventDialogState();
  }
}

class _AddEventDialogState extends State<AddEventDialog> {
  var newEventPlace;
  var newEventName;

  var newEventNameFormController = TextEditingController();
  var newEventPlaceFormController = TextEditingController();

  @override
  void dispose() {
    // Clean up the controller when the widget is disposed.
    newEventNameFormController.dispose();
    newEventPlaceFormController.dispose();
    super.dispose();
  }

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
        title: const Text(
          "Create New Event",
          style: TextStyle(fontSize: 24.0, fontWeight: FontWeight.bold),
        ),
        content: SizedBox(
          height: 300,
          width: 200,
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
                    "Enter Event Name",
                    style: TextStyle(fontWeight: FontWeight.bold),
                  ),
                ),
                Container(
                  padding: const EdgeInsets.all(8.0),
                  child: TextField(
                    controller: newEventNameFormController,
                    decoration: const InputDecoration(
                        border: OutlineInputBorder(),
                        hintText: 'Event Name',
                        labelText: 'Event Name'),
                  ),
                ),
                const Padding(
                  padding: EdgeInsets.all(8.0),
                  child: Text(
                    "Enter Event Place",
                    style: TextStyle(fontWeight: FontWeight.bold),
                  ),
                ),
                Container(
                  padding: const EdgeInsets.all(8.0),
                  child: TextField(
                    controller: newEventPlaceFormController,
                    decoration: const InputDecoration(
                        border: OutlineInputBorder(),
                        hintText: 'Event Place',
                        labelText: 'Event Place'
                    ),
                  ),
                ),
                Container(
                  width: double.infinity,
                  height: 60,
                  padding: const EdgeInsets.all(8.0),
                  child: ElevatedButton(
                    onPressed: () {
                      widget.callback(Event(eventName: newEventNameFormController.text, eventPlace: newEventPlaceFormController.text));
                      newEventNameFormController.text = "";
                      newEventPlaceFormController.text = "";
                      Navigator.pop(context);
                      setState(() {});
                    },
                    style: ElevatedButton.styleFrom(
                      backgroundColor: const Color.fromRGBO(0, 148, 128, 1.0),
                      // fixedSize: Size(250, 50),
                    ),
                    child: const Text(
                      "Create Event",
                    ),
                  ),
                ),
              ],
            ),
          ),
        )
    );
  }
}
