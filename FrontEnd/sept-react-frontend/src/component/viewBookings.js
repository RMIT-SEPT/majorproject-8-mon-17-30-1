import React, { Component } from "react";
import ReactTable from "react-table-6";
import "react-table-6/react-table.css";
import {
    NotificationContainer,
    NotificationManager,
} from "react-notifications";
import Service from "../service/service";
import AuthService from "../service/auth";

import BookingService from "../service/booking";
import NewWorker from "./newWorker";

export default class viewBookings extends Component {
    constructor(props) {
        super(props);
        this.state = {
            bookings: [],
            whichBooking: this.getQueryString("view"),
            whichBookingMessage: "",
            currentUser: undefined
        };
    }

    getQueryString(field, url) {
        let href = url ? url : window.location.href;
        let reg = new RegExp("[?&]" + field + "=([^&#]*)", "i");
        let string = reg.exec(href);
        return string ? string[1] : null;
    }

    async componentDidMount() {
        const user = AuthService.getCurrentUser();

        if (user) {
            await this.setState({
                currentUser: user,
            });
            await this.updateBookingList();
        }
        
    }

    async updateBookingList() {
        const { currentUser } = this.state;

        switch (this.state.whichBooking) {
            case "all":
                await BookingService.viewBooking(currentUser.username).then((data) => {
                    this.handleResponse(data);
                });
                this.setState({
                    whichBookingMessage: "All upcoming bookings",
                });
                break;
            case "history":
                await BookingService.viewBookingHistory(currentUser.username).then(
                    (data) => {
                        this.handleResponse(data);
                    }
                );
                this.setState({
                    whichBookingMessage: "Previous and cancelled bookings",
                });
                break;
            case "allhistory":
                await BookingService.viewAllBookingHistory().then((data) => {
                    this.handleResponse(data);
                });
                this.setState({
                    whichBookingMessage: "Full booking history",
                });
                break;
        }
    }

    handleResponse(data) {
        const bookings = this.formatBooking(data);
        console.log(bookings);
        this.setState({ bookings });
    }

    formatBooking(bookings) {
        return bookings.map((booking) => {
            return {
                sName: booking.serviceName,
                wName: booking.workerFullName,
                date: new Date(booking.date).toString(),
                bookingId: booking.bookingId.toString(),
            };
        });
    }

    handleCancel = (bookingId) => {
        return () => {
            Service.cancelBooking(bookingId).then(
                () => {
                    NotificationManager.info("Booking Cancelled");
                    this.updateBookingList();
                    //                  window.location.reload(false);
                },
                (error) => {
                    NotificationManager.error("Failed to cancel");
                }
            );
        };
    };

    render() {
        const { currentUser } = this.state;
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
                accessor: "wName",
            },
            {
                Header: "",
                accessor: "bookingId",
                Cell: (cell) =>
                    this.state.whichBooking === "all" && (
                        <div>
                            <button
                                className="btn btn-danger"
                                onClick={this.handleCancel(
                                    cell.original.bookingId
                                )}
                            >
                                Cancel Booking
                            </button>
                        </div>
                    ),
            },
        ];

        return (
            <div className="container">
                <h3 class="title is-3">Bookings</h3>
                {currentUser ? (
                    <div>
                        <header>
                            <h4>{this.state.whichBookingMessage}</h4>
                        </header>
                        <ReactTable
                            data={data}
                            columns={columns}
                            defaultPageSize={5}
                        />
                        <div>
                            <hr />
                            <NotificationContainer />
                        </div>
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
