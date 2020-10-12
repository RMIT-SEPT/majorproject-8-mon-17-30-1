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
import Form from "react-validation/build/form";
import CheckButton from "react-validation/build/button";

export default class ViewWorkers extends Component {
  constructor(props) {
    super(props);
    this.state = {
      workers: [],
      currentUser: undefined,
      editUsername: '',
      editFName: '',
      editLName: '',
      editId: '',
      editPassword: '',
    };
  }

  onChangePassword = (event) => {
    this.setState({ editPassword: event.target.value });
  };

  onChangeFirstName = (event) => {
    this.setState({ editFName: event.target.value });
  };

  onChangeLastName = (event) => {
    this.setState({ editLName: event.target.value });
  };

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
      console.log(data);
      this.handleResponse(data);
    });
  }

  handleResponse(data) {
    const workers = this.formatBooking(data);
    console.log(workers);
    this.setState({ workers });
  }

  formatBooking(workers) {
    return workers.map((worker) => {
      console.log(worker);
      return {
        id: worker.workerId,
        userName: worker.userName,
        fName: worker.firstName,
        lName: worker.lastName,

      };
    });
  }

  handleDelete = (workerId) => {
    return () => {
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

  updateEditDetails(worker) {
    this.setState({
      editUsername: worker.userName,
      editFName: worker.fName,
      editLName: worker.lName,
      editId: worker.id,
    });
  }

  handleEdit = () => {
      return WorkerService.editWorker(
          this.state.editUsername,
          this.state.editPassword,
          this.state.editFName,
          this.state.editLName,
          this.state.editId,
          ).then(
          () => {
            NotificationManager.info("Worker Edited");
          },
          (error) => {
            NotificationManager.error("Failed to edit");
          }
      );
  };

  render() {
    const data = this.state.workers;
    const columns = [
      {
        Header: "Worker ID",
        accessor: "id",
      },
      {
        Header: "Username",
        accessor: "userName",
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
               onClick={() => this.updateEditDetails(cell.original)}
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
    var showEdit;

    if (this.state.currentUser) {
      if (this.state.currentUser.role === "ADMIN") {
        viewToShow = (
          <div>
            <div>
              <h3>Create a new worker</h3>
              <NewWorker callback={i => this.updateWorkerList() && console.log(i)}/>
            </div>
            <ReactTable data={data} columns={columns} defaultPageSize={5} />
            <div>
              <hr />
              <NotificationContainer />
            </div>
            <div>

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

    if (this.state.editId != '') {
      showEdit = (
          <div className="container">
            <header className="jumbotron">
              <h3>Edit Worker</h3>
            </header>
            <div className="card-container">
              <div className="card col-md-12">
                <Form
                    onSubmit={this.handleEdit}
                    ref={(c) => {
                      this.form = c;
                    }}
                >
                  <div className="formGroup">
                    <h6>Password</h6>
                    <input
                        type="text"
                        value={this.state.editPassword}
                        onChange={this.onChangePassword}
                        name="username"
                    />
                  </div>

                  <div className="formGroup">
                    <h6>First Name</h6>
                    <input
                        type="text"
                        value={this.state.editFName}
                        onChange={this.onChangeFirstName}
                        name="fName"
                    />
                  </div>

                  <div className="formGroup">
                    <h6>Last Name</h6>
                    <input
                        type="text"
                        value={this.state.editLName}
                        onChange={this.onChangeLastName}
                        name="lName"
                    />
                  </div>

                  <div className="form-group">
                    <button
                        className="btn btn-primary btn-block">
                      <span>Update worker</span>
                    </button>
                  </div>
                  {this.state.message && (
                      <div className="form-group">
                        <div className="alert alert-danger" role="alert">
                          {this.state.message}
                        </div>
                      </div>
                  )}
                  <CheckButton
                      style={{ display: "none" }}
                      ref={(c) => {
                        this.checkBtn = c;
                      }}
                  />
                </Form>
              </div>
            </div>
          </div>
      );

    }
    else {
      showEdit = null;
    }

    return (
      <div className="container">
        <header className="jumbotron">
          <h3>Workers</h3>
        </header>
        {viewToShow}
        {showEdit}
      </div>
    );
  }
}
