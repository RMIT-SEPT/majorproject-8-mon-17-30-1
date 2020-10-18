import axios from "axios";
import authHeader from "./auth-header";

// For local development, this is hardcoded in at the moment
export const API_URL = "http://ec2-13-211-211-139.ap-southeast-2.compute.amazonaws.com:8080/api/v1/booking/";

class Booking {
    viewBooking(username) {
        return axios
            .get(API_URL + "viewActive", {
                params: {
                    username: username
                },
                headers: authHeader()
            })
            .then(response => {
                return response.data;
            }).catch(error => {
                console.log(error);
            });
    }

    viewBookingHistory(username) {
        return axios
            .get(API_URL + "viewHistory", {
                params: {
                    username: username
                },
                headers: authHeader()
            })
            .then(response => {
                return response.data;
            }).catch(error => {
                console.log(error);
            });
    }

    viewAllBookingHistory() {
        return axios
            .get(API_URL + "viewAllHistory", {
                headers: authHeader()
            })
            .then(response => {
                return response.data;
            }).catch(error => {
                console.log(error);
            });
    }
}

export default new Booking();