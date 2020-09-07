import React, {Component} from "react";
import Select from "react-select";

/*
    These list components will require a major refactoring to get rid of all this duplicate code
 */

const businessOptions = [
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

export default class BusinessList extends Component {
    constructor(props) {
        super(props);

        this.onChangeBusiness = this.onChangeBusiness.bind(this);
    }

    state = {
        selected: ""
    };

    onChangeBusiness = (selected) => {
        this.setState({ selected });
        // props.onBusinessChange();
    }

    render() {
        return (
            <Select name='business'
                    options={businessOptions}
                    onChange={this.onChangeBusiness}
                    validations={[required]}
            />
        );
    }
}