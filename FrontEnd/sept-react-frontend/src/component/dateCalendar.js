import React, { Component } from "react";
import DatePicker from "react-datepicker";

import "react-datepicker/dist/react-datepicker.css";

export default class DateCalendar extends Component {
    constructor(props) {
        super(props);
    }

    state = {
        startDate: new Date()
    };

    handleDateChange = date => {
        this.setState({
            startDate: date
        });
    };

    render() {
        return (
            <DatePicker
                selected={this.state.startDate}
                onChange={this.handleDateChange}
                minDate={new Date()}
                showDisabledMonthNavigation
                dateFormat="d MMMM yyyy"
            />
        );
    }
}