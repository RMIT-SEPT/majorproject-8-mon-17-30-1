import React, { Component } from "react";
import ReactTable from "react-table-6";
import "react-table-6/react-table.css";
import {
  NotificationContainer,
  NotificationManager,
} from "react-notifications";
import Service from "../service/service";

import WorkerService from "../service/workers";
import NewWorker from "./newWorker";
import AuthService from "../service/auth";

export default class ViewWorkers extends Component {
  constructor(props) {
    super(props);
    this.state = {
      workers: [],
      currentUser: undefined,
    };
  }

  async componentDidMount() {
    const user = AuthService.getCurrentUser();
    if (user) {
      this.setState({
        currentUser: user,
      });

      await WorkerService.viewWorkers().then((data) => {
        this.handleResponse(data);
      });
    }
  }

  handleResponse(data) {
    const workers = this.formatBooking(data);
    console.log(workers);
    this.setState({ workers });
  }

  formatBooking(workers) {
    return workers.map((workers) => {
      return {
        id: workers.workerId,
        fName: workers.firstName,
        lName: workers.lastName,
      };
    });
  }

  handleDelete = (workerId) => {
    return () => {
      var result = "";
      WorkerService.deleteWorker(workerId).then(
        () => {
          NotificationManager.info("Booking Cancelled");
          console.log("Booking Cancelled");
        },
        (error) => {
          NotificationManager.error("Failed to cancel");
        }
      );
    };
  };

  render() {
    const data = this.state.workers;
    const columns = [
      {
        Header: "Worker ID",
        accessor: "id",
      },
      {
        Header: "First Name",
        accessor: "fName",
      },
      {
        Header: "Last Name",
        accessor: "lName",
      },
      {
        Header: "Edit",
        accessor: "bookingId",
        Cell: (cell) => (
          <div>
            <button
              className="btn btn-primary"
              // onClick={this.handleCancel(cell.original.bookingId)}
            >
              Edit
            </button>
            <button
              className="btn btn-danger"
              onClick={this.handleDelete(cell.original.id)}
            >
              Delete
            </button>
          </div>
        ),
      },
    ];

    var viewToShow;

    if (this.state.currentUser) {
      if (this.state.currentUser.role === "ADMIN") {
        viewToShow = (
          <div>
            <div>
              <h3>Create a new worker</h3>
              <NewWorker />
            </div>
            <ReactTable data={data} columns={columns} defaultPageSize={2} />
            <div>
              <hr />
              <NotificationContainer />
            </div>
          </div>
        );
      } else {
        viewToShow = (
          <div>
            <h5>You are not logged in as an administrator!</h5>
          </div>
        );
      }
    } else {
      viewToShow = (
        <div>
          <h5>You are not logged in!</h5>
        </div>
      );
    }

    return (
      <div className="container">
        <header className="jumbotron">
          <h3>Workers</h3>
        </header>
        {viewToShow}
      </div>
    );
  }
}
