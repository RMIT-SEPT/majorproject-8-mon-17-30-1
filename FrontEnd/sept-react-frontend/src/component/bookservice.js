import React, {Component} from "react";
import Form from "react-validation/build/form";
import CheckButton from "react-validation/build/button";
//import Select from "react-validation/build/select";
import Select from "react-select";

import Service from "../service/service"

const required = value => {
    if (!value) {
        return (
            <div className="alert alert-danger" role="alert">
                This field is required!
            </div>
        );
    }
};

const serviceOptions = [
    { value: 'service 1', label: 'Service 1' },
    { value: 'service 2', label: 'Service 2' },
]

const workerOptions = [
    { value: 'worker 1', label: 'Worker 1' },
    { value: 'worker 2', label: 'Worker 2' },
]

const dateOptions = [
    { value: 'date 1', label: 'Date 1' },
    { value: 'date 2', label: 'Date 2' },
    { value: 'date 3', label: 'Date 3' }
]

const timeOptions = [
    { value: 'time 1', label: 'Time 1' },
    { value: 'time 2', label: 'Time 2' },
    { value: 'time 3', label: 'Time 3' }
]


export default class BookService extends Component {
    constructor(props) {
        super(props);
        this.handleBooking = this.handleBooking.bind(this);
        this.onChangeService = this.onChangeService.bind(this);
        this.onChangeWorker = this.onChangeWorker.bind(this);
        this.onChangeDate = this.onChangeDate.bind(this);
        this.onChangeTime = this.onChangeTime.bind(this);

        this.state = {
            service: "",
            worker: "",
            date: "",
            time: "",
            loading: false,
            message: ""
        };
    }

    onChangeService(e) {
        this.setState({
            service: e.target.value
        });
    }

    onChangeWorker(e) {
        this.setState({
            worker: e.target.value
        });
    }

    onChangeDate(e) {
        this.setState({
            date: e.target.value
        });
    }

    onChangeTime(e) {
        this.setState({
            time: e.target.value
        });
    }

    handleBooking(e) {
        e.preventDefault();

        this.setState({
            message: "",
            loading: true
        });

        this.form.validateAll();

        if (this.checkBtn.context._errors.length === 0) {
            Service.bookService(this.state.service, this.state.worker, this.state.date, this.state.time).then(
                error => {
                    const resMessage =
                        (error.response &&
                            error.response.data &&
                            error.response.data.message) ||
                        error.message ||
                        error.toString();

                    this.setState({
                        loading: false,
                        message: resMessage
                    });
                }
            );
        } else {
            this.setState({
                loading: false
            });
        }
    }

    render() {
        return (
            <div className="container">
                <h3>Book a Service.</h3>

                <div className="col-md-12">
                    <div className="card card-container col-md-12">
                        <Form
                            onSubmit={this.handleBooking}
                            ref={c => {
                                this.form = c;
                            }}
                        >
                            <div className="form-group">
                                <label htmlFor="service">Service</label>
                                <Select name='service'
                                        options={serviceOptions}
                                        value={this.state.service}
                                        onChange={this.onChangeService}
                                        validations={[required]}
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="worker">Worker</label>
                                <Select name='worker'
                                        options={workerOptions}
                                        value={this.state.worker}
                                        onChange={this.onChangeWorker}
                                        validations={[required]}
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="date">Date</label>
                                <Select name='date'
                                        options={dateOptions}
                                        value={this.state.date}
                                        onChange={this.onChangeDate}
                                        validations={[required]}
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="time">Time</label>
                                <Select name='time'
                                        options={timeOptions}
                                        value={this.state.time}
                                        onChange={this.onChangeTime}
                                        validations={[required]}
                                />
                            </div>

                            <div className="form-group">
                                <button
                                    className="btn btn-primary btn-block"
                                    disabled={this.state.loading}
                                >
                                    {this.state.loading && (
                                        <span className="spinner-border spinner-border-sm"/>
                                    )}
                                    <span>Book Service</span>
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
                                ref={c => {
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
