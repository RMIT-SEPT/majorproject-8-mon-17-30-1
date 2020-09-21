import React, { Component } from "react";
import Form from "react-validation/build/form";
import CheckButton from "react-validation/build/button";
import Service from "../service/service";
import TimePicker from "react-time-picker";

const required = (value) => {
  if (!value) {
    return (
      <div className="alert alert-danger" role="alert">
        This field is required!
      </div>
    );
  }
};

export default class NewService extends Component {
  constructor(props) {
    super(props);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleDuration = this.handleDuration.bind(this);
    this.handleServiceName = this.handleServiceName.bind(this);
    
    this.state = {
      serviceName: "",
      duration: undefined,
    };
  }

  handleServiceName(event) {
    this.setState({ serviceName: event.target.value });
  };

  handleDuration(event) {
    this.setState({ duration: event });
  }

  handleSubmit(e) {
    e.preventDefault();

    this.form.validateAll();

    if (this.checkBtn.context._errors.length === 0) {
        var time = this.state.duration.split(':');
        var minutes = (+time[0]) * 60 + (+time[1]);
        Service.createService(
        this.props.businessId,
        this.state.serviceName,
        minutes
      ).then((error) => {
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
      });
    } else {
      this.setState({
        loading: false,
      });
    }
  }

  render() {
    return (
      <div className="card-container">
        <div className="card col-md-12">
          <Form
            onSubmit={this.handleSubmit}
            ref={(c) => {
              this.form = c;
            }}
          >
            <div className="formGroup">
              <h6>Service name</h6>
              <input
                type="text"
                onChange={this.handleServiceName}
                name="username"
                validations={[required]}
              />
            </div>

            <div className="formGroup">
              <h6>Duration</h6>
              <TimePicker
                onChange={this.handleDuration}
                value={this.state.duration}
                validations={[required]}
                disableClock={true}
                hourPlaceholder={"hh"}
                minutePlaceholder={"mm"}
              />
            </div>

            <div className="form-group">
              <button
                className="btn btn-primary btn-block"
                disabled={this.state.loading}
              >
                {this.state.loading && (
                  <span className="spinner-border spinner-border-sm" />
                )}
                <span>Create service</span>
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
