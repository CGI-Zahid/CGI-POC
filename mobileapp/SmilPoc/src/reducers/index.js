// reducers/index.js

import { combineReducers } from 'redux';

import nav from './navReducer'
import home from './homeReducer'
import login from './loginReducer'
export default combineReducers({
  nav,
  home,
  login
})
export const getNav = ({nav}) => nav
export const getHome = ({home}) => home
export const getLogin = ({login}) => login
