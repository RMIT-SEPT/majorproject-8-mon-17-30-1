import React, { Component } from "react";

export default class BookService extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div className="container">
        <header className="jumbotron">
            <h3>Book a Service.</h3>

            <h4>Select an Available Service</h4>
            <select name="ServiceSelect" id="ServiceSelect">
                <option value="placeHolder">Place Holder 1</option>
                <option value="placeHolder">Place Holder 2</option>
                <option value="placeHolder">Place Holder 3</option>
            </select>

            <h4>Select an Available Time</h4>
            <select name="TimeSelect" id="TimeSelect">
                <option value="placeHolder">Place Holder 1</option>
                <option value="placeHolder">Place Holder 2</option>
                <option value="placeHolder">Place Holder 3</option>
            </select>
        </header>

      </div>
    );
  }
}
