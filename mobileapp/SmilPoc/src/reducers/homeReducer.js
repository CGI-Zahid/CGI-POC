const DEFAULT_STATE = {ismonitoring: false}
export default (state = DEFAULT_STATE, {type, payload})=> {
  switch(type) {
    case 'START_MONITOR':
      return {
        ...state,
        ismonitoring: true
      }

    default:
      return state
  }
}
