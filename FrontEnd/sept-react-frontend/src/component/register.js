import React, { Component, Fragment } from "react";
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";

import Auth from "../service/auth";

const required = (value) => {
  if (!value) {
    return (
      <div className="alert alert-danger" role="alert">
        This field is required!
      </div>
    );
  }
};

export default class Register extends Component {
  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
    this.handleCustomerChange = this.handleCustomerChange.bind(this);
    this.handleAdminChange = this.handleAdminChange.bind(this);
    this.handleWorkerChange = this.handleWorkerChange.bind(this);
    this.handleRegister = this.handleRegister.bind(this);

    this.state = {
      username: "",
      password: "",
      firstName: "",
      lastName: "",
      loading: false,
      message: "",
      role: "CUSTOMER",
      CUSTOMER: {
        role: "CUSTOMER",
        streetAddress: "",
        city: "",
        state: "VIC",
        postcode: "",
      },
      WORKER: {
        role: "WORKER",
      },
      ADMIN: {
        role: "ADMIN",
        businessName: "",
      },
    };
  }

  handleChange(e) {
    this.setState({
      [e.target.name]: e.target.value,
    });
  }

  handleCustomerChange(e) {
    this.setState({
      CUSTOMER: {
        ...this.state.CUSTOMER,
        [e.target.name]: e.target.value,
      },
    });
  }

  handleAdminChange(e) {
    this.setState({
      ADMIN: {
        ...this.state.ADMIN,
        [e.target.name]: e.target.value,
      },
    });
  }

  handleWorkerChange(e) {
    this.setState({
      WORKER: {
        ...this.state.WORKER,
        [e.target.name]: e.target.value,
      },
    });
  }

  handleRegister(e) {
    e.preventDefault();

    this.setState({
      message: "",
      loading: true
    });

    this.form.validateAll();

    // if (this.checkBtn.context._errors.length === 0) {
      console.log(this.state);
      Auth.register(
        this.state.username,
        this.state.password,
        this.state.firstName,
        this.state.lastName,
        this.state[this.state.role]
      ).then(
        () => {
          this.props.history.push("/dashboard");
          window.location.reload();
        },
        (error) => {
          const resMessage =
            (error.response &&
              error.response.data &&
              error.response.data.message) ||
            error.message ||
            error.toString();

          this.setState({
            loading: false,
            message: resMessage,
          });
        }
      );
    // } else {
    //   this.setState({
    //     loading: false,
    //   });
    // }
  }

  render() {
    return (
      <div className="col-md-12">
        <header className="jumbotron">
          <h3>Register</h3>
        </header>

        <div className="card card-container">
          <select
            name="role"
            value={this.state.role}
            onChange={this.handleChange}
            label="Select"
          >
            <option value="CUSTOMER">Customer</option>
            <option value="WORKER">Worker</option>
            <option value="ADMIN">Admin</option>
          </select>
          <Form
            onSubmit={this.handleRegister}
            ref={(c) => {
              this.form = c;
            }}
          >
            <div className="form-group">
              <label htmlFor="username">Username</label>
              <Input
                type="text"
                className="form-control"
                name="username"
                value={this.state.username}
                onChange={this.handleChange}
                validations={[required]}
              />
            </div>

            <div className="form-group">
              <label htmlFor="password">Password</label>
              <Input
                type="password"
                className="form-control"
                name="password"
                value={this.state.password}
                onChange={this.handleChange}
                validations={[required]}
              />
            </div>

            <div className="form-group">
              <label htmlFor="firstName">First name</label>
              <Input
                type="text"
                className="form-control"
                name="firstName"
                value={this.state.firstName}
                onChange={this.handleChange}
                validations={[required]}
              />
            </div>

            <div className="form-group">
              <label htmlFor="lastName">Last name</label>
              <Input
                type="text"
                className="form-control"
                name="lastName"
                value={this.state.lastName}
                onChange={this.handleChange}
                validations={[required]}
              />
            </div>

            {this.state.role === "CUSTOMER" && (
              <Fragment>
                <div className="form-group">
                  <label htmlFor="streetAddress">Street address</label>
                  <Input
                    type="text"
                    className="form-control"
                    name="streetAddress"
                    value={this.state.CUSTOMER.streetAddress}
                    onChange={this.handleCustomerChange}
                    validations={[required]}
                  />
                </div>
                <div className="form-group">
                  <label htmlFor="city">City</label>
                  <Input
                    type="text"
                    className="form-control"
                    name="city"
                    value={this.state.CUSTOMER.city}
                    onChange={this.handleCustomerChange}
                    validations={[required]}
                  />
                </div>
                <div className="form-group">
                  <label htmlFor="state">State</label>
                  <select
                    name="state"
                    onChange={this.handleCustomerChange}
                    label="Select"
                    value={this.state.CUSTOMER.state}
                  >
                    <option value="VIC">VIC</option>
                    <option value="QLD">QLD</option>
                    <option value="NSW">NSW</option>
                    <option value="TAS">TAS</option>
                    <option value="SA">SA</option>
                    <option value="WA">WA</option>
                    <option value="ACT">ACT</option>
                    <option value="NT">NT</option>
                  </select>
                </div>
                <div className="form-group">
                  <label htmlFor="postcode">Postcode</label>
                  <Input
                    type="text"
                    className="form-control"
                    name="postcode"
                    value={this.state.CUSTOMER.postcode}
                    onChange={this.handleCustomerChange}
                    validations={[required]}
                  />
                </div>
              </Fragment>
            )}

            {
              this.state.role === "WORKER"
              // Nothing extra for worker atm
            }

            {this.state.role === "ADMIN" && (
              <Fragment>
                <div className="form-group">
                  <label htmlFor="businessName">Business name</label>
                  <Input
                    type="text"
                    className="form-control"
                    name="businessName"
                    value={this.state.ADMIN.businessName}
                    onChange={this.handleAdminChange}
                    validations={[required]}
                  />
                </div>
              </Fragment>
            )}

            <div className="form-group">
              <button
                className="btn btn-primary btn-block"
                disabled={this.state.loading}
                type="submit"
              >
                {this.state.loading && (
                  <span className="spinner-border spinner-border-sm"></span>
                )}
                <span>Register</span>
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
    );
  }
}
