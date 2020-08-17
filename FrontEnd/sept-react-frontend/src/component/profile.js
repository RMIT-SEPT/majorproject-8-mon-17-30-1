import React, { Component } from "react";
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";
import AuthService from "../service/auth";

export default class Profile extends Component {
  constructor(props) {
    super(props);

    this.state = {
      // showModeratorBoard: false,
      // showAdminBoard: false,
      currentUser: undefined
    };
  }

  componentDidMount() {
    const user = AuthService.getCurrentUser();

    if (user) {
      this.setState({
        currentUser: user,
      });
    }
  }

  render() {
    const { currentUser
    } = this.state;

    return (
      <div className="container">
        <header className="jumbotron">
          <h3>Profile</h3>
        </header>

        {currentUser ? (
          <div className="card card-container">
            <Form
              onSubmit={this.handleLogin}
              ref={(c) => {
                this.form = c;
              }}
            >
              <div className="form-group">
                <label htmlFor="username">Username</label>
                <Input
                  type="text"
                  className="form-control"
                  name="username"
                  value={this.state.currentUser.username}
                  onChange={this.onChangeUsername}
                  disabled
                />
              </div>

              <div className="form-group">
                <label htmlFor="username">Role type</label>
                <Input
                  type="text"
                  className="form-control"
                  name="role-type"
                  value={this.state.currentUser.role}
                  onChange={this.onChangeUsername}
                  disabled
                />
              </div>

              <div className="form-group">
                <button
                  className="btn btn-primary btn-block"
                  disabled={this.state.loading}
                >
                  {this.state.loading && (
                    <span className="spinner-border spinner-border-sm"></span>
                  )}
                  <span>Update details</span>
                </button>
              </div>

              {this.state.message && (
                <div className="form-group">
                  <div className="alert alert-danger" role="alert">
                    {this.state.message}
                  </div>
                </div>
              )}
              <CheckButton
                style={{ display: "none" }}
                ref={(c) => {
                  this.checkBtn = c;
                }}
              />
            </Form>
          </div>
        ) : (
          <p>
            You are not logged in! You need to login <a href="/login">here</a>.
          </p>
        )}
      </div>
    );
  }
}
