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
import NewWorkerAvailability from "./newAvailability";
import AuthService from "../service/auth";
import {Link} from "react-router-dom";

export default class ViewWorkers extends Component {
  constructor(props) {
    super(props);
    this.state = {
      workers: [],
      currentUser: undefined,
      workerId: ""
    };
  }

  async componentDidMount() {
    const user = AuthService.getCurrentUser();
    if (user) {
      this.setState({
        currentUser: user,
      });
      await this.updateWorkerList()

    }
  }

  async updateWorkerList() {
    await WorkerService.viewWorkers().then((data) => {
      this.handleResponse(data);
    });
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
          NotificationManager.info("Worker Deleted");
          this.updateWorkerList()
        },
        (error) => {
          NotificationManager.error("Failed to delete");
        }
      );
    };
  };

  handleAvailability = (workerId) => {
    return () => {
      this.setState({workerId: workerId});
    }
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
            <button
                className="btn btn-success"
                onClick={this.handleAvailability(cell.original.id)}
            >
              Add Availability
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
            <ReactTable data={data} columns={columns} defaultPageSize={5} />
            <div>
              <hr />
              <NotificationContainer />
              <NewWorkerAvailability key={this.state.workerId} workerId={this.state.workerId}/>
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
