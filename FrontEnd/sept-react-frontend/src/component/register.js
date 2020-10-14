import React, { Component, Fragment } from "react";
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";

import Auth from "../service/auth";

const required = (value) => {
    if (!value) {
        return <div className="notification is-light is-warning">This field is required!</div>;
    }
};
const postcode = (value) => {
    if (!/^[0-9]{4}$/gm.test(value)) {
        return <div className="notification is-light is-warning">Invalid postcode!</div>;
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
            errors: [],
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
            loading: true,
        });

        this.form.validateAll();

        // if (this.checkBtn.context._errors.length === 0) {
            Auth.register(
                this.state.username,
                this.state.password,
                this.state.firstName,
                this.state.lastName,
                this.state[this.state.role]
            )
                .then((jwtResponse) => {
                    this.props.history.push("/dashboard");
                    window.location.reload();
                })
                .catch((errors) => {
                    this.setState({
                        loading: false,
                        errors: errors,
                    });
                    console.log(errors);
                });
        // } else {
        //     this.setState({
        //         loading: false,
        //     });
        // }
    }

    render() {
        return (
            <div className="container">
                <h3 class="title is-3">Register</h3>
                <div className="card events-card">
                    <div class="card-content">
                        <div class="content">
                            <Form
                                onSubmit={this.handleRegister}
                                ref={(c) => {
                                    this.form = c;
                                }}
                            >
                                <div class="field">
                                    <label class="label">Registration type</label>
                                    <div class="control has-icons-left">
                                        <div class="select">
                                            <select
                                                name="role"
                                                value={this.state.role}
                                                onChange={this.handleChange}
                                                label="Select"
                                            >
                                                <option value="CUSTOMER">Customer</option>
                                                {/* <option value="WORKER">
                                                    Worker
                                                </option> */}
                                                <option value="ADMIN">Admin</option>
                                            </select>
                                        </div>
                                        <div class="icon is-small is-left">
                                            <i class="fas fa-users-cog"></i>
                                        </div>
                                    </div>
                                </div>

                                <div className="field">
                                    <label class="label">AGME username</label>
                                    <div className="control">
                                        <Input
                                            type="text"
                                            className="input"
                                            name="username"
                                            value={this.state.username}
                                            onChange={this.handleChange}
                                            validations={[required]}
                                            placeholder="Create your username"
                                        />
                                    </div>
                                </div>

                                <div className="field">
                                    <label class="label">Password</label>
                                    <div className="control">
                                        <Input
                                            type="password"
                                            className="input"
                                            name="password"
                                            value={this.state.password}
                                            onChange={this.handleChange}
                                            validations={[required]}
                                            placeholder="Create your password"
                                        />
                                    </div>
                                </div>

                                <div className="field">
                                    <label class="label">First name</label>
                                    <div className="control">
                                        <Input
                                            type="text"
                                            className="input"
                                            name="firstName"
                                            value={this.state.firstName}
                                            onChange={this.handleChange}
                                            validations={[required]}
                                            placeholder="Enter your first name"
                                        />
                                    </div>
                                </div>

                                <div className="field">
                                    <label class="label">Last name</label>
                                    <div className="control">
                                        <Input
                                            type="text"
                                            className="input"
                                            name="lastName"
                                            value={this.state.lastName}
                                            onChange={this.handleChange}
                                            validations={[required]}
                                            placeholder="Enter your last name"
                                        />
                                    </div>
                                </div>

                                {this.state.role === "CUSTOMER" && (
                                    <Fragment>
                                        <div className="field">
                                            <label class="label">Street address</label>
                                            <div className="control">
                                                <Input
                                                    type="text"
                                                    className="input"
                                                    name="streetAddress"
                                                    value={this.state.CUSTOMER.streetAddress}
                                                    onChange={this.handleCustomerChange}
                                                    validations={[required]}
                                                    placeholder="Enter your street address"
                                                />
                                            </div>
                                        </div>
                                        <div className="field">
                                            <label class="label">City</label>
                                            <div className="control">
                                                <Input
                                                    type="text"
                                                    className="input"
                                                    name="city"
                                                    value={this.state.CUSTOMER.city}
                                                    onChange={this.handleCustomerChange}
                                                    validations={[required]}
                                                    placeholder="Enter your city"
                                                />
                                            </div>
                                        </div>
                                        <div className="field">
                                            <label class="label">State</label>
                                            <div className="control">
                                                <div class="select">
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
                                            </div>
                                        </div>
                                        <div className="field">
                                            <label class="label">Postcode</label>
                                            <div className="control">
                                                <Input
                                                    type="number"
                                                    pattern="^[0-9]{4}$"
                                                    className="input"
                                                    name="postcode"
                                                    value={this.state.CUSTOMER.postcode}
                                                    onChange={this.handleCustomerChange}
                                                    validations={[required, postcode]}
                                                    placeholder="Enter your postcode"
                                                />
                                            </div>
                                        </div>
                                    </Fragment>
                                )}

                                {
                                    this.state.role === "WORKER"
                                    // Nothing extra for worker atm
                                }

                                {this.state.role === "ADMIN" && (
                                    <Fragment>
                                        <div className="field">
                                            <label class="label">Business name</label>
                                            <div className="control">
                                                <div className="control">
                                                    <Input
                                                        type="text"
                                                        className="input"
                                                        name="businessName"
                                                        value={this.state.ADMIN.businessName}
                                                        onChange={this.handleAdminChange}
                                                        validations={[required]}
                                                    />
                                                </div>
                                            </div>
                                        </div>
                                    </Fragment>
                                )}

                                <div class="field is-grouped">
                                    <div class="control">
                                        <button
                                            className={`button is-link ${this.state.loading ? "is-loading" : ""}`}
                                            disabled={this.state.loading}
                                        >
                                            <span>Register</span>
                                        </button>
                                    </div>
                                </div>
                                {this.state.errors.map((errorMessage) => {
                                    return <div class="notification is-light is-danger">{errorMessage}</div>;
                                })}
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
            </div>
        );
    }
}
