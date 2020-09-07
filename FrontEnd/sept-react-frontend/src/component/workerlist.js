import React, {Component} from "react";
import Select from "react-select";

/*
    These list components will require a major refactoring to get rid of all this duplicate code
 */

const workerOptions = [
    { value: 'template', label: 'Template' }
]

const required = value => {
    if (!value) {
        return (
            <div className="alert alert-danger" role="alert">
                This field is required!
            </div>
        );
    }
};

export default class WorkerList extends Component {
    constructor(props) {
        super(props);

        this.onChangeWorker = this.onChangeWorker.bind(this);
    }

    state = {
        selected: ""
    };

    onChangeWorker = (selected) => {
        this.setState({ selected });
    }

    render() {
        return (
            <Select name='worker'
                    options={workerOptions}
                    onChange={this.onChangeWorker}
                    validations={[required]}
            />
        );
    }
}