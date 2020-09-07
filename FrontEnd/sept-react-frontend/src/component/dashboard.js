import React, { Component } from "react";

import AuthService from "../service/auth";
import Bookings from "../component/Bookings";
import {
	NotificationContainer,
	NotificationManager,
} from "react-notifications";
import { Button } from "reactstrap";

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

	createNotification = (type) => {
		return () => {
			switch (type) {
				case "info":
					NotificationManager.info("Info message");
					break;
				case "success":
					NotificationManager.success("Success message", "Title here");
					break;
				case "warning":
					NotificationManager.warning(
						"Warning message",
						"Close after 3000ms",
						3000
					);
					break;
				case "error":
					NotificationManager.error("Error message", "Click me!", 5000, () => {
						alert("callback");
					});
					break;
			}
		};
	};

	render() {
		const { currentUser } = this.state;

		const CancelButton = () => {
			const [showResults, setShowResults] = React.useState(false);
			const onClick = () => setShowResults(true);
			return (
				<div>
					<input type="submit" value="CancelButton" onClick={onClick} />
					{showResults ? <Results /> : null}
				</div>
			);
		};

		const Results = () => (
			<div id="results" className="search-results">
				Some Results
			</div>
		);

		return (
			<div className="container">
				<header className="jumbotron">
					<h3>Dashboard</h3>
				</header>
				{currentUser ? (
					<p>Logged in as {currentUser.role}</p>
				) : (
					<p>
						You are not logged in! You need to login <a href="/login">here</a>.
					</p>
				)}
				{/* <Bookings></Bookings> */}
				<CancelButton></CancelButton>
				<Button onClick={this.onClick}>Cancel Booking</Button>

				<div>
					<button
						className="btn btn-info"
						onClick={this.createNotification("info")}
					>
						Info
					</button>
					<hr />
					<button
						className="btn btn-success"
						onClick={this.createNotification("success")}
					>
						Success
					</button>
					<hr />
					<button
						className="btn btn-warning"
						onClick={this.createNotification("warning")}
					>
						Warning
					</button>
					<hr />
					<button
						className="btn btn-danger"
						onClick={this.createNotification("error")}
					>
						Error
					</button>

					<NotificationContainer />
				</div>

				<p></p>
			</div>
		);
	}
}
