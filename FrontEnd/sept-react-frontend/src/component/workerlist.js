import React, {Component} from "react";
import Select from "react-select";

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

        this.state = {
            worker: ""
        };
    }

    onChangeWorker(e) {
        this.setState({
            worker: e.target.value
        });
    }

    render() {
        return (
            <Select name='worker'
                    options={workerOptions}
                    value={this.state.worker}
                    onChange={this.onChangeWorker}
                    validations={[required]}
            />
        );
    }
}