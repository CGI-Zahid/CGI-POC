import { ActionConst } from 'react-native-router-flux';



const DEFAULT_STATE = {
  scene: {},
loggedin: false,
currentUser: {}
}


  export default (state = DEFAULT_STATE, action)=> {
    console.log(" nav reducer  ",action)


  let payload = action.payload;

  switch(action.type) {
    case 'LOGOUT':
      return {
        ...state
      }
    case ActionConst.FOCUS:
      //do i need to set a scene??
      //scene: payload.scene ,
      return {
        ...state,

      };
    case 'LOGINOK':

      return {
        ...state,
        loggedin: true,
        currentUser: payload
      };

    case 'LOGIN_TOKEN':
      return {
        ...state,
        loggedin: true,
        currentUser: payload
      }

    case 'TOKENRETRIEVE_TOKENRETRIEVE':
      return {
        ...state,
        loggedin: true,
        fcmtoken: payload
      }


    default:
      return state
  }
}
