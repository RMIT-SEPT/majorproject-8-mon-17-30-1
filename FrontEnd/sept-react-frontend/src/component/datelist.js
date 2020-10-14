import React, {Component} from "react";
import DatePicker from "react-datepicker";

import "react-datepicker/dist/react-datepicker.css";

export default class DateList extends Component {
    state = {
        startDate: new Date(),
    };

    constructor(props) {
        super(props);
    }

    handleDateChange = (date) => {
        this.setState({
            startDate: date,
        });
    };

    from9to5 = (time) => {
        const hour = time.getHours();
        return hour >= 9 && hour < 17;
    }

    render() {
        return (
            <DatePicker
                selected={this.state.startDate}
                onChange={this.handleDateChange}
                showTimeSelect
                filterTime={this.from9to5}
                minDate={new Date()}
                inline
                fixedHeight
                dateFormat="d MMMM yyyy hh:mm"
            />
        );
    };
}
