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
				<header className="jumbotron">
					<h3>Dashboard</h3>
					<button id="bookingsButton">
						<Link to={{pathname: "/bookings", search: "?view=all"}} className="btn btn-primary btn-lg active">
							Bookings
						</Link>
					</button>
					<button id="bookingsButton">
						<Link to={{pathname: "/bookings", search: "?view=history"}} className="btn btn-primary btn-lg active">
							Booking History
						</Link>
					</button>
					<button id="bookingsButton">
						<Link to={{pathname: "/bookings", search: "?view=allhistory"}} className="nav-link">
							Admin Booking History
						</Link>
					</button>
					<button id="bookingsButton">
					{currentUser && currentUser.role === "ADMIN" && <button id="bookingsButton">
						<Link to={{pathname: "/workers"}} className="btn btn-primary btn-lg active">
							Workers
						</Link>
					</button>}
				</header>
				<BookService/>
				{currentUser ? (
					<p>Logged in as {currentUser.role}</p>
				) : (
					<p>
						You are not logged in! You need to login <a href="/login">here</a>.
					</p>
				)}
			</div>
		);
	}
}
