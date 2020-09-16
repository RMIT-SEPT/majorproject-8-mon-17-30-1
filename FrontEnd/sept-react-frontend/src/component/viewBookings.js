import React, { Component } from "react";
import ReactTable from "react-table-6";
import "react-table-6/react-table.css";

import BookingService from "../service/booking"

export default class viewBookings extends Component {
    constructor(props) {
        super(props);
        //let data = BookingService.viewBooking("John_Smith");
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

    render() {
        const data = [{

        },{

        }]
        const columns = [{
            Header: 'Service',
            accessor: 'sName'
        }, {
            Header: 'Worker',
            accessor: 'wName'
        }, {
            Header: 'Time',
            accessor: 'time'
        }]

        return (
            <div className="container">
                <header className="jumbotron">
                    <h3>Bookings</h3>

                </header>
                <ReactTable
                    data={data}
                    columns={columns}
                    defaultPageSize = {2}
                />
            </div>



        );
    }
}