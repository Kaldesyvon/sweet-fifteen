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
    return Scaffold(
        body: ListView(
          children: <Widget>[
            Container(
              height: 250,
              decoration: const BoxDecoration(
                  border: Border(
                      bottom: BorderSide(width: 3.0, color: Colors.black)
                  )
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.center,
                mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceAround,
                    children: const <Widget>[
                      CircleAvatar(
                        backgroundColor: Color.fromRGBO(0, 148, 128, 1.0),
                        minRadius: 70.0,
                        child: CircleAvatar(
                          radius: 65.0,
                          backgroundImage:
                            NetworkImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT91BGh-UnWqRo-TMrO2MyxEB64ARpBkVpNdvM9oz0&s")
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(
                    height: 10,
                  ),
                  const Text(
                    'John Doe',
                    style: TextStyle(
                      fontSize: 35,
                      fontWeight: FontWeight.bold,
                      color: Colors.black,
                    ),
                  ),
                  const Text(
                    'Student',
                    style: TextStyle(
                      color: Colors.black,
                      fontSize: 20,
                    ),
                  ),
                ],
              ),
            ),
            Container(
              padding: const EdgeInsets.only(top: 20),
              child:
                const Center(
                  child: Text('Interests',
                    style: TextStyle(
                      color: Colors.black,
                      fontSize: 30,
                      fontWeight: FontWeight.bold
                    )
                  )
                )
            ),
            Container(
              padding: const EdgeInsets.all(20),
              child: Row(
                children: const [
                  CircleAvatar(
                    radius: 32.0,
                    backgroundColor: Color.fromRGBO(0, 148, 128, 1.0),
                    child: Icon(Icons.skateboarding, color: Colors.white),
                  ), Spacer(),
                  CircleAvatar(
                    radius: 32.0,
                    backgroundColor: Color.fromRGBO(0, 148, 128, 1.0),
                    child: Icon(Icons.ice_skating, color: Colors.white),
                  ), Spacer(),
                  CircleAvatar(
                      radius: 32.0,
                      backgroundColor: Color.fromRGBO(0, 148, 128, 1.0),
                      child: Icon(Icons.pedal_bike, color: Colors.white)
                  ), Spacer(),
                  CircleAvatar(
                      radius: 32.0,
                      backgroundColor: Color.fromRGBO(0, 148, 128, 1.0),
                      child: Icon(Icons.computer, color: Colors.white)
                  )
                ],
              ),
            ),
            Container(
              padding: const EdgeInsets.only(left: 64, right: 64),
              child: Row(
                children: const [
                  CircleAvatar(
                    radius: 32.0,
                    backgroundColor: Color.fromRGBO(0, 148, 128, 1.0),
                    child: Icon(Icons.hiking, color: Colors.white),
                  ), Spacer(),
                  CircleAvatar(
                    radius: 32.0,
                    backgroundColor: Color.fromRGBO(0, 148, 128, 1.0),
                    child: Icon(Icons.backpack, color: Colors.white),
                  ), Spacer(),
                  CircleAvatar(
                      radius: 32.0,
                      backgroundColor: Color.fromRGBO(0, 148, 128, 1.0),
                      child: Icon(Icons.camera_alt, color: Colors.white)
                  )
                ],
              ),
            )
          ],
        ),
    );
  }
}
