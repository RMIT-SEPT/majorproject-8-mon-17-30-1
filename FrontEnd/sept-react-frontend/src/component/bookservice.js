import React, {Component} from "react";
import Form from "react-validation/build/form";
import CheckButton from "react-validation/build/button";
import DateList from "../component/datelist";
import Service from "../service/service";
import Select from "react-select";

let businessOptions;
let serviceOptions;
let workerOptions;

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
    // List component refs
    dateListRef = React.createRef();

    constructor(props) {
        super(props);
        this.onChangeBusiness = this.onChangeBusiness.bind(this);
        this.onChangeService = this.onChangeService.bind(this);
        this.onChangeWorker = this.onChangeWorker.bind(this);

        this.populateBusinessList();

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

    onChangeBusiness = (businessID) => {
        this.setState({ businessID });
        this.populateServiceList(businessID);
    }

    onChangeService = (serviceID) => {
        this.setState({ serviceID });
        this.populateWorkerList(serviceID);
    }

    onChangeWorker = (workerID) => {
        this.setState({ workerID });
    }

    populateBusinessList() {
        // businessOptions = Service.getBusinessesAll();
        console.log(Service.getBusinessesAll());
    }

    populateServiceList(businessID) {
        serviceOptions = Service.getServicesByBusinessID(businessID);
        console.log(serviceOptions);
    }

    populateWorkerList(serviceID) {
        workerOptions = Service.getWorkersByServiceID(serviceID);
        console.log(workerOptions);
    }

    handleBooking(e) {
        e.preventDefault();

        const dateList = this.dateListRef.current;

        this.setState({
            message: "",
            loading: true,
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
                                <h6>Please select a Business</h6>
                                <Select name='business'
                                        options={businessOptions}
                                        onChange={this.onChangeBusiness}
                                        validations={[required]}
                                />
                            </div>

                            <div className="form-group">
                                <h6>Please select a Service</h6>
                                <Select name='service'
                                        options={serviceOptions}
                                        onChange={this.onChangeService}
                                        validations={[required]}
                                />
                            </div>

                            <div className="form-group">
                                <h6>Please select a Worker</h6>
                                <Select name='worker'
                                        options={workerOptions}
                                        onChange={this.onChangeWorker}
                                        validations={[required]}
                                />
                            </div>

                            <div className="form-group">
                                <h6>Please select a Date and Time</h6>
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
