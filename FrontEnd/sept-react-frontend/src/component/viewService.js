import React, { Component } from "react";
import ReactTable from "react-table-6";
import "react-table-6/react-table.css";
import {
  NotificationContainer,
  NotificationManager,
} from "react-notifications";
import Service from "../service/service";
import NewService from "./newService";
import AuthService from "../service/auth";

export default class ViewService extends Component {
  constructor(props) {
    super(props);

    this.handleDelete = this.handleDelete.bind(this);

    this.state = {
      currentUser: undefined,
      business: undefined,
      services: [],
      rerender: true
    };
  }

  async componentDidMount() {
    const user = AuthService.getCurrentUser();

    if (user) {
      this.setState({
        currentUser: user,
      });

      if (user.role === "ADMIN") {
        Service.getBusinessByAdminUsername(user.username).then((response) => {
          this.setState({
            business: response.data,
          });

          this.updateServices();
        });
      }
    }
  }

  async updateServices() {

    await Service.getServicesByBusinessID(this.state.business.businessId).then(
        (response) => {
          this.handleResponse(response.data);
        }
      );
  }

  handleResponse(data) {
    const services = this.formatService(data);
    console.log(services);
    this.setState({ services });
  }

  formatService(services) {
    return services.map((service) => {
      const durationDate = new Date(0);
      durationDate.setMinutes(service.durationMinutes);
      const durationString = durationDate.toISOString().substr(11, 8);

      return {
        serviceId: service.serviceId,
        serviceName: service.serviceName,
        duration: durationString,
      };
    });
  }

  handleEdit = (serviceId) => {
    // return () => {
    //   Service.cancelBooking(serviceId).then(
    //     () => {
    //       NotificationManager.info("Booking Cancelled");
    //       console.log("Booking Cancelled");
    //     },
    //     (error) => {
    //       NotificationManager.error("Failed to cancel");
    //     }
    //   );
    // };
  };

  handleDelete = (serviceId) => {
    return () => {
      Service.deleteService(serviceId).then(
          () => {
            NotificationManager.info("Service Deleted");
            this.updateServices();
          },
          error => {
            NotificationManager.error("Failed to delete");
          }
      );
      
    };
  };

  render() {
    const data = this.state.services;
    const columns = [
      {
        Header: "Service Name",
        accessor: "serviceName",
        Cell: row => <div style={{ textAlign: "center" }}>{row.value}</div>
      },
      {
        Header: "Duration",
        accessor: "duration",
        Cell: row => <div style={{ textAlign: "center" }}>{row.value}</div>
      },
      {
        Header: "",
        accessor: "serviceId",
        Cell: (cell) => (
          <div style={{ textAlign: "center" }}>
            {/* <button
              className="btn btn-primary"
              onClick={this.handleEdit(cell.original.serviceId)}
            >
              Edit
            </button> */}
            <button
              className="btn btn-danger"
              onClick={this.handleDelete(cell.original.serviceId)}
            >
              Delete
            </button>
          </div>
        ),
      },
    ];

    var viewToShow;

    if (this.state.currentUser) {
      if (this.state.business) {
        viewToShow = (
          <div>
            <div>
              <h3>Create a new service</h3>
              <NewService businessId={this.state.business.businessId} callback={i => this.updateServices() && console.log(i)}/>
            </div>
            <header>
              <h3>Viewing services for {this.state.business.businessName}</h3>
            </header>
            <ReactTable data={data} columns={columns} defaultPageSize={5} />
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
          <h3>Services</h3>
        </header>
        {viewToShow}
      </div>
    );
  }
}
