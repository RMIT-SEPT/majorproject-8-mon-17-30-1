import React, { Component } from "react";

import AuthService from "../service/auth";
import {Link} from "react-router-dom";

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
						<Link to={{pathname: "/bookings", query: {whichBooking: "all"}}} className="nav-link">
							Bookings
						</Link>
					</button>
					<button id="bookingsButton">
						<Link to={{pathname: "/bookings", query: {whichBooking: "history"}}} className="nav-link">
							Booking History
						</Link>
					</button>
				</header>
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
