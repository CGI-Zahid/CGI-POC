### Mobile App development notes



#### General notes

* Preferred IDE: Atom with Nuclide plugin , allows debugging
* or VSCODE
* install yarn for quicker package installations
* Debugging via browser: http://localhost:8081/debugger-ui

* Avoid using console, in favour of xlog: https://github.com/EngsShi/react-native-xlog


#### Push notifications

* Only IOS is fully setup , see details for module: react-native-fcm
* Xcode build issues, most common is seeing RNFIRmessaging.h as missing , solution: select Project (blue, not the target) and add to Header Search path: $(SRCROOT)/../node_modules/react-native-fcm/ios

#### Cocoapods

* Follow normal use
* After installing pods, use xcode workspace instead of project
* open ios/SmilPoc.xcworkspace



#### Xcode problems resolution when archiving


- In Xcode, go to the project scheme (Product -> Scheme -> Manage Scheme -> double click your project).

- Click on the 'Build' option at the left pane.

- Uncheck 'Parallelize Build' under Build Options.

- Then in Targets section, click '+' button then search for 'React'. Select it and click 'Add'.
'React' should now appear under Targets section. Click and drag it to the top so that it will be the first item in the list (before your project).

- Clean the project and build.



#### Upgrading to the newest version of React Native

* Please be aware that standard react-native upgrade will replace ios/android main files (e.g. appdelegate) with a standard one

* Best way is to use this package that will try to resolve conflicts
* https://github.com/facebook/react-native/tree/master/react-native-git-upgrade
