import React from "react";

import ClientApp from "./ClientApp";
import ErrorModal from "./ErrorModal";
import { connect } from "react-redux";

class App extends React.Component {
  render() {
    const { error } = this.props.globalErrors;

    return (
      <>
        <ClientApp />
        {error && <ErrorModal message={error} />}
      </>
    );
  }
}
const mapStateToProps = (state) => {
  return { globalErrors: state.globalErrors };
};
export default connect(mapStateToProps)(App);
