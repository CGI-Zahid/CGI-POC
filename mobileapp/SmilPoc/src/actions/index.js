

import {Actions} from 'react-native-router-flux'
import {
 AsyncStorage
} from 'react-native';


const _USERTOKENKEY = '@com.cgi.smil:usertoken';
export const handleLogin = (email, pwd)=>{
  //API call here


  // Redux require you to return an object with type
  // return {type: 'LOGINOK', payload: {email: email, pwd: pwd}}

  return dispatch => {
    // setTimeout( () => {
    //   Actions.home()
    //   dispatch({type: 'LOGINOK', payload: {email: email, pwd: pwd}})
    // },2000)

    AsyncStorage.getItem(_USERTOKENKEY).then ( (value)=> {

      console.log("got token ",value);

      Actions.home()
      dispatch({type: 'LOGINOK', payload: {email: email, pwd: pwd}})

    })

  }
}

export const loginWithToken = (token)=>{
  //for existing user

  // Actions.home()
  // Redux require you to return an object with type
  // return {type: 'LOGIN_TOKEN', payload: {token: token}}

  return dispatch => {
    setTimeout( () => {
      dispatch({type: 'LOGIN_TOKEN', payload: {token: token}})
    },2000)
  }
}


export const authorizeUser = (email, pwd)=>{
  //API call here

  fetch('https://mainapi.com/endpoint/', {
    method: 'POST',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      email: email,
      password: pwd,
    })
  }).then ((response)=> response.json())
  .then ( (json)=>{
    return {type: 'LOGIN_TOKEN', payload: {token: token}}
  }).catch((error) => {
    return {type: 'BADLOGIN',payload: {}}
  });


}

export const  storeToken =  (token) => {
  try {
   AsyncStorage.setItem(_USERTOKENKEY, token).then( ()=> {
     console.log("STORED your token",token);
     return {type: "TOKEN_STORED", payload: {}}

   } );

} catch (error) {
  // Error saving data
  return {type: "TOKEN_STORE_FAIL",payload: {}}
}
}

export const tryLocalToken = () => {
  try {
        return {type: 'TOKENRETRIEVE', payload: AsyncStorage.getItem(_USERTOKENKEY) }

  } catch (error) {
    // Error retrieving data
    return {type: 'LOGIN_TOKEN_BAD', payload: {token: ""}}
  }

}
