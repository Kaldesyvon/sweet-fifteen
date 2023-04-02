import 'package:flutter/material.dart';

import 'profile.dart';

typedef AddProfileCallback = void Function(Profile profile);

class AddProfileDialog extends StatefulWidget {
  final AddProfileCallback callback;
  const AddProfileDialog({super.key, required this.callback});

  @override
  State<StatefulWidget> createState() {
    return _AddProfileDialogState();
  }
}

class _AddProfileDialogState extends State<AddProfileDialog> {
  var newProfileAddress;
  var newProfileUsername;
  var newProfileDescription;

  var newProfileUsernameFormController = TextEditingController();
  var newProfileDescriptionFormController = TextEditingController();
  var newProfileAddressFormController = TextEditingController();

  @override
  void dispose() {
    // Clean up the controller when the widget is disposed.
    newProfileUsernameFormController.dispose();
    newProfileDescriptionFormController.dispose();
    newProfileAddressFormController.dispose();
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
          "Create New Profile",
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
                    "Enter Profile Username",
                    style: TextStyle(fontWeight: FontWeight.bold),
                  ),
                ),
                Container(
                  padding: const EdgeInsets.all(8.0),
                  child: TextField(
                    controller: newProfileUsernameFormController,
                    decoration: const InputDecoration(
                        border: OutlineInputBorder(),
                        hintText: 'Profile Username',
                        labelText: 'Profile Username'),
                  ),
                ),
                const Padding(
                  padding: EdgeInsets.all(8.0),
                  child: Text(
                    "Enter Profile Description",
                    style: TextStyle(fontWeight: FontWeight.bold),
                  ),
                ),
                Container(
                  padding: const EdgeInsets.all(8.0),
                  child: TextField(
                    controller: newProfileDescriptionFormController,
                    decoration: const InputDecoration(
                        border: OutlineInputBorder(),
                        hintText: 'Profile Description',
                        labelText: 'Profile Description'
                    ),
                  ),
                ),
                const Padding(
                  padding: EdgeInsets.all(8.0),
                  child: Text(
                    "Enter Profile Address",
                    style: TextStyle(fontWeight: FontWeight.bold),
                  ),
                ),
                Container(
                  padding: const EdgeInsets.all(8.0),
                  child: TextField(
                    controller: newProfileAddressFormController,
                    decoration: const InputDecoration(
                        border: OutlineInputBorder(),
                        hintText: 'Profile Address',
                        labelText: 'Profile Address'
                    ),
                  ),
                ),
                Container(
                  width: double.infinity,
                  height: 60,
                  padding: const EdgeInsets.all(8.0),
                  child: ElevatedButton(
                    onPressed: () {
                      widget.callback(Profile(username: newProfileUsernameFormController.text, desc: newProfileDescriptionFormController.text, address: newProfileAddressFormController.text));
                      newProfileUsernameFormController.text = "";
                      newProfileDescriptionFormController.text = "";
                      newProfileAddressFormController.text = "";
                      Navigator.pop(context);
                      setState(() {});
                    },
                    style: ElevatedButton.styleFrom(
                      backgroundColor: const Color.fromRGBO(0, 148, 128, 1.0),
                      // fixedSize: Size(250, 50),
                    ),
                    child: const Text(
                      "Create Profile",
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
