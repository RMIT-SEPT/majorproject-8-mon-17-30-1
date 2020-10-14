import React, {Component} from "react";
import Form from "react-validation/build/form";
import CheckButton from "react-validation/build/button";
import DateList from "../component/datelist";
import Service from "../service/service";
import Select from "react-select";
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

export default class BookService extends Component {
    // List component refs
    dateListRef = React.createRef();

    constructor(props) {
        super(props);
        this.onChangeBusiness = this.onChangeBusiness.bind(this);
        this.onChangeService = this.onChangeService.bind(this);
        this.onChangeWorker = this.onChangeWorker.bind(this);

        this.handleBooking = this.handleBooking.bind(this);

        this.state = {
            businessID: 0,
            serviceID: 0,
            workerID: 0,
            dateTime: new Date(),
            loading: false,
            message: "",
            businessOptions: [],
            serviceOptions: [],
            workerOptions: [],
        };
    }

    // Populate business list on mount
    async componentDidMount() {
        Service.getBusinessesAll().then((response) => {
            const businessOptions = this.formatBusiness(response.data);
            this.setState({businessOptions});
        });
    }

    formatBusiness(businesses) {
        return businesses.map((business) => {
            return {value: business.businessId, label: business.businessName};
        });
    }

    formatServices(services) {
        return services.map((service) => {
            return {value: service.serviceId, label: service.serviceName};
        });
    }

    formatWorkers(workers) {
        return workers.map((worker) => {
            return {
                value: worker.workerId,
                label: worker.firstName + " " + worker.lastName,
            };
        });
    }

    onChangeBusiness(business) {
        const businessID = business.value;
        this.setState({businessID});
        this.populateServiceList(businessID);
    }

    onChangeService(service) {
        const serviceID = service.value;
        this.setState({serviceID});
        this.populateWorkerList(serviceID);
    }

    onChangeWorker(worker) {
        const workerID = worker.value;
        this.setState({workerID});
    }

    populateServiceList(businessID) {
        Service.getServicesByBusinessID(businessID).then((response) => {
            const serviceOptions = this.formatServices(response.data);
            this.setState({serviceOptions});
        });
    }

    populateWorkerList(serviceID) {
        Service.getWorkersByServiceID(serviceID).then((response) => {
            const workerOptions = this.formatWorkers(response.data);
            this.setState({workerOptions});
        });
    }

    handleBooking(e) {
        e.preventDefault();

        const dateList = this.dateListRef.current;

        this.setState({
            message: "",
            loading: true,
            dateTime: dateList.state.startDate,
        });

        this.form.validateAll();

        const local = new Date(
            dateList.state.startDate.getTime() -
            dateList.state.startDate.getTimezoneOffset() * 60000
        ).toISOString();

        if (this.checkBtn.context._errors.length === 0) {
            Service.bookService(
                this.state.serviceID,
                this.state.workerID,
                local,
                Auth.getCurrentUser().username
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
            <div className="card events-card">
                <header className="card-header">
                    <h4 class="card-header-title is-4">Book a service</h4>
                </header>

                <Form
                    onSubmit={this.handleBooking}
                    ref={(c) => {
                        this.form = c;
                    }}
                >
                    <div class="card-content">
                        <div class="content">
                            <div className="field">
                                <label class="label">Business</label>
                                <div className="control">
                                    <Select
                                        name="business-name"
                                        options={this.state.businessOptions}
                                        onChange={this.onChangeBusiness}
                                        validations={[required]}
                                        placeholder={"Select a business"}
                                    />
                                </div>
                            </div>

                            <div className="field">
                                <label class="label">Service</label>
                                <div className="control">
                                    <Select
                                        name="service"
                                        options={this.state.serviceOptions}
                                        onChange={this.onChangeService}
                                        validations={[required]}
                                        placeholder={"Select a service"}
                                    />
                                </div>
                            </div>

                            <div className="field">
                                <label class="label">Worker</label>
                                <div className="control">
                                    <Select
                                        name="worker"
                                        options={this.state.workerOptions}
                                        onChange={this.onChangeWorker}
                                        validations={[required]}
                                        placeholder={"Select a worker"}
                                    />
                                </div>
                            </div>

                            <div className="field">
                                <label class="label">Date and time</label>
                                <div className="control">
                                    <DateList ref={this.dateListRef} workerId={this.state.workerID}/>
                                </div>
                            </div>

                            <div class="field is-grouped">
                                <div class="control">
                                    <button
                                        className="button is-link"
                                        disabled={this.state.loading}
                                    >
                                        {this.state.loading && (
                                            <span className="spinner-border spinner-border-sm"/>
                                        )}
                                        <span>Book Service</span>
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
                                style={{display: "none"}}
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
