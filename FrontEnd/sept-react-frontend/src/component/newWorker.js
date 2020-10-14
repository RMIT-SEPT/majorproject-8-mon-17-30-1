import React, { Component } from "react";
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";
import Workers from "../service/workers";

const required = (value) => {
    if (!value) {
        return <div className="notification is-light is-warning">This field is required!</div>;
    }
};

export default class NewWorker extends Component {
    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);

        this.state = {
            username: "",
            fName: "",
            lName: "",
            last: true,
            errors: [],
        };
    }

    onChangeUsername = (event) => {
        this.setState({ username: event.target.value });
    };

    onChangeFirstName = (event) => {
        this.setState({ fName: event.target.value });
    };

    onChangeLastName = (event) => {
        this.setState({ lName: event.target.value });
    };

    handleSubmit(e) {
        e.preventDefault();

        this.setState({
            errors: [],
            loading: true,
        });

        this.form.validateAll();

        if (this.checkBtn.context._errors.length === 0) {
            Workers.newWorker(this.state.username, this.state.fName, this.state.lName)
                .then(() => {
                    
                })
                .catch((errors) => {
                    this.setState({
                        loading: false,
                        errors: errors,
                    });
                    console.log(errors);
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
                    <h4 class="card-header-title is-4">Add new worker</h4>
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
                                <label class="label">AGME username</label>
                                <div className="control">
                                    <Input
                                        type="text"
                                        className="input"
                                        onChange={this.onChangeUsername}
                                        name="username"
                                        validations={[required]}
                                        placeholder="Create the worker's username"
                                    />
                                </div>
                            </div>

                            <div className="field">
                                <label class="label">First name</label>
                                <div className="control">
                                    <Input
                                        type="text"
                                        className="input"
                                        onChange={this.onChangeFirstName}
                                        name="fName"
                                        validations={[required]}
                                        placeholder="Enter the worker's first name"
                                    />
                                </div>
                            </div>

                            <div className="field">
                                <label class="label">Last name</label>
                                <div className="control">
                                    <Input
                                        type="text"
                                        className="input"
                                        onChange={this.onChangeLastName}
                                        name="lName"
                                        validations={[required]}
                                        placeholder="Enter the worker's last name"
                                    />
                                </div>
                            </div>

                            <div class="field is-grouped">
                                <div class="control">
                                    <button
                                        className={`button is-link ${this.state.loading ? "is-loading" : ""}`}
                                        disabled={this.state.loading}
                                    >
                                        <span>Create new worker</span>
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
        );
    }
}
