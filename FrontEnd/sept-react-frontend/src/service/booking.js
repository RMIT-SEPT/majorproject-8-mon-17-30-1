import axios from "axios";
import authHeader from "./auth-header";

// For local development, this is hardcoded in at the moment
const API_URL = "http://localhost:8080/api/v1/booking/";

class Booking {
    viewBooking(username) {
        console.log(API_URL + "viewActive");
        return axios
            .get(API_URL + "viewActive", {
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
        console.log(API_URL + "viewHistory");
        return axios
            .get(API_URL + "viewHistory", {
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