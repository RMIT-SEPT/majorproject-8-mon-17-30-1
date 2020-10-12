import React, { Component } from "react";
import { Link } from "react-router-dom";
import BookService from "./bookservice";

import AuthService from "../service/auth";
export default class Dashboard extends Component {
    constructor(props) {
        super(props);

        this.state = {
            // showModeratorBoard: false,
            // showAdminBoard: false,
            currentUser: undefined,
        };
    }

    onClick() {}

    componentDidMount() {
        const user = AuthService.getCurrentUser();

        if (user) {
            this.setState({
                currentUser: user,
            });
        }
    }

    render() {
        const { currentUser } = this.state;

        return (
            <div className="container">
                <h3 class="title is-3">Dashboard</h3>
                {currentUser ? (
                    <div>
                        <div class="card events-card">
                            <section class="hero is-info welcome is-small">
                                <div class="hero-body">
                                    <div class="container">
                                        <h1 class="title">
                                            Hello, {currentUser.username}.
                                        </h1>
                                        <h2 class="subtitle">
                                            Good to see you back!
                                        </h2>
                                    </div>
                                </div>
                            </section>
                        </div>
                        <BookService />
                    </div>
                ) : (
                    <h2 class="subtitle">
                        You are not logged in! You need to login{" "}
                        <a href="/login">here</a>.
                    </h2>
                )}
            </div>
        );
    }
}
