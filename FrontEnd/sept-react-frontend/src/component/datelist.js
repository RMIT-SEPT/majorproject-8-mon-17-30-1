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
        startDate: undefined,
        minHour: 0,
        maxHour: 0,
        includeDatesStart: [],
        includeDatesEnd: [],
    };

    constructor(props) {
        super(props);

        this.handleMonthChange = this.handleMonthChange.bind(this);
    }

    componentDidMount() {
        this.updateWorkerAvailability();
    }

    handleDateChange = (date) => {
        this.setState({
            startDate: date,
        });
        this.updateHours(date);
    };

    async updateWorkerAvailability() {
        this.setState({
            includeDatesStart: [],
            includeDatesEnd: []
        });
        await Workers.viewWorkerDateData(this.props.workerId).then(availabilityResponse => {
            for(let i = 0; i < availabilityResponse.availability.length; i++) {
                for(let d = new Date(); d < new Date(availabilityResponse.availability[i].effectiveEndDate); d.setDate(d.getDate() + 1)) {
                    if(availabilityResponse.availability[i].day === dayofWeek[d.getDay()]) {
                        this.state.includeDatesStart.push(new Date(d.getTime()).setHours(parseInt(availabilityResponse.availability[i].startTime.substr(0, 2))));
                        this.state.includeDatesEnd.push(new Date(d.getTime()).setHours(parseInt(availabilityResponse.availability[i].endTime.substr(0, 2))));
                    }
                }
            }
        });
    }

    updateHours(date) {
        let index = -1;
        for(let i = 0; i < this.state.includeDatesStart.length; i++) {
            if(new Date(this.state.includeDatesStart[i]).getDate() === date.getDate()) {
                index = i;
            }
        }
        if(index >= 0) {

            this.setState({
                minHour: new Date(this.state.includeDatesStart[index]).getHours(),
                maxHour: new Date(this.state.includeDatesEnd[index]).getHours(),
            });
        } else {
            this.setState({
                minHour: 0,
                maxHour: 0,
            });
        }
    }

    updateTime = (time) => {
        const hour = time.getHours();
        return hour >= this.state.minHour && hour < this.state.maxHour;
    }

    handleMonthChange() {
        this.updateWorkerAvailability();
    }

    render() {
        return (
            <DatePicker
                selected={this.state.startDate}
                onChange={this.handleDateChange}
                onMonthChange={this.handleMonthChange}
                showTimeSelect
                filterTime={this.updateTime}
                includeDates={this.state.includeDatesStart}
                forceShowMonthNavigation
                inline
                fixedHeight
                dateFormat="d MMMM yyyy hh:mm"
            />
        );
    };
}
