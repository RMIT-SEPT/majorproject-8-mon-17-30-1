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
            bookings: [],
            service: [],
            worker: []
        }
    }

    async componentDidMount() {
        await BookingService.viewBooking("John_Smith").then(data => {
            this.handleResponse(data);
        })
    }

    handleResponse(data) {
        const bookings = this.formatServices(data);
        console.log(data);
        this.setState({bookings})
    }

    /*	mapData() {
            this.state.bookings.map(booking => {
                this.setState({service: booking.serviceName});
                this.setState({worker: booking.workerFullName});
                console.log(booking.workerFullName);
                console.log(booking.date);
            })
        }*/

    formatServices(bookings) {
        return bookings.map(booking => {
            return {sName: booking.serviceName, wName: booking.workerFullName, date: new Date(booking.date).toString(), bookingId: booking.bookingId}
        })
    }


    //onclick button has to be configured to enter selected booking id, must change

    onClick = (bookingId) => {
        return () => {

            var result = "";
            Service.cancelBooking(bookingId).then(
                () => {
                    NotificationManager.info("Booking Cancelled");
                    console.log('Booking Cancelled')
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
        const data = this.state.bookings;
        const columns = [
            {
                Header: "Date",
                accessor: "date",
            },
            {
                Header: "Service Name",
                accessor: "sName",
            },
            {
                Header: "Worker Name",
                accessor: "wName"
            },
            {
                Header: "",
                accessor: "bookingId",
                Cell: ({bookingId}) =>
                    (<div><button
                className="btn btn-danger"
                onClick={this.onClick(bookingId)}
                >Cancel Booking</button>
            <hr />
            <NotificationContainer />
            </div>)
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
