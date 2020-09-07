import React, {Component} from "react";
import Form from "react-validation/build/form";
import CheckButton from "react-validation/build/button";
import DateList from "../component/datelist";
import BusinessList from "../component/businesslist";
import ServiceList from "../component/servicelist";
import WorkerList from "../component/workerlist";
import Service from "../service/service"

export default class BookService extends Component {
    // List component refs
    businessListRef = React.createRef();
    serviceListRef = React.createRef();
    workerListRef = React.createRef();
    dateListRef = React.createRef();

    constructor(props) {
        super(props);
        this.handleBooking = this.handleBooking.bind(this);

        this.state = {
            businessID: "",
            serviceID: "",
            workerID: "",
            dateTime: "",
            loading: false,
            message: ""
        };
    }

    handleBooking(e) {
        e.preventDefault();

        const businessList = this.businessListRef.current;
        const serviceList = this.serviceListRef.current;
        const workerList = this.workerListRef.current;
        const dateList = this.dateListRef.current;

        this.setState({
            message: "",
            loading: true,
            businessID: businessList.state.selected,
            serviceID: serviceList.state.selected,
            workerID: workerList.state.selected,
            dateTime: dateList.state.selected
        });

        this.form.validateAll();

        if (this.checkBtn.context._errors.length === 0) {
            Service.bookService(this.state.serviceID, this.state.workerID, this.state.dateTime).then(
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
            <div className="card-container">
                <div className="col-md-12">
                    <div className="card col-md-12">
                        <Form
                            onSubmit={this.handleBooking}
                            ref={c => {
                                this.form = c;
                            }}
                        >
                            <h3>Book a Service.</h3>

                            <div className="form-group">
                                <BusinessList ref={this.businessListRef}/>
                            </div>

                            <div className="form-group">
                                <ServiceList ref={this.serviceListRef}/>
                            </div>

                            <div className="form-group">
                                <WorkerList ref={this.workerListRef}/>
                            </div>

                            <div className="form-group">
                                <DateList ref={this.dateListRef}/>
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
