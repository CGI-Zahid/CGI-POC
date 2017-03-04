import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View
} from 'react-native';

import {
  Button, h3,FormLabel, FormInput, FormValidationMessage
} from 'react-native-elements';

import * as actions from './actions';


import { connect } from 'react-redux';
import {getNav,  getLogin} from './reducers'
import * as Animatable from 'react-native-animatable';



const mapDispatchToProps = {
  ...actions
}

const mapStateToProps = (state, props)=> {
  return {
    ...getNav(state),
    ...getLogin(state)
  }
}

class Login extends Component {

  state = {
    email: '',
    pwd: '',
    errmessage: ''
  }

  constructor(props) {
        super(props);
    }

  _login(){
    const {email, pwd} = this.state;

    if (email.length < 1  ){
      this.setState({errmessage: 'Please enter valid credentials'});

    }else{
        this.setState({errmessage: ''});


        //this.props.dispatch(actions.handleLogin(email, pwd) );
        //instead of above
        this.props.handleLogin(email, pwd)

    }

    //using props, i should be able to dispatch
  }


  componentDidMount(){

    //this will check if token is present and move to next view
    // this.props.tryLocalToken();


    this.refs.view.pulse(1800)

  }

  render() {

  const {errmessage} = this.state;
    return (
      <View style={{justifyContent:'center',flexDirection: 'column', flex: 1,marginTop: 10}}>
        <View style={styles.container}>


          <FormLabel>Email</FormLabel>
          <FormInput
            onChangeText={(text) => this.setState({email: text})}
            />

          <FormLabel>Password</FormLabel>

          <FormInput
            onChangeText={(text) => this.setState({pwd: text})}
            secureTextEntry
            />

          <FormValidationMessage>
            {errmessage}

          </FormValidationMessage>

        </View>
        <View style={styles.container2}>
          <Animatable.View ref="view">

            <Button
              raised
              icon={{name: 'vpn-key'}}
              title='Login'
              onPress={this._login.bind(this)}
              />
          </Animatable.View>
        </View>

      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 0.8,

  },
  container2: {
    flex: 0.2,

  },

});

export default connect(mapStateToProps, mapDispatchToProps)(Login)
