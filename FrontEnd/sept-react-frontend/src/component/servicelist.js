import React, {Component} from "react";
import Select from "react-select";

const serviceOptions = [
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

export default class ServiceList extends Component {
    constructor(props) {
        super(props);

        this.onChangeService = this.onChangeService.bind(this);
    }

    state = {
        selected: ""
    };

    onChangeService = (selected) => {
        this.setState({ selected });
    }

    render() {
        return (
            <Select name='service'
                    options={serviceOptions}
                    onChange={this.onChangeService}
                    validations={[required]}
            />
        );
    }
}