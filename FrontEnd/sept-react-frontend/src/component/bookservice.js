import React, {Component} from "react";
import Form from "react-validation/build/form";
import CheckButton from "react-validation/build/button";
import Select from "react-validation/build/select";

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

export default class BookService extends Component {
    constructor(props) {
        super(props);
        this.handleBooking = this.handleBooking.bind(this);
        this.onChangeService = this.onChangeService.bind(this);
        this.onChangeDate = this.onChangeDate.bind(this);
        this.onChangeTime = this.onChangeTime.bind(this);

        this.state = {
            service: "",
            date: "",
            time: "",
            loading: false,
            message: ""
        };
    }

    onChangeTime(e) {
        this.setState({
            time: e.target.value
        });
    }

    onChangeService(e) {
        this.setState({
            service: e.target.value
        });
    }

    onChangeDate(e) {
        this.setState({
            date: e.target.value
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
            Service.bookService(this.state.service, this.state.date, this.state.time).then(
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
                                        value={this.state.service}
                                        onChange={this.onChangeService}
                                        validations={[required]}
                                >
                                    <option value=''>Choose a Service</option>
                                    <option value='1'>Haircut</option>
                                    <option value='2'>Hair dye</option>
                                    <option value='3'>Buzzcut</option>
                                </Select>
                            </div>

                            <div className="form-group">
                                <label htmlFor="date">Date</label>
                                <Select name='date'
                                        value={this.state.date}
                                        onChange={this.onChangeDate}
                                        validations={[required]}
                                >
                                    <option value=''>Choose an Available Date</option>
                                    <option value='1'>Date 1</option>
                                    <option value='2'>Date 2</option>
                                    <option value='3'>Date 3</option>
                                </Select>
                            </div>

                            <div className="form-group">
                                <label htmlFor="time">Time</label>
                                <Select name='time'
                                        value={this.state.time}
                                        onChange={this.onChangeTime}
                                        validations={[required]}
                                >
                                    <option value=''>Choose an Available Time</option>
                                    <option value='1'>Time 1</option>
                                    <option value='2'>Time 2</option>
                                    <option value='3'>Time 3</option>
                                </Select>
                            </div>

                            <div className="form-group">
                                <button
                                    className="btn btn-primary btn-block"
                                    disabled={this.state.loading}
                                >
                                    {this.state.loading && (
                                        <span className="spinner-border spinner-border-sm"></span>
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
