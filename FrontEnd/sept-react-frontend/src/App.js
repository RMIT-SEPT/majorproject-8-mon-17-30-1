import React, { Component } from "react";
import { BrowserRouter as Router, Switch, Route, Link, withRouter } from "react-router-dom";
// import "bootstrap/dist/css/bootstrap.min.css";
import "bulma/css/bulma.min.css";
import "./App.css";
import AuthService from "./service/auth";

import Login from "./component/login";
import Register from "./component/register";
import Home from "./component/home";
import Profile from "./component/profile";
import Dashboard from "./component/dashboard";
import About from "./component/about";
import Contact from "./component/contact";
import viewBookings from "./component/viewBookings";
import viewWorkers from "./component/viewWorkers";
import ViewService from "./component/viewService";

class App extends Component {
    constructor(props) {
        super(props);
        this.handleEvent = this.handleEvent.bind(this);
        this.handleSideColumn = this.handleSideColumn.bind(this);
        this.logOut = this.logOut.bind(this);

        this.state = {
            currentUser: undefined,
            activeHamburger: false,
            activeLeftColumn: "dashboard",
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

    logOut() {
        AuthService.logout();
        window.location.reload();
    }

    handleEvent() {
        this.setState({ activeHamburger: !this.state.activeHamburger });
    }

    handleSideColumn(e) {
        this.setState({ activeLeftColumn: e });
    }

    render() {
        const { currentUser } = this.state;

        return (
            <Router>
                <div>
                    <nav className="navbar is-white">
                        <div class="container">
                            <div class="navbar-brand">
                                <Link
                                    to={"/"}
                                    className="navbar-item brand-text"
                                >
                                    AGME
                                </Link>
                                <div
                                    class={`navbar-burger burger ${
                                        this.state.activeHamburger
                                            ? "is-active"
                                            : ""
                                    }`}
                                    onClick={this.handleEvent}
                                    data-target="navMenu"
                                >
                                    <span></span>
                                    <span></span>
                                    <span></span>
                                </div>
                            </div>
                            <div
                                id="navMenu"
                                class={`navbar-menu ${
                                    this.state.activeHamburger
                                        ? "is-active"
                                        : ""
                                }`}
                            >
                                <div class="navbar-start">
                                    <Link to={"/about"} className="navbar-item">
                                        About us
                                    </Link>

                                    <Link
                                        to={"/contact"}
                                        className="navbar-item"
                                    >
                                        Contact us
                                    </Link>

                                    {currentUser &&
                                        currentUser.role === "CUSTOMER" && (
                                            <Link
                                                to={"/bookings"}
                                                className="navbar-item"
                                            >
                                                Bookings
                                            </Link>
                                        )}
                                </div>

                                {currentUser ? (
                                    <div class="navbar-end">
                                        <Link
                                            to={"/profile"}
                                            className="navbar-item"
                                        >
                                            {currentUser.username}
                                        </Link>
                                        <Link
                                            to={"/login"}
                                            className="navbar-item"
                                            onClick={this.logOut}
                                        >
                                            Logout
                                        </Link>
                                    </div>
                                ) : (
                                    <div class="navbar-end">
                                        <Link
                                            to={"/login"}
                                            className="navbar-item"
                                        >
                                            Login
                                        </Link>
                                        <Link
                                            to={"/register"}
                                            className="navbar-item"
                                        >
                                            Register
                                        </Link>
                                    </div>
                                )}
                            </div>
                        </div>
                    </nav>

                    <div className="container mt-3">
                        <div class="columns">
                            {currentUser && (
                                <div class="column is-3 ">
                                    <aside class="menu is-hidden-mobile">
                                        <div>
                                            <p class="menu-label">General</p>
                                            <ul class="menu-list">
                                                <li>
                                                    <Link
                                                        to={"/dashboard"}
                                                        onClick={() =>
                                                            this.handleSideColumn(
                                                                "dashboard"
                                                            )
                                                        }
                                                        className={
                                                            this.state
                                                                .activeLeftColumn ===
                                                            "dashboard"
                                                                ? "is-active"
                                                                : ""
                                                        }
                                                    >
                                                        Dashboard
                                                    </Link>
                                                </li>
                                            </ul>
                                        </div>
                                        {currentUser.role === "ADMIN" && (
                                            <div>
                                                <p class="menu-label">
                                                    Administrator
                                                </p>
                                                <ul class="menu-list">
                                                    <li>
                                                        <Link
                                                            to={{
                                                                pathname:
                                                                    "/bookings",
                                                                search:
                                                                    "?view=all",
                                                            }}
                                                            onClick={() =>
                                                                this.handleSideColumn(
                                                                    "bookingsall"
                                                                )
                                                            }
                                                            className={
                                                                this.state
                                                                    .activeLeftColumn ===
                                                                "bookingsall"
                                                                    ? "is-active"
                                                                    : ""
                                                            }
                                                        >
                                                            Bookings
                                                        </Link>
                                                        <ul>
                                                            <li>
                                                                <Link
                                                                    to={{
                                                                        pathname:
                                                                            "/bookings",
                                                                        search:
                                                                            "?view=history",
                                                                    }}
                                                                    onClick={() =>
                                                                        this.handleSideColumn(
                                                                            "bookingshistory"
                                                                        )
                                                                    }
                                                                    className={
                                                                        this
                                                                            .state
                                                                            .activeLeftColumn ===
                                                                        "bookingshistory"
                                                                            ? "is-active"
                                                                            : ""
                                                                    }
                                                                >
                                                                    Bookings
                                                                    history
                                                                </Link>
                                                            </li>
                                                            <li>
                                                                <Link
                                                                    to={{
                                                                        pathname:
                                                                            "/bookings",
                                                                        search:
                                                                            "?view=allhistory",
                                                                    }}
                                                                    onClick={() =>
                                                                        this.handleSideColumn(
                                                                            "bookingsallhistory"
                                                                        )
                                                                    }
                                                                    className={
                                                                        this
                                                                            .state
                                                                            .activeLeftColumn ===
                                                                        "bookingsallhistory"
                                                                            ? "is-active"
                                                                            : ""
                                                                    }
                                                                >
                                                                    All bookings
                                                                    history
                                                                </Link>
                                                            </li>
                                                        </ul>
                                                    </li>
                                                    <li>
                                                        <Link
                                                            to={{
                                                                pathname:
                                                                    "/workers",
                                                            }}
                                                            onClick={() =>
                                                                this.handleSideColumn(
                                                                    "workers"
                                                                )
                                                            }
                                                            className={
                                                                this.state
                                                                    .activeLeftColumn ===
                                                                "workers"
                                                                    ? "is-active"
                                                                    : ""
                                                            }
                                                        >
                                                            Workers
                                                        </Link>
                                                    </li>
                                                    <li>
                                                        <Link
                                                            to={{
                                                                pathname:
                                                                    "/services",
                                                            }}
                                                            onClick={() =>
                                                                this.handleSideColumn(
                                                                    "services"
                                                                )
                                                            }
                                                            className={
                                                                this.state
                                                                    .activeLeftColumn ===
                                                                "services"
                                                                    ? "is-active"
                                                                    : ""
                                                            }
                                                        >
                                                            Services
                                                        </Link>
                                                    </li>
                                                </ul>
                                            </div>
                                        )}
                                    </aside>
                                </div>
                            )}
                            <div class="column is-9">
                                <Switch>
                                    <Route exact path={"/"} component={Home} />
                                    <Route
                                        exact
                                        path="/login"
                                        component={Login}
                                    />
                                    <Route
                                        exact
                                        path="/register"
                                        component={Register}
                                    />
                                    <Route
                                        exact
                                        path="/profile"
                                        component={Profile}
                                    />
                                    <Route
                                        exact
                                        path="/dashboard"
                                        component={Dashboard}
                                    />
                                    <Route
                                        exact
                                        path="/about"
                                        component={About}
                                    />
                                    <Route
                                        exact
                                        path="/contact"
                                        component={Contact}
                                    />
                                    <Route
                                        exact
                                        path="/bookings"
                                        component={viewBookings}
                                    />
                                    <Route
                                        exact
                                        path="/workers"
                                        component={viewWorkers}
                                    />
                                    <Route
                                        exact
                                        path="/services"
                                        component={ViewService}
                                    />
                                </Switch>
                            </div>
                        </div>
                    </div>
                </div>
            </Router>
        );
    }
}

export default App;
