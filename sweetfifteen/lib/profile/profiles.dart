import 'dart:math';

import 'package:flutter/material.dart';
import 'package:sweetfifteen/profile/add-profile-dialog.dart';

import 'profile.dart';

class ProfilesPage extends StatefulWidget {
  const ProfilesPage({super.key});

  @override
  State<StatefulWidget> createState() {
    return _ProfilesPageState();
  }
}

class _ProfilesPageState extends State<ProfilesPage> {
  final profiles = [
    const Profile(username: "JohnDoe", desc: "lorem ipsum", address: "dakde1"),
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: ListView.separated(
          scrollDirection: Axis.vertical,
          itemCount: profiles.length,
          padding: const EdgeInsets.all(10),
          shrinkWrap: true,
          itemBuilder: (BuildContext context, int index) {
            return profiles[index];
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
                return AddProfileDialog(
                    callback: (profile) {
                      profiles.add(profile);
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
