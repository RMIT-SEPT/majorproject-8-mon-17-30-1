import React, {Component} from "react";
import DatePicker from "react-datepicker";
import Workers from "../service/workers";

import "react-datepicker/dist/react-datepicker.css";

const dayofWeek = new Array(7);
dayofWeek[0] = "SUNDAY";
dayofWeek[1] = "MONDAY";
dayofWeek[2] = "TUESDAY";
dayofWeek[3] = "WEDNESDAY";
dayofWeek[4] = "THURSDAY";
dayofWeek[5] = "FRIDAY";
dayofWeek[6] = "SATURDAY";

export default class DateList extends Component {
    state = {
        startDate: new Date(),
        minHour: 0,
        maxHour: 0,
    };

    constructor(props) {
        super(props);
    }

    handleDateChange = (date) => {
        this.setState({
            startDate: date,
        });
        this.updateWorkerAvailability(date);
    };

    async updateWorkerAvailability(date) {
        await Workers.viewWorkerDateData(this.props.workerId).then(availabilityResponse => {
            let index = -1;
            for(let i = 0; i < availabilityResponse.availability.length; i++) {
                if(availabilityResponse.availability[i].day === dayofWeek[date.getDay()]
                && new Date(availabilityResponse.availability[i].effectiveStartDate) < date
                && new Date(availabilityResponse.availability[i].effectiveEndDate) > date) {
                    index = i;
                }
            }
            if(index >= 0) {
                this.setState({
                    minHour: availabilityResponse.availability[index].startTime.substr(0, 2),
                    maxHour: availabilityResponse.availability[index].endTime.substr(0, 2),
                });
            } else {
                this.setState({
                    minHour: 0,
                    maxHour: 0,
                });
            }
        });
    }

    from9to5 = (time) => {
        const hour = time.getHours();
        return hour >= this.state.minHour && hour < this.state.maxHour;
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
