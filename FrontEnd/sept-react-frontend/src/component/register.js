import React, { Component } from "react";
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";

export default class Register extends Component {
  constructor(props) {
    super(props);
    this.handleRoleChange = this.handleRoleChange.bind(this);
    this.handleRegister = this.handleRegister.bind(this);

    this.state = {
      username: "",
      password: "",
      firstName: "",
      lastName: "",
      loading: false,
      message: "",
      role: "CUSTOMER", // default option
      CUSTOMER: {
        streetAddress,
        city,
        state,
        postcode,
      },
      WORKER: {},
      ADMIN: {
        businessName,
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
      loading: true,
    });

    this.form.validateAll();

    if (this.checkBtn.context._errors.length === 0) {
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
    } else {
      this.setState({
        loading: false,
      });
    }
  }

  render() {
    return (
      <div className="col-md-12">
        <header className="jumbotron">
          <h3>Register</h3>
        </header>

        <div className="card card-container">
          <Input
            type="select"
            name="role"
            onChange={this.handleChange}
            label="Multiple Select"
            multiple
          >
            <option value="CUSTOMER">Customer</option>
            <option value="WORKER">Worker</option>
            <option value="ADMIN">Admin</option>
          </Input>
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
                value={this.state.firstName}
                onChange={this.handleChange}
                validations={[required]}
              />
            </div>

            {this.state.role === "CUSTOMER" && (
                <div className="form-group">
                  <label htmlFor="streetAddress">Street address</label>
                  <Input
                    type="text"
                    className="form-control"
                    name="streetAddress"
                    value={this.state.CUSTOMER.streetAddress}
                    onChange={this.handleChange}
                    validations={[required]}
                  />
                </div>
              ) && (
                <div className="form-group">
                  <label htmlFor="city">City</label>
                  <Input
                    type="text"
                    className="form-control"
                    name="city"
                    value={this.state.CUSTOMER.city}
                    onChange={this.handleChange}
                    validations={[required]}
                  />
                </div>
              ) && (
                <div className="form-group">
                  <label htmlFor="state">State</label>
                  <Input
                    type="select"
                    name="state"
                    onChange={this.handleChange}
                    label="Multiple Select"
                    multiple
                  >
                    <option value="VIC">VIC</option>
                    <option value="QLD">QLD</option>
                    <option value="NSW">NSW</option>
                    <option value="TAS">TAS</option>
                    <option value="SA">SA</option>
                    <option value="WA">WA</option>
                    <option value="ACT">ACT</option>
                    <option value="NT">NT</option>
                  </Input>
                </div>
              ) && (
                <div className="form-group">
                  <label htmlFor="postcode">Postcode</label>
                  <Input
                    type="text"
                    className="form-control"
                    name="postcode"
                    value={this.state.CUSTOMER.postcode}
                    onChange={this.handleChange}
                    validations={[required]}
                  />
                </div>
              )}

            {
              this.state.role === "WORKER"
              // Nothing extra for worker atm
            }

            {this.state.role === "ADMIN" && (
              <div className="form-group">
                <label htmlFor="businessName">Business name</label>
                <Input
                  type="text"
                  className="form-control"
                  name="businessName"
                  value={this.state.ADMIN.businessName}
                  onChange={this.handleChange}
                  validations={[required]}
                />
              </div>
            )}

            <div className="form-group">
              <button
                className="btn btn-primary btn-block"
                disabled={this.state.loading}
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
