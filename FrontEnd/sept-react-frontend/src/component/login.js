import React, { Component } from "react";


export default class Login extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div className="container">
        <header className="jumbotron">
          <h3>Login</h3>
          <form action="check_user.php" method="post">
            <div class="form-group">
              <label for="email">Email</label>
              <input type="text" class="form-control" name="email" placeholder="Enter your registered Email"></input>
            </div>
            <div class="form-group">
              <label for="password">Password</label>
              <input type="password" class="form-control" name="password" placeholder="Enter your Password"></input>
            </div>
            <button type="submit" class="btn btn-primary">Login</button>
          </form>
        </header>
      </div>
    );
  }
}
