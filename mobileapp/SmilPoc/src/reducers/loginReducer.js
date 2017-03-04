const DEFAULT_STATE = {count: 0}
export default (state = DEFAULT_STATE, {type, payload})=> {

  switch(type) {
    case 'INCREMENT':
      return {
        ...state,
        count: state.count+1,
      }

    default:
      return state
  }
}
