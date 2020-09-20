import React, {Component} from "react";
import Form from "react-validation/build/form";
import CheckButton from "react-validation/build/button";
import Workers from "../service/workers"

export default class NewWorker extends Component {

    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);

        this.state = {
            username: "",
            fName: "",
            lName: ""
        };
    }

    onChangeUsername = (event) => {
        this.setState({username: event.target.value});
    };

    onChangeFirstName = (event) => {
        this.setState({fName: event.target.value});
    };

    onChangeLastName = (event) => {
        this.setState({lName: event.target.value});
    };

    handleSubmit(e) {
        e.preventDefault();

        // this.form.validateAll();

        if (this.checkBtn.context._errors.length === 0) {
            Workers.newWorker(this.state.username, this.state.fName, this.state.lName).then(
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
                            onSubmit={this.handleSubmit}
                            ref={c => {
                                this.form = c;
                            }}
                        >
                            <h3>Create a new worker.</h3>

                            <div className="formGroup">
                                <h6>Username</h6>
                                <input type="text" onChange={this.onChangeUsername} name="username"/>
                            </div>

                            <div className="formGroup">
                                <h6>First Name</h6>
                                <input type="text" onChange={this.onChangeFirstName} name="fName"/>
                            </div>

                            <div className="formGroup">
                                <h6>Last Name</h6>
                                <input type="text" onChange={this.onChangeLastName} name="lName" />
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
                                style={{display: "none"}}
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
