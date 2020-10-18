import React, { Component } from "react";
import Form from "react-validation/build/form";
import CheckButton from "react-validation/build/button";
import Workers from "../service/workers";
import Select from "react-select";
import TimePicker from "./timePicker";
import DateCalendar from "./dateCalendar";
import Service from "../service/service";

export default class NewWorkerAvailability extends Component {
    timePickerStartRef = React.createRef();
    timePickerEndRef = React.createRef();
    datePickerRef = React.createRef();

    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.onChangeService = this.onChangeService.bind(this);
        this.onChangeDay = this.onChangeDay.bind(this);

        this.state = {
            service: "",
            day: "",
            startTime: "",
            endTime: "",
            effectiveEndDate: "",
            serviceOptions: [],
            workerId: this.props.workerId,
            days: [
                {
                    value: "MONDAY",
                    label: "Mon",
                },
                {
                    value: "TUESDAY",
                    label: "Tue",
                },
                {
                    value: "WEDNESDAY",
                    label: "Wed",
                },
                {
                    value: "THURSDAY",
                    label: "Thu",
                },
                {
                    value: "FRIDAY",
                    label: "Fri",
                },
                {
                    value: "SATURDAY",
                    label: "Sat",
                },
                {
                    value: "SUNDAY",
                    label: "Sun",
                },
            ],
        };
    }

    componentDidMount() {
        console.log("workerId: " + this.state.workerId);
        this.populateServiceList(this.state.workerId);
    }

    onChangeService(serviceIn) {
        const service = serviceIn.value;
        this.setState({ service });
    }

    onChangeDay(dayIn) {
        const day = dayIn.value;
        this.setState({ day });
    }

    onChangeEffectiveDateEnd = (event) => {
        this.setState({ effectiveEndDate: event.target.value });
    };

    populateServiceList(workerId) {
        Service.getServicesByWorkerId(workerId).then((response) => {
            const serviceOptions = this.formatServices(response.data);
            this.setState({ serviceOptions });
        });
    }

    formatServices(services) {
        return services.map((service) => {
            return { value: service.serviceId, label: service.serviceName };
        });
    }

    handleViewWorker(workerId) {
        this.setState({
            workerId,
        });
    }

    handleSubmit(e) {
        e.preventDefault();
        const timePickerStart = this.timePickerStartRef.current;
        const timePickerEnd = this.timePickerEndRef.current;
        const datePicker = this.datePickerRef.current;
        // this.form.validateAll();

        if (this.checkBtn.context._errors.length === 0) {
            //workerId, serviceId, day, startTime, endTime, effectiveEndDate
            Workers.newAvailability(
                this.state.workerId,
                this.state.service,
                this.state.day,
                timePickerStart.state.startDate.toLocaleTimeString(),
                timePickerEnd.state.startDate.toLocaleTimeString(),
                datePicker.state.startDate
            ).then(
                () => {
                    
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
            <div className="card events-card">
                <header className="card-header">
                    <h4 class="card-header-title is-4">
                        Add worker availability
                    </h4>
                </header>
                <div key={this.props.bar}>{this.props.bar}</div>
                <Form
                    onSubmit={this.handleSubmit}
                    ref={(c) => {
                        this.form = c;
                    }}
                >
                    <div class="card-content">
                        <div class="content">
                            <div className="field">
                                <label class="label">Service</label>
                                <div className="control">
                                    <Select
                                        name="business-name"
                                        options={this.state.serviceOptions}
                                        onChange={this.onChangeService}
                                        // validations={[required]}
                                    />
                                </div>
                            </div>

                            <div className="field">
                                <label class="label">Day</label>
                                <div className="control">
                                    <Select
                                        name="day"
                                        options={this.state.days}
                                        onChange={this.onChangeDay}
                                        // validations={[required]}
                                    />
                                </div>
                            </div>

                            <div className="field">
                                <label class="label">Start time</label>
                                <div className="control">
                                    <TimePicker ref={this.timePickerStartRef} />
                                </div>
                            </div>

                            <div className="field">
                                <label class="label">End time</label>
                                <div className="control">
                                    <TimePicker ref={this.timePickerEndRef} />
                                </div>
                            </div>

                            <div className="field">
                                <label class="label">Effective end date</label>
                                <div className="control">
                                    <DateCalendar ref={this.datePickerRef} />
                                </div>
                            </div>

                            <div class="field is-grouped">
                                <div class="control">
                                    <button
                                        className="button is-link"
                                        disabled={this.state.loading}
                                    >
                                        {this.state.loading && (
                                            <span className="spinner-border spinner-border-sm" />
                                        )}
                                        <span>Create new availability</span>
                                    </button>
                                </div>
                            </div>

                            {this.state.message && (
                                <div className="form-group">
                                    <div
                                        className="alert alert-danger"
                                        role="alert"
                                    >
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
                        </div>
                    </div>
                </Form>
            </div>
        );
    }
}
