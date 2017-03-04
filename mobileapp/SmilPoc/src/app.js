import React, { Component } from 'react';
import { Router, Actions, Scene } from 'react-native-router-flux';
import { connect, Provider } from 'react-redux';
import { createStore, applyMiddleware, compose } from 'redux';
import promiseMiddleware from 'redux-promise-middleware';

const ConnectedRouter = connect()(Router);
import reducers from './reducers';
import Login from './login';
import Home from './home';
import thunk from 'redux-thunk'
// create store...
const middleware = [promiseMiddleware()];/* ...your middleware (i.e. thunk) */
// const store = compose(
//   applyMiddleware( ...middleware)
// )(createStore)(reducers);

const store = createStore(
  reducers,
  compose(
    applyMiddleware(thunk,promiseMiddleware())
  )
)


const Scenes = Actions.create(
  <Scene key='root' hideNavBar>
    <Scene key='login' component={Login} initial/>
    <Scene key='home' component={Home}/>

  </Scene>
)

class App extends Component {

  componentDidMount(){
    // console.log("App, any methods here? ",this)
  }

  render () {
    return (
      <Provider store={store}>
        <ConnectedRouter scenes={Scenes} />
      </Provider>
    );
  }
}

export default App;
