import React, { Component } from "react";
import Book from "../component/bookservice";

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
					<Book />
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
