import 'package:flutter/material.dart';
import 'package:sweetfifteen/profile/profiles.dart';


class Profile extends StatefulWidget {
  final String username;
  final String desc;
  final String address;


  const Profile({super.key, required this.username, required this.desc, required this.address});

  @override
  State<StatefulWidget> createState() {
    return _ProfileState();
  }
}

class _ProfileState extends State<Profile> {
  _ProfileState();

  @override
  Widget build(BuildContext context) {
    // return ListTile(
    //     shape: RoundedRectangleBorder(
    //       side: const BorderSide(color: Colors.black, width: 1),
    //       borderRadius: BorderRadius.circular(20)
    //     ),
    //     onTap: () => {
    //
    //     },
    //     contentPadding: const EdgeInsets.symmetric(horizontal: 20.0, vertical: 10.0),
    //     leading: Container(
    //       padding: const EdgeInsets.all(10),
    //       decoration: const BoxDecoration(
    //           border: Border(
    //           right: BorderSide(width: 1.0, color: Colors.black))),
    //       child: const Icon(Icons.autorenew, color: Colors.black),
    //     ),
    //     title: Text(
    //       widget.username,
    //       style: const TextStyle(color: Colors.black, fontWeight: FontWeight.bold),
    //     ),
    //     // title: Text(
    //     // widget.desc,
    //     // style: const TextStyle(color: Colors.black, fontWeight: FontWeight.bold),
    //     // ),
    //
    //     subtitle: Row(
    //       children: <Widget>[
    //         const Icon(Icons.place, color: Colors.black),
    //         Text(widget.address, style: const TextStyle(color: Colors.black))
    //       ],
    //     ),
    //     trailing:
    //     const Icon(Icons.keyboard_arrow_right, color: Colors.black, size: 30.0));
    return Text(
      'Username: ' + widget.username
         + '\n' +
      'Description: ' + widget.desc
         + '\n' +
      'Address: ' + widget.address,
      textAlign: TextAlign.left,
      overflow: TextOverflow.ellipsis,
      style: const TextStyle(fontWeight: FontWeight.bold),
    );
  }
}
