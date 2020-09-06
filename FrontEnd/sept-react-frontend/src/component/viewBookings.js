import React, { Component } from "react";
import ReactTable from "react-table-6";
import "react-table-6/react-table.css";

export default class viewBookings extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        const data = [{
            fName: 'John',
            lName: 'Smith'
        },{
            fName: 'Lucas',
            lName: 'Mellor'
        }]
        const columns = [{
            Header: 'FirstName',
            accessor: 'fName'
        },{
            Header: 'LastName',
            accessor: "lName"
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