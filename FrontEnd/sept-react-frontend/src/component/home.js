import React, { Component } from "react";

export default class Home extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div className="container">
        <header className="jumbotron">
          <h3>Welcome to AGME</h3>
          <p>[cool stock photo]</p>
        </header>
      </div>
    );
  }
}