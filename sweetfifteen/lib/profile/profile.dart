import 'package:flutter/material.dart';

class ProfilePage extends StatefulWidget {
  final String username = "";
  final String desc = "";
  final String address = "";

  const ProfilePage({super.key});

  @override
  State<StatefulWidget> createState() {
    return _ProfileState();
  }
}

class _ProfileState extends State<ProfilePage> {
  _ProfileState();

  @override
  Widget build(BuildContext context) {
    return Container(
      child:
          Text(
            'Username: ${widget.username}\nDescription: ${widget.desc}\nAddress: ${widget.address}',

            overflow: TextOverflow.ellipsis,
            style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 20),
          )
    );
  }
}
