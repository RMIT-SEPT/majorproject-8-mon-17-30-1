import React, { Component } from "react";
import ReactTable from "react-table-6";
import "react-table-6/react-table.css";
import {
	NotificationContainer,
	NotificationManager,
} from "react-notifications";
import Service from "../service/service";

import BookingService from "../service/booking"

export default class viewBookings extends Component {
	constructor(props) {
		super(props);
		this.state = {
			bookings: []
		}
	}

	async componentDidMount() {
		await BookingService.viewBooking("John_Smith").then(response => {
			const bookings = response.data;
			this.setState({bookings});
			console.log(bookings);
		})
	}



	//onclick button has to be configured to enter selected booking id, must change

	onClick = (bookingId) => {
		return () => {

			var result = "";
			Service.cancelBooking(bookingId).then(
				() => {
					NotificationManager.info("Booking Cancelled");
				},
				error => {
					NotificationManager.error("Failed to cancel");
				}
			);

			// switch (result) {
			// 	case "success":
			// 		NotificationManager.info("Booking Cancelled");
			// 		break;
			// 	case "success1":
			// 		NotificationManager.success("Success message", "Title here");
			// 		break;
			// 	case "error":
			// 		NotificationManager.error("Didnt work", "Click me!", 5000, () => {
			// 			alert("callback");
			// 		});
			// 		break;
			// }
		};
	};

	render() {
		const data = [
			{
				fName: "John",
                lName: "Smith",
                bookId: "1"
			},
			{
				fName: "Lucas",
                lName: "Mellor",
                bookId: "2"
			},
		];
		const columns = [
			{
				Header: "FirstName",
				accessor: "fName",
			},
			{
				Header: "LastName",
				accessor: "lName",
            },
            {
                Header: "Booking ID",
                accessor: "bookId"
            }
		];

		return (
			<div className="container">
				<header className="jumbotron">
					<h3>Bookings</h3>
				</header>
				<ReactTable data={data} columns={columns} defaultPageSize={2} />
				<div>
					<button
						className="btn btn-danger"
						onClick={this.onClick("0")}
					>
						Cancel Booking
					</button>
					<hr />
					<NotificationContainer />
				</div>
			</div>
		);
	}
}
