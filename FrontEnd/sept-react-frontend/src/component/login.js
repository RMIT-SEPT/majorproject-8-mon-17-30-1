import React, { Component } from "react";
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";

import Auth from "../service/auth";

const required = (value) => {
    if (!value) {
        return <div class="notification is-light is-warning">This field is required!</div>;
    }
};

export default class Login extends Component {
    constructor(props) {
        super(props);
        this.handleLogin = this.handleLogin.bind(this);
        this.onChangeUsername = this.onChangeUsername.bind(this);
        this.onChangePassword = this.onChangePassword.bind(this);

        this.state = {
            username: "",
            password: "",
            loading: false,
            errors: [],
        };
    }

    onChangeUsername(e) {
        this.setState({
            username: e.target.value,
        });
    }

    onChangePassword(e) {
        this.setState({
            password: e.target.value,
        });
    }

    handleLogin(e) {
        e.preventDefault();

        this.setState({
            errors: [],
            loading: true,
        });

        this.form.validateAll();

        if (this.checkBtn.context._errors.length === 0) {
            Auth.login(this.state.username, this.state.password)
                .then((jwtResponse) => {
                    this.props.history.push("/dashboard");
                    window.location.reload();
                })
                .catch((errors) => {
                    this.setState({
                        loading: false,
                        errors: errors,
                    });
                    console.log(errors);
                });
        } else {
            this.setState({
                loading: false,
            });
        }
    }

    render() {
        var errorMessages = undefined;
        if (this.state.errors.length > 0) {
            errorMessages = this.state.errors.map((errorMessage) => {
                return <div class="notification is-danger">{errorMessage}</div>;
            });
        }

        return (
            <div className="container">
                <h3 class="title is-3">Login</h3>
                <div className="card events-card">
                    <div class="card-content">
                        <div class="content">
                            <Form
                                onSubmit={this.handleLogin}
                                ref={(c) => {
                                    this.form = c;
                                }}
                            >
                                <div className="field">
                                    <label class="label">AGME username</label>
                                    <div className="control">
                                        <Input
                                            type="text"
                                            className="input"
                                            name="username"
                                            value={this.state.username}
                                            onChange={this.onChangeUsername}
                                            validations={[required]}
                                        />
                                    </div>
                                </div>

                                <div className="field">
                                    <label class="label">Password</label>
                                    <div className="control">
                                        <Input
                                            type="password"
                                            className="input"
                                            name="password"
                                            value={this.state.password}
                                            onChange={this.onChangePassword}
                                            validations={[required]}
                                        />
                                    </div>
                                </div>

                                <div class="field is-grouped">
                                    <div class="control">
                                        <button
                                            className={`button is-link ${this.state.loading ? "is-loading" : ""}`}
                                            disabled={this.state.loading}
                                        >
                                            <span>Login</span>
                                        </button>
                                    </div>
                                </div>
                                {this.state.errors.map((errorMessage) => {
                                    return <div class="notification is-light is-danger">{errorMessage}</div>;
                                })}
                                <CheckButton
                                    style={{ display: "none" }}
                                    ref={(c) => {
                                        this.checkBtn = c;
                                    }}
                                />
                            </Form>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}
