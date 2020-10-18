import React, { Component } from "react";
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";
import Service from "../service/service";
import TimePicker from "react-time-picker";

const required = (value) => {
    if (!value) {
        return <div className="notification is-light is-warning">This field is required!</div>;
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
            last: true,
            errors: [],
            loading: false,
            success: false
        };
    }

    handleServiceName(event) {
        this.setState({ serviceName: event.target.value });
    }

    handleDuration(event) {
        this.setState({ duration: event });
    }

    async handleSubmit(e) {
        e.preventDefault();

        this.setState({
            errors: [],
            loading: true,
            success: false
        });

        this.form.validateAll();

        if (this.checkBtn.context._errors.length === 0) {
            var time = this.state.duration.split(":");
            var minutes = +time[0] * 60 + +time[1];
            await Service.createService(this.props.businessId, this.state.serviceName, minutes).then((error) => {
                const resMessage =
                    (error.response && error.response.data && error.response.data.message) ||
                    error.message ||
                    error.toString();

                this.setState({
                    loading: false,
                    message: resMessage,
                    success: true,
                    serviceName: "",
                    duration: undefined
                });
            });

            this.props.callback(this.state.last);
        } else {
            this.setState({
                loading: false,
            });
        }
    }

    render() {
        return (
            <div className="card events-card">
                <header className="card-header">
                    <h4 class="card-header-title is-4">Add new service</h4>
                </header>
                <div class="card-content">
                    <div class="content">
                        <Form
                            onSubmit={this.handleSubmit}
                            ref={(c) => {
                                this.form = c;
                            }}
                        >
                            <div className="field">
                                <label class="label">Service name</label>
                                <div className="control">
                                    <Input
                                        type="text"
                                        className="input"
                                        value={this.state.serviceName}
                                        onChange={this.handleServiceName}
                                        name="username"
                                        validations={[required]}
                                        placeholder="Enter your new service name"
                                    />
                                </div>
                            </div>

                            <div className="field">
                                <label class="label">Duration</label>
                                <div className="control">
                                    <TimePicker
                                        onChange={this.handleDuration}
                                        value={this.state.duration}
                                        required
                                        disableClock={true}
                                        hourPlaceholder={"hh"}
                                        minutePlaceholder={"mm"}
                                        format="hh:m"
                                    />
                                </div>
                            </div>

                            <div class="field is-grouped">
                                <div class="control">
                                    <button
                                        className={`button is-link ${this.state.loading ? "is-loading" : ""}`}
                                        disabled={this.state.loading}
                                    >
                                        <span>Create new service</span>
                                    </button>
                                </div>
                            </div>
                            {this.state.errors.map((errorMessage) => {
                                return <div class="notification is-light is-danger">{errorMessage}</div>;
                            })}
                            {this.state.success && <div class="notification is-light is-success">Your service was successfully created</div>}
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
}
