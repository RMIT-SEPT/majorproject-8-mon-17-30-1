import axios from "axios";
import authHeader from "./auth-header";

// For local development, this is hardcoded in at the moment
const API_URL_ACTIVE = "http://localhost:8080/api/v1/booking";
const API_URL_HISTORY = "http://localhost:8080/api/v1/booking/viewHistory";

class Booking {
    viewBooking(username) {
        console.log(API_URL_ACTIVE);
        return axios
            .get(API_URL_ACTIVE, {
                params: {
                    username: username
                },
                headers: authHeader()
            })
            .then(response => {
                return response.data;
            })
            .catch(error => {
                console.log(error);
            });
    }

    viewBookingHistory(username) {
        console.log(API_URL_HISTORY);
        return axios
            .get(API_URL_HISTORY, {
                params: {
                    username: username
                },
                headers: authHeader()
            })
            .then(response => {
                return response.data;
            })
            .catch(error => {
                console.log(error);
            });

    }
}

export default new Booking();