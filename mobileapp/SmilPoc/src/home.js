import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  Platform, Dimensions, Image
} from 'react-native';

import {
  Button, h2,Tile
} from 'react-native-elements';

import { connect } from 'react-redux';

import {getNav,  getHome} from './reducers'

import * as actions from './actions';
import FCM, {FCMEvent, RemoteNotificationResult, WillPresentNotificationResult, NotificationType} from 'react-native-fcm';


const _BG = require('./assets/pexels-photo.jpg');
const {height, width} = Dimensions.get('window');



const mapDispatchToProps = {
  ...actions
}

const mapStateToProps = (state, props)=> {
  return {
    ...getNav(state),
    ...getHome(state)
  }
}


//the above translates into :

// this.state = {
//   nav: {loggedin:.....}
// }

class Home extends Component {
  _test(){
    //dispatch action that will then alter something
    // this.props.startMonitoring()


  }

  //fcm token

  //cn0EGG7PUi4:APA91bG1kXnWl3d6XS_AhH3LsH9t_oUY4MRgQJabSJ5v1dYbNvnM_7V4qtseC1yVP7latX2GHE6qycoe0CaUtB_UzzfTsl2obarGuHCn4MmXtyPZHH4_6ShQ67WrPSmch-YlZRlSb5GK

  componentDidMount(){
    console.disableYellowBox = true;
    //because i'm using imageSrc as a binary, there is a warning, im supressing it here

       FCM.requestPermissions(); // for iOS
       FCM.getFCMToken().then(token => {
          //  console.log("FCM token",token)
           // store fcm token in your server
          //  this.props.storeToken(token);
       });

       this.refreshTokenListener = FCM.on(FCMEvent.RefreshToken, (token) => {
            // console.log("Refresh token",token)
            // fcm token may not be available on first load, catch it here
        });

        this.notificationListener = FCM.on(FCMEvent.Notification, async (notif) => {
           // there are two parts of notif. notif.notification contains the notification payload, notif.data contains data payload
           if(notif.local_notification){
             //this is a local notification
           }
           if(notif.opened_from_tray){
             //app is open/resumed because user clicked banner
           }
          //  await someAsyncCall();

           if(Platform.OS ==='ios'){
             //optional
             //iOS requires developers to call completionHandler to end notification process. If you do not call it your background remote notifications could be throttled, to read more about it see the above documentation link.
             //This library handles it for you automatically with default behavior (for remote notification, finish with NoData; for WillPresent, finish depend on "show_in_foreground"). However if you want to return different result, follow the following code to override
             //notif._notificationType is available for iOS platfrom
             switch(notif._notificationType){
               case NotificationType.Remote:
                 notif.finish(RemoteNotificationResult.NewData) //other types available: RemoteNotificationResult.NewData, RemoteNotificationResult.ResultFailed
                 break;
               case NotificationType.NotificationResponse:
                 notif.finish();
                 break;
               case NotificationType.WillPresent:
                 notif.finish(WillPresentNotificationResult.All) //other types available: WillPresentNotificationResult.None
                 break;
             }
           }
       });

       FCM.send('1037053801988', {
          zipcode: '92001',

        });
  }

  showLocalNotification(notif) {
    FCM.presentLocalNotification({
      title: notif.title,
      body: notif.body,
      priority: "high",
      click_action: notif.click_action,
      show_in_foreground: true,
      local: true
    });
  }

  componentWillUnmount() {
      // stop listening for events
      this.notificationListener.remove();
      this.refreshTokenListener.remove();
  }

  render() {
    // <Button
    //   raised
    //   icon={{name: 'cached'}}
    //   title='Test'
    //   onPress={this._test.bind(this)}/>

                //
//try this to work: imageSrc={{require: './assets/pexels-photo.jpg'}}
    return (

      <View style={styles.container}>
        <Tile
           imageSrc={_BG}
           title="We'll send you event notifications based on your phone's geo-location. And if your user profile indicates you want to receive push notifications, you're now all set up to receive them!"
           featured
           caption='Thanks for logging in'
           height={height}
        />
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,

  },

});

export default connect(mapStateToProps, mapDispatchToProps)(Home)
