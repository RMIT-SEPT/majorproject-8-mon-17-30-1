import React, {Component} from "react";
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
            workerViewDateData: {},
        };
    }

    async componentDidMount() {
        const user = AuthService.getCurrentUser();
        if (user) {
            this.setState({
                currentUser: user,
            });
            await this.updateWorkerList()
            await this.handleView(0);
            console.log(this.state.workerViewDateData);
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
        this.setState({workers});
    }

    async handleView(workerId) {
        await WorkerService.viewWorkerDateData(workerId).then( response => {
            console.log(response);
            console.log(response.availability);
            const workerViewDateData = this.formatDateData(response.availability);
            this.setState({workerViewDateData});
            console.log(this.state.workerViewDateData);
            console.log(workerViewDateData);
        });
    }

    formatDateData(workerDateData) {
        return workerDateData.map((date) => {
            return {
                day: date.day,
                startDate: date.effectiveStartDate,
                endDate: date.effectiveEndDate,
                startTime: date.startTime,
                endTime: date.endTime,
            };
        });
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
                            className="btn btn-primary"
                            // onClick={this.handleView(cell.original.id)}
                        >
                            View
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

        const workerViewData = this.state.workerViewDateData;
        const workerViewDateColumns = [
            {
                Header: "Day",
                accessor: "day",
            },
            {
                Header: "Worker Availability",
                accessor: "startTime",
            },
            {
                Header: "Worker Availability",
                accessor: "endTime",
            },
            {
                Header: "Worker Availability",
                accessor: "startDate",
            },
            {
                Header: "Worker Availability",
                accessor: "endDate",
            },
        ]

        var viewToShow;
        var workerView;

        if (this.state.currentUser) {
            if (this.state.currentUser.role === "ADMIN") {
                viewToShow = (
                    <div>
                        <div>
                            <h3>Create a new worker</h3>
                            <NewWorker/>
                        </div>
                        <ReactTable data={data} columns={columns} defaultPageSize={5}/>
                        <div>
                            <hr/>
                            <NotificationContainer/>
                        </div>
                    </div>
                );

                workerView = (
                    <div>
                        <h4>Worker Availability</h4>
                        <ReactTable data={workerViewData} columns={workerViewDateColumns} defaultPageSize={5} />
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
                {workerView}
            </div>
        );
    }
}
